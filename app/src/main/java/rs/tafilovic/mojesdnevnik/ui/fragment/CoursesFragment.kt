package rs.tafilovic.mojesdnevnik.ui.fragment

import androidx.lifecycle.Observer
import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.presentation.adapter.CoursesAdapter
import rs.tafilovic.mojesdnevnik.viewmodel.CoursesViewModel
import javax.inject.Inject

class CoursesFragment : BaseListFragment() {
    @Inject
    lateinit var viewModel: CoursesViewModel

    private val adapter =
        CoursesAdapter()

    override fun inject() {
        (requireContext().applicationContext as MyApp).appComponent().inject(this)
    }

    override fun init(page: Int, timelineParams: TimelineParams) {

        binding.recycler.adapter = adapter

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            showMessage(it.message)
        })

        viewModel.get(timelineParams)
    }
}