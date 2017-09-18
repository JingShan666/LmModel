package bbc.com.moteduan_lib2.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import bbc.com.moteduan_lib.R
import bbc.com.moteduan_lib.database.SpDataCache
import bbc.com.moteduan_lib.network.Req
import bbc.com.moteduan_lib.tools.ConvertUtils
import bbc.com.moteduan_lib.tools.ToastUtils
import bbc.com.moteduan_lib2.OpenCityActivity
import bbc.com.moteduan_lib2.UserInfoActivity
import bbc.com.moteduan_lib2.base.BaseFragment
import bbc.com.moteduan_lib2.home.invite.InviteActivity
import bbc.com.moteduan_lib2.home.invite.InvitePageBean
import bbc.com.moteduan_lib2.mineInvite.InviteDetailsDidNotSignUpActivity
import bbc.com.moteduan_lib2.mineNotive.NoticeDetailsDidNotSignUpActivity
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.google.gson.Gson
import com.liaoinstan.springview.container.DefaultHeader
import com.liaoinstan.springview.widget.SpringView
import com.liemo.shareresource.Const
import com.liemo.shareresource.Url
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import wei.toolkit.WebViewActivity
import wei.toolkit.bean.EventBusMessages
import wei.toolkit.utils.*
import wei.toolkit.widget.CircleImageBorderView
import wei.toolkit.widget.RoundedImageView
import wei.toolkit.widget.VRecycleView
import wei.toolkit.widget.VViewPager

/**
 * Created by Administrator on 2017/8/28 0028.
 * 首页
 *
 */
class InvitePageFragment : BaseFragment() {
    val TAG = "InvitePageFragment"
    var locationText: TextView? = null
    var springView: SpringView? = null
    var recycleView: VRecycleView? = null
    var delegateAdapter: DelegateAdapter? = null
    var virtualManager: VirtualLayoutManager? = null
    val REQUEST_CODE_OPEN_CITY = 1
    val REQUEST_CODE_DETAILS = 2
    var itemBean: InvitePageBean.OneBean? = null
    var dialogDataReq:DialogUtils.DataReqDialog? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_invite_page, container, false)
        EventBus.getDefault().register(this)
        dialogDataReq = DialogUtils.DataReqDialog(context)
        locationText = view?.findViewById(R.id.fragment_invite_page_location_text) as TextView?
        springView = view?.findViewById(R.id.fragment_invite_page_sv) as SpringView?
        recycleView = view?.findViewById(R.id.fragment_invite_page_rv) as VRecycleView?

        springView?.header = DefaultHeader(context)
        springView?.setListener(springViewListener)

        virtualManager = VirtualLayoutManager(context)
        recycleView?.layoutManager = virtualManager
        delegateAdapter = DelegateAdapter(virtualManager)
        recycleView?.adapter = delegateAdapter
        initDatas()
        initEvents()
        return view
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        setOpenCityText(SpDataCache.getCity())

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_OPEN_CITY) {
            if (data == null) return
            val city = data.getStringExtra("city")
            val latitude = data.getDoubleExtra("latitude", 0.00)
            val longitude = data.getDoubleExtra("longitude", 0.00)
            setOpenCityText(city)
            SpDataCache.saveAddress(context, city, city, latitude, longitude)
        } else if (requestCode == REQUEST_CODE_DETAILS) {
            itemBean?.let {
                itemBean?.k_num = 1
                itemBean = null
                delegateAdapter?.let {
                    delegateAdapter?.notifyDataSetChanged()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    val springViewListener: SpringView.OnFreshListener? = object : SpringView.OnFreshListener {
        override fun onLoadmore() {
        }

        override fun onRefresh() {
            if (delegateAdapter == null || recycleView == null) return
            initDatas()
        }
    }

    fun initEvents() {
        locationText?.setOnClickListener({ startActivityForResult(Intent(activity, OpenCityActivity::class.java), REQUEST_CODE_OPEN_CITY) })
    }

    fun setOpenCityText(text: String) {
        if (TextUtils.isEmpty(text)) return
        if (locationText != null) {
            locationText?.text = SpDataCache.getCity()
        }
    }

    @Subscribe
    fun onEventBusMainThread(gpsChangeNotification: EventBusMessages.GpsChangeNotification?) {
        if (gpsChangeNotification != null) {
            Loger.log(TAG, "onEventBusMainThread city = " + gpsChangeNotification.city + " latitude = " + gpsChangeNotification.latitude + " longitude = " + gpsChangeNotification.longitude)
            setOpenCityText(gpsChangeNotification.city)
        } else {
            setOpenCityText(SpDataCache.getCity())
        }
    }

    fun initDatas() {
        var memberId = ""
        var mobile = ""
        var city = SpDataCache.getCity()
        var loginBean = SpDataCache.getSelfInfo(context)
        loginBean?.let {
            memberId = loginBean.data.m_id
            mobile = loginBean.data.m_mobile
        }
        var map = HashMap<String, Any>()
        map.put("member_id", memberId)
        map.put("mobile", mobile)
        map.put("current_city", city)
        Req.post(Url.Home.homePage, map, object : Req.ReqCallback {
            override fun success(result: String?) {
                Loger.log(TAG, result)
                val invitePageBean = Gson().fromJson(result, InvitePageBean::class.java) ?: return
                delegateAdapter?.clear()
                if (invitePageBean.carousels?.size ?: 0 > 0) {
                    delegateAdapter?.addAdapter(BannerAdapter(invitePageBean.carousels))
                }

                delegateAdapter?.addAdapter(OrderAdapter())
                if (invitePageBean.two == null) invitePageBean.two = ArrayList<InvitePageBean.OneBean>()
                delegateAdapter?.addAdapter(MenuAdapter(invitePageBean.two, 2))
                if (invitePageBean.one == null) invitePageBean.one = ArrayList<InvitePageBean.OneBean>()
                delegateAdapter?.addAdapter(MenuAdapter(invitePageBean.one, 1))
                if (invitePageBean.three == null) invitePageBean.three = ArrayList<InvitePageBean.OneBean>()
                delegateAdapter?.addAdapter(MenuAdapter(invitePageBean.three, 3))
                delegateAdapter?.notifyDataSetChanged()
            }

            override fun failed(msg: String?) {
            }

            override fun completed() {
                springView?.onFinishFreshAndLoad()
            }

        })

    }


    inner class BannerAdapter(beanList: MutableList<InvitePageBean.CarouselsBean>) : DelegateAdapter.Adapter<BannerHolder>() {
        val list: MutableList<InvitePageBean.CarouselsBean>? = beanList

        override fun onBindViewHolder(holder: BannerHolder?, position: Int) {
            holder?.indicator?.removeAllViews()
            for (b in 0..list?.size!! - 1) {
                val imageView = ImageView(holder?.indicator?.context)
                val layoutParams = LinearLayout.LayoutParams(ConvertUtils.dp2px(holder?.indicator?.context, 6f), ConvertUtils.dp2px(holder?.indicator?.context, 6f))
                layoutParams.rightMargin = ConvertUtils.dp2px(holder?.indicator?.context, 6f)
                imageView.layoutParams = layoutParams
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator))
                imageView.isSelected = b == 0
                holder?.indicator?.addView(imageView)
            }

            holder?.viewPager?.adapter = object : PagerAdapter() {
                override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
                    return view == `object`
                }

                override fun getCount(): Int {
                    return list?.size ?: 0
                }

                override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                    val bean = list?.get(position)
                    val imageView = ImageView(container?.context)
                    val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    imageView.layoutParams = layoutParams
                    imageView?.scaleType = ImageView.ScaleType.FIT_XY
                    ImageLoad.bind(imageView, bean?.photo_url)
                    imageView.setOnClickListener {
                        context.startActivity(Intent(context, WebViewActivity::class.java)
                                .putExtra("url", bean?.html_url)
                                .putExtra("title", bean?.html_name))
                    }
                    container?.addView(imageView)
                    return imageView
                }

                override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                    container?.removeView(`object` as View?)
                }
            }

            holder?.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    val indicatorSize = holder.indicator?.childCount ?: 0
                    for (p in 0..indicatorSize - 1) {
                        val view = holder.indicator?.getChildAt(p)
                        view?.isSelected = p == position
                    }
                }

            })
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BannerHolder {
            val holder = BannerHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_invite_page_banner, parent, false))
            return holder
        }

        override fun onCreateLayoutHelper(): LayoutHelper {
            return LinearLayoutHelper()
        }

        override fun getItemCount(): Int {
            return 1
        }
    }

    class BannerHolder(view: View?) : RecyclerView.ViewHolder(view) {
        val viewPager: VViewPager? = view?.findViewById(R.id.item_invite_page_banner_vp) as VViewPager?
        val indicator: LinearLayout? = view?.findViewById(R.id.item_invite_page_banner_indicator) as LinearLayout?
    }

    inner class MenuAdapter(beanList: MutableList<InvitePageBean.OneBean>, type: Int?) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {
        val list = beanList
        val t = type /*1 娱乐  2通告 3商务*/
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
            if (viewType == 0) {
                return MenuTitleHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_invite_page_title_order, parent, false))
            }
            return MenuHolder(LayoutInflater.from(parent?.context).inflate(R.layout.dateitem, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder is MenuTitleHolder) {
                holder.nodataTv?.visibility = if (list.size < 1) View.VISIBLE else View.GONE
                when (t) {
                    1 -> {
                        holder.imageView?.setImageResource(R.drawable.pic_yule2x)
                        holder.imageView?.setOnClickListener {
                            context.startActivity(Intent(context, InviteActivity::class.java)
                                    .putExtra("url", Url.NavigationLabel.play)
                                    .putExtra("title", "娱乐")
                                    .putExtra("dataType", 1))
                        }
                    }
                    2 -> {
                        holder.imageView?.setImageResource(R.drawable.pic_tonggao2x)
                        holder.imageView?.setOnClickListener {
                            context.startActivity(Intent(context, InviteActivity::class.java)
                                    .putExtra("url", Url.NavigationLabel.notice)
                                    .putExtra("title", "通告")
                                    .putExtra("dataType", 2))
                        }
                    }
                    3 -> {
                        holder.imageView?.setImageResource(R.drawable.pic_shangwu2x)
                        holder.imageView?.setOnClickListener {
                            context.startActivity(Intent(context, InviteActivity::class.java)
                                    .putExtra("url", Url.NavigationLabel.business)
                                    .putExtra("title", "商业")
                                    .putExtra("dataType", 3))
                        }
                    }
                }
            } else if (holder is MenuHolder) {
                val h = holder
                val bean = list[position - 1]
                ImageLoad.bind(h.conver, bean.photos_all)
                ImageLoad.bind(h.portrait, bean.u_head_photo)
                h.name?.text = bean.u_nick_name
                h.age?.text = bean.u_age.toString()

                if (!TextUtils.isEmpty(bean.start_time) && !TextUtils.isEmpty(bean.end_time)) {
                    h.time?.text = DateUtils.getCustomFormatTime(bean.start_time, bean.end_time)
                }

                val addressArr = bean.adress.split("==".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (addressArr[0] != null) {
                    h.address?.text = addressArr[0]
                } else {
                    h.address?.text = bean.adress
                }

                h.price?.text = bean.reward_price.toString()

                if (bean.k_num == 1) {
                    h.apply?.isEnabled = false
                    h.apply?.text = "已报名"
                } else {
                    h.apply?.isEnabled = true
                    h.apply?.text = "报名"
                }

                h.apply?.setOnClickListener(View.OnClickListener {
                    val loginBean = SpDataCache.getSelfInfo(context)
                    if (null == loginBean || TextUtils.isEmpty(loginBean.data.m_id)) {
                        ToastUtil.show(context, "您还未登录，请先登录")
                        val intent = Intent()
                        intent.action = "bbc.com.moteduan_lib2.login.LoginActivity"
                        context.startActivity(intent)
                        return@OnClickListener
                    }
                    AlertDialog.Builder(context)
                            .setMessage("确定要报名吗？")
                            .setPositiveButton("确定") { dialog, which ->
                                dialog.dismiss()
                                dialogDataReq?.show()
                                val map = java.util.HashMap<String, Any>()
                                map.put("member_id", loginBean.data.m_id)
                                map.put("trader_id", bean.trader_id)
                                Req.post(if (t == Const.INVITE_TYPE_NOVICE) Url.Invite.applyNotice else Url.applyInviteOrder, map, object : Req.ReqCallback {
                                    override fun success(result: String) {
                                        try {
                                            val jsonObject = JSONObject(result)
                                            val code = jsonObject.getString("code")
                                            val tips = jsonObject.getString("tips")
                                            ToastUtils.getInstance(context).showText(tips)
                                            if ("200" != code) {
                                                return
                                            }
//                                                        list.removeAt(position - 1)
//                                                        notifyDataSetChanged()
                                            bean.k_num = 1
                                            h.apply?.isEnabled = false
                                            h.apply?.text = "已报名"
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    override fun failed(msg: String) {
                                        ToastUtils.getInstance(context).showText(context.resources.getString(R.string.error_network_block))
                                    }
                                    override fun completed() {
                                        dialogDataReq?.dismiss()
                                    }
                                })
                            }
                            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                            .show()
//                    val map = java.util.HashMap<String, Any>()
//                    map.put("member_id", SpDataCache.getSelfInfo(context)!!.data.m_id)
//                    Req.post(Url.getModelAuthState, map, object : Req.ReqCallback {
//                        override fun success(result: String) {
//                            LogDebug.log(TAG, result)
//                            var jsonObject: JSONObject? = null
//                            try {
//                                jsonObject = JSONObject(result)
//                                val code = jsonObject.getString("code")
//                                val tips = jsonObject.getString("tips")
//                                if ("200" != code) {
//                                    startActivity(Intent(activity, AuthenticationActivity::class.java))
//                                    ToastUtils.getInstance(context).showText(tips)
//                                    return
//                                }
//                            } catch (e: JSONException) {
//                                e.printStackTrace()
//                            }
//                        }
//                        override fun failed(msg: String) {}
//                        override fun completed() {}
//                    })
                })

                h.portrait?.setOnClickListener({ startActivity(Intent(context, UserInfoActivity::class.java).putExtra("userId", bean.u_id)) })

                h.itemView.setOnClickListener({
                    if (t == Const.INVITE_TYPE_NOVICE) {

                        startActivityForResult(Intent(context, NoticeDetailsDidNotSignUpActivity::class.java)
                                .putExtra("orderId", bean.trader_id), REQUEST_CODE_DETAILS)

                    } else {
                        startActivityForResult(Intent(context, InviteDetailsDidNotSignUpActivity::class.java)
                                .putExtra("orderId", bean.trader_id), REQUEST_CODE_DETAILS)
                    }
                    itemBean = bean
                })


            }
        }

        override fun onCreateLayoutHelper(): LayoutHelper {
            return LinearLayoutHelper()
        }

        override fun getItemCount(): Int {
            return list.size + 1
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) 0 else 1

        }
    }

    class MenuTitleHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView? = itemView?.findViewById(R.id.item_invite_page_title_order_img) as ImageView?
        val nodataTv: TextView? = itemView?.findViewById(R.id.item_invite_page_title_order_nodata) as TextView?
    }

    class MenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var conver: RoundedImageView? = itemView.findViewById(R.id.datecover) as RoundedImageView
        var portrait: CircleImageBorderView? = itemView.findViewById(R.id.dateicon) as CircleImageBorderView
        var name: TextView? = itemView.findViewById(R.id.datename) as TextView
        var age: TextView? = itemView.findViewById(R.id.datesex) as TextView
        var time: TextView? = itemView.findViewById(R.id.datetime) as TextView
        var address: TextView? = itemView.findViewById(R.id.datelocation) as TextView
        var price: TextView? = itemView.findViewById(R.id.datemoney) as TextView
        var apply: TextView? = itemView.findViewById(R.id.dateapply) as TextView

    }

    inner class OrderAdapter : DelegateAdapter.Adapter<OrderHolder>() {
        override fun onBindViewHolder(holder: OrderHolder?, position: Int) {
            holder?.business?.setOnClickListener {
                context.startActivity(Intent(context, InviteActivity::class.java)
                        .putExtra("url", Url.NavigationLabel.business)
                        .putExtra("title", "商业")
                        .putExtra("dataType", 3))
            }
            holder?.notice?.setOnClickListener {
                context.startActivity(Intent(context, InviteActivity::class.java)
                        .putExtra("url", Url.NavigationLabel.notice)
                        .putExtra("title", "通告")
                        .putExtra("dataType", 2))
            }
            holder?.play?.setOnClickListener {
                context.startActivity(Intent(context, InviteActivity::class.java)
                        .putExtra("url", Url.NavigationLabel.play)
                        .putExtra("title", "娱乐")
                        .putExtra("dataType", 1))
            }
        }

        override fun getItemCount(): Int {
            return 1
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OrderHolder {
            val holder = OrderHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_invite_page_order, parent, false))
            return holder
        }

        override fun onCreateLayoutHelper(): LayoutHelper {
            return LinearLayoutHelper()
        }

    }

    class OrderHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val notice: TextView? = itemView?.findViewById(R.id.item_invite_page_order_notice) as TextView?
        val play: TextView? = itemView?.findViewById(R.id.item_invite_page_order_play) as TextView?
        val business: TextView? = itemView?.findViewById(R.id.item_invite_page_order_business) as TextView?
    }


}