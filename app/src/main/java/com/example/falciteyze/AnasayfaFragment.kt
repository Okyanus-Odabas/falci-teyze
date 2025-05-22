package com.example.falciteyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AnasayfaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_anasayfa, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val falci1 = view.findViewById<CardView>(R.id.kadinCard)
        val falci2 = view.findViewById<CardView>(R.id.oeCard)
        val falci3 = view.findViewById<CardView>(R.id.tontonCard)

        falci1.setOnClickListener {
            val action = AnasayfaFragmentDirections.falGecis("falci_kahin")
            findNavController().navigate(action)
        }

        falci2.setOnClickListener {
            val action = AnasayfaFragmentDirections.falGecis("falci_oe")
            findNavController().navigate(action)
        }

        falci3.setOnClickListener {
            findNavController().navigate(R.id.ruyaGecis)
        }
    }
}
