package com.example.sharephotoapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.sharephotoapp.R
import com.example.sharephotoapp.databinding.FragmentShareBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp
import java.util.*


class ShareFragment : Fragment() {

    var selectedPhoto : Uri? = null
    var selectedBitmap : Bitmap? = null
    private var storage : FirebaseStorage = FirebaseStorage.getInstance()
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var database : FirebaseFirestore = FirebaseFirestore.getInstance()

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result : ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val intent = result.data

            selectedPhoto = intent?.data

            if (selectedPhoto != null){

                if (Build.VERSION.SDK_INT >= 28){

                    val source = context?.let {
                        ImageDecoder.createSource(it.contentResolver , selectedPhoto!!) }
                    selectedBitmap = ImageDecoder.decodeBitmap(source!!)
                    binding.selectedPhotoImageView.setImageBitmap(selectedBitmap)

                }else{

                    selectedBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver , selectedPhoto)
                    binding.selectedPhotoImageView.setImageBitmap(selectedBitmap)

                }

            }

        }
    }

    val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        when (it) {
            true -> { println("Permission has been granted by user") }
            false -> {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        }

    }

    private var _binding : FragmentShareBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchImage()

        sharePhoto()

    }

    private fun fetchImage(){

        binding.selectPhotoBtnFragmentShare.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext() , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                permissionsResultCallback.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

            }else{

                Log.d("ShareFragment" , "Permission Granted")

                val imageIntent = Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                startForResult.launch(imageIntent)

            }
        }
    }

    private fun sharePhoto(){
        binding.sharePhotoBtnFragmentShare.setOnClickListener { view ->

            val uuid = UUID.randomUUID()
            val imageId = "${uuid}.jpg"
            val reference = storage.reference
            val imageReference = reference.child("images").child(imageId)

            if (selectedPhoto != null){
                imageReference.putFile(selectedPhoto!!)
                    .addOnSuccessListener { taskSnapshot ->

                        val uploadedImageReference = FirebaseStorage.getInstance().reference.child("images").child(imageId)

                        uploadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                            val downloadedImageUrl = uri.toString()
                            val currentUserEmail = auth.currentUser!!.email.toString()
                            val userCaption = binding.photoCaptionFragmentShare.text.toString()
                            val date = com.google.firebase.Timestamp.now()

                            val postHashMap = hashMapOf<String , Any>()
                            postHashMap["imageurl"] = downloadedImageUrl
                            postHashMap["useremail"] = currentUserEmail
                            postHashMap["usercaption"] = userCaption
                            postHashMap["date"] = date

                            database.collection("Post").add(postHashMap).addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    findNavController().navigate(R.id.feedFragment)
                                }
                            }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(context?.applicationContext , exception.localizedMessage , Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(context?.applicationContext , exception.localizedMessage , Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}