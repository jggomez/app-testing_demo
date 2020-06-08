package com.devhack.appdemofortests.ui.activities

import android.content.Intent
import android.widget.Toast
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseActivity
import com.devhack.appdemofortests.databinding.ActivityMainBinding
import com.devhack.appdemofortests.ui.activities.viewmodels.UserViewModel
import com.devhack.appdemofortests.usecases.User
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    private val userViewModel by inject<UserViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        initViewModel()
    }

    override fun showProgress() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
    }

    override fun hideProgress() {

    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun initViewModel() {
        liveDataObserve(userViewModel.registerUserLiveData, ::registerUserStateChange)
    }

    private fun registerUser() {
        userViewModel.register(createUser())
    }

    private fun createUser() =
        User(
            binding.txtName.text.toString(),
            binding.txtLastName.text.toString(),
            binding.txtCellPhone.text.toString(),
            binding.txtAddress.text.toString()
        )

    private fun registerUserStateChange(state: State?) =
        when (state) {
            is State.Loading -> showProgress()
            is State.Failed -> {
                hideProgress()
                handleFailure(state.failure)
            }
            is State.Success -> {
                hideProgress()
                handleSuccessRegister(state.responseTo())
            }
            else -> hideProgress()
        }

    private fun handleSuccessRegister(success: Boolean) {
        Toast.makeText(this, success.toString(), Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, UsersListActivity::class.java))
        finish()
    }
}
