package com.facevel.inc.app.ui.fragment

import android.content.Context
import android.view.LayoutInflater
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.LoaderSheetBinding
import dagger.hilt.android.AndroidEntryPoint


class Loader(val context: Context) {
    lateinit var binding: LoaderSheetBinding

    //private var dialog = RoundedBottomSheetDialog(context)
    fun startLoader() {
        // dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.loader_sheet, null, false)
        binding = LoaderSheetBinding.bind(view)
//
//        dialog.setContentView(view)
//        dialog.setCancelable(false)
//        if (!dialog.isShowing)
//            dialog.show()
    }

    fun setDespText(text: String) {
        binding.loaderDesp.text = text
    }

    fun stopLoading() {
//        if (dialog.isShowing) {
//            dialog.dismiss()
//        }
    }
}
