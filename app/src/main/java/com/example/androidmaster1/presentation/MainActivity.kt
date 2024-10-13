package com.example.androidmaster1.presentation

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission_group.STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidmaster1.data.App
import com.example.androidmaster1.databinding.ActivityMainBinding
import com.example.androidmaster1.domain.ImageDao
import com.example.androidmaster1.other_classes.image
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var permission = false
    private val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        checkPermission()
    }
    private var adapterList = listOf<image>()

    private val mainViewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val imageDao: ImageDao = (application as App).db.imageDao()
                return MainViewModel(imageDao) as T
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager =
            GridLayoutManager(this, 3)


        lifecycleScope.launch {
            mainViewModel.images.collect{ list ->
                adapterList = list
                binding.recyclerView.adapter = MyAdapter(this@MainActivity, adapterList)
            }
        }

        checkPermission()

        binding.addButton.setOnClickListener{
            if(permission) startActivity(Intent(this, TakePhotoActivity::class.java))
            else Snackbar.make(binding.root, "Give permission to the camera, for taking photo", Snackbar.LENGTH_LONG).show()
        }

    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            mainViewModel.images.collect{ list ->
                adapterList = list
            }
        }
        binding.recyclerView.adapter = MyAdapter(this, adapterList)
    }

    private fun checkPermission(){
        if(ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                permission = true
            }else{
                launcher.launch(WRITE_EXTERNAL_STORAGE)
            }
        }else{
            launcher.launch(CAMERA)
        }
    }

}