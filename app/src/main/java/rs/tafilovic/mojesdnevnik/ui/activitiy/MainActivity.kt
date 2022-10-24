package rs.tafilovic.mojesdnevnik.ui.activitiy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.databinding.ActivityMainBinding
import rs.tafilovic.mojesdnevnik.model.*
import rs.tafilovic.mojesdnevnik.presentation.adapter.MainPageAdapter
import rs.tafilovic.mojesdnevnik.ui.fragment.StudentsFragment
import rs.tafilovic.mojesdnevnik.util.LocalStore
import rs.tafilovic.mojesdnevnik.util.Logger
import rs.tafilovic.mojesdnevnik.util.PrefsHelper
import rs.tafilovic.mojesdnevnik.viewmodel.MainViewModel
import javax.inject.Inject

@Suppress("PrivatePropertyName")
class MainActivity : BaseActivity() {

    override val TAG = this.javaClass.name

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var prefsHelper: PrefsHelper


    @Inject
    lateinit var localStore: LocalStore

    private lateinit var viewPageAdapter: MainPageAdapter

    private lateinit var students: List<Student>

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApp).appComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        viewPageAdapter = MainPageAdapter(this, null)

        viewModel.studentsMutableLiveDate.observe(this, Observer {
            if (it == null) return@Observer
            Logger.d(TAG, "studentsMutableLiveDate.observer: $it")
            students = it

            val selectedStudent = it.find { student ->
                student.id == localStore.getSelectedStudentId()
            } ?: it.first()

            viewModel.setSelectedStudent(selectedStudent)
        })

        viewModel.selectedStudentLiveData.observe(this, Observer { student ->
            if (student == null) return@Observer

            supportActionBar?.title = student.fullName
        })

        viewModel.selectedSchoolLiveData.observe(this, Observer { school ->
            if (school == null) return@Observer

            binding.chipSchool.text = school.schoolName
        })

        viewModel.selectedSchoolYear.observe(this) {
            binding.chipSchoolYear.text = String.format(
                "%s (%s)",
                it.classes.values.first().section,
                it.year
            )
        }

        viewModel.timelineParamsLiveData.observe(this, Observer {
            if (it == null) return@Observer
            Logger.d(TAG, "timelineParamsLiveData.observer: $it")
            setPagerAdapter(it)
        })

        viewModel.stateMutableLiveData.observe(this, Observer {
            if (it == null) return@Observer
            Logger.d(TAG, "onStateMutableLiveData() - status = ${it.statusValue}")
            if (it.statusValue == StatusCode.LOADING) {
                binding.progressLoader.visibility = View.VISIBLE
            } else {
                binding.progressLoader.visibility = View.GONE
                if (it.statusValue == StatusCode.ERROR) {
                    val msg = "Error - code: ${it.message}, message: ${it.message}"
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        })


        binding.chipSchool.setOnClickListener {
            viewModel.schoolsLiveData.value?.let {
                val schoolItems = it.map { it.schoolName }.toTypedArray()

                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.select_school)
                    .setItems(
                        schoolItems
                    ) { _, which ->
                        viewModel.setSelectedSchool(it[which])
                    }.show()
            }
        }

        binding.chipSchoolYear.setOnClickListener {
            viewModel.schoolYearsLiveData.value?.let { schools ->
                val schoolYearsItems =
                    schools.map { school -> "${school.classes.values.first().section}, ${school.year}" }
                        .toTypedArray()

                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.select_school_year)
                    .setItems(
                        schoolYearsItems
                    ) { _, which ->
                        viewModel.setSelectedSchoolYear(schools[which])
                    }.show()
            }
        }
    }

    private fun setPagerAdapter(timelineParams: TimelineParams) {
        if (timelineParams.studentId == null || timelineParams.schoolId == null || timelineParams.classId == null) return

        viewPageAdapter.timelineParams = timelineParams

        binding.viewPager.adapter = viewPageAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = viewPageAdapter.items[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_students -> {
                showStudents()
            }
            R.id.mi_logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        prefsHelper.removeCredentials(deleteUsernamePassword = true)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showStudents() {
        val bundle = Bundle().apply {
            putParcelableArrayList(StudentsFragment.STUDENTS_KEY, ArrayList(students))
        }
        val studentsFragment = StudentsFragment.getInstance(bundle) {
            Toast.makeText(this, "Selected student: ${it.fullName}", Toast.LENGTH_SHORT).show()
            viewModel.setSelectedStudent(it)
            localStore.setSelectedStudentId(it.id)
        }
        studentsFragment.show(supportFragmentManager, TAG)
    }

    override fun onConnectionChanged(connected: Boolean) {
        super.onConnectionChanged(connected)
        viewModel.setIsConnected(connected)
        binding.tvNoInternetConnection.visibility = if (connected) View.GONE else View.VISIBLE
    }

    fun setProgressVisibility(visibility: Int) {
        binding.progressLoader.visibility = visibility
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem != 0) {
            binding.viewPager.setCurrentItem(0, true)
            return
        }
        super.onBackPressed()
    }
}
