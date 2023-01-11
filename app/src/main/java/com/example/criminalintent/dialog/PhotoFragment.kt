package com.example.criminalintent.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.R
import com.example.criminalintent.getScaledBitmap
import java.io.File

class PhotoFragment: DialogFragment() {

    private val args: PhotoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialog = inflater.inflate(R.layout.photo_dialog, container, false)
        val dismissButton = dialog.findViewById<ImageButton>(R.id.ok_button)
        val photoView = dialog.findViewById<ImageView>(R.id.crime_photo)
        val photoFile = args.photoUri.let{
            File(requireContext().applicationContext.filesDir, it)
        }
        photoView.doOnLayout {
            val scaledBitmap = getScaledBitmap(photoFile.path,
                            300,
                            300)
            photoView.setImageBitmap(scaledBitmap)
            photoView.tag = args.photoUri
        }

        dismissButton.setOnClickListener {
            dismiss()
        }

        return dialog
    }

}