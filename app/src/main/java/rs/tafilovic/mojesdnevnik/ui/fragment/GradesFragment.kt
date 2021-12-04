package rs.tafilovic.mojesdnevnik.ui.fragment

import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.presentation.adapter.GradesAdapter
import rs.tafilovic.mojesdnevnik.viewmodel.GradesViewModel
import javax.inject.Inject

class GradesFragment : BaseListFragment() {

    lateinit var adapter: GradesAdapter

    @Inject
    lateinit var viewModel: GradesViewModel

    override fun inject() {
        (requireContext().applicationContext as MyApp).appComponent().inject(this)
    }

    override fun init(page: Int, timelineParams: TimelineParams) {
        adapter = GradesAdapter {
            val grades = it.parts.flatMap { it.value.grades }
            if (grades.isNullOrEmpty()) {
                showMessage(getString(R.string.no_grades))
            } else {
                GradeDetailsFragmentDialog().also {
                    it.submitItems(grades)
                    it.show(childFragmentManager, GradeDetailsFragmentDialog.TAG)
                }
            }
        }

        binding.recycler.adapter = adapter
        viewModel.liveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.get(timelineParams)
    }
}