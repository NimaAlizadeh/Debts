package com.example.debts.ui.deviceChecking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.debts.R
import com.example.debts.databinding.FragmentDeviceCheckingBinding
import com.example.debts.databinding.FragmentRegisterBinding


class DeviceCheckingFragment : Fragment() {
    private lateinit var binding: FragmentDeviceCheckingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDeviceCheckingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

        }
    }
}