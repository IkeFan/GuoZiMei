package com.mmy.guozimei.utils

import android.app.Activity
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import java.io.IOException
import java.util.*

/**
 * @file       CameraUtils.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/9/11 0011
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
object CameraUtils {
    val DEFAULT_WIDTH = 1280
    val DEFAULT_HEIGHT = 720
    val DESIRED_PREVIEW_FPS = 30

    var mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT
    var mCamera: Camera? = null
    var mCameraPreviewFps = 0
    var mOrientation = 0

    /**
     * 打开相机，默认打开前置相机
     * @param expectFps
     */
    fun openFrontalCamera(expectFps: Int, activity: Activity) {
        if (mCamera != null) {
            throw RuntimeException("camera already initialized!")
        }
        var info = Camera.CameraInfo()
        var numCameras = Camera.getNumberOfCameras()
        for (i in 0 until numCameras) {
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mCamera = Camera.open(i)
                mCameraID = info.facing
                break
            }
        }
        // 如果没有前置摄像头，则打开默认的后置摄像头
        if (mCamera == null) {
            mCamera = Camera.open()
            mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK
        }
        // 没有摄像头时，抛出异常
        if (mCamera == null) {
            throw RuntimeException("Unable to open camera")
        }

        var parameters = mCamera?.parameters
        mCameraPreviewFps = CameraUtils.chooseFixedPreviewFps(parameters!!, expectFps * 1000)
        parameters?.setRecordingHint(true)
        mCamera?.parameters = parameters
        setPreviewSize(mCamera!!, CameraUtils.DEFAULT_WIDTH, CameraUtils.DEFAULT_HEIGHT)
        setPictureSize(mCamera!!, CameraUtils.DEFAULT_WIDTH, CameraUtils.DEFAULT_HEIGHT)
        mOrientation = calculateCameraPreviewOrientation(activity)
        mCamera?.setDisplayOrientation(mOrientation)
    }

    /**
     * 根据ID打开相机
     * @param cameraID
     * @param expectFps
     */
    fun openCamera(cameraID: Int, expectFps: Int, activity: Activity) {
        if (mCamera != null) {
            throw RuntimeException("camera already initialized!")
        }
        mCamera = Camera.open(cameraID)
        if (mCamera == null) {
            throw RuntimeException("Unable to open camera")
        }
        mCameraID = cameraID
        var parameters = mCamera?.parameters
        mCameraPreviewFps = CameraUtils.chooseFixedPreviewFps(parameters!!, expectFps * 1000)
        parameters?.setRecordingHint(true)
        mCamera?.parameters = parameters
        setPreviewSize(mCamera!!, CameraUtils.DEFAULT_WIDTH, CameraUtils.DEFAULT_HEIGHT)
        setPictureSize(mCamera!!, CameraUtils.DEFAULT_WIDTH, CameraUtils.DEFAULT_HEIGHT)
        mOrientation = calculateCameraPreviewOrientation(activity)
        mCamera?.setDisplayOrientation(mOrientation)
    }

    /**
     * 开始预览
     * @param holder
     */
    fun startPreviewDisplay(holder: SurfaceHolder) {
        if (mCamera == null) {
            throw IllegalStateException("Camera must be set when start preview")
        }
        try {
            mCamera?.setPreviewDisplay(holder)
            mCamera?.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 切换相机
     * @param cameraID
     */
    fun switchCamera(cameraID: Int, holder: SurfaceHolder, activity: Activity) {
        if (mCameraID == cameraID) {
            return
        }
        mCameraID = cameraID
        // 释放原来的相机
        releaseCamera()
        // 打开相机
        openCamera(cameraID, CameraUtils.DESIRED_PREVIEW_FPS, activity)
        // 打开预览
        startPreviewDisplay(holder)
    }

    /**
     * 释放相机
     */
    fun releaseCamera() {
        if (mCamera != null) {
            mCamera?.stopPreview()
            mCamera?.release()
            mCamera = null
        }
    }

    /**
     * 开始预览
     */
    fun startPreview() {
        if (mCamera != null) {
            mCamera?.startPreview()
        }
    }

    /**
     * 停止预览
     */
    fun stopPreview() {
        if (mCamera != null) {
            mCamera?.stopPreview()
        }
    }

    /**
     * 拍照
     */
    fun takePicture(shutterCallback: Camera.ShutterCallback, rawCallback: Camera.PictureCallback,
                    pictureCallback: Camera.PictureCallback) {
        if (mCamera != null) {
            mCamera?.takePicture(shutterCallback, rawCallback, pictureCallback)
        }
    }

    /**
     * 设置预览大小
     * @param camera
     * @param expectWidth
     * @param expectHeight
     */
    fun setPreviewSize(camera: Camera, expectWidth: Int, expectHeight: Int) {
        var parameters = camera.parameters
        var size = calculatePerfectSize(parameters.supportedPreviewSizes,
                expectWidth, expectHeight)
        parameters.setPreviewSize(size!!.width, size.height)
        camera.parameters = parameters
    }

    /**
     * 获取预览大小
     * @return
     */
    fun getPreviewSize(): Camera.Size? {
        if (mCamera != null) {
            return mCamera?.parameters?.previewSize
        }
        return null
    }

    /**
     * 设置拍摄的照片大小
     * @param camera
     * @param expectWidth
     * @param expectHeight
     */
    fun setPictureSize(camera: Camera, expectWidth: Int, expectHeight: Int) {
        var parameters = camera.parameters
        var size = calculatePerfectSize(parameters.supportedPictureSizes,
                expectWidth, expectHeight)
        if(size==null){
            parameters.setPictureSize(400, 750)
        }else{
            parameters.setPictureSize(size!!.width, size.height)
        }
        camera.parameters = parameters
    }

    /**
     * 获取照片大小
     * @return
     */
    fun getPictureSize(): Camera.Size? {
        if (mCamera != null) {
            return mCamera?.parameters?.pictureSize
        }
        return null
    }

    /**
     * 计算最完美的Size
     * @param sizes
     * @param expectWidth
     * @param expectHeight
     * @return
     */
    fun calculatePerfectSize(sizes: List<Camera.Size>, expectWidth: Int,
                             expectHeight: Int): Camera.Size? {
        sortList(sizes) // 根据宽度进行排序
        var result = sizes[0]
        var widthOrHeight = false // 判断存在宽或高相等的Size
        // 辗转计算宽高最接近的值
        sizes.forEach {
            // 如果宽高相等，则直接返回
            if (it.width == expectWidth && it.height == expectHeight) {
                result = it
                return@forEach
            }
            // 仅仅是宽度相等，计算高度最接近的size
            if (it.width == expectWidth) {
                widthOrHeight = true
                if (Math.abs(result.height - expectHeight)
                        > Math.abs(it.height - expectHeight)) {
                    result = it
                }
            }
            // 高度相等，则计算宽度最接近的Size
            else if (it.height == expectHeight) {
                widthOrHeight = true
                if (Math.abs(result.width - expectWidth)
                        > Math.abs(it.width - expectWidth)) {
                    result = it
                }
            }
            // 如果之前的查找不存在宽或高相等的情况，则计算宽度和高度都最接近的期望值的Size
            else if (!widthOrHeight) {
                if (Math.abs(result.width - expectWidth)
                        > Math.abs(it.width - expectWidth)
                        && Math.abs(result.height - expectHeight)
                        > Math.abs(it.height - expectHeight)) {
                    result = it
                }
            }
        }
        return result
    }

    /**
     * 排序
     * @param list
     */
    private fun sortList(list: List<Camera.Size>) {
        Collections.sort(list, { o1, o2 ->
            if (o1.width > o2.width) {
                1
            } else if (o1.width < o2.width) {
                -1
            }
            0
        })
    }

    /**
     * 选择合适的FPS
     * @param parameters
     * @param expectedThoudandFps 期望的FPS
     * @return
     */
    fun chooseFixedPreviewFps(parameters: Camera.Parameters, expectedThoudandFps: Int): Int {
        var supportedFps = parameters.getSupportedPreviewFpsRange()
        for (entry in supportedFps) {
            if (entry[0] == entry[1] && entry[0] == expectedThoudandFps) {
                parameters.setPreviewFpsRange(entry[0], entry[1])
                return entry[0]
            }
        }
        var temp = intArrayOf(0, 0)
        parameters.getPreviewFpsRange(temp)
        var guess: Int
        guess = if (temp[0] == temp[1]) {
            temp[0]
        } else {
            temp[1] / 2
        }
        return guess
    }

    /**
     * 设置预览角度，setDisplayOrientation本身只能改变预览的角度
     * previewFrameCallback以及拍摄出来的照片是不会发生改变的，拍摄出来的照片角度依旧不正常的
     * 拍摄的照片需要自行处理
     * 这里Nexus5X的相机简直没法吐槽，后置摄像头倒置了，切换摄像头之后就出现问题了。
     * @param activity
     */
    fun calculateCameraPreviewOrientation(activity: Activity): Int {
        var info = Camera.CameraInfo()
        Camera.getCameraInfo(mCameraID, info)
        var rotation = activity.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360
        } else {
            result = (info.orientation - degrees + 360) % 360
        }
        mOrientation = result
        return result
    }


    /**
     * 获取当前的Camera ID
     * @return
     */
    fun getCameraID():Int
    {
        return mCameraID
    }

    /**
     * 获取当前预览的角度
     * @return
     */
    fun getPreviewOrientation():Int
    {
        return mOrientation
    }

    /**
     * 获取FPS（千秒值）
     * @return
     */
    fun getCameraPreviewThousandFps():Int
    {
        return mCameraPreviewFps
    }

    /**
     * 自动聚焦
     */
    fun autoFocus(cb: Camera.AutoFocusCallback){
        if(mCamera!=null)mCamera?.autoFocus(cb)
    }
}