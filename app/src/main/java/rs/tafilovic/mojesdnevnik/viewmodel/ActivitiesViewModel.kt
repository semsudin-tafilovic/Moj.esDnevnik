package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rs.tafilovic.mojesdnevnik.model.*
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val liveData = MutableLiveData<List<SubjectActivity>>()
    val statusLiveData = MutableLiveData<Status<List<SubjectActivity>>>()

    fun get(timelineParams: TimelineParams) {
        repository.getActivities(timelineParams.studentClassId) {
            if (it.statusValue == StatusCode.FINISHED) {
                liveData.postValue(it.result)
            } else {
                statusLiveData.postValue(it)
            }
        }
    }
}