package rs.tafilovic.mojesdnevnik.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

data class SchoolYearPart(
    val value: Int,
    val desc: String,
    val grades: List<Int>,
    var finalGrade: Float
)

data class Course(var name: String, var parts: List<SchoolYearPart>)

@Parcelize
data class SchoolClass(val section: String, val studentClassId: String) : Parcelable {
    var id: String? = null
}

@Parcelize
data class SchoolYear(
    @SerializedName("year") val year: String,
    @SerializedName("year_id") val yearId: String,
    @SerializedName("classes") val classes: HashMap<String, SchoolClass>
) : Parcelable

@Parcelize
data class School(val schoolName: String, val schoolyears: HashMap<String, SchoolYear>) :
    Parcelable {
    var id: String? = null
}

@Parcelize
data class Student(
    val id: Long,
    val fullName: String,
    val jmbg: String,
    val gender: String,
    val schools: HashMap<String, School>
) : Parcelable {

/*
    fun getSchoolId(): String {
        return schools.keys.first()
    }

    fun getSchool(): School {
        return schools.values.first()
    }

    fun getSchoolYear(): SchoolYear {
        val schoolYears = getSchool().schoolyears.values
        return schoolYears.maxBy { it.yearId }!!
    }
*/

/*
    fun getClassId(): String? {
        val schoolClasses = getSchoolYear().classes
        return schoolClasses.keys.first()
    }

    fun getStudentClassId(): Long {
        return getSchoolYear().classes.values.first().studentClassId.toLong()
    }
*/

    companion object {
        val DIF_UTIL = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.jmbg == newItem.jmbg
            }
        }
    }
}

@Parcelize
data class TimelineParams(
    var studentId: String? = null,
    var schoolId: String? = null,
    var classId: String? = null,
    var studentClassId: String? = null
) :
    Parcelable

@Parcelize
data class StudentSchoolYear(val student: Student?, val schoolYear: SchoolYear?) : Parcelable

data class Students(var data: List<Student>)


data class Cookie(val name: String, val value: String, val expirationTime: Long) {

    fun getCookieFormatted(): String {
        return "$name=$value;"
    }

    companion object {
        val COOKIE = "Cookie"
        val COOKIE_EXPIRE = "Cookie_Expire"
        val USERNAME = "username"
        val PASSWORD = "password"
    }
}

data class Grades(val courses: List<Course>)

data class Session(val html: String, val cookies: List<Cookie>) {

    fun getCookiesFormatted(): String {
        val sb = StringBuffer()
        cookies.forEach {
            sb.append(it.getCookieFormatted())
        }
        return sb.toString()
    }
}

data class Grade(
    val id: Int,
    @SerializedName("grade_type_id") val gradeTypeId: Int,
    val name: String,
    val value: Int,
    val sequence: Int
)

data class Timeline(val data: HashMap<String, List<Event>>, val meta: HashMap<String, Long>)

data class Engagement(val id: Int, val name: String?)

data class EvaluationElementCourse(
    val id: Long,
    val sequence: Int,
    val evaluationElement: EvaluationElement?
) {
    fun getEvaluationElement(): String? {
        val sb = StringBuffer()

        if (evaluationElement == null) return null

        if (!evaluationElement.name.isNullOrEmpty())
            sb.append(evaluationElement.name).append("\n")

        if (!evaluationElement.name_sq_AL.isNullOrEmpty())
            sb.append(evaluationElement.name_sq_AL).append("\n")

        if (!evaluationElement.name_bs_BA.isNullOrEmpty())
            sb.append(evaluationElement.name_bs_BA).append("\n")

        if (!evaluationElement.name_bg_BG.isNullOrEmpty())
            sb.append(evaluationElement.name_bg_BG).append("\n")

        if (!evaluationElement.name_hu_HU.isNullOrEmpty())
            sb.append(evaluationElement.name_hu_HU).append("\n")

        if (!evaluationElement.name_ro_RO.isNullOrEmpty())
            sb.append(evaluationElement.name_ro_RO).append("\n")

        if (!evaluationElement.name_ru_UA.isNullOrEmpty())
            sb.append(evaluationElement.name_ru_UA).append("\n")

        if (!evaluationElement.name_sk_SK.isNullOrEmpty())
            sb.append(evaluationElement.name_sk_SK).append("\n")

        if (!evaluationElement.name_hr_HR.isNullOrEmpty())
            sb.append(evaluationElement.name_hr_HR).append("\n")

        return sb.toString()
    }
}

data class EvaluationElement(
    val id: Long?,
    val sequence: Int?,
    val name: String?,
    val name_sq_AL: String?,
    val name_bs_BA: String?,
    val name_bg_BG: String?,
    val name_hu_HU: String?,
    val name_ro_RO: String?,
    val name_ru_UA: String?,
    val name_sk_SK: String?,
    val name_hr_HR: String?
)

data class Event(
    val type: String,
    val date: String,
    val createTime: String,
    val fullGrade: String,
    val grade: Grade,
    val gradeCategory: String,
    val note: String,
    val course: String,
    val classCourseId: Long,
    val schoolClass: String,
    val school: String,
    val iopNote: String,
    val activityType: String,
    val cssClass: String,
    val schoolHour: Int,
    val workHourNote: String?,
    val teacherNote: String?,
    val classMasterNote: String?,
    val absentType: String?,
    val status: String?,
    val statusId: Int,
    val engagement: Engagement?,
    val schoolyearPart: String?,
    val evaluationElementCourse: EvaluationElementCourse?
) {
    companion object {
        val DIF_UTIL = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.activityType == newItem.activityType &&
                        oldItem.classCourseId == newItem.classCourseId &&
                        oldItem.course == newItem.course &&
                        oldItem.createTime == newItem.createTime &&
                        oldItem.date == newItem.date &&
                        oldItem.fullGrade == newItem.fullGrade &&
                        oldItem.grade == newItem.grade &&
                        oldItem.school == newItem.school &&
                        oldItem.cssClass == newItem.cssClass &&
                        oldItem.gradeCategory == newItem.gradeCategory &&
                        oldItem.iopNote == newItem.iopNote &&
                        oldItem.note == newItem.note &&
                        oldItem.schoolClass == newItem.schoolClass
            }
        }
    }
}

data class Activities(
    val date: String,
    val createTime: String,
    val type: Int,
    val note: String,
    val name: String,
    val faClass: String
)

data class ActivitiesPart(val activities: List<Activities>)

data class SubjectActivity(
    val course: String,
    val classCourseId: Long,
    val sequence: Int,
    val parts: HashMap<String, ActivitiesPart>
) {
    companion object {
        val DIF_UTIL = object : DiffUtil.ItemCallback<SubjectActivity>() {
            override fun areItemsTheSame(
                oldItem: SubjectActivity,
                newItem: SubjectActivity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SubjectActivity,
                newItem: SubjectActivity
            ): Boolean {
                return oldItem.course == newItem.course && oldItem.classCourseId == newItem.classCourseId && oldItem.sequence == newItem.sequence
            }

        }
    }
}

/*************** BEGIN GRADES API ENDPOINT POJO ***************/

data class FullGrade(
    val descriptive: Boolean,
    val date: String,
    val createTime: String,
    val fullGrade: String,
    val grade: Int,
    val gradeCategory: String,
    val note: String?,
    val schoolyearPartId: String?,
    val evaluationElement: EvaluationElement?
)

data class FinalGrade(
    val name: String,
    val value: Int,
    @SerializedName("schoolyear_part_id") val schoolyearPartId: Int,
    val engagement: String?
)

data class FullGradesCollection(
    val grades: List<FullGrade>,
    val final: FinalGrade?,
    val average: Float
)

data class FullGrades(
    val course: String,
    val classCourseId: Long,
    val classCourseGradeTypeId: Int,
    val sequence: Int,
    val parts: HashMap<String, FullGradesCollection>
) {
    companion object {
        val DIF_UTIL = object : DiffUtil.ItemCallback<FullGrades>() {
            override fun areItemsTheSame(oldItem: FullGrades, newItem: FullGrades): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FullGrades, newItem: FullGrades): Boolean {
                return oldItem.course == newItem.course &&
                        oldItem.classCourseId == newItem.classCourseId &&
                        oldItem.sequence == newItem.sequence
            }
        }
    }
}

/*************** END GRADES API ENDPOINT POJO ***************/


/*************** BEGIN ABSENTS API ENDPOINT POJO ***************/

data class AbsentClass(
    val classCourseId: Long,
    val name: String,
    val sequence: String,
    val absentStatuses: HashMap<String, AbsentStatus>
) {

    fun map(): List<Absent> {
        val absents = arrayListOf<Absent>()
        for (s in absentStatuses) {
            absents.add(Absent(name, s.value.name, s.value.statusId, s.value.absents))
        }
        return absents
    }
}

data class AbsentStatus(val statusId: Int, val name: String, val absents: List<AbsentClassDetails>)

data class AbsentClassDetails(
    val id: Long,
    val workHourId: Long,
    val teacherNote: String?,
    val statusName: String?,
    val workHourNote: String?,
    val workdayDate: String
)

data class Absent(
    val classCourseName: String,
    val absentStatus: String,
    val absentStatusId: Int,
    val absentDetails: List<AbsentClassDetails>
) {

    companion object {
        val DIF_UTIL = object : DiffUtil.ItemCallback<Absent>() {
            override fun areItemsTheSame(oldItem: Absent, newItem: Absent): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Absent, newItem: Absent): Boolean {
                return oldItem.absentStatus == newItem.absentStatus &&
                        oldItem.absentStatusId == newItem.absentStatusId &&
                        oldItem.classCourseName == newItem.classCourseName
            }
        }
    }
}

/*************** END ABSENTS API ENDPOINT POJO ***************/


/*************** BEGIN BEHAVIOR API ENDPOINT POJO ***************/

data class Behavior(
    val id: Long,
    val name: String,
    val date: String,
    val schoolyearPartId: Int,
    val note: String?
) {
    companion object {
        val DIF_UTIL = object : DiffUtil.ItemCallback<Behavior>() {
            override fun areItemsTheSame(oldItem: Behavior, newItem: Behavior): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Behavior, newItem: Behavior): Boolean {
                return oldItem.id == newItem.id && oldItem.date == newItem.date && oldItem.name == newItem.name
            }
        }
    }
}

/*************** END BEHAVIOR API ENDPOINT POJO ***************/


/*************** BEGIN COURSES API ENDPOINT POJO ***************/

data class Teacher(val name: String, val consultationInfo: String?)

data class MainCourse(
    val id: Long,
    val sequence: Int,
    val name: String,
    val teachers: List<Teacher>,
    val subCourses: List<SubCourse>
) {
    companion object {
        val DIF_UTIL = object : DiffUtil.ItemCallback<MainCourse>() {
            override fun areItemsTheSame(oldItem: MainCourse, newItem: MainCourse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MainCourse, newItem: MainCourse): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.sequence == newItem.sequence &&
                        oldItem.teachers == newItem.teachers
            }
        }
    }
}

data class SubCourse(
    val id: Long,
    val sequence: Int,
    val parentClassCourseId: Long,
    val name: String,
    val teachers: List<Teacher>
)


/*************** END COURSES API ENDPOINT POJO ***************/


enum class StatusCode { ERROR, LOADING, FINISHED }

class Status<T>(val statusValue: StatusCode, val result: T?, val message: String?) {

    constructor(statusValue: StatusCode, message: String?) : this(
        statusValue,
        null,
        message
    )

    constructor(statusValue: StatusCode) : this(statusValue, null)

    val statusDesc: String = when (statusValue) {
        StatusCode.ERROR -> "Error"
        StatusCode.LOADING -> "Loading"
        StatusCode.FINISHED -> "Finished successful"
    }
}

data class State(val responseCode: Int, val message: String)