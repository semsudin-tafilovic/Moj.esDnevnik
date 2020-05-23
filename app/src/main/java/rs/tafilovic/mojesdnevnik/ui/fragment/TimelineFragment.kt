package rs.tafilovic.mojesdnevnik.ui.fragment

import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_list.*
import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.model.StudentSchoolYear
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

    override fun init(page: Int, studentSchoolYear: StudentSchoolYear) {
        Logger.d(TAG, "init()")
        model.eventLiveData.observe(viewLifecycleOwner, Observer {
            val adapter = TimelineAdapter()
            recycler.adapter = adapter
            adapter.submitList(it)
        })

        model.getState().observe(viewLifecycleOwner, Observer {
            it?.let {
                updateState(it.statusValue)
            }
        })

        model.getTimeline(studentSchoolYear)
    }
}