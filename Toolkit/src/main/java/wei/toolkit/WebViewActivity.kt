@file:Suppress("UNREACHABLE_CODE")

package wei.toolkit

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import wei.toolkit.utils.Loger

/**
 * Created by Administrator on 2017/6/26 0026.
 */
class WebViewActivity : BaseActivity() {
    val TAG = "WebViewActivity"
    private var back: ImageView? = null
    private var title: TextView? = null
    private var sure: TextView? = null
    private var webview: WebView? = null
    private var progress: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        back = findViewById(R.id.activity_webview_back) as ImageView
        title = findViewById(R.id.activity_webview_label) as TextView
        sure = findViewById(R.id.activity_webview_sure) as TextView
        progress = findViewById(R.id.activity_webview_progress) as ProgressBar
        webview = findViewById(R.id.activity_webview) as WebView
        sure?.visibility = View.GONE
        initData()
        initEvents()

    }

    private fun initData() {
        title?.text = intent.getStringExtra("title") ?: ""
        webview?.settings?.javaScriptEnabled = true
        webview?.setWebViewClient(MyWebViewClient())
        webview?.setWebChromeClient(MyWebViewChromeClient())
        webview?.loadUrl(intent.getStringExtra("url") ?: "")
    }

    private fun initEvents() {
        back?.setOnClickListener { finish() }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }
    }

    inner class MyWebViewChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            Loger.log(TAG,"$newProgress")
            when (newProgress) {
                100 -> {
                    progress?.visibility = View.GONE
                }
                else -> {
                    if (progress?.visibility != View.VISIBLE) {
                        progress?.visibility = View.VISIBLE
                    }
                    progress?.progress = newProgress
                }
            }
        }
    }
}

