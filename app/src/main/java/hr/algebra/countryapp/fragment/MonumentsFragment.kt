package hr.algebra.countryapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.countryapp.R
import hr.algebra.countryapp.adapter.MonumentsAdapter
import hr.algebra.countryapp.adapter.RulersAdapter
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.databinding.FragmentItemsBinding
import hr.algebra.countryapp.databinding.FragmentMonumentsBinding
import hr.algebra.countryapp.framework.fetchMonuments
import hr.algebra.countryapp.framework.fetchRulers


class MonumentsFragment : Fragment() {

    private lateinit var monuments: MutableList<Monument>
    private lateinit var binding: FragmentMonumentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        monuments = requireContext().fetchMonuments()
        binding = FragmentMonumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMonuments.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MonumentsAdapter(requireContext(), monuments)
        }
    }
}