package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rs.tafilovic.mojesdnevnik.model.Behavior
import rs.tafilovic.mojesdnevnik.model.SchoolYear
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class BehaviorsViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val liveData = MutableLiveData<List<Behavior>>()
    val statusLiveData = MutableLiveData<Status<List<Behavior>>>()

    fun get(schoolYear: SchoolYear) {
        repository.getBehaviors(schoolYear) {
            if (it.statusValue == StatusCode.FINISHED) {
                liveData.postValue(it.result)
            } else {
                statusLiveData.postValue(it)
            }
        }
    }
}