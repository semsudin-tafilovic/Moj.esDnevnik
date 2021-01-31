package rs.tafilovic.mojesdnevnik.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rs.tafilovic.mojesdnevnik.model.*
import rs.tafilovic.mojesdnevnik.rest.ApiClient
import rs.tafilovic.mojesdnevnik.rest.SessionManager
import rs.tafilovic.mojesdnevnik.util.Logger

class Repository(
    private val apiClient: ApiClient,
    private val sessionManager: SessionManager
) {
    private val TAG = this::class.java.name

    var students: List<Student>? = null
        get
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

    suspend fun login(username: String, password: String, rememberMe: Boolean): String? {
        Logger.d(TAG, "login(username: $username, password: $password)")
        return sessionManager.getCookies(username, password, rememberMe)
    }

    fun getStudents(cookies: String, onResult: (Status<List<Student>>) -> Unit) {
        Logger.d(TAG, "Cookies: $cookies")

        ApiClient.cookies = cookies
        apiClient.getApiService().getStudents().enqueue(object : Callback<Students> {
            override fun onFailure(call: Call<Students>, t: Throwable) {
                notifyError(t, onResult)
            }

            override fun onResponse(call: Call<Students>, response: Response<Students>) {
                if (response.isSuccessful) {
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
                    onResult(Status(StatusCode.FINISHED, students, null))
                } else {
                    onResult(Status(StatusCode.ERROR, response.message()))
                }
            }
        })

    }

    fun getTimeline(
        studentId: String,
        schoolId: String,
        classId: String,
        pageSize: Int,
        page: Int,
        onResult: (Status<Timeline>) -> Unit
    ) {
        apiClient.getApiService().getTimeline(
            studentId,
            pageSize,
            page,
            schoolId,
            classId
        ).enqueue(object : Callback<Timeline> {
            override fun onFailure(call: Call<Timeline>, t: Throwable) {
                Logger.d(TAG, "onFailure() - error: " + t.message)
                notifyError(t, onResult)
            }

            override fun onResponse(call: Call<Timeline>, response: Response<Timeline>) {
                Logger.d(TAG, "onResponse() - response: " + response.body())
                notifyResult(response, onResult)
            }
        })
    }

    fun getActivities(studentClassId: String?, onResult: (Status<List<SubjectActivity>>) -> Unit) {
        studentClassId?.let {
            apiClient.getApiService().getActivities(studentClassId)
                .enqueue(object : Callback<List<SubjectActivity>> {
                    override fun onFailure(call: Call<List<SubjectActivity>>, t: Throwable) {
                        notifyError(t, onResult)
                    }

                    override fun onResponse(
                        call: Call<List<SubjectActivity>>,
                        response: Response<List<SubjectActivity>>
                    ) {
                        notifyResult(response, onResult)
                    }
                })
        }
    }

    fun getGrades(studentClassId: String?, onResult: (Status<List<FullGrades>>) -> Unit) {
        studentClassId?.let {
            apiClient.getApiService().getGrades(studentClassId)
                .enqueue(object : Callback<List<FullGrades>> {
                    override fun onFailure(call: Call<List<FullGrades>>, t: Throwable) {
                        Logger.e(TAG, "getGrades() - error: "+t.message);
                        notifyError(t, onResult)
                    }

                    override fun onResponse(
                        call: Call<List<FullGrades>>,
                        response: Response<List<FullGrades>>
                    ) {
                        notifyResult(response, onResult)
                    }
                })
        }
    }

    fun getAbsents(studentClassId: String?, onResult: (Status<List<Absent>?>) -> Unit) {
        studentClassId?.let {
            apiClient.getApiService().getAbsents(studentClassId)
                .enqueue(object : Callback<HashMap<String, AbsentClass>> {
                    override fun onFailure(call: Call<HashMap<String, AbsentClass>>, t: Throwable) {
                        notifyError(t, onResult)
                    }

                    override fun onResponse(
                        call: Call<HashMap<String, AbsentClass>>,
                        response: Response<HashMap<String, AbsentClass>>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()?.values?.toList()
                            val absents = arrayListOf<Absent>()

                            if (data != null)
                                for (i in data)
                                    absents.addAll(i.map())

                            onResult(Status(StatusCode.FINISHED, absents, null))
                        } else {
                            onResult(Status(StatusCode.ERROR, null, response.message()))
                        }
                    }
                })
        }
    }

    fun getBehaviors(studentClassId: String?, onResult: (Status<List<Behavior>>) -> Unit) {
        studentClassId?.let {
            apiClient.getApiService().getBehaviors(studentClassId)
                .enqueue(object : Callback<List<Behavior>> {

                    override fun onFailure(call: Call<List<Behavior>>, t: Throwable) {
                        notifyError(t, onResult)
                    }

                    override fun onResponse(
                        call: Call<List<Behavior>>,
                        response: Response<List<Behavior>>
                    ) {
                        notifyResult(response, onResult)
                    }
                })
        }
    }

    fun getCourses(studentClassId: String?, onResult: (Status<List<MainCourse>>) -> Unit) {
        studentClassId?.let {
            apiClient.getApiService().getCourses(studentClassId)
                .enqueue(object : Callback<List<MainCourse>> {

                    override fun onFailure(call: Call<List<MainCourse>>, t: Throwable) {
                        notifyError(t, onResult)
                    }

                    override fun onResponse(
                        call: Call<List<MainCourse>>,
                        response: Response<List<MainCourse>>
                    ) {
                        notifyResult(response, onResult)
                    }
                })
        }

    }

    private fun <T> notifyError(t: Throwable, onError: (Status<T>) -> Unit) {
        Logger.e(TAG, "notifyError: ${t.message}")
        onError(Status(StatusCode.ERROR, t.message))
    }

    private fun <T> notifyResult(response: Response<T>, onResult: (Status<T>) -> Unit) {
        Logger.d(
            TAG,
            "notifyResult - success: ${response.isSuccessful}, responseCode: ${response.code()}"
        )
        if (response.isSuccessful) {
            val data = response.body()
            onResult(Status(StatusCode.FINISHED, data, null))
        } else {
            onResult(Status(StatusCode.ERROR, response.message()))
        }

    }
}