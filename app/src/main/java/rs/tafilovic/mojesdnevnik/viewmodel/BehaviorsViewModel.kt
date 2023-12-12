package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rs.tafilovic.mojesdnevnik.model.Behavior
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class BehaviorsViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val liveData = MutableLiveData<List<Behavior>?>()
    val statusLiveData = MutableLiveData<Status<List<Behavior>>>()

    fun get(timelineParams: TimelineParams?) {
        viewModelScope.launch(Dispatchers.IO) {
            val it = repository.getBehaviors(timelineParams?.studentClassId)
            if (it.statusValue == StatusCode.FINISHED) {
                liveData.postValue(it.result)
            }
            statusLiveData.postValue(it)
        }
    }
}