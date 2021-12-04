package rs.tafilovic.mojesdnevnik.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.databinding.FragmentLicenceAgreementBinding

class LicenceAgreementFragment : DialogFragment() {

    private var _binding: FragmentLicenceAgreementBinding? = null
    private val binding: FragmentLicenceAgreementBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.EulaDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLicenceAgreementBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eulaStream = context?.assets?.open("text/eula.html")?.bufferedReader()?.use {
            it.readText()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            binding.tvContent.text = Html.fromHtml(eulaStream, Html.FROM_HTML_MODE_COMPACT)
        else
            binding.tvContent.text = Html.fromHtml(eulaStream)

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}