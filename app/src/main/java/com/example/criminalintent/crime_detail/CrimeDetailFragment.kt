package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.criminalintent.databinding.FragmentCrimeDetailBinding
import java.util.*

class CrimeDetailFragment: Fragment() {
    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
    get() = checkNotNull(_binding) {
        "Can't access binding couse it's null."
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            crimeTitle.doOnTextChanged{ text, _, _, _ ->

            }
            crimeDate.apply {
                isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener{
                _, isChecked ->

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}