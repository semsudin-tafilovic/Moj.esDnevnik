package rs.tafilovic.mojesdnevnik.rest

import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rs.tafilovic.mojesdnevnik.model.AbsentClass
import rs.tafilovic.mojesdnevnik.model.Behavior
import rs.tafilovic.mojesdnevnik.model.FullGrades
import rs.tafilovic.mojesdnevnik.model.MainCourse
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.model.Students
import rs.tafilovic.mojesdnevnik.model.SubjectActivity
import rs.tafilovic.mojesdnevnik.model.Timeline
import rs.tafilovic.mojesdnevnik.util.Logger
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


interface ApiService {
    @GET("students")
    suspend fun getStudents(): retrofit2.Response<Students>

    @GET("timeline/{studentId}")
    suspend fun getTimeline(
        @Path("studentId") studentId: String,
        @Query("take") take: Int,
        @Query("page") page: Int,
        @Query("school") schoolId: String,
        @Query("class") classId: String
    ): retrofit2.Response<Timeline>

    @GET("activities/{studentClassId}")
    suspend fun getActivities(@Path("studentClassId") studentClassId: String): retrofit2.Response<List<SubjectActivity>>

    @GET("grades/{studentClassId}")
    suspend fun getGrades(@Path("studentClassId") studentClassId: String): retrofit2.Response<List<FullGrades>>

    @GET("absents/{studentClassId}")
    suspend fun getAbsents(@Path("studentClassId") studentClassId: String): retrofit2.Response<HashMap<String, AbsentClass>>

    @GET("behavior/{studentClassId}")
    suspend fun getBehaviors(@Path("studentClassId") studentClassId: String): retrofit2.Response<List<Behavior>>

    @GET("courses/{studentClassId}")
    suspend fun getCourses(@Path("studentClassId") studentClassId: String): retrofit2.Response<List<MainCourse>>
}

private val HEADER_PRAGMA = "Pragma"
private val HEADER_CACHE_CONTROL = "Cache-Control"
private val NO_AUTHENTICATION = "No-Authentication"


class CookieInterceptor : Interceptor {

    val TAG = this.javaClass.name
    override fun intercept(chain: Interceptor.Chain): Response {
        Logger.d(TAG, "cookieInterceptor()")
        val builder = chain.request().newBuilder()
        Logger.i(TAG, "Cookie: ${ApiClient.cookies}")
        if (chain.request().header(NO_AUTHENTICATION) == null)
            builder.addHeader("Cookie", ApiClient.cookies)
        return chain.proceed(builder.build())
    }
}

class OfflineInterceptor : Interceptor {
    val TAG = this.javaClass.name

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val isConnected = ApiClient.isConnected.get()
        if (!isConnected) {

            Logger.d(TAG, "offlineInterceptor()")
            val cacheControl = CacheControl.Builder()
                .maxStale(1, TimeUnit.HOURS)
                .build()

            request = request.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .cacheControl(cacheControl)
                .build()

        }
        return chain.proceed(request)
    }
}

class NetworkInterceptor : Interceptor {
    private val TAG = this.javaClass.name
    override fun intercept(chain: Interceptor.Chain): Response {
        Logger.d(TAG, "networkInterceptor()")

        val response = chain.proceed(chain.request())

        val cacheControl = CacheControl.Builder()
            .maxAge(1, TimeUnit.HOURS)
            .build()

        return response.newBuilder()
            .removeHeader(HEADER_PRAGMA)
            .removeHeader(HEADER_CACHE_CONTROL)
            .header(HEADER_CACHE_CONTROL, cacheControl.toString())
            .build()
    }
}


class ApiClient(cacheDir: File) {

    private val TAG = ApiClient::class.java

    private val cacheSize = (10 * 1024 * 1024).toLong()
    private val responseDir = File(cacheDir, "response")
    private val apiCache = Cache(responseDir, cacheSize)


    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val cookieInterceptor = CookieInterceptor()

    private val networkInterceptor = NetworkInterceptor()

    private val offlineInterceptor = OfflineInterceptor()

    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .cache(apiCache)
        .addInterceptor(cookieInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(networkInterceptor)
        .addInterceptor(offlineInterceptor)
        .build()

    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://moj.esdnevnik.rs/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val service: ApiService = retrofit.create(ApiService::class.java)

    fun getApiService() = service

    companion object {
        var cookies: String = ""
        var isConnected: AtomicBoolean = AtomicBoolean(false)
    }
}
