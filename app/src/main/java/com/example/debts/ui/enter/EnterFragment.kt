package com.example.debts.ui.enter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.debts.api.ApiService
import com.example.debts.databinding.FragmentEnterBinding
import com.example.debts.ui.activities.MainActivity
import com.example.debts.utils.StoreUserData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class EnterFragment : Fragment() {

    private lateinit var binding: FragmentEnterBinding

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var userData: StoreUserData


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEnterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userData.getUserToken().map {
            if(it.isNotEmpty()){
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.apply {
            goRegisterText.setOnClickListener {
                findNavController().navigate(EnterFragmentDirections.actionEnterFragmentToRegisterFragment())
            }

            enterButton.setOnClickListener {
                lifecycle.coroutineScope.launchWhenCreated{
                    lifecycle.coroutineScope.launchWhenCreated{
                        val response = apiService.enterUser()
                        if(response.isSuccessful && response.code() == 201){
                            userData.setUserToken("")
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            withContext(Dispatchers.Main){
                                Toast.makeText(requireContext(), "ورود به حساب با مشکل مواجه شد" + "\n" + "لطفا دوباره تلاش کنید", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }
}