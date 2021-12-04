package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rs.tafilovic.mojesdnevnik.model.School
import rs.tafilovic.mojesdnevnik.model.SchoolYear
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.repository.Repository
import rs.tafilovic.mojesdnevnik.util.LocalStore
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository, val localStore: LocalStore) :
    ViewModel() {

    val TAG = MainViewModel::class.java.name

    val stateMutableLiveData = MutableLiveData<Status<List<Student>>>()

    val studentsMutableLiveDate = MutableLiveData<List<Student>>()

    val selectedStudentLiveData = MutableLiveData<Student>()

    val selectedSchoolLiveData: MutableLiveData<School> = MutableLiveData()

    var selectedSchoolYear: MutableLiveData<SchoolYear> = MutableLiveData()

    val schoolsLiveData = MutableLiveData<List<School>>()

    val schoolYearsLiveData = MutableLiveData<List<SchoolYear>>()

    val timelineParamsLiveData: TimelineParamsLiveData

    init {
        timelineParamsLiveData = TimelineParamsLiveData(
            selectedStudentLiveData,
            selectedSchoolLiveData,
            selectedSchoolYear
        )
    }

    fun setSelectedStudent(student: Student) {
        selectedStudentLiveData.postValue(student).also {
            val schools = student.schools
                .map { it.value }
                .sortedByDescending { it.id?.toInt() }

            schoolsLiveData.postValue(schools).also {
                val selectedSchool =
                    schools.find { school -> school.id == localStore.getSelectedSchoolId() }
                        ?: schools.first()

                setSelectedSchool(selectedSchool)

            }
        }
    }

    fun setSelectedSchool(school: School) {
        selectedSchoolLiveData.postValue(school).also {
            val schoolYears = school.schoolyears
                .map { it.value }
                .sortedByDescending { it.yearId }

            schoolYears.let {
                schoolYearsLiveData.postValue(it)
                setSelectedSchoolYear(it.first())
            }

            school.id?.let {
                localStore.setSelectedSchoolId(it)
            }
        }

    }

    fun setSelectedSchoolYear(schoolYear: SchoolYear) {
        selectedSchoolYear.postValue(schoolYear)
    }

    fun setIsConnected(connected: Boolean) {
        if (connected)
            refresh()
    }

    fun refresh() {
        val students = repository.students
        students?.let {
            studentsMutableLiveDate.postValue(it)
        }
    }
}