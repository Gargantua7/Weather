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
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.ui.place.manager.ManagerItemTouchHelper
import pers.gargantua.weather.ui.place.manager.ManagerRecyclerAdapter
import pers.gargantua.weather.ui.place.search.SearchRecyclerAdapter

/**
 * 负责地点查询和管理的 Activity
 * @author Gargantua丶
 */
class PlaceActivity : AppCompatActivity() {

    /** ViewModel 层 */
    private val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    /** 城市管理 RecyclerView 适配器 */
    private val managerRecyclerAdapter by lazy { ManagerRecyclerAdapter(this) }

    /** 城市查询 RecyclerView 适配器 */
    private val searchRecyclerAdapter: SearchRecyclerAdapter by lazy { SearchRecyclerAdapter(this, viewModel.placeList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 使状态栏沉浸
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_place)

        WeatherApplication.log(this.javaClass, "onCreate")

        // 设置输入框文本变化监听器
        place_search_edit.addTextChangedListener(object : TextWatcher {
            // 在文本变化后调用
            override fun afterTextChanged(s: Editable?) {
                // 获取当前文本
                val content = s.toString()
                if (content.isNotEmpty() || content != "") {
                    // 文本不为空时，搜索城市并且使查询城市的 RecyclerView 可见
                    viewModel.searchPlaces(content)
                    search_recycler_view.visibility = View.VISIBLE
                } else {
                    // 文本为空时，清除查询适配器并隐藏，并且刷新管理适配器
                    managerRecyclerAdapter.notifyDataSetChanged()
                    viewModel.placeList.clear()
                    searchRecyclerAdapter.notifyDataSetChanged()
                    search_recycler_view.visibility = View.GONE
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        // 设置返回按钮，设置 RESULT_CANCELED
        place_back_button.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        // 设置城市管理 RecyclerView 布局管理器为线性布局
        manager_recycler_view.layoutManager = LinearLayoutManager(this)
        // 设置城市管理 RecyclerView 适配器
        manager_recycler_view.adapter = managerRecyclerAdapter
        // 为城市管理 RecyclerView 添加 ItemTouchHelper，用于拖动项目
        ItemTouchHelper(ManagerItemTouchHelper(managerRecyclerAdapter)).attachToRecyclerView(manager_recycler_view)

        // 设置城市查询 RecyclerView 布局管理器为线性布局
        search_recycler_view.layoutManager = LinearLayoutManager(this)
        // 设置城市查询 RecyclerView 适配器
        search_recycler_view.adapter = searchRecyclerAdapter

        // 监听城市列表 LiveData 变化
        viewModel.placeLiveData.observe(this) { result ->
            // 获取数据
            val places = result.getOrNull()
            // 判断非空
            if (places != null) {
                // 更新城市查询 RecyclerView
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                searchRecyclerAdapter.notifyDataSetChanged()
            } else {
                // 空返回体，发出提示
                Toast.makeText(this, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    /**
     * 重写方法，如果在查询 RecyclerView 时按下返回键，则只是清空输入框关闭查询
     */
    override fun onBackPressed() {
        if (place_search_edit.text.toString() != "") {
            place_search_edit.setText("")
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}