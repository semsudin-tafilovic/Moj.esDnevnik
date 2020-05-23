package rs.tafilovic.mojesdnevnik.rest

import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rs.tafilovic.mojesdnevnik.model.*
import rs.tafilovic.mojesdnevnik.util.Logger
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


interface ApiService {
    @GET("students")
    fun getStudents(): Call<Students>

    @GET("timeline/{studentId}")
    fun getTimeline(
        @Path("studentId") studentId: String,
        @Query("take") take: Int,
        @Query("page") page: Int,
        @Query("school") schoolId: String,
        @Query("class") classId: String
    ): Call<Timeline>

    @GET("activities/{studentClassId}")
    fun getActivities(@Path("studentClassId") studentClassId: String): Call<List<SubjectActivity>>

    @GET("grades/{studentClassId}")
    fun getGrades(@Path("studentClassId") studentClassId: String): Call<List<FullGrades>>

    @GET("absents/{studentClassId}")
    fun getAbsents(@Path("studentClassId") studentClassId: String): Call<HashMap<String, AbsentClass>>

    @GET("behavior/{studentClassId}")
    fun getBehaviors(@Path("studentClassId") studentClassId: String): Call<List<Behavior>>

    @GET("courses/{studentClassId}")
    fun getCourses(@Path("studentClassId") studentClassId: String): Call<List<MainCourse>>
}

private val HEADER_PRAGMA = "Pragma"
private val HEADER_CACHE_CONTROL = "Cache-Control"
private val NO_AUTHENTICATION = "No-Authentication"


class CookieInterceptor() : Interceptor {

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
        .cache(apiCache)
        .addInterceptor(cookieInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        //.addNetworkInterceptor(networkInterceptor)
        .addInterceptor(offlineInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
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
