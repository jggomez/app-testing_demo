package com.devhack.appdemofortests.ui.adapters.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devhack.appdemofortests.databinding.ItemUserBinding
import com.devhack.appdemofortests.usecases.User

class UsersAdapter(
    private var dataSet: List<User>
) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =
        dataSet.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.binding.txtName.text = "${item.name.toUpperCase()} ${item.lastName.toUpperCase()}"
        holder.binding.txtAddress.text = item.address
        holder.binding.txtCellPhoneNumber.text = item.cellPhone
    }

    fun update(users: List<User>) {
        dataSet = users
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)
}
