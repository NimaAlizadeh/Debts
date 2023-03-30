package com.example.debts.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.debts.R
import com.example.debts.databinding.FragmentEnterBinding
import com.example.debts.databinding.FragmentRegisterBinding
import com.example.debts.models.user.UserEntity
import com.example.debts.viewmodel.RegisterPageVM


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel: RegisterPageVM by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            registerButton.setOnClickListener {
                val user = UserEntity()
                user.userName = registerUsername.text.toString()
                user.email = registerEmail.text.toString()
                user.passWord = registerPassword.text.toString()
                user.phone = registerPhone.text.toString()

                viewModel.registerUser(user)
            }

            viewModel.loading.observe(viewLifecycleOwner){
                if(it){
                    registerProgressbar.visibility = View.VISIBLE
                    registerButton.visibility = View.INVISIBLE
                }
                else{
                    registerButton.visibility = View.VISIBLE
                    registerProgressbar.visibility = View.INVISIBLE
                }
            }

//            viewModel.registerUser.observe(viewLifecycleOwner){
//                if(it == "")
//            }
        }
    }
}