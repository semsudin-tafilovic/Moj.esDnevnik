package rs.tafilovic.mojesdnevnik.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import rs.tafilovic.mojesdnevnik.databinding.FragmentListBinding
import rs.tafilovic.mojesdnevnik.model.Status
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.ui.activitiy.BaseActivity
import rs.tafilovic.mojesdnevnik.ui.activitiy.MainActivity
import rs.tafilovic.mojesdnevnik.util.Logger

abstract class BaseListFragment : Fragment() {
    val TAG = this.javaClass.name

    private var _binding: FragmentListBinding? = null

    protected val binding: FragmentListBinding
        get() = _binding!!

    companion object {
        val PAGE_KEY = "page"
        val TIMELINE_PARAMS = "timeline_params"

        inline fun <reified T : BaseListFragment> getInstance(
            page: Int,
            timelineParams: TimelineParams?
        ): T {
            val bundle = Bundle().apply {
                putInt(PAGE_KEY, page)
                putParcelable(TIMELINE_PARAMS, timelineParams)
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

    abstract fun init(page: Int, timelineParams: TimelineParams)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val page = arguments?.getInt(PAGE_KEY) ?: return
        val timelineParams =
            arguments?.getParcelable<TimelineParams>(TIMELINE_PARAMS) ?: return

        init(page, timelineParams)
    }

    protected fun showMessage(msg: String?) {
        Logger.d(TAG, "showMessage: $msg")
        if (activity is BaseActivity)
            (activity as BaseActivity).showToast(msg)
    }

    protected fun updateState(statsCode: StatusCode) {
        Logger.d(TAG, "updateStatus() - status: $statsCode")
        if (activity is MainActivity) {
            (activity as MainActivity).setProgressVisibility(if (statsCode == StatusCode.LOADING) View.VISIBLE else View.GONE)
        }
    }

    protected fun <T> onLoadingStatusChanged(it:Status<T>, itemCount:Int){
        showMessage(it.message)
        if (it.statusValue != StatusCode.LOADING) {
            binding.llNoData.visibility =
                if (itemCount == 0) View.VISIBLE else View.GONE
            Logger.d(TAG, "Items count: $itemCount")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}