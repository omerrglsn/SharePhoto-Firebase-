package com.example.sharephotoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharephotoapp.databinding.RecyclerRowBinding
import com.example.sharephotoapp.model.Post
import com.squareup.picasso.Picasso

class RecyclerViewAdapter (val postList : ArrayList<Post>) : RecyclerView.Adapter<RecyclerViewAdapter.PostVH>() {

    class PostVH ( val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        return PostVH(RecyclerRowBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {
        holder.binding.userEmailRecyclerRow.text = postList[position].userEmail
        holder.binding.userCaptionRecyclerRow.text = postList[position].userCaption
        Picasso.get().load(postList[position].imageUrl).into(holder.binding.userSharedImageRecyclerRow)
    }

    override fun getItemCount(): Int = postList.size
}