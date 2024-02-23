package hr.algebra.countryapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.countryapp.R
import hr.algebra.countryapp.adapter.ItemAdapter
import hr.algebra.countryapp.adapter.RulersAdapter
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.api.model.Ruler
import hr.algebra.countryapp.databinding.FragmentItemsBinding
import hr.algebra.countryapp.databinding.FragmentRulersBinding
import hr.algebra.countryapp.framework.fetchCountries
import hr.algebra.countryapp.framework.fetchRulers


class RulersFragment : Fragment() {

    private lateinit var rulers: MutableList<Ruler>
    private lateinit var binding: FragmentRulersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rulers = requireContext().fetchRulers()
        binding = FragmentRulersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRulers.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RulersAdapter(requireContext(), rulers)
        }
    }

}