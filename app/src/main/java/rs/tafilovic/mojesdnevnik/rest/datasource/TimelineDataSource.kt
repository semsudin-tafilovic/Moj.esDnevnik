package rs.tafilovic.mojesdnevnik.rest.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import rs.tafilovic.mojesdnevnik.model.Event
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.StudentSchoolYear
import rs.tafilovic.mojesdnevnik.repository.Repository
import rs.tafilovic.mojesdnevnik.util.Logger

class TimelineDataSource(
    private val scope: CoroutineScope,
    private val repository: Repository,
    private val studentSchoolYear: StudentSchoolYear?
) : PageKeyedDataSource<Int, Event>() {

    val TAG = this::class.java.name

    var status = MutableLiveData<Status<Event>>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Event>
    ) {
        scope.launch {
            studentSchoolYear?.let {
                val studentId = it.student?.id.toString()
                val schoolId = it.student?.getSchoolId().toString()
                val classId = it.schoolYear?.classes?.keys?.first().toString()

                Logger.d(TAG, "loadInitial()")
                status.postValue(Status(StatusCode.LOADING))
                repository.getTimeline(studentId, schoolId, classId, params.requestedLoadSize, 1) {
                    val result = it.result?.data?.values?.flatten() ?: listOf()
                    Logger.d(TAG, "loadInitial() - result: $result")
                    callback.onResult(result.sortedByDescending { i -> i.date }, null, 2)
                    status.postValue(Status(it.statusValue))
                }
            }

        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {
        scope.launch {
            studentSchoolYear?.let {

                val studentId = it.student?.id.toString()
                val schoolId = it.student?.getSchoolId().toString()
                val classId = it.schoolYear?.classes?.keys?.first().toString()

                status.postValue(Status(StatusCode.LOADING))
                Logger.d(TAG, "loadAfter()")
                repository.getTimeline(
                    studentId,
                    schoolId,
                    classId,
                    params.requestedLoadSize,
                    params.key
                ) {
                    val result = it.result?.data?.values?.flatten() ?: listOf()
                    Logger.d(TAG, "loadAfter() - result: $result")
                    callback.onResult(result.sortedByDescending { i -> i.date }, params.key + 1)
                    status.postValue(Status(it.statusValue))
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {
        TODO("Not yet implemented")
    }
}