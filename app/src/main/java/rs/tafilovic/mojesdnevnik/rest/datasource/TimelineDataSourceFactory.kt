package rs.tafilovic.mojesdnevnik.rest.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import rs.tafilovic.mojesdnevnik.model.Event
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.repository.Repository

class TimelineDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: Repository,
    private val timelineParams: LiveData<TimelineParams>
) :
    DataSource.Factory<Int, Event>() {
    val dataSourceLiveData = MutableLiveData<TimelineDataSource>()

    override fun create(): DataSource<Int, Event> {
        val dataSource = TimelineDataSource(
            scope,
            repository,
            timelineParams
        )
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

}