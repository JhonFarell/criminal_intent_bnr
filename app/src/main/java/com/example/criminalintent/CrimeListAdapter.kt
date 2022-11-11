package com.example.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.criminalintent.databinding.ListItemCrimeBinding
import com.example.criminalintent.databinding.ListItemCrimeHardBinding

class CrimeHolder (private val binding: ListItemCrimeBinding)
        : RecyclerView.ViewHolder(binding.root) {
                fun bind (crime: CrimeModel) {
                        binding.crimeNumber.text = crime.title
                        binding.crimeDescription.text = crime.date.toString()

                        binding.crimeSolved.visibility = if (crime.isSolved) {
                                View.VISIBLE
                        } else {View.INVISIBLE}

                        binding.root.setOnClickListener {
                                Toast.makeText(binding.root.context,
                                        "Fuck you Joe Rogan!",
                                        Toast.LENGTH_SHORT).show()
                        }
                }

        }

class CrimeHolderExtreme (private val binding: ListItemCrimeHardBinding)
        :RecyclerView.ViewHolder(binding.root) {
                fun bind (crime: CrimeModel) {
                        binding.crimeNumber.text = crime.title
                        binding.crimeDate.text = crime.date.toString()
                        binding.callThePolice.setOnClickListener {
                                Toast.makeText(binding.root.context,
                                        "Daaaaaaaaamn it's a crime... :(",
                                        Toast.LENGTH_SHORT).show()
                        }



                }

}

class CrimeListAdapter(private val crimes: List<CrimeModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val bindingCrime = ListItemCrimeBinding.inflate(inflater,parent,false)
                val bindingHardCrime = ListItemCrimeHardBinding.inflate(inflater, parent, false)
                return if (viewType == HARD_CRIME) {
                        CrimeHolderExtreme(bindingHardCrime)
                }
                        else {CrimeHolder(bindingCrime)}

                }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
               var crime = crimes[position]

                if (getItemViewType(position)== HARD_CRIME) {
                        (holder as CrimeHolderExtreme).bind(crime)
                } else {(holder as CrimeHolder).bind(crime)}
        }

        override fun getItemCount(): Int = crimes.size

        override fun getItemViewType(position: Int): Int {
                var crime = crimes[position]
                return when {
                        crime.isCriminal -> HARD_CRIME
                        else -> CRIME
                }
        }

        companion object type {
                val HARD_CRIME = 0
                val CRIME = 1
        }

}