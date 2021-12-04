package rs.tafilovic.mojesdnevnik.viewmodel

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import rs.tafilovic.mojesdnevnik.model.Event
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.repository.Repository
import rs.tafilovic.mojesdnevnik.rest.datasource.TimelineDataSource
import rs.tafilovic.mojesdnevnik.rest.datasource.TimelineDataSourceFactory
import rs.tafilovic.mojesdnevnik.util.Logger
import javax.inject.Inject

class TimelineViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val TAG: String = TimelineViewModel::class.java.name

    val eventLiveData: LiveData<PagedList<Event>>
    val dataSourceFactory: TimelineDataSourceFactory

    private val _dataLoadedCount: MutableLiveData<Int> = MutableLiveData(0)
    val dataLoadedCount: LiveData<Int>
        get() = _dataLoadedCount

    private var timelineParams = MutableLiveData<TimelineParams>()
    private val pageSize = 30
    private val initialLoadSizeHint = 30


    init {
        dataSourceFactory =
            TimelineDataSourceFactory(viewModelScope, repository, timelineParams)
        eventLiveData = Transformations.switchMap(timelineParams) { createLiveData() }
    }

    fun getState(): LiveData<Status<Event>> {
        return Transformations.switchMap(
            dataSourceFactory.dataSourceLiveData,
            TimelineDataSource::status
        )
    }

    private fun createLiveData(): LiveData<PagedList<Event>> {
        return LivePagedListBuilder(dataSourceFactory, buildConfig())
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Event>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    Logger.d(TAG, "onZeroItemsLoaded")
                    _dataLoadedCount.postValue(0)
                }

                override fun onItemAtFrontLoaded(itemAtFront: Event) {
                    super.onItemAtFrontLoaded(itemAtFront)
                    Logger.d(TAG, "onItemAtFrontLoaded: $itemAtFront")
                    _dataLoadedCount.postValue(1)
                }

                override fun onItemAtEndLoaded(itemAtEnd: Event) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    Logger.d(TAG, "onItemAtEndLoaded $itemAtEnd")
                    _dataLoadedCount.postValue(1)
                }
            })
            .build()
    }

    fun getTimeline(timelineParams: TimelineParams) {
        this.timelineParams.value = timelineParams
        dataSourceFactory.dataSourceLiveData.value?.invalidate()
    }

    private fun buildConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(initialLoadSizeHint)
            .setPrefetchDistance(2)
            .setEnablePlaceholders(false)
            .build()
    }
}