package rs.tafilovic.mojesdnevnik.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_licence_agreement.*
import rs.tafilovic.mojesdnevnik.R

class LicenceAgreementFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.EulaDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_licence_agreement, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eulaStream = context?.assets?.open("text/eula.html")?.bufferedReader()?.use {
            it.readText()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            tvContent.text = Html.fromHtml(eulaStream, Html.FROM_HTML_MODE_COMPACT)
        else
            tvContent.text = Html.fromHtml(eulaStream)

        btnClose.setOnClickListener {
            dismiss()
        }
    }
}