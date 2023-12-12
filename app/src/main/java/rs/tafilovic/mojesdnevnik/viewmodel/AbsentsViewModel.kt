package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rs.tafilovic.mojesdnevnik.model.Absent
import rs.tafilovic.mojesdnevnik.model.AbsentClass
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class AbsentsViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val liveData = MutableLiveData<List<Absent>?>()
    val statusLiveData = MutableLiveData<Status<List<AbsentClass>>>()

    fun get(timelineParams: TimelineParams?) {
        viewModelScope.launch(Dispatchers.IO) {
            val it = repository.getAbsents(timelineParams?.studentClassId)
            if (it.statusValue == StatusCode.FINISHED) {
                liveData.postValue(it.result)
            }
            statusLiveData.postValue(Status(it.statusValue, null, it.message))
        }
    }
}