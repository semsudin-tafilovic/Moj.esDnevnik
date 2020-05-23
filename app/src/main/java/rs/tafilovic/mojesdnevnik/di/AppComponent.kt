package rs.tafilovic.mojesdnevnik.di

import dagger.Component
import rs.tafilovic.mojesdnevnik.ui.activitiy.LoginActivity
import rs.tafilovic.mojesdnevnik.ui.activitiy.MainActivity
import rs.tafilovic.mojesdnevnik.ui.fragment.*
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(loginActivity: LoginActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(timelineFragment: TimelineFragment)
    fun inject(activitiesFragment: ActivitiesFragment)
    fun inject(gradesFragment: GradesFragment)
    fun inject(absentsFragment: AbsentsFragment)
    fun inject(behaviorsFragment: BehaviorsFragment)
    fun inject(coursesFragment:CoursesFragment)
}