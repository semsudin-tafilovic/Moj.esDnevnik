package rs.tafilovic.mojesdnevnik.rest

import android.os.Handler
import android.os.Looper
import org.jsoup.Jsoup
import rs.tafilovic.mojesdnevnik.model.Cookie
import rs.tafilovic.mojesdnevnik.model.Session
import rs.tafilovic.mojesdnevnik.util.Logger
import rs.tafilovic.mojesdnevnik.util.PrefsHelper
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.net.*

class SessionManager(private val prefsHelper: PrefsHelper) {

    private val TAG: String = this.javaClass.name

    private val GET = "GET"
    private val POST = "POST"
    private val login_url = "https://moj.esdnevnik.rs/login"
    private val username_text = "username";
    private val password_text = "password";
    private val token_text = "_token"
    private val user_agent_text = "User-Agent"
    private val userAgent =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36"

    private lateinit var cookieManager: CookieManager

    private fun get(): String? {
        try {
            cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            CookieHandler.setDefault(cookieManager)

            val url = URL(login_url)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

            conn.apply {
                addRequestProperty(
                    user_agent_text,
                    userAgent
                )
                addRequestProperty("Accept", "*/*")
                doInput = true
                requestMethod = GET
            }

            val stream = BufferedInputStream(conn.inputStream)
            return readStream(stream)
        } catch (e: Exception) {
            Logger.e(TAG, e.message)
        }
        return null
    }

    private fun post(params: HashMap<String, String>): Session {
        val url = URL(login_url)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

        conn.apply {
            addRequestProperty(
                user_agent_text,
                userAgent
            )
            addRequestProperty("Accept", "*/*")
            doInput = true
            doOutput = true
            requestMethod = POST
        }

        val sb = StringBuffer()
        var i = 0
        for (p in params) {
            sb.append(p.key).append("=").append(p.value)
            if (i < params.size - 1)
                sb.append("&")
            i++
        }
        Logger.i(TAG, "Post params: $sb")

        val postParams = sb.toString().toByteArray(Charsets.UTF_8)
        val outputStream = conn.outputStream
        outputStream.write(postParams)

        val inputStream = BufferedInputStream(conn.inputStream)
        val html = readStream(inputStream)

        //Log.i(TAG, "Post Login html: $html")

        val cookies = cookieManager.cookieStore.cookies
        val parsedCookies = ArrayList<Cookie>()
        cookies.forEach {
            parsedCookies.add(
                Cookie(
                    it.name,
                    it.value,
                    System.currentTimeMillis() + it.maxAge * 1000
                )
            )
        }

        Logger.d(TAG, "POST RESPONSE CODE: ${conn.responseCode}")

        return Session(html, parsedCookies)
    }

    suspend fun getCookies(): String? {

        val username = prefsHelper.getString(Cookie.USERNAME)
        val password = prefsHelper.getString(Cookie.PASSWORD)

        if (username.isNullOrEmpty() || password.isNullOrEmpty())
            return null

        val getHtml = get() ?: return null
        val token = selectToken(getHtml)

        val params = HashMap<String, String>()
        params[username_text] = username
        params[password_text] = password
        params[token_text] = token

        val session = post(params)

        prefsHelper.setString(Cookie.COOKIE, session.getCookiesFormatted())
        prefsHelper.setLong(Cookie.COOKIE_EXPIRE, session.cookies[0].expirationTime)

        return session.getCookiesFormatted()
    }

    suspend fun getCookies(username: String, password: String, rememberMe: Boolean): String? {
        Logger.d(
            TAG,
            "getCookies(username: $username, password: $password, rememberMe:$rememberMe)"
        )
        val getHtml = get() ?: return null
        val token = selectToken(getHtml)
        Logger.i(TAG, "Token: $token")

        val params = HashMap<String, String>()
        params[username_text] = username
        params[password_text] = password
        params[token_text] = token

        val session = post(params)

        prefsHelper.setString(Cookie.COOKIE, session.getCookiesFormatted())
        prefsHelper.setLong(Cookie.COOKIE_EXPIRE, session.cookies[0].expirationTime)

        if (rememberMe) {
            prefsHelper.setString(Cookie.USERNAME, username)
            prefsHelper.setString(Cookie.PASSWORD, password)
        }

        return session.getCookiesFormatted()
    }

    private fun selectToken(html: String): String {
        val document = Jsoup.parse(html)
        return document.select("body > div.main-wrap > div > div.login-wrap-right > div.tab.active > form > input[type=hidden]")
            .first().attr("value")
    }

    private fun readStream(inputStream: InputStream): String {
        val reader = BufferedReader(inputStream.reader(Charsets.UTF_8))
        var output = ""
        reader.use {
            output = it.readText()
        }
        return output
    }

/*
    private fun loginWithJsoup() {
        Thread(Runnable {

            val formData = HashMap<String, String>()
            val cookies = HashMap<String, String>()
            val userAgent =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36";

            val loginForm =
                Jsoup.connect(login_url).method(Connection.Method.GET).userAgent(userAgent)
                    .execute()
            val loginDoc = loginForm.parse()

            cookies.putAll(loginForm.cookies())

            val authToken =
                loginDoc.select("body > div.main-wrap > div > div.login-wrap-right > div.tab.active > form > input[type=hidden]")
                    .first().attr("value")

            // Log.i(TAG, "Token expiration: " + Date(authToken.expirationTime))
            Log.i(TAG, "Auth token: $authToken")
            formData["commit"] = "Sign in"
            formData["utf8"] = "e2 9c 93"
            formData[username_text] = username
            formData[password_text] = password
            formData["_token"] = authToken

            val homePage = Jsoup.connect(login_url)
                .cookies(cookies)
                .data(formData)
                .method(Connection.Method.POST)
                .userAgent(userAgent)
                .execute()

            val homePageDoc = homePage.parse()

            val sb = StringBuffer()
            val cookies2 = homePage.cookies()

           val response = ApiClient.getService(cookies2).getStudents().execute()


            runOnUiThread {
                if (response.isSuccessful) {
                    val studentType = object : TypeToken<List<Student>>() {}.type
                    val jsonArray = JSONObject(response.body()!!).get("data")
                    val student = Gson().fromJson<List<Student>>(jsonArray.toString(), studentType)
                    tvText.setText(student.toString())
                } else {
                    tvText.setText(response.message())
                }
            }
        }).start()
    }*/
}