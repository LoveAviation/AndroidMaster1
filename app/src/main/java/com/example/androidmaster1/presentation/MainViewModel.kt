package com.example.androidmaster1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmaster1.domain.ImageDao
import com.example.androidmaster1.other_classes.image
import kotlinx.coroutines.launch

class MainViewModel(private val imageDao: ImageDao): ViewModel() {
    val images = this.imageDao.getAll()

    fun saveImage(name: String, date: String){
        viewModelScope.launch {
            imageDao.insert(
                image(
                    imgSrc = name,
                    date = date
                )
            )
        }
    }
}