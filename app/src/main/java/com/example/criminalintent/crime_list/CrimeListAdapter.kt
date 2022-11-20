package com.example.criminalintent.crime_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.CrimeModel
import com.example.criminalintent.databinding.ListItemCrimeBinding
import java.text.DateFormat
import java.util.*

class CrimeHolder (private val binding: ListItemCrimeBinding)
        : RecyclerView.ViewHolder(binding.root) {
                fun bind (crime: CrimeModel, onCrimeClicked:(crimeId: UUID) -> Unit) {

                        binding.crimeNumber.text = crime.title
                        binding.crimeDescription.text = DateFormat
                                .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                                .format(crime.date)

                        binding.crimeSolved.visibility = if (crime.isSolved) {
                                View.VISIBLE
                        } else {View.INVISIBLE}

                        binding.isCriminal.visibility = if (crime.isCriminal){
                                View.VISIBLE
                        }else {View.INVISIBLE}

                        binding.root.setOnClickListener {
                                onCrimeClicked(crime.id)
                        }
                        binding.isCriminal.setOnClickListener {
                                Toast.makeText(binding.root.context,
                                                "I'm calling the police!",
                                                Toast.LENGTH_SHORT).show()
                        }
                }

        }

class CrimeListAdapter(private val crimes: List<CrimeModel>,
                       private val onCrimeClicked: (crimeId: UUID) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val bindingCrime = ListItemCrimeBinding.inflate(inflater, parent, false)
                return CrimeHolder(bindingCrime)
        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val crime = crimes[position]

                (holder as CrimeHolder).bind(crime, onCrimeClicked)
        }

        override fun getItemCount(): Int = crimes.size
}
