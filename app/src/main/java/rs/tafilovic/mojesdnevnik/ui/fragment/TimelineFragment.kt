package rs.tafilovic.mojesdnevnik.ui.fragment

import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.presentation.adapter.TimelineAdapter
import rs.tafilovic.mojesdnevnik.util.Logger
import rs.tafilovic.mojesdnevnik.viewmodel.TimelineViewModel
import javax.inject.Inject


class TimelineFragment : BaseListFragment() {

    @Inject
    lateinit var model: TimelineViewModel

    override fun inject() {
        (requireContext().applicationContext as MyApp).appComponent().inject(this)
    }

    override fun init(page: Int, timelineParams: TimelineParams) {
        Logger.d(TAG, "init()")
        val adapter = TimelineAdapter()
        binding.recycler.adapter = adapter

        model.eventLiveData.observe(viewLifecycleOwner) { adapter.submitList(it) }

        model.getState().observe(viewLifecycleOwner) {
            it?.let {
                updateState(it.statusValue)
                onLoadingStatusChanged(it, adapter.itemCount)
            }
        }

        model.getTimeline(timelineParams)
    }
}