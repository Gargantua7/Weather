package pers.gargantua.weather.ui.place

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_place.*
import pers.gargantua.weather.R

class PlaceActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private val managerRecyclerAdapter by lazy { ManagerRecyclerAdapter(this) }

    private val searchRecyclerAdapter: SearchRecyclerAdapter by lazy { SearchRecyclerAdapter(this, viewModel.placeList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_place)

        searchPlaceEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val content = s.toString()
                if (content.isNotEmpty() || content != "") {
                    viewModel.searchPlaces(content)
                    search_recyclerView.visibility = View.VISIBLE
                } else {
                    search_recyclerView.visibility = View.GONE
                    managerRecyclerAdapter.notifyDataSetChanged()
                    viewModel.placeList.clear()
                    searchRecyclerAdapter.notifyDataSetChanged()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        back_button.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        manager_recyclerview.layoutManager = LinearLayoutManager(this)
        manager_recyclerview.adapter = managerRecyclerAdapter
        ItemTouchHelper(ManagerItemTouchHelper(managerRecyclerAdapter)).attachToRecyclerView(manager_recyclerview)

        search_recyclerView.layoutManager = LinearLayoutManager(this)
        search_recyclerView.adapter = searchRecyclerAdapter

        viewModel.placeLiveData.observe(this) { result ->
            val places = result.getOrNull()
            if (places != null) {
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                searchRecyclerAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        if (searchPlaceEdit.text.toString() != "") {
            searchPlaceEdit.setText("")
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}