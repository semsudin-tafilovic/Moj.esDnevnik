package rs.tafilovic.mojesdnevnik.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.model.StudentSchoolYear
import rs.tafilovic.mojesdnevnik.ui.fragment.*

class MainPageAdapter(fragmentActivity: FragmentActivity, val studentSchoolYear: StudentSchoolYear) :
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
            1 -> BaseListFragment.getInstance<GradesFragment>(position, studentSchoolYear)
            2 -> BaseListFragment.getInstance<ActivitiesFragment>(position, studentSchoolYear)
            3 -> BaseListFragment.getInstance<AbsentsFragment>(position, studentSchoolYear)
            4 -> BaseListFragment.getInstance<BehaviorsFragment>(position, studentSchoolYear)
            5 -> BaseListFragment.getInstance<CoursesFragment>(position, studentSchoolYear)
            else -> BaseListFragment.getInstance<TimelineFragment>(position, studentSchoolYear)
        }
    }
}