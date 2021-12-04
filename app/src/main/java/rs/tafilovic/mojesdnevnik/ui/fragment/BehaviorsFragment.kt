package rs.tafilovic.mojesdnevnik.ui.fragment

import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.presentation.adapter.BehaviorsAdapter
import rs.tafilovic.mojesdnevnik.viewmodel.BehaviorsViewModel
import javax.inject.Inject

class BehaviorsFragment : BaseListFragment() {

    @Inject
    lateinit var viewModel: BehaviorsViewModel

    private val adapter =
        BehaviorsAdapter()

    override fun inject() {
        (requireContext().applicationContext as MyApp).appComponent().inject(this)
    }

    override fun init(page: Int, timelineParams: TimelineParams) {

        binding.recycler.adapter = adapter

        viewModel.liveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            onLoadingStatusChanged(it, adapter.itemCount)
        }

        viewModel.get(timelineParams)
    }
}