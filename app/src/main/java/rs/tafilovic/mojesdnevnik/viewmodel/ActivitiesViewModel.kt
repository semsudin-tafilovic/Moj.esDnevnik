package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.SubjectActivity
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val liveData = MutableLiveData<List<SubjectActivity>>()
    val statusLiveData = MutableLiveData<Status<List<SubjectActivity>>>()

    fun get(timelineParams: TimelineParams?) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getActivities(timelineParams?.studentClassId)
            if (response.statusValue == StatusCode.FINISHED) {
                response.result?.let { data ->
                    liveData.postValue(data)
                }
            } else {
                statusLiveData.postValue(response)
            }
        }
    }
}