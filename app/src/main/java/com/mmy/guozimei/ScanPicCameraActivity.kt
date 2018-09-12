package com.mmy.guozimei

import android.Manifest
import android.app.Activity
import android.graphics.PixelFormat
import android.hardware.Camera
import android.os.Handler
import android.util.Log
import android.view.SurfaceHolder
import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.utils.CameraUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_camera.*

class ScanPicCameraActivity : BaseActivity<IPresenter<*>>() {
    var previewCallBack = SurfaceCallBack()
    val MSG_REQUEST_FOCUS: Int = 1002

    var mHandler = Handler { msg ->
        when (msg.what) {
            MSG_REQUEST_FOCUS -> {
                CameraUtils.autoFocus(Camera.AutoFocusCallback { focus, ca ->
                    if (focus) {
                        "扫描结束".showToast(mFrameApp)
                        finish()
                    }
                })
                return@Handler true
            }
            else -> {
                false
            }
        }
    }


    override fun initView() {
        setToolbar("扫描", true)
        var rxPermissions = RxPermissions(this)
        if (!rxPermissions.isGranted(Manifest.permission.CAMERA)) {
            rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { permission ->
                        when {
                            permission.granted -> {
                                // 用户已经同意该权限

                            }
                            permission.shouldShowRequestPermissionRationale -> Log.d("ScanPicCameraActivity", permission.name + " is denied. More info should be provided.")
                            else -> // 用户拒绝了该权限，并且选中『不再询问』
                                Log.d("ScanPicCameraActivity", permission.name + " is denied.")
                        }
                    }
        }
    }

    fun getActivity(): Activity = this

    override fun initData() {
        surface_view.holder.setKeepScreenOn(true)
        surface_view.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surface_view.holder.setFormat(PixelFormat.TRANSPARENT)
        surface_view.holder.addCallback(previewCallBack)
    }

    override fun requestSuccess(any: IBean) {

    }

    override fun setupDagger(appComponent: AppComponent) {

    }

    override fun getLayoutID(): Any = R.layout.activity_camera

    override fun onPause() {
        super.onPause()
        CameraUtils.stopPreview()
    }

    override fun onResume() {
        super.onResume()
        CameraUtils.startPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraUtils.releaseCamera()
    }


    inner class SurfaceCallBack : SurfaceHolder.Callback2 {
        override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {

        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            CameraUtils.startPreviewDisplay(holder!!)
            mHandler.postDelayed({
                mHandler.sendEmptyMessage(MSG_REQUEST_FOCUS)
            }, 500)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            CameraUtils.releaseCamera()
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            CameraUtils.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK, CameraUtils.DESIRED_PREVIEW_FPS, getActivity())
        }

    }
}
