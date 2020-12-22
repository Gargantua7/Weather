package pers.gargantua.weather.ui.place

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_place.*
import kotlinx.android.synthetic.main.fragment_place.*
import pers.gargantua.weather.R

class PlaceActivity : AppCompatActivity() {

    private val searchFragment by lazy { supportFragmentManager.findFragmentById(R.id.search_fragment) as SearchFragment }

    private val managerRecyclerAdapter by lazy { ManagerRecyclerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_place)

        supportFragmentManager.beginTransaction().apply {
            hide(searchFragment)
            commit()
        }

        searchPlaceEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val content = s.toString()
                if (content.isNotEmpty()) {
                    searchFragment.viewModel.searchPlaces(content)
                    if (searchFragment.isHidden)
                        supportFragmentManager.beginTransaction().apply {
                            show(searchFragment)
                            commit()
                        }
                } else {
                    supportFragmentManager.beginTransaction().apply {
                        hide(searchFragment)
                        commit()
                    }
                    managerRecyclerAdapter.notifyDataSetChanged()
                    searchFragment.viewModel.placeList.clear()
                    searchFragment.adapter.notifyDataSetChanged()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        back_button.setOnClickListener {
            finish()
        }

        manager_recyclerview.layoutManager = LinearLayoutManager(this)
        manager_recyclerview.adapter = managerRecyclerAdapter
    }
}