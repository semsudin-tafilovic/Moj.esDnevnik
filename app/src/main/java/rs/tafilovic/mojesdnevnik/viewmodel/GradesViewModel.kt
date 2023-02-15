package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rs.tafilovic.mojesdnevnik.model.FullGrades
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class GradesViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val liveData = MutableLiveData<List<FullGrades>>()
    val statusLiveData = MutableLiveData<Status<FullGrades>>()

    fun get(timelineParams: TimelineParams?) {
        repository.getGrades(timelineParams?.studentClassId) {
            if (it.statusValue == StatusCode.FINISHED) {
                it.result?.let { data ->
                    liveData.postValue(data)
                }
            } else {
                statusLiveData.postValue(Status(it.statusValue, null, it.message))
            }
        }
    }

}