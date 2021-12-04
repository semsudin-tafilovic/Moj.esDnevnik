package rs.tafilovic.mojesdnevnik.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import rs.tafilovic.mojesdnevnik.databinding.FragmentStudentsBinding
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.presentation.adapter.StudentsAdapter

/**
 * A simple [Fragment] subclass.
 */
class StudentsFragment : DialogFragment() {

    private val TAG = StudentsFragment::class.java.name

    private var _binding: FragmentStudentsBinding? = null
    private val binding: FragmentStudentsBinding
        get() = _binding!!

    private var adapter: StudentsAdapter? = null

    lateinit var onClickCallback: (Student) -> Unit

    companion object {
        val STUDENTS_KEY = "students"

        fun getInstance(bundle: Bundle, onClick: (Student) -> Unit): StudentsFragment {
            return StudentsFragment().apply {
                arguments = bundle
                onClickCallback = onClick
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter =
            StudentsAdapter {
                onClickCallback(it)
                dismiss()
            }

        binding.recycler.adapter = adapter

        val students = arguments?.getParcelableArrayList<Student>(STUDENTS_KEY)
        if (students != null) {
            adapter?.submitList(students)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
