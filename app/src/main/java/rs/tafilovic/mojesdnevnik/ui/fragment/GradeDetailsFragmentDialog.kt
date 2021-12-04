package rs.tafilovic.mojesdnevnik.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.FullGrade
import rs.tafilovic.mojesdnevnik.presentation.adapter.GradeDetailsAdapter

class GradeDetailsFragmentDialog : DialogFragment() {
    companion object {
        val TAG = GradeDetailsFragmentDialog::class.java.name
    }

    val adapter: GradeDetailsAdapter = GradeDetailsAdapter()

    lateinit var recycler: RecyclerView

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
        recycler = view.findViewById(R.id.recycler)
        recycler.adapter = adapter
    }

    fun submitItems(grades: List<FullGrade>) {
        adapter.submitItems(grades)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler.adapter = null
    }
}