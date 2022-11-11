package com.example.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.databinding.ListItemCrimeBinding

class CrimeHolder (private val binding: ListItemCrimeBinding)
        : RecyclerView.ViewHolder(binding.root) {
                fun bind (crime: CrimeModel) {
                        binding.crimeNumber.text = crime.title
                        binding.crimeDescription.text = crime.date.toString()

                        binding.crimeSolved.visibility = if (crime.isSolved) {
                                View.VISIBLE
                        } else {View.INVISIBLE}

                        binding.isCriminal.visibility = if (crime.isCriminal){
                                View.VISIBLE
                        }else {View.INVISIBLE}

                        binding.root.setOnClickListener {
                                Toast.makeText(binding.root.context,
                                        "Fuck you Joe Rogan!",
                                        Toast.LENGTH_SHORT).show()
                        }
                        binding.isCriminal.setOnClickListener {
                                Toast.makeText(binding.root.context,
                                                "I'm calling the police!",
                                                Toast.LENGTH_SHORT).show()
                        }
                }

        }

class CrimeListAdapter(private val crimes: List<CrimeModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val bindingCrime = ListItemCrimeBinding.inflate(inflater, parent, false)
                return CrimeHolder(bindingCrime)
        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                var crime = crimes[position]

                (holder as CrimeHolder).bind(crime)
        }

        override fun getItemCount(): Int = crimes.size
}
