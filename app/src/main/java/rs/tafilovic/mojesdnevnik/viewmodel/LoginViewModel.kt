package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.repository.Repository
import rs.tafilovic.mojesdnevnik.util.Logger
import java.net.UnknownHostException
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    val TAG = this::class.java.name

    val stateLiveData = MutableLiveData<Status<List<Student>>>()
    val studentsLiveData = MutableLiveData<List<Student>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cookies = repository.login()
                if (cookies == null) {
                    studentsLiveData.postValue(emptyList())
                    return@launch
                }
                val students = repository.getStudents(cookies)
                onStudentsResponse(students)
            } catch (e: Exception) {
                onStudentsResponse(Status(StatusCode.ERROR, exception = e))
            }
        }
    }

    fun login(username: String?, password: String?, rememberMe: Boolean) {
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            stateLiveData.postValue(Status(StatusCode.ERROR, "Morate uneti korisničko ime i šifru"))
            return
        }
        stateLiveData.postValue(Status(StatusCode.LOADING))
        viewModelScope.launch(Dispatchers.IO) {
            Logger.d(TAG, "login(username: $username, password: $password)")
            try {
                val cookie = repository.login(username, password, rememberMe)
                val students = repository.getStudents(cookie)
                onStudentsResponse(students)
            } catch (e: Exception) {
                onStudentsResponse(Status(StatusCode.ERROR, exception = e))
            }
        }
    }

    private fun onStudentsResponse(it: Status<List<Student>>) {
        when (it.statusValue) {
            StatusCode.LOADING -> {
                stateLiveData.postValue(Status(StatusCode.LOADING))
            }

            StatusCode.FINISHED -> {
                studentsLiveData.postValue(it.result ?: emptyList())
                //stateLiveData.postValue(Status(StatusCode.FINISHED))
            }

            else -> {
                if (it.exception is UnknownHostException) {
                    stateLiveData.postValue(
                        Status(
                            StatusCode.ERROR,
                            "Nema internet konekcije ili je slaba."
                        )
                    )
                } else {
                    stateLiveData.postValue(Status(StatusCode.ERROR, "Uneti podaci nisu tačni."))
                }
            }
        }
    }

}