package rs.tafilovic.mojesdnevnik.ui.fragment

import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.presentation.adapter.ActivitiesAdapter
import rs.tafilovic.mojesdnevnik.viewmodel.ActivitiesViewModel
import javax.inject.Inject

class ActivitiesFragment : BaseListFragment() {

    @Inject
    lateinit var viewModel: ActivitiesViewModel

    private var activityDetailsDialogFragment: ActivityDetailsFragmentDialog? = null

    override fun inject() {
        (requireContext().applicationContext as MyApp).appComponent().inject(this)
    }

    override fun init(page: Int, timelineParams: TimelineParams) {
        val adapter = ActivitiesAdapter { subject ->
            context?.let {
                val items = subject.parts.flatMap {
                    it.value.activities
                }
                if (!items.isNullOrEmpty()) {
                    activityDetailsDialogFragment = ActivityDetailsFragmentDialog().also {
                        it.show(childFragmentManager, ActivityDetailsFragmentDialog.TAG)
                        it.submitItems(items)
                    }
                } else {
                    showMessage(getString(R.string.no_activity_for_selected_class))
                }
            }
        }

        binding.recycler.adapter = adapter

        viewModel.liveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            onLoadingStatusChanged(it, adapter.itemCount)
        }

        viewModel.get(timelineParams)
    }

    override fun onPause() {
        super.onPause()
        activityDetailsDialogFragment?.let { it.dismiss() }
    }
}