package rs.tafilovic.mojesdnevnik.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.StudentSchoolYear
import rs.tafilovic.mojesdnevnik.ui.activitiy.BaseActivity
import rs.tafilovic.mojesdnevnik.ui.activitiy.MainActivity
import rs.tafilovic.mojesdnevnik.util.Logger

abstract class BaseListFragment : Fragment() {
    val TAG = this.javaClass.name


    companion object {
        val PAGE_KEY = "page"
        val STUDENT_YEAR_KEY = "student_school_year"

        inline fun <reified T : BaseListFragment> getInstance(
            page: Int,
            studentSchoolYear: StudentSchoolYear
        ): T {
            val bundle = Bundle().apply {
                putInt(PAGE_KEY, page)
                putParcelable(STUDENT_YEAR_KEY, studentSchoolYear)
            }

            val fragment = T::class.java.newInstance().apply {
                arguments = bundle
            }

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    abstract fun inject()

    abstract fun init(page: Int, studentSchoolYear: StudentSchoolYear)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        val page = arguments?.getInt(PAGE_KEY) ?: return
        val studentSchoolYear =
            arguments?.getParcelable<StudentSchoolYear>(STUDENT_YEAR_KEY) ?: return

        init(page, studentSchoolYear)
    }

    protected fun showMessage(msg: String?) {
        Logger.d(TAG, "showMessage: $msg")
        if (activity is BaseActivity)
            (activity as BaseActivity).showToast(msg)
    }

    protected fun updateState(statsCode: StatusCode) {
        Logger.d(TAG, "updateStatus() - status: $statsCode")
        if (activity is MainActivity) {
            (activity as MainActivity).progressLoader.visibility =
                if (statsCode == StatusCode.LOADING) View.VISIBLE else View.GONE
        }
    }
}