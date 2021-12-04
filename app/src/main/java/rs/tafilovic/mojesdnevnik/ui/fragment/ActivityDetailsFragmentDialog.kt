package rs.tafilovic.mojesdnevnik.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.Activities
import rs.tafilovic.mojesdnevnik.presentation.adapter.ActivityDetailsAdapter


class ActivityDetailsFragmentDialog : DialogFragment() {

    companion object {
        val TAG = ActivityDetailsFragmentDialog::class.java.name
    }

    val adapter: ActivityDetailsAdapter = ActivityDetailsAdapter()

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            val params = it.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            //params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = params
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        recycler.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        recycler.adapter = adapter

        view.setOnClickListener { dismissAllowingStateLoss() }
    }

    fun submitItems(activities: List<Activities>) {
        adapter.submitItems(activities = activities.sortedByDescending { it.createTime })
    }
}