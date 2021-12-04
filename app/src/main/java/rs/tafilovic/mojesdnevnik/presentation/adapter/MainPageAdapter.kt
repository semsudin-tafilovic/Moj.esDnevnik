package rs.tafilovic.mojesdnevnik.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import rs.tafilovic.mojesdnevnik.model.TimelineParams
import rs.tafilovic.mojesdnevnik.ui.fragment.*

class MainPageAdapter(fragmentActivity: FragmentActivity, var timelineParams: TimelineParams?) :
    FragmentStateAdapter(fragmentActivity) {

    val items = arrayOf(
        "Vremenska linija",
        "Ocene",
        "Aktivnosti",
        "Izostanci",
        /*"Pohvale i mere",*/
        "Vladanje",
        "Svi predmeti"
    )

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> BaseListFragment.getInstance<GradesFragment>(position, timelineParams)
            2 -> BaseListFragment.getInstance<ActivitiesFragment>(position, timelineParams)
            3 -> BaseListFragment.getInstance<AbsentsFragment>(position, timelineParams)
            4 -> BaseListFragment.getInstance<BehaviorsFragment>(position, timelineParams)
            5 -> BaseListFragment.getInstance<CoursesFragment>(position, timelineParams)
            else -> BaseListFragment.getInstance<TimelineFragment>(position, timelineParams)
        }
    }

}