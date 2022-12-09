package com.example.criminalintent.crime_detail

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
import com.example.criminalintent.R
import com.example.criminalintent.databinding.FragmentCrimeDetailBinding
import com.example.criminalintent.dialog.DatePickerFragment
import com.example.criminalintent.dialog.TimePickerFragment
import kotlinx.coroutines.launch
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.util.*


private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeDetailFragment : Fragment() {

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Can't access binding couse it's null."
        }
    private val args: CrimeDetailFragmentArgs by navArgs()
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    private val selectSuspect = registerForActivityResult (
        ActivityResultContracts.PickContact()) {
        uri: Uri? -> uri?.let {parseContactsSelection(it)}

    }

    private val permissionRequestLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        backPress()
        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    crimeDetailViewModel.crime.collect { crime ->
                        crime?.let { updateUi(it) }
                    }
                }
            }

            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            crimeIsCriminal.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isCriminal = isChecked)
                }
            }
            chooseSuspect.setOnClickListener {
                selectSuspect.launch(null)
            }

            val selectSuspectIntent = selectSuspect.contract.createIntent(
                requireContext(),
                null
            )
            chooseSuspect.isEnabled = canResolveIntent(selectSuspectIntent)

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
        ) { _, bundle ->
            val newTime =
                bundle.getSerializable(TimePickerFragment.BUNDlE_KEY_TIME) as Date


            crimeDetailViewModel.updateCrime { it.copy(date = newTime) }
        }

    }


    override fun onResume() {
        super.onResume()
        backPress()
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
            updateCrime.isEnabled = crime.title.isNotBlank()
            crimeDate.text = crimeDetailViewModel.dateFormat(crime.date)
            crimeTime.text = crimeDetailViewModel.timeFormat(crime.date)
            crimeSolved.isChecked = crime.isSolved
            crimeIsCriminal.isChecked = crime.isCriminal
            chooseSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.choose_suspect)
            }
            callSuspect.isEnabled = !chooseSuspect.text.equals("CHOOSE SUSPECT")



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

            deleteCrime.setOnClickListener {
                dialogBuilder(false)
            }

            updateCrime.setOnClickListener {
                crimeDetailViewModel.updateDbValue()
                Toast.makeText(activity, "Crime saved", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    CrimeDetailFragmentDirections.backToList()
                )
            }

            sendCrimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject)
                    )
                }
                val chooserIntent = Intent.createChooser(
                    reportIntent,
                    getString(R.string.send_report)
                )
                startActivity(chooserIntent)
            }

            callSuspect.setOnClickListener{
                val check = context?.let {
                    ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS)
                }

                when (check) {
                    PackageManager.PERMISSION_DENIED -> {
                        permissionRequestLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                    else -> {
                        val suspectName = crimeDetailViewModel.crime.value?.suspect.toString()
                        parseContactsToMakeACall("$suspectName")

                    }
                }
            }

        }
    }

    private fun getCrimeReport(crime: CrimeModel): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else getString(R.string.crime_report_unsolved)

        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspectText = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        val isIllegal = when (crime.isCriminal) {
            true -> { getString(R.string.is_illegal) }
            else -> { if (crime.suspect.isBlank()) {
                getString(R.string.is_legal_without_suspect)
             } else { getString(R.string.is_legal_with_suspect) }
            }
        }

        return getString(
            R.string.crime_report,
            crime.title, dateString, solvedString, suspectText, isIllegal
        )
    }

    private fun parseContactsToMakeACall (suspect: String) {
        val uri = ContactsContract.Contacts.CONTENT_URI
        val name = ContactsContract.Contacts.DISPLAY_NAME
        val id = ContactsContract.Contacts._ID
        lateinit var suspectId: String
        lateinit var suspectNumber: String

        val queryFields = arrayOf(name,id)

        val cursor = requireActivity().contentResolver
            .query(uri, queryFields, "$name=?", arrayOf("$suspect"), null)

        cursor?.use {
            cursor ->

            if (cursor.moveToFirst()) {
                suspectId = cursor.getString(1).toString()
            }
        }

        //cdk - means CommonDataKinds
        val cdkUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cdkId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val cdkPhoneNumber = ContactsContract.CommonDataKinds.Phone.NUMBER

        val commonDataKindsQueryFields = arrayOf(cdkId, cdkPhoneNumber)

        val commonDataKindsCursor = requireActivity().contentResolver
            .query(cdkUri, commonDataKindsQueryFields, "$cdkId=?",
                arrayOf("$suspectId"), null)


        commonDataKindsCursor?.use {

            if (it.moveToFirst()) {
                suspectNumber = it.getString(1)

                val number = Uri.parse("tel:$suspectNumber")
                val callSuspect = Intent(Intent.ACTION_DIAL, number)
                startActivity(callSuspect)
            } else {
                suspectNumber = ""
                Toast.makeText(activity, "There is no phone number for ${crimeDetailViewModel.crime.value?.suspect}"
                    ,Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun parseContactsSelection (contactsUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

        val queryCursor = requireActivity().contentResolver
            .query(contactsUri, queryFields, null, null, null)

        queryCursor?.use {
            cursor ->
            if (cursor.moveToFirst()) {
                val suspect = cursor.getString(0)

                crimeDetailViewModel.updateCrime {
                    oldCrime -> oldCrime.copy(suspect = suspect)
                }
            }
        }

    }

    private fun canResolveIntent (intent: Intent): Boolean {
        val packageManager = requireActivity().packageManager
        val resolveActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolveActivity != null
    }

    private fun dialogBuilder(dialogType: Boolean) {


        val text = when (dialogType) {
            true -> "All changes will be lost. Are you sure?"
            else -> "Are you sure?"
        }

        val positiveButton = when (dialogType) {
            true -> { _: DialogInterface, _: Int ->
                findNavController().navigate(CrimeDetailFragmentDirections.backToList())
                Toast.makeText(activity, "Changes discard", Toast.LENGTH_SHORT).show()
            }
            else -> { _: DialogInterface, _: Int ->
                    lifecycleScope.launch { crimeDetailViewModel.deleteCrime() }
                    findNavController().navigate(CrimeDetailFragmentDirections.backToList())
                    Toast.makeText(activity, "Crime deleted", Toast.LENGTH_SHORT).show()
            }
        }


        val negativeButton = when (dialogType) {
            true -> { _: DialogInterface, _: Int ->
                Toast.makeText(activity, "State isn't lost", Toast.LENGTH_SHORT).show()
            }
            else -> { _: DialogInterface, _: Int ->
                Toast.makeText(activity, "Crime delete canceled", Toast.LENGTH_SHORT).show()
            }
        }

        val builder = AlertDialog.Builder(activity)

        with(builder) {
            setTitle(text)
            setPositiveButton("OK", DialogInterface.OnClickListener(positiveButton))
            setNegativeButton("CANCEL", DialogInterface.OnClickListener(negativeButton))
            builder.show()
        }
    }

    private fun backPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialogBuilder(true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}
