package com.example.facedetection.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.facedetection.network.model.ImageResponse
import io.reactivex.Single
import java.net.URL

class NetworkManager {

    companion object {
        val INSTANCE = NetworkManager()
    }


    fun getImagesByPage(pageNum: Int): Single<ImageResponse> {
        return RetrofitManager.INSTANCE.getNetworkApi().getImagesByPage(page = pageNum)
    }


    fun getImageAsBitmap(imgUrl: String): Bitmap {
        val url = URL(imgUrl)
        val connection = url.openConnection()
        connection.connect()
        val input = connection.getInputStream()
        return BitmapFactory.decodeStream(input)
    }


}