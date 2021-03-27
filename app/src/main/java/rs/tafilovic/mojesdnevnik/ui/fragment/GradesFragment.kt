package rs.tafilovic.mojesdnevnik.ui.fragment

import androidx.lifecycle.Observer
import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.presentation.adapter.GradesAdapter
import rs.tafilovic.mojesdnevnik.viewmodel.GradesViewModel
import javax.inject.Inject

class GradesFragment : BaseListFragment() {

    private val adapter =
        GradesAdapter()

    @Inject
    lateinit var viewModel: GradesViewModel

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