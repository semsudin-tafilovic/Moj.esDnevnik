package rs.tafilovic.mojesdnevnik.rest.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.tafilovic.mojesdnevnik.model.Event
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.repository.Repository
import rs.tafilovic.mojesdnevnik.util.Logger

class TimelineDataSource(
    private val scope: CoroutineScope,
    private val repository: Repository,
    private val timelineParamsLiveData: LiveData<TimelineParams>
) : PageKeyedDataSource<Int, Event>() {

    val TAG = this::class.java.name

    var status = MutableLiveData<Status<Event>>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Event>
    ) {

        val timelineParams = timelineParamsLiveData.value

        val studentId = timelineParams?.studentId
        val schoolId = timelineParams?.schoolId
        val classId = timelineParams?.classId

        if (studentId == null || schoolId == null || classId == null) return

        scope.launch(Dispatchers.IO) {
            Logger.d(TAG, "loadInitial()")
            status.postValue(Status(StatusCode.LOADING))
            val timeline = repository.getTimeline(
                studentId,
                schoolId,
                classId,
                params.requestedLoadSize,
                1
            )

            withContext(Dispatchers.Main) {
                val result = timeline.result?.data?.values?.flatten() ?: listOf()
                Logger.d(TAG, "loadInitial() - result: $result")
                callback.onResult(result.sortedByDescending { i -> i.date }, null, 2)
                status.postValue(Status(timeline.statusValue))
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {
        val timelineParams = timelineParamsLiveData.value

        val studentId = timelineParams?.studentId
        val schoolId = timelineParams?.schoolId
        val classId = timelineParams?.classId

        if (studentId == null || schoolId == null || classId == null) return

        scope.launch(Dispatchers.IO) {
            status.postValue(Status(StatusCode.LOADING))
            Logger.d(TAG, "loadAfter()")
            val timeline = repository.getTimeline(
                studentId,
                schoolId,
                classId,
                params.requestedLoadSize,
                params.key
            )
            withContext(Dispatchers.Main) {
                val result = timeline.result?.data?.values?.flatten() ?: listOf()
                Logger.d(TAG, "loadAfter() - result: $result")
                callback.onResult(result.sortedByDescending { i -> i.date }, params.key + 1)
                status.postValue(Status(timeline.statusValue))
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {

    }
}