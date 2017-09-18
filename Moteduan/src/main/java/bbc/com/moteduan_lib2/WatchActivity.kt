package bbc.com.moteduan_lib2

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bbc.com.moteduan_lib.R
import bbc.com.moteduan_lib.base.BaseActivity
import bbc.com.moteduan_lib.network.ImageLoad
import bbc.com.moteduan_lib.network.Req
import com.liemo.shareresource.Url
import bbc.com.moteduan_lib2.bean.WatchBean
import com.google.gson.Gson
import com.liaoinstan.springview.container.DefaultFooter
import com.liaoinstan.springview.container.DefaultHeader
import com.liaoinstan.springview.widget.SpringView
import wei.toolkit.helper.EmptyDataViewHolder
import wei.toolkit.utils.Loger
import wei.toolkit.widget.CircleImageBorderView
import wei.toolkit.widget.VRecycleView

class WatchActivity : BaseActivity() {
    private val TAG: String = "WatchActivity"
    private var back: ImageView? = null
    private var title: TextView? = null
    private var springView: SpringView? = null
    private var recycleView: VRecycleView? = null
    private var memberId: String? = ""
    private var reqType: String? = "1" /*1我关注的  2关注我的*/
    private var dataList: MutableList<WatchBean.AuthenBean.ListBean>? = ArrayList()
    private var pageSize: Int = 10
    private var pageNumber: Int = 1
    private var virtualRvAdapter: VirtualRvAdapter? = null
    private var titleString: String? = ""
    private var timeStamp: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)
        memberId = intent?.getStringExtra("memberId")
        reqType = intent?.getStringExtra("reqType")
        titleString = intent?.getStringExtra("title")
        if (TextUtils.isEmpty(memberId)) {
            toast.showText("未找到用户id")
            finish()
        }
        initView()
        initEvents()
        loadData(1)
    }

    override fun initView() {
        back = findViewById(R.id.bar_action_back) as ImageView?
        title = findViewById(R.id.bar_action_title) as TextView?
        title?.text = titleString
        springView = findViewById(R.id.activity_watch_sv) as SpringView?
        springView?.header = DefaultHeader(this)
        springView?.footer = DefaultFooter(this)
        recycleView = findViewById(R.id.activity_watch_rv) as VRecycleView?


        val linearManage = LinearLayoutManager(this)
        recycleView?.layoutManager = linearManage
        virtualRvAdapter = VirtualRvAdapter()
    }

    override fun initEvents() {
        back?.setOnClickListener {
            finish()
        }
        springView?.setListener(springViewListener)
    }

    /*1下拉 2上拉*/
    fun loadData(type: Int) {
        val map: HashMap<String, Any>? = HashMap()
        map?.put("member_id", memberId!!)
        map?.put("type", reqType!!)
        map?.put("pageSize", pageSize)
        when (type) {
            1 -> {
                pageNumber = 1
                map?.put("timeStamp", if (timeStamp < 1) {
                    timeStamp = System.currentTimeMillis()
                    timeStamp
                } else {
                    timeStamp
                })
            }
            2 -> {
                ++pageNumber
                map?.put("timeStamp", timeStamp)
            }
        }
        map?.put("pageNumber", pageNumber)
        Req.post(Url.getWatchList, map, object : Req.ReqCallback {
            override fun success(result: String?) {
                Loger.log(TAG, result)
                val bean: WatchBean? = Gson().fromJson(result, WatchBean::class.java)
                if ("200" != bean?.code) {
                    var tips = bean?.tips
                    if ("201" == bean?.code) {
                        if (1 == type) {
                            /*下拉的返回暂无数据，dataList数据列表里还有就清除掉*/
                            if (dataList?.size!! > 0) {
                                dataList?.clear()
                            }
                            recycleView?.adapter = virtualRvAdapter
                            return
                        } else if (2 == type) {
                            tips = resources.getString(R.string.sr_no_more_data)
                        }
                    }
                    toast.showText(tips)
                    return
                }
                if (1 == type) {
                    dataList?.clear()
                    timeStamp = if (bean.timeStamp > 0) bean.timeStamp else System.currentTimeMillis()
                    bean?.authen?.list?.let {
                        dataList?.addAll(it)
                    }
                    recycleView?.adapter = virtualRvAdapter

                } else {
                    bean?.authen?.list?.let {
                        dataList?.addAll(it)
                        recycleView?.adapter?.notifyDataSetChanged()
                    }

                }

            }

            override fun failed(msg: String?) {
            }

            override fun completed() {
                springView?.onFinishFreshAndLoad()
            }
        })
    }

    val springViewListener: SpringView.OnFreshListener? = object : SpringView.OnFreshListener {
        override fun onLoadmore() {
            loadData(2)
        }

        override fun onRefresh() {
            loadData(1)
        }
    }


    private inner class VirtualRvAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onBindViewHolder(h: RecyclerView.ViewHolder?, position: Int) {
            Loger.log(TAG, "VirtualRvAdapter onBindViewHolder")

            when (h) {
                is EmptyDataViewHolder -> {
                    val holder: EmptyDataViewHolder? = h
                    holder?.itemView?.visibility = View.VISIBLE
                }
                is RvHolder -> {
                    val holder: RvHolder? = h
                    val bean: WatchBean.AuthenBean.ListBean? = dataList?.get(position)
                    ImageLoad.bind(holder?.portrait, bean?.u_head_photo)
                    holder?.name?.text = bean?.u_nick_name
                    holder?.age?.text = "${bean?.u_age}岁"
                    holder?.weight?.text = "${bean?.u_tall}cm/${bean?.u_weight}kg"
                    holder?.itemView?.setOnClickListener {
                        if (bean?.u_sex != 2) {
                            startActivity(Intent(this@WatchActivity, UserInfoActivity::class.java).putExtra("userId", bean?.u_id))
                        }
                    }
                }
            }

        }

        override fun getItemCount(): Int {
            Loger.log(TAG, "VirtualRvAdapter getItemCount ${dataList?.size!!}")
            return isEmptyData() + (dataList?.size ?: 0)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            Loger.log(TAG, "VirtualRvAdapter onCreateViewHolder")
            if (-1 == viewType) {
                val view: View? = LayoutInflater.from(parent?.context).inflate(R.layout.helper_empty_data_view, parent, false)
                return EmptyDataViewHolder(view)
            }
            val view: View? = LayoutInflater.from(parent?.context).inflate(R.layout.item_watch, parent, false)
            return RvHolder(view)
        }

        override fun getItemViewType(position: Int): Int {

            return if (isEmptyData() > 0) -1 else super.getItemViewType(position)
        }

        fun isEmptyData(): Int {
            return dataList?.let { if (dataList?.size!! > 0) 0 else 1 } ?: 0
        }


    }

    private inner class RvHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val portrait: CircleImageBorderView? = itemView?.findViewById(R.id.item_watch_portrait) as CircleImageBorderView
        val name: TextView? = itemView?.findViewById(R.id.item_watch_name) as TextView?
        val age: TextView? = itemView?.findViewById(R.id.item_watch_age) as TextView?
        val weight: TextView? = itemView?.findViewById(R.id.item_watch_weight) as TextView?
    }
}
