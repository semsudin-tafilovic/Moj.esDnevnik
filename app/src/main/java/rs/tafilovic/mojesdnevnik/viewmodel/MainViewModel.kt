package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rs.tafilovic.mojesdnevnik.model.*
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val TAG = MainViewModel::class.java.name

    val stateMutableLiveData = MutableLiveData<Status<List<Student>>>()

    val studentsMutableLiveDate = MutableLiveData<List<Student>>()

    val selectedStudentLiveData = MutableLiveData<Student>()

    private val selectedSchoolYearLiveData = MutableLiveData<SchoolYear>()

    var studentSchoolYear: MutableLiveData<StudentSchoolYear> = MutableLiveData()

    val selectedSchoolLiveData: MutableLiveData<School> = MutableLiveData()

    init {
        studentsMutableLiveDate.postValue(repository.students)
    }

    fun setSelectedStudent(student: Student) {
        selectedStudentLiveData.postValue(student)
    }

    fun setSelectedSchool(position: Int) {
        val schools = selectedStudentLiveData.value?.schools?.entries
            ?.sortedByDescending { it.key.toInt() }
            ?.map { it.value }

        selectedSchoolLiveData.postValue(
            if (schools != null && schools.isNotEmpty()) schools[position] else null
        )
    }

    fun setSelectedSchoolYear(position: Int) {

        val schoolYear = selectedStudentLiveData.value?.getSchool()?.schoolyears
            ?.map { it.value }
            ?.sortedByDescending { it.yearId }
            ?.get(position)

        selectedSchoolYearLiveData.postValue(schoolYear)
        studentSchoolYear.postValue(StudentSchoolYear(selectedStudentLiveData.value, schoolYear))
    }

    fun setIsConnected(connected: Boolean) {
/*        if (connected)
            refresh()*/
    }

}