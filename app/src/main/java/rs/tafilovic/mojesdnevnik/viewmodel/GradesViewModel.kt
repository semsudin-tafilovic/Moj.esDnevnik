package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rs.tafilovic.mojesdnevnik.model.*
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class GradesViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val liveData = MutableLiveData<List<FullGrades>>()
    val statusLiveData = MutableLiveData<Status<FullGrades>>()

    fun get(schoolYear: SchoolYear) {
        repository.getGrades(schoolYear) {
            if (it.statusValue == StatusCode.FINISHED) {
                liveData.postValue(it.result)
            } else {
                statusLiveData.postValue(Status(it.statusValue, null, it.message))
            }
        }
    }

}