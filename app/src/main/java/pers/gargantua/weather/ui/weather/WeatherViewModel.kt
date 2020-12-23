package pers.gargantua.weather.ui.weather

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import pers.gargantua.weather.WeatherApplication
import pers.gargantua.weather.logic.GlobalData
import pers.gargantua.weather.logic.Repository
import pers.gargantua.weather.logic.dao.DataDao
import pers.gargantua.weather.logic.model.DaoPlace
import pers.gargantua.weather.logic.model.Location
import pers.gargantua.weather.logic.model.Weather

/**
 * @author Gargantua丶
 **/
class WeatherViewModel : ViewModel() {

    fun save() = Repository.save()

    fun restoreSave() {
        if (Repository.isSaved())
            Repository.getSave()
    }
}