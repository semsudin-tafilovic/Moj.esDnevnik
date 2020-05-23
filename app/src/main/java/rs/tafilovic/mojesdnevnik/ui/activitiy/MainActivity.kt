package rs.tafilovic.mojesdnevnik.ui.activitiy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.SchoolYear
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.model.StudentSchoolYear
import rs.tafilovic.mojesdnevnik.presentation.adapter.MainPageAdapter
import rs.tafilovic.mojesdnevnik.ui.fragment.StudentsFragment
import rs.tafilovic.mojesdnevnik.util.Logger
import rs.tafilovic.mojesdnevnik.util.PrefsHelper
import rs.tafilovic.mojesdnevnik.viewmodel.MainViewModel
import javax.inject.Inject

@Suppress("PrivatePropertyName")
class MainActivity : BaseActivity() {

    override val TAG = this.javaClass.name

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var prefsHelper: PrefsHelper

    private lateinit var viewPageAdapter: MainPageAdapter

    private lateinit var students: List<Student>

    // Spinners onItemSelectedListener executes on initialization.
    // Increment this counter and use only callbacks when this counter is greater then 0
    private var spinnerCurrentSelection = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as MyApp).appComponent().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        spSchoolYears.onItemSelectedListener = onItemSelectedListener

        viewModel.studentsMutableLiveDate.observe(this, Observer {
            Logger.d(TAG, "studentsMutableLiveDate.observer: $it")
            students = it
            viewModel.setSelectedStudent(it.first())
        })

        viewModel.selectedStudentLiveData.observe(this, Observer {
            tvSchoolName.text = it?.getSchool()?.schoolName
            supportActionBar?.setTitle(it.fullName)
            val schoolYears = it?.getSchool()?.schoolyears
                ?.map { map -> map.value }
                ?.sortedByDescending { i -> i.yearId }

            setupSchoolYearsSpinner(schoolYears)
        })

        viewModel.studentSchoolYear.observe(this, Observer {
            Logger.d(TAG, "studentSchoolYear.observer: $it")
            setPagerAdapter(it)
        })

        viewModel.stateMutableLiveData.observe(this, Observer {
            Logger.d(TAG, "onStateMutableLiveData() - status = ${it.statusValue}")
            if (it.statusValue == StatusCode.LOADING) {
                progressLoader.visibility = View.VISIBLE
            } else {
                progressLoader.visibility = View.GONE
                if (it.statusValue == StatusCode.ERROR) {
                    val msg = "Error - code: ${it.message}, message: ${it.message}"
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setPagerAdapter(studentSchoolYear: StudentSchoolYear) {
        viewPageAdapter =
            MainPageAdapter(
                this,
                studentSchoolYear
            )
        viewPager.adapter = viewPageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
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
        prefsHelper.removeCredentials()
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
        }
        studentsFragment.show(supportFragmentManager, TAG)
    }

    private fun setupSchoolYearsSpinner(schoolYears: List<SchoolYear>?) {
        if (schoolYears.isNullOrEmpty()) return
        Logger.d(TAG, "setupSchoolYearsSpinner()")

        val items =
            schoolYears.map {
                String.format(
                    "%s (%s)",
                    it.classes.values.first().section,
                    it.year
                )
            }
        val spinnerAdapter =
            ArrayAdapter(this, R.layout.row_school_year, items)
        spSchoolYears.adapter = spinnerAdapter
    }

    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            Logger.d(TAG, "onNothingSelected:")
            // do nothing
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            Logger.d(TAG, "onItemSelected: $position")
            if (spinnerCurrentSelection != position)
                viewModel.setSelectedSchoolYear(position)
            spinnerCurrentSelection = position
        }
    }

    override fun onConnectionChanged(connected: Boolean) {
        super.onConnectionChanged(connected)
        viewModel.setIsConnected(connected)
        tvNoInternetConnection.visibility = if (connected) View.GONE else View.VISIBLE
    }
}
