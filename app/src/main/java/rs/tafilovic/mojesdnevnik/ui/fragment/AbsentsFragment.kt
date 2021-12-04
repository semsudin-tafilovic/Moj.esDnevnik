package rs.tafilovic.mojesdnevnik.ui.fragment

import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.presentation.adapter.AbsentsAdapter
import rs.tafilovic.mojesdnevnik.viewmodel.AbsentsViewModel
import javax.inject.Inject

class AbsentsFragment : BaseListFragment() {

    @Inject
    lateinit var viewModel: AbsentsViewModel

    private val adapter = AbsentsAdapter()

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