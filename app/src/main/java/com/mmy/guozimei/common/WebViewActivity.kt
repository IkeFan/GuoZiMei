package com.mmy.guozimei.common

import android.graphics.Bitmap
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity<IPresenter<*>>() {
    val TAG = "WebViewActivity"

    override fun setupDagger(appComponent: AppComponent) {

    }

    override fun getLayoutID(): Any = R.layout.activity_web_view

    override fun requestSuccess(any: IBean) {

    }

    override fun initView() {
        val webSettings = web_view.settings
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.setSupportZoom(false)
        webSettings.builtInZoomControls = false
        webSettings.domStorageEnabled = true
    }

    override fun initData() {
        if(intent.hasExtra("url")){
            web_view.loadUrl(intent.getStringExtra("url"))
        }
    }

    override fun initEvent() {
        web_view.addJavascriptInterface(this, "android")
        web_view.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                hidLoading()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showLoading()
            }
        }

        web_view.webChromeClient = object: WebChromeClient(){

        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(web_view.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){
            web_view.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        web_view.destroy()
        web_view.removeJavascriptInterface("android")
        super.onDestroy()
    }

    @JavascriptInterface
    fun backToActivity(){
        Log.e(TAG,TAG+":backToActivity")
        finish()
    }

//    override fun finish() {
//        val view = window.decorView as ViewGroup
//        view.removeAllViews()
//        super.finish()
//    }

}
