package pers.gargantua.weather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import pers.gargantua.weather.logic.Repository
import pers.gargantua.weather.logic.model.Place

/**
 * [PlaceActivity] 的 ViewModel 层，用于保存临时数据
 * @author Gargantua丶
 **/
class PlaceViewModel : ViewModel() {

    /** LiveData 对象，监听查询关键字变化*/
    private val searchLiveData = MutableLiveData<String>()

    /** 查询到的地址集合 */
    val placeList = ArrayList<Place>()

    /** 监听 [searchLiveData] 变化，并将异步处理后的 LiveData 对象赋值给本值 */
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    /** 更新 [searchLiveData] 的参数，即更新后就会拉取地点查询数据 */
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

}