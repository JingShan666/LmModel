package bbc.com.moteduan_lib2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bbc.com.moteduan_lib.R
import bbc.com.moteduan_lib.app.App
import bbc.com.moteduan_lib.database.SpDataCache
import bbc.com.moteduan_lib.log.LogDebug
import bbc.com.moteduan_lib.maps.LmLocation
import bbc.com.moteduan_lib.maps.LmMap
import bbc.com.moteduan_lib.maps.UploadGPS
import bbc.com.moteduan_lib.network.Req
import bbc.com.moteduan_lib2.bean.OpenCityBean
import com.google.gson.Gson
import com.liemo.shareresource.Url
import wei.toolkit.utils.PermissionUtils
import wei.toolkit.utils.ToastUtil

/**
 * 城市定位页面
 */

class OpenCityActivity : AppCompatActivity() {
    private val TAG = "OpenCityActivity"
    private var close: ImageView? = null
    private var gpsLocation: TextView? = null
    private var recycleView: RecyclerView? = null
    private var lmLocation: LmLocation? = null
    private var dataList: MutableList<OpenCityBean.OpendCityBean>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_city)
        initViews()
        initEvent()
        requestLocation()
        requestOpenCityList()
    }

    fun initViews() {
        close = findViewById(R.id.activity_open_city_close) as ImageView?
        gpsLocation = findViewById(R.id.activity_open_city_gps_location) as TextView?
        recycleView = findViewById(R.id.activity_open_city_rv) as RecyclerView?
        val linearManage = LinearLayoutManager(this@OpenCityActivity)
        recycleView?.layoutManager = linearManage

    }

    fun requestLocation() {

        val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        PermissionUtils.checkPermission(this@OpenCityActivity, permission, object : PermissionUtils.PermissionCallback {
            override fun onGranted() {
                val imap = LmMap.get()
                imap.setLocationListener({ location ->
                    imap.stopLocation()
                    lmLocation = location
                    gpsLocation?.text = lmLocation?.city
                    SpDataCache.saveGpsAddress(this@OpenCityActivity, lmLocation?.city, lmLocation?.city, lmLocation?.latitude!!, lmLocation?.longitude!!)
                    val loginBean = SpDataCache.getSelfInfo(App.getApp())
                    if (loginBean != null) {
                        UploadGPS.request<Any>(loginBean.data.m_id, lmLocation?.latitude!!, lmLocation?.longitude!!, lmLocation?.city, null)
                    }
                    LogDebug.log(TAG, lmLocation?.toString())
                })
                imap.startLocation()
            }

            override fun onDenied(deniedName: Array<out String>?) {
                gpsLocation?.text = "定位失败,定位权限未开启"
            }
        })
    }

    fun initEvent() {
        close?.setOnClickListener { finish() }
        gpsLocation?.setOnClickListener {
            lmLocation?.let {
                val intent = Intent()
                intent.putExtra("city", lmLocation?.city)
                intent.putExtra("latitude", lmLocation?.latitude)
                intent.putExtra("longitude", lmLocation?.longitude)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }


    fun requestOpenCityList() {
        Req.post(Url.openCity, null, object : Req.ReqCallback {
            override fun success(result: String?) {
                val openCity: OpenCityBean? = Gson().fromJson(result, OpenCityBean::class.java)
                openCity?.let {
                    if ("200" != openCity.code) {
                        ToastUtil.show(this@OpenCityActivity, openCity.tips)
                        return
                    }

                    openCity.opend_city?.let {
                        dataList?.addAll(openCity.opend_city)
                        recycleView?.adapter = RvAdapter()
                    }

                }
            }

            override fun failed(msg: String?) {
                ToastUtil.show(this@OpenCityActivity, "城市列表读取失败")
            }

            override fun completed() {
            }
        })
    }


    private inner class RvAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return RvHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_open_city, parent, false))
        }

        override fun getItemCount(): Int {
            return dataList?.size!!
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            val bean = dataList?.get(position)
            val h: RvHolder? = holder as RvHolder?
            h?.text?.text = bean?.current_city
            h?.text?.setOnClickListener {
                val intent = Intent()
                intent.putExtra("city", bean?.current_city)
                intent.putExtra("latitude", bean?.lat)
                intent.putExtra("longitude", bean?.lng)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

    }

    private inner class RvHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val text: TextView? = itemView?.findViewById(R.id.item_open_city_text) as TextView?
    }
}
