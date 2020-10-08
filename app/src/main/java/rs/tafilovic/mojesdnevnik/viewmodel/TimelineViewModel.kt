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
import javax.inject.Inject

class TimelineViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val eventLiveData: LiveData<PagedList<Event>>
    val dataSourceFactory: TimelineDataSourceFactory

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
        return LivePagedListBuilder(dataSourceFactory, buildConfig()).build()
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