package rs.tafilovic.mojesdnevnik.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rs.tafilovic.mojesdnevnik.model.Absent
import rs.tafilovic.mojesdnevnik.model.Behavior
import rs.tafilovic.mojesdnevnik.model.FullGrades
import rs.tafilovic.mojesdnevnik.model.MainCourse
import rs.tafilovic.mojesdnevnik.model.School
import rs.tafilovic.mojesdnevnik.model.SchoolClass
import rs.tafilovic.mojesdnevnik.model.SchoolYear
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.model.SubjectActivity
import rs.tafilovic.mojesdnevnik.model.Timeline
import rs.tafilovic.mojesdnevnik.rest.ApiClient
import rs.tafilovic.mojesdnevnik.rest.SessionManager
import rs.tafilovic.mojesdnevnik.util.Logger

class Repository(
    private val apiClient: ApiClient,
    private val sessionManager: SessionManager
) {
    private val TAG = this::class.java.name

    var students: List<Student>? = null
        private set

    lateinit var cookies: String

    fun setIsConnected(connected: Boolean) {
        ApiClient.isConnected.set(connected)
    }

    suspend fun login(): String? {
        val cookies = sessionManager.getCookies()
        if (cookies != null) {
            ApiClient.cookies = cookies
            return cookies
        }
        return null
    }

    suspend fun login(username: String, password: String, rememberMe: Boolean): String {
        Logger.d(TAG, "login(username: $username, password: $password)")
        return sessionManager.getCookies(username, password, rememberMe)
    }

    suspend fun getStudents(
        cookies: String,
    ): Status<List<Student>> {
        Logger.d(TAG, "Cookies: $cookies")

        ApiClient.cookies = cookies
        val response = apiClient.getApiService().getStudents()
        return if (response.isSuccessful) {
            students = response.body()?.data
            students?.forEach { student ->
                student.schools.forEach { entrySchool: Map.Entry<String, School> ->
                    entrySchool.value.id = entrySchool.key
                    entrySchool.value.schoolyears.forEach { entrySchoolYear: Map.Entry<String, SchoolYear> ->
                        entrySchoolYear.value.classes.forEach { entrySchoolClass: Map.Entry<String, SchoolClass> ->
                            entrySchoolClass.value.id = entrySchoolClass.key
                        }
                    }
                }
            }
            Status(StatusCode.FINISHED, students, null)
        } else {
            Status(StatusCode.ERROR, response.message())
        }
    }

    suspend fun getTimeline(
        studentId: String,
        schoolId: String,
        classId: String,
        pageSize: Int,
        page: Int,
    ): Status<Timeline> {
        val response = apiClient.getApiService().getTimeline(
            studentId,
            pageSize,
            page,
            schoolId,
            classId
        )

        return if (response.isSuccessful) {
            val data = response.body()
            Status(StatusCode.FINISHED, data, null)
        } else {
            Status(StatusCode.ERROR, response.message())
        }
    }

    suspend fun getActivities(studentClassId: String?): Status<List<SubjectActivity>> {
        return if (studentClassId.isNullOrEmpty()) {
            Status(StatusCode.ERROR, "Student nije izabran ili nije mogao biti učitan.")
        } else {
            val response = apiClient.getApiService().getActivities(studentClassId)
            if (response.isSuccessful) {
                val data = response.body()
                Status(StatusCode.FINISHED, data, null)
            } else {
                Status(StatusCode.ERROR, response.message())
            }
        }
    }

    suspend fun getGrades(studentClassId: String?): Status<List<FullGrades>> {
        return if (studentClassId.isNullOrEmpty()) {
            Status(StatusCode.ERROR, "Ocene ne mogu biti učitane.")
        } else {
            val response = apiClient.getApiService().getGrades(studentClassId)
            if (response.isSuccessful) {
                Status(StatusCode.FINISHED, response.body(), null)
            } else {
                Status(StatusCode.ERROR, response.message())
            }
        }
    }

    suspend fun getAbsents(studentClassId: String?): Status<List<Absent>?> {
        return if (studentClassId.isNullOrEmpty()) {
            Status(StatusCode.ERROR, "Izostanci ne mogu biti učitani.")
        } else {
            val response = apiClient.getApiService().getAbsents(studentClassId)
            if (response.isSuccessful) {
                val data = response.body()?.values?.toList()
                val absents = arrayListOf<Absent>()

                if (data != null)
                    for (i in data)
                        absents.addAll(i.map())
                Status(StatusCode.FINISHED, absents, null)
            } else {
                Status(StatusCode.ERROR, response.message())
            }
        }
    }

    suspend fun getBehaviors(studentClassId: String?): Status<List<Behavior>> {
        return if (studentClassId.isNullOrEmpty()) {
            Status(StatusCode.ERROR, "Vladanje ne može biti učitano.")
        } else {
            val response = apiClient.getApiService().getBehaviors(studentClassId)
            if (response.isSuccessful) {
                Status(StatusCode.FINISHED, response.body(), null)
            } else {
                Status(StatusCode.ERROR, response.message())
            }
        }
    }

    suspend fun getCourses(studentClassId: String?): Status<List<MainCourse>>{
        return if (studentClassId.isNullOrEmpty()) {
            Status(StatusCode.ERROR, "Vladanje ne može biti učitano.")
        } else {
            val response = apiClient.getApiService().getCourses(studentClassId)
            if (response.isSuccessful) {
                Status(StatusCode.FINISHED, response.body(), null)
            } else {
                Status(StatusCode.ERROR, response.message())
            }
        }
    }
}