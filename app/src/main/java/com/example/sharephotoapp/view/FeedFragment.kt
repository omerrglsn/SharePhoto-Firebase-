package com.example.sharephotoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharephotoapp.R
import com.example.sharephotoapp.adapter.RecyclerViewAdapter
import com.example.sharephotoapp.databinding.FragmentFeedBinding
import com.example.sharephotoapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class FeedFragment : Fragment() {

    private var auht : FirebaseAuth = FirebaseAuth.getInstance()
    private var database : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter

    var postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding : FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()

        var layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = RecyclerViewAdapter(postList)
        binding.recyclerView.adapter = recyclerViewAdapter

    }

    private fun fetchData(){

        database.collection("Post")
            .orderBy("date" , Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

            if (exception != null){
                Toast.makeText(context?.applicationContext , exception.localizedMessage , Toast.LENGTH_SHORT).show()
            }else{
                if (snapshot?.isEmpty == false){

                    val documents = snapshot.documents

                    postList.clear()

                    for (document in documents){

                        val userEmail = document.get("useremail") as String
                        val userCaption = document.get("usercaption") as String
                        val imageUrl = document.get("imageurl") as String

                        val downloadedPost = Post(userEmail , userCaption , imageUrl)
                        postList.add(downloadedPost)

                    }

                    recyclerViewAdapter.notifyDataSetChanged()

                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}