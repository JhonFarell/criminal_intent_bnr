package com.example.criminalintent.crime_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.CrimeModel
import com.example.criminalintent.databinding.FragmentCrimeDetailBinding
import com.example.criminalintent.dialog.DatePickerFragment
import com.example.criminalintent.dialog.TimePickerFragment
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*

class CrimeDetailFragment: Fragment() {
    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
    get() = checkNotNull(_binding) {
        "Can't access binding couse it's null."
    }
    private val args: CrimeDetailFragmentArgs by navArgs()
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.crimeTitle.text.isBlank()) {
                    Toast.makeText(activity, "Fill the title!", Toast.LENGTH_SHORT).show()
                } else {
                    crimeDetailViewModel.updateDbValue()
                    findNavController().navigate(CrimeDetailFragmentDirections.backToList())
                    parentFragmentManager.popBackStack()
                }
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
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
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    crimeDetailViewModel.crime.collect {
                        crime -> crime?.let { updateUi(it) }
                    }
                }
            }

            crimeSolved.setOnCheckedChangeListener{
                _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            crimeIsCriminal.setOnCheckedChangeListener {
                _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isCriminal = isChecked)
                }
            }

        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->  
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
        }

        setFragmentResultListener(
            TimePickerFragment.REQUEST_KEY_TIME
        ) {
            _, bundle ->
            val newTime =
                bundle.getSerializable(TimePickerFragment.BUNDlE_KEY_TIME) as Date


            crimeDetailViewModel.updateCrime { it.copy(date = newTime) }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(crime: CrimeModel) {
        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crimeDetailViewModel.dateFormat(crime.date)
            crimeTime.text = crimeDetailViewModel.timeFormat(crime.date)
            crimeSolved.isChecked = crime.isSolved
            crimeIsCriminal.isChecked = crime.isCriminal

            crimeDate.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectDate(crime.date)
                )
            }

            crimeTime.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectTime(crime.date)
                )
            }
        }
    }
}