package hr.algebra.countryapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.countryapp.R
import hr.algebra.countryapp.adapter.ItemAdapter
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.databinding.FragmentItemsBinding
import hr.algebra.countryapp.framework.fetchCountries

class ItemsFragment : Fragment() {

    private lateinit var countries: MutableList<Country>
    private lateinit var binding: FragmentItemsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        countries = requireContext().fetchCountries()
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ItemAdapter(requireContext(), countries)
        }
    }
}