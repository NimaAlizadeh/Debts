package com.example.debts.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.debts.R
import com.example.debts.api.ApiService
import com.example.debts.databinding.FragmentEnterBinding
import com.example.debts.databinding.FragmentRegisterBinding
import com.example.debts.models.user.UserEntity
import com.example.debts.utils.Constants
import com.example.debts.viewmodel.RegisterPageVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel: RegisterPageVM by viewModels()

    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            registerButton.setOnClickListener {
                viewModel.loading.postValue(true)
                
                val user = UserEntity()
                user.email = registerEmail.text.toString()
                user.passWord = registerPassword.text.toString()
                user.phone = registerPhone.text.toString()

                lifecycle.coroutineScope.launchWhenCreated {
                    val response = apiService.newUser(user)
                    if(response.isSuccessful && response.code() == 201){
                        Constants.USER_ID = response.body()!!.toString()
                        withContext(Dispatchers.Main){
                            Toast.makeText(requireContext(), "حساب کاربری با موفقیت ساخته شد", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(requireContext(), "ساخت اکانت به مشکل خورد" + "\n" + "دوباره تلاش کنید", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                viewModel.loading.postValue(false)
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
        }
    }
}