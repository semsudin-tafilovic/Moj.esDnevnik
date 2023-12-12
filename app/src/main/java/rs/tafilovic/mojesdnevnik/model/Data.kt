package rs.tafilovic.mojesdnevnik.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

data class SchoolYearPart(
    @SerializedName("value")
    val value: Int,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("grades")
    val grades: List<Int>,
    @SerializedName("finalGrade")
    var finalGrade: Float
)

data class Course(
    @SerializedName("name") var name: String,
    @SerializedName("parts") var parts: List<SchoolYearPart>
)

@Parcelize
data class SchoolClass(
    @SerializedName("section")
    val section: String,
    @SerializedName("studentClassId")
    val studentClassId: String
) : Parcelable {
    @IgnoredOnParcel
    @SerializedName("id")
    var id: String? = null
}

@Parcelize
data class SchoolYear(
    @SerializedName("year") val year: String,
    @SerializedName("year_id") val yearId: String,
    @SerializedName("classes") val classes: HashMap<String, SchoolClass>
) : Parcelable

@Parcelize
data class School(
    @SerializedName("schoolName")
    val schoolName: String,
    @SerializedName("schoolyears")
    val schoolyears: HashMap<String, SchoolYear>
) :
    Parcelable {
    @IgnoredOnParcel
    @SerializedName("id")
    var id: String? = null
}

@Parcelize
data class Student(
    @SerializedName("id")
    val id: Long,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("jmbg")
    val jmbg: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("schools")
    val schools: HashMap<String, School>
) : Parcelable {
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
    @SerializedName("studentId")
    var studentId: String? = null,
    @SerializedName("schoolId")
    var schoolId: String? = null,
    @SerializedName("classId")
    var classId: String? = null,
    @SerializedName("studentClassId")
    var studentClassId: String? = null
) :
    Parcelable

@Parcelize
data class StudentSchoolYear(
    @SerializedName("student")
    val student: Student?,
    @SerializedName("schoolYear")
    val schoolYear: SchoolYear?
) : Parcelable

data class Students(
    @SerializedName("data") var data: List<Student>
)


data class Cookie(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("expirationTime")
    val expirationTime: Long
) {

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

data class Grades(@SerializedName("courses") val courses: List<Course>)

data class Session(
    @SerializedName("html") val html: String,
    @SerializedName("cookies") val cookies: List<Cookie>
) {

    fun getCookiesFormatted(): String {
        val sb = StringBuffer()
        cookies.forEach {
            sb.append(it.getCookieFormatted())
        }
        return sb.toString()
    }
}

data class Grade(
    @SerializedName("id")
    val id: Int,
    @SerializedName("grade_type_id")
    val gradeTypeId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: Int,
    @SerializedName("sequence")
    val sequence: Int
)

data class Timeline(
    @SerializedName("data")
    val data: HashMap<String, List<Event>>,
    @SerializedName("meta")
    val meta: HashMap<String, Long>
)

data class Engagement(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?
)

data class EvaluationElementCourse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("evaluationElement")
    val evaluationElement: EvaluationElement?
) {
    fun getEvaluationElement(): String? {
        val sb = StringBuffer()

        if (evaluationElement == null) return null

        if (!evaluationElement.name.isNullOrEmpty())
            sb.append(evaluationElement.name).append("\n")
        else {
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
        }

        return sb.toString()
    }
}

data class EvaluationElement(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("sequence")
    val sequence: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("name_sq_AL")
    val name_sq_AL: String?,
    @SerializedName("name_bs_BA")
    val name_bs_BA: String?,
    @SerializedName("name_bg_BG")
    val name_bg_BG: String?,
    @SerializedName("name_hu_HU")
    val name_hu_HU: String?,
    @SerializedName("name_ro_RO")
    val name_ro_RO: String?,
    @SerializedName("name_ru_UA")
    val name_ru_UA: String?,
    @SerializedName("name_sk_SK")
    val name_sk_SK: String?,
    @SerializedName("name_hr_HR")
    val name_hr_HR: String?
)


data class Event(
    @SerializedName("type")
    val type: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("fullGrade")
    val fullGrade: String,
    @SerializedName("grade")
    val grade: Grade,
    @SerializedName("gradeCategory")
    val gradeCategory: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("course")
    val course: String,
    @SerializedName("classCourseId")
    val classCourseId: Long,
    @SerializedName("schoolClass")
    val schoolClass: String,
    @SerializedName("school")
    val school: String,
    @SerializedName("iopNote")
    val iopNote: String,
    @SerializedName("activityType")
    val activityType: String,
    @SerializedName("cssClass")
    val cssClass: String,
    @SerializedName("schoolHour")
    val schoolHour: Int,
    @SerializedName("workHourNote")
    val workHourNote: String?,
    @SerializedName("teacherNote")
    val teacherNote: String?,
    @SerializedName("classMasterNote")
    val classMasterNote: String?,
    @SerializedName("absentType")
    val absentType: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("statusId")
    val statusId: Int,
    @SerializedName("engagement")
    val engagement: Engagement?,
    @SerializedName("schoolyearPart")
    val schoolyearPart: String?,
    @SerializedName("evaluationElementCourse")
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
    @SerializedName("date")
    val date: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("note")
    val note: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("faClass")
    val faClass: String
)

data class ActivitiesPart(@SerializedName("activities") val activities: List<Activities>)

data class SubjectActivity(
    @SerializedName("course")
    val course: String,
    @SerializedName("classCourseId")
    val classCourseId: Long,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("createTime")
    val createTime: Date?,
    @SerializedName("date")
    val date: Date?,
    @SerializedName("parts")
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
                return oldItem.course == newItem.course &&
                        oldItem.classCourseId == newItem.classCourseId &&
                        oldItem.sequence == newItem.sequence &&
                        oldItem.createTime == newItem.createTime
            }

        }
    }
}

/*************** BEGIN GRADES API ENDPOINT POJO ***************/

data class FullGrade(
    @SerializedName("descriptive")
    val descriptive: Boolean,
    @SerializedName("date")
    val date: String,
    @SerializedName("createDate")
    val createDate: String,
    @SerializedName("fullGrade")
    val fullGrade: String,
    @SerializedName("grade")
    val grade: Int,
    @SerializedName("gradeCategory")
    val gradeCategory: String,
    @SerializedName("note")
    val note: String?,
    @SerializedName("schoolyearPartId")
    val schoolyearPartId: String?,
    @SerializedName("evaluationElement")
    val evaluationElement: EvaluationElement?
)

data class FinalGrade(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: Int,
    @SerializedName("schoolyear_part_id")
    val schoolyearPartId: Int,
    @SerializedName("engagement")
    val engagement: String?
)

data class FullGradesCollection(
    @SerializedName("grades")
    val grades: List<FullGrade>,
    @SerializedName("final")
    val final: FinalGrade?,
    @SerializedName("average")
    val average: Float
)

data class FullGrades(
    @SerializedName("course")
    val course: String,
    @SerializedName("classCourseId")
    val classCourseId: Long,
    @SerializedName("classCourseGradeTypeId")
    val classCourseGradeTypeId: Int,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("parts")
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
    @SerializedName("classCourseId")
    val classCourseId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("sequence")
    val sequence: String,
    @SerializedName("absentStatuses")
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

data class AbsentStatus(
    @SerializedName("statusId") val statusId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("absents") val absents: List<AbsentClassDetails>
)

data class AbsentClassDetails(
    @SerializedName("id")
    val id: Long,
    @SerializedName("workHourId")
    val workHourId: Long,
    @SerializedName("teacherNote")
    val teacherNote: String?,
    @SerializedName("statusName")
    val statusName: String?,
    @SerializedName("workHourNote")
    val workHourNote: String?,
    @SerializedName("workdayDate")
    val workdayDate: String
)

data class Absent(
    @SerializedName("classCourseName")
    val classCourseName: String,
    @SerializedName("absentStatus")
    val absentStatus: String,
    @SerializedName("absentStatusId")
    val absentStatusId: Int,
    @SerializedName("absentDetails")
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
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("schoolyearPartId")
    val schoolyearPartId: Int,
    @SerializedName("note")
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

data class Teacher(
    @SerializedName("name") val name: String,
    @SerializedName("consultationInfo") val consultationInfo: String?
)

data class MainCourse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("teachers")
    val teachers: List<Teacher>,
    @SerializedName("subCourses")
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
    @SerializedName("id")
    val id: Long,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("parentClassCourseId")
    val parentClassCourseId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("teachers")
    val teachers: List<Teacher>
)


/*************** END COURSES API ENDPOINT POJO ***************/


enum class StatusCode { ERROR, LOADING, FINISHED }

class Status<T>(
    val statusValue: StatusCode,
    val result: T?,
    val message: String?,
    val exception: Exception? = null
) {

    constructor(statusValue: StatusCode, message: String?) : this(
        statusValue,
        null,
        message
    )

    constructor(statusValue: StatusCode, exception: Exception? = null) : this(
        statusValue = statusValue,
        result = null,
        message = null,
        exception = exception
    )

    val statusDesc: String = when (statusValue) {
        StatusCode.ERROR -> "Error"
        StatusCode.LOADING -> "Loading"
        StatusCode.FINISHED -> "Finished successful"
    }
}

data class State(val responseCode: Int, val message: String)