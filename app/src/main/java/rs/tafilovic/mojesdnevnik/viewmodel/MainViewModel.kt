package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rs.tafilovic.mojesdnevnik.model.*
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val TAG = MainViewModel::class.java.name

    val studentsMutableLiveDate = MutableLiveData<List<Student>>()
    val stateMutableLiveData = MutableLiveData<Status<List<Student>>>()

    val selectedStudentLiveData = MutableLiveData<Student>()
    private val selectedSchoolYearLiveData = MutableLiveData<SchoolYear>()

    val studentSchoolYear: LiveData<StudentSchoolYear>

    init {
        studentsMutableLiveDate.postValue(repository.students)
        studentSchoolYear = Transformations.switchMap(selectedSchoolYearLiveData) {
            MutableLiveData(StudentSchoolYear(selectedStudentLiveData.value, it))
        }
    }

    fun setSelectedStudent(student: Student) {
        selectedStudentLiveData.postValue(student)
        selectedSchoolYearLiveData.postValue(student.getSchoolYear())
    }

    fun setSelectedSchoolYear(schoolYear: SchoolYear) {
        selectedSchoolYearLiveData.postValue(schoolYear)
    }


    fun setSelectedSchoolYear(position: Int) {

        val schoolYear = selectedStudentLiveData.value?.getSchool()?.schoolyears
            ?.map { it.value }
            ?.sortedByDescending { it.yearId }
            ?.get(position)

        selectedSchoolYearLiveData.postValue(schoolYear)
    }

    fun setIsConnected(connected: Boolean) {
/*        if (connected)
            refresh()*/
    }

}