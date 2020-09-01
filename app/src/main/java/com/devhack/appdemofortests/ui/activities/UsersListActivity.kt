package com.devhack.appdemofortests.ui.activities

import androidx.recyclerview.widget.LinearLayoutManager
import co.devhack.androidextensions.components.liveDataObserve
import co.devhack.androidextensions.ui.startActivity
import co.devhack.base.State
import co.devhack.presentation.BaseActivity
import com.devhack.appdemofortests.databinding.ActivityUsersListBinding
import com.devhack.appdemofortests.ui.activities.viewmodels.UserViewModel
import com.devhack.appdemofortests.ui.adapters.user.UsersAdapter
import com.devhack.appdemofortests.ui.dialogs.DialogLoading
import com.devhack.appdemofortests.ui.dialogs.showAnimLoading
import com.devhack.appdemofortests.usecases.User
import org.koin.android.ext.android.inject

class UsersListActivity : BaseActivity() {

    private val userViewModel by inject<UserViewModel>()
    private lateinit var binding: ActivityUsersListBinding
    private lateinit var dialogLoading: DialogLoading
    private lateinit var usersAdapter: UsersAdapter

    override fun initView() {
        binding = ActivityUsersListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        initViewModel()
        initRecycler()
    }

    override fun onResume() {
        super.onResume()
        getAllUsers()
    }

    override fun showProgress() {
        dialogLoading = showAnimLoading()
    }

    override fun hideProgress() {
        dialogLoading.dismiss()
    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener {
            startActivity<MainActivity>()
        }
    }

    private fun initRecycler() {
        usersAdapter = UsersAdapter(emptyList())
        binding.rvUsers.adapter = usersAdapter
        binding.rvUsers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun getAllUsers() {
        userViewModel.getAllUsers()
    }

    private fun initViewModel() {
        liveDataObserve(userViewModel.getAllUsersLiveData, ::getAllUsersStateChange)
    }

    private fun getAllUsersStateChange(state: State?) =
        when (state) {
            is State.Loading -> showProgress()
            is State.Failed -> {
                hideProgress()
                handleFailure(state.failure)
            }
            is State.Success -> {
                hideProgress()
                handleSuccessGetAllUsers(state.responseTo())
            }
            else -> hideProgress()
        }

    private fun handleSuccessGetAllUsers(users: List<User>) {
        usersAdapter.update(users)
    }
}
