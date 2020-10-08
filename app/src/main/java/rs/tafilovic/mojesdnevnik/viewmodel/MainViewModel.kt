package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rs.tafilovic.mojesdnevnik.model.School
import rs.tafilovic.mojesdnevnik.model.SchoolYear
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.repository.Repository
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository) : ViewModel() {

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
        selectedStudentLiveData.postValue(student).apply {
            val schools = student.schools
                .map { it.value }
                .sortedByDescending { it.id?.toInt() }
            schoolsLiveData.postValue(schools)

            setSelectedSchool(0)
        }
    }

    fun setSelectedSchool(position: Int) {
        val school = schoolsLiveData.value?.get(position)
        selectedSchoolLiveData.postValue(school).apply {
            schoolYearsLiveData.postValue(school?.schoolyears
                ?.map { it.value }
                ?.sortedByDescending { it.yearId })
            setSelectedSchoolYear(0)
        }
    }

    fun setSelectedSchoolYear(position: Int) {
        val schoolYear = schoolYearsLiveData.value?.get(position)
        selectedSchoolYear.postValue(schoolYear)
    }

    fun setIsConnected(connected: Boolean) {
        if (connected)
            refresh()
    }

    fun refresh() {
        val students = repository.students;
        if (students != null)
            studentsMutableLiveDate.postValue(students)
    }
}