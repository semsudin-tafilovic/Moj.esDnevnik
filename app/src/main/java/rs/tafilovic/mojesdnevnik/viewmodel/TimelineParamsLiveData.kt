package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import rs.tafilovic.mojesdnevnik.model.School
import rs.tafilovic.mojesdnevnik.model.SchoolYear
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.model.TimelineParams

class TimelineParamsLiveData(
    student: LiveData<Student?>,
    school: LiveData<School?>,
    schoolYear: LiveData<SchoolYear?>
) : MediatorLiveData<TimelineParams>() {

    init {
        addSource(student) {
            postValue(
                TimelineParams(
                    it?.id?.toString(),
                    school.value?.id,
                    schoolYear.value?.classes?.map { it.id }?.first(),
                    schoolYear.value?.classes?.map { it.studentClassId.toString() }?.first()
                )
            )
        }

        addSource(school) {
            postValue(
                TimelineParams(
                    student.value?.id?.toString(),
                    it?.id,
                    schoolYear.value?.classes?.map { it.id }?.first(),
                    schoolYear.value?.classes?.map { it.studentClassId.toString() }?.first()
                )
            )
        }

        addSource(schoolYear) {
            postValue(
                TimelineParams(
                    student.value?.id?.toString(),
                    school.value?.id,
                    it?.classes?.map { it.id }?.first(),
                    it?.classes?.map { it.studentClassId.toString() }?.first()
                )
            )
        }
    }
}