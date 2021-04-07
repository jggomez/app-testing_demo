package com.devhack.appdemofortests.ui.activities

import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.base.State
import co.devhack.presentation.BaseActivity
import com.devhack.appdemofortests.databinding.ActivityMainBinding
import com.devhack.appdemofortests.ui.activities.viewmodels.UserViewModel
import com.devhack.appdemofortests.ui.dialogs.DialogLoading
import com.devhack.appdemofortests.ui.dialogs.showAnimLoading
import com.devhack.appdemofortests.usecases.User
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    private val userViewModel by inject<UserViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogLoading: DialogLoading

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        initViewModel()
    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    override fun hideProgress() {
        dialogLoading.dismiss()
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
            binding.txtCellPhoneNumber.text.toString(),
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
        finish()
    }
}
