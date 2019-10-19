package com.example.facedetection.managers

import android.graphics.Bitmap
import android.util.Log
import com.example.facedetection.network.NetworkManager
import com.example.facedetection.network.model.ImageResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.util.*


class DataManager {
    private val TAG = DataManager::class.java.simpleName

    private lateinit var disposable: Disposable

    companion object {
        val INSTANCE = DataManager()
    }


    fun getAllImages(): Single<ImageResponse> {
        return NetworkManager.INSTANCE.getAllImages()
    }


    fun detectImages(imgUrlList: List<String>) {
        val bitmapList = arrayListOf<Bitmap>()
        disposable = imgUrlList.toObservable()
            .flatMap { i -> Observable.just(getImageAsBitmap(i)) }
            .subscribeOn(Schedulers.newThread())
            .subscribeBy(
                onComplete = {
                    startDetection(bitmapList)
                    disposable.dispose()
                },
                onError = { onFailed(it.message) },
                onNext = { bitmapList.add(it) }
            )
    }

    private fun startDetection(bitmapList: ArrayList<Bitmap>) {
        FaceDetectorManager.INSTANCE.startDetection(bitmapList)
    }


    private fun getImageAsBitmap(url: String): Bitmap {
        return NetworkManager.INSTANCE.getImageAsBitmap(url)
    }

    private fun onFailed(message: String?) {
        Log.e(TAG, message)
    }

}