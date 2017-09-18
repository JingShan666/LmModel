package wei.moments

import android.annotation.SuppressLint
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
import com.google.gson.Gson
import com.liaoinstan.springview.container.DefaultFooter
import com.liaoinstan.springview.container.DefaultHeader
import com.liaoinstan.springview.widget.SpringView
import com.liemo.shareresource.Url
import org.json.JSONArray
import org.json.JSONObject
import wei.moments.base.BaseActivity
import wei.moments.bean.LoginBean
import wei.moments.bean.MomentsMsgListBean
import wei.moments.database.SPUtils
import wei.moments.network.ReqCallback
import wei.moments.network.ReqUrl
import wei.toolkit.utils.DateUtils
import wei.toolkit.utils.ImageLoad
import wei.toolkit.utils.Loger
import wei.toolkit.utils.ToastUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MomentsMsgListActivity : BaseActivity() {
    private val TAG: String = "MomentsMsgListActivity"
    private var back: ImageView? = null
    private var title: TextView? = null
    private var springView: SpringView? = null
    private var recycleView: RecyclerView? = null
    private var timeStamp: Long = 0
    private var pageNumber: Int = 1
    private var pageSize: Int = 10
    private val dataList: MutableList<MomentsMsgListBean.DynamicsBean.ListBean>? = ArrayList<MomentsMsgListBean.DynamicsBean.ListBean>()
    private var rvAdapter: RvAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lm_activity_moments_msg_list)
        back = findViewById(R.id.bar_action_back) as ImageView?
        title = findViewById(R.id.bar_action_title) as TextView?
        title?.text = "消息"
        springView = findViewById(R.id.activity_moments_msg_list_sv) as SpringView?
        springView?.header = DefaultHeader(this)
        springView?.footer = DefaultFooter(this)
        recycleView = findViewById(R.id.activity_moments_msg_list_rv) as RecyclerView?
        val layoutManage: LinearLayoutManager? = LinearLayoutManager(this)
        layoutManage?.orientation = LinearLayoutManager.VERTICAL
        recycleView?.layoutManager = layoutManage
        rvAdapter = RvAdapter()
        recycleView?.adapter = rvAdapter
        initEvents()
        loadData(1)
    }

    /*1下拉，2上拉*/
    fun loadData(type: Int) {
        val map = HashMap<String, Any>()
        val loginBean: LoginBean? = SPUtils.getSelfInfo(this)
        map.put("use_id", loginBean?.data!!.m_id)
        map.put("use_type", SPUtils.ROLE_TYPE_DEF)
        map.put("pageSize", pageSize)
        when (type) {
            1 -> {
                pageNumber = 1
                if (timeStamp < 1) timeStamp = System.currentTimeMillis()
                map.put("pageNumber", pageNumber)
                map.put("timeStamp", timeStamp)
            }
            2 -> {
                ++pageNumber
                map.put("pageNumber", pageNumber)
                map.put("timeStamp", timeStamp)
            }
            else -> {
            }
        }
        ReqUrl.post(Url.momentsMsgList, map, object : ReqCallback.Callback1<String> {
            override fun success(result: String?) {
                Loger.log(TAG, result)
                val bean: MomentsMsgListBean? = Gson().fromJson(result, MomentsMsgListBean::class.java)
                if ("200" != bean?.code) {
                    ToastUtil.show(this@MomentsMsgListActivity, bean?.tips)
                    return
                }
                when (type) {
                    1 -> {
                        dataList?.clear()
                        timeStamp = if (bean.timeStamp > 0) bean.timeStamp else System.currentTimeMillis()
                    }
                }
                dataList?.addAll(bean.dynamics.list)
                rvAdapter?.notifyDataSetChanged()
            }

            override fun failed(msg: String?) {
                ToastUtil.show(this@MomentsMsgListActivity, msg)
            }

            override fun completed() {
                springView?.onFinishFreshAndLoad()
            }

        })
    }

    fun initEvents() {
        back?.setOnClickListener { finish() }
        springView?.setListener(springViewListener)
    }


    var springViewListener = object : SpringView.OnFreshListener {
        override fun onLoadmore() {
            loadData(2)
        }

        override fun onRefresh() {
            loadData(1)
        }

    }


    inner class RvAdapter : RecyclerView.Adapter<RvHolder>() {
        override fun getItemCount(): Int {
            return dataList?.size!!
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RvHolder? {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.lm_item_moments_msg_list, parent, false)
            return RvHolder(view)
        }

        @SuppressLint("SimpleDateFormat")
        override fun onBindViewHolder(holder: RvHolder?, position: Int) {
            val bean: MomentsMsgListBean.DynamicsBean.ListBean? = dataList?.get(position)
            ImageLoad.bind(holder?.portrait, bean?.b_head_photo)
            holder?.name?.text = bean?.b_names
            when (bean?.dynamic_type) {
                1 -> {
                    // 点赞
                    holder?.content?.text = ""
                    holder?.contentImg?.setImageResource(R.mipmap.lm_icon_praise_on)
                }
                2 -> {
                    // 送花
                    holder?.content?.text = ""
                    holder?.contentImg?.setImageResource(R.mipmap.lm_icon_flower_on)
                }
                3 -> {
                    // 评论
                    holder?.content?.text = bean?.contenting
                    holder?.contentImg?.setImageResource(0)
                }
                else -> {
                }
            }
            var sendtime: String = ""
            if (!TextUtils.isEmpty(bean?.timing)) {
                val timeDiff: Long = DateUtils.getTimeDiff(bean?.timing)
                if (timeDiff > 0) {
                    val minute: Int = (timeDiff / 1000 / 60).toInt()
                    try {
                        if (minute < 60) {
                            sendtime = "${minute}分钟前"
                        } else if (minute < 24 * 60) {
                            sendtime = "${minute / 60}小时前"
                        } else if (minute < 7 * 24 * 60) {
                            sendtime = "${minute / (24 * 60)}天前"
                        } else {
                            val calendar: Calendar? = Calendar.getInstance()
                            calendar?.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bean?.timing)
                            if (DateUtils.isToyear(bean?.timing)) {
                                sendtime = "${calendar?.get(Calendar.MONTH)}月${calendar?.get(Calendar.DAY_OF_MONTH)}日  ${calendar?.get(Calendar.HOUR_OF_DAY)}:${String.format("%02d", calendar?.get(Calendar.MINUTE))}"
                            } else {
                                sendtime = "${calendar?.get(Calendar.YEAR)}年${calendar?.get(Calendar.MONTH)}月${calendar?.get(Calendar.DAY_OF_MONTH)}日"
                            }
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }
            }
            holder?.time?.text = sendtime

            var imgUrl: String? = null
            if (!TextUtils.isEmpty(bean?.video_path)) {
                val jsonObject: JSONObject? = JSONObject(bean?.video_path)
                val jsonArray: JSONArray? = jsonObject?.getJSONArray("list")
                if (jsonArray?.length()!! > 0) {
                    val jsonObject1: JSONObject? = jsonArray.getJSONObject(0)
                    val thumbnail: String? = jsonObject1?.getString("thumbnail")
                    imgUrl = thumbnail!!
                }
            }

            if (!TextUtils.isEmpty(bean?.photos)) {
                val jsonObject: JSONObject? = JSONObject(bean?.photos)
                val jsonArray: JSONArray? = jsonObject?.getJSONArray("list")
                if (jsonArray?.length()!! > 0) {
                    val jsonObject1: JSONObject? = jsonArray.getJSONObject(0)
                    imgUrl = jsonObject1?.getString("url")
                }
            }

            if (!TextUtils.isEmpty(imgUrl)) {
                holder?.img?.visibility = View.VISIBLE
                holder?.momentContent?.visibility = View.GONE
                ImageLoad.bind(holder?.img, imgUrl)
            } else {
                // 如果没有可展示在右边的图像，则显示动态文字内容
                if (!TextUtils.isEmpty(bean?.content)) {
                    holder?.img?.visibility = View.GONE
                    holder?.momentContent?.visibility = View.VISIBLE
                    holder?.momentContent?.text = bean?.content
                }
            }

            holder?.portrait?.setOnClickListener {
                if (!TextUtils.isEmpty(bean?.b_content_sex)) {
                    if (bean?.b_content_sex != "2") {
                        if (!TextUtils.isEmpty(bean?.b_use_id)) {
                            startActivity(Intent("bbc.com.moteduan_lib2.UserInfoActivity").putExtra("userId", bean?.b_use_id))
                        }
                    }
                }
            }
            holder?.itemView?.setOnClickListener {
//                when (bean?.content_type) {
//                    1, 3 -> {
//                        startActivity(Intent(this@MomentsMsgListActivity, MomentDetailsPictureActivity::class.java).putExtra("contentId", bean?.content_id))
//                    }
//                    2 -> {
//                        startActivity(Intent(this@MomentsMsgListActivity, MomentDetailsVideoActivity::class.java).putExtra("contentId", bean?.content_id))
//                    }
//                }

                startActivity(Intent(this@MomentsMsgListActivity, MomentDetailsActivity::class.java).putExtra("contentId", bean?.content_id))
            }


        }

    }

    inner class RvHolder(view: View) : RecyclerView.ViewHolder(view) {
        var portrait: ImageView? = view.findViewById(R.id.lm_item_moments_msg_list_portrait) as ImageView?
        var name: TextView? = view.findViewById(R.id.lm_item_moments_msg_list_name) as TextView?
        var content: TextView? = view.findViewById(R.id.lm_item_moments_msg_list_content) as TextView?
        var contentImg: ImageView? = view.findViewById(R.id.lm_item_moments_msg_list_content_img) as ImageView?
        var img: ImageView? = view.findViewById(R.id.lm_item_moments_msg_list_img) as ImageView?
        var time: TextView? = view.findViewById(R.id.lm_item_moments_msg_list_time) as TextView?
        var momentContent: TextView? = view.findViewById(R.id.lm_item_moments_msg_list_moment_content) as TextView?
    }

}

