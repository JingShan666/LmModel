package wei.toolkit.utils

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import wei.toolkit.R

/**
 * Created by Administrator on 2017/8/15 0015.
 */
class DialogUtils {
    companion object Factory {
        /**
         * @param context
         * *
         * @param callback
         */
        fun commonAlert(context: Context, title: String, content: String, positiveName: String, neutralName: String, negativeName: String, callback: Handler.Callback?) {
            val builder = AlertDialog.Builder(context)
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title)
            }
            if (!TextUtils.isEmpty(content)) {
                builder.setMessage(content)
            }
            if (!TextUtils.isEmpty(positiveName)) {
                builder.setPositiveButton(positiveName) { dialog, which ->
                    if (callback != null) {
                        val message = Message.obtain()
                        message.what = 0
                        callback.handleMessage(message)
                    }

                    dialog.dismiss()
                }
            }
            if (!TextUtils.isEmpty(neutralName)) {
                builder.setNeutralButton(neutralName) { dialog, which ->
                    if (callback != null) {
                        val message = Message.obtain()
                        message.what = 1
                        callback.handleMessage(message)
                    }
                    dialog.dismiss()
                }
            }
            if (!TextUtils.isEmpty(negativeName)) {
                builder.setNegativeButton(negativeName) { dialog, which ->
                    if (callback != null) {
                        val message = Message.obtain()
                        message.what = 2
                        callback.handleMessage(message)
                    }
                    dialog.dismiss()
                }
            }
            builder.show()

        }
    }


    class DataReqDialog(context: Context) {
        val con = context
        var dialog = lazy {
            val alertBuilder = AlertDialog.Builder(con).apply {
                val view = LayoutInflater.from(con).inflate(R.layout.dialog_data_req, null)
                setView(view)
                val imageView: ImageView = view.findViewById(R.id.dialog_data_req_img) as ImageView
                Glide.with(con)
                        .load(R.drawable.gif_loaddata)
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView)
            }
            alertBuilder.create().apply {
//                val p = window.attributes
//                p.width = (50 * con.resources.displayMetrics.density + 0.5f).toInt()
//                p.height = (50 * con.resources.displayMetrics.density + 0.5f).toInt()
//                window.attributes = p
            }

        }

        fun show() {
            dialog.value.show()
        }

        fun dismiss() {
            dialog.value.dismiss()
        }
    }


}