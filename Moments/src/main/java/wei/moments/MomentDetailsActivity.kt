package wei.moments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.sharesdk.onekeyshare.OnekeyShare
import com.google.gson.Gson
import com.liemo.shareresource.Url
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import wei.moments.base.*
import wei.moments.bean.FlowerListBean
import wei.moments.bean.LoginBean
import wei.moments.bean.MomentListItemBean
import wei.moments.bean.comment.CommentListBean
import wei.moments.bean.comment.CommentReplyBean
import wei.moments.database.SPUtils
import wei.moments.decoration.GridlayoutDecoration
import wei.moments.holder.PictureHoder
import wei.moments.network.ReqCallback
import wei.moments.network.ReqUrl
import wei.moments.network.ReqUrl.post
import wei.toolkit.WatchPictureActivity
import wei.toolkit.utils.*
import wei.toolkit.widget.CircleImageBorderView
import wei.toolkit.widget.VideoPlayView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MomentDetailsActivity : BaseActivity() {
    companion object {
        val TAG = "MomentDetailsActivity"
        private var commentFragmentIsInit: Boolean = false

    }

    private var bottomCommentEditRv: RelativeLayout? = null
    private var commentFragment: MomentDetailsActivity.CommentFragment? = null

    private var mTabLayout: TabLayout? = null
    private var mBaseViewPager: ViewPager? = null
    private var mFragments: MutableList<Fragment>? = null
    private val titles = arrayOf("评论")
    private var mMomentListItemBean: MomentListItemBean? = null
    private var mVideoPlayView: VideoPlayView? = null
    private var mThumbnail: ImageView? = null
    private var mPlayIcon: ImageView? = null
    private var mPlay: RelativeLayout? = null
    private var mProgressBar: ProgressBar? = null
    private var mBack: ImageView? = null
    private var mTitle: TextView? = null
    private var shareImg: ImageView? = null
    private var addressTv: TextView? = null
    private var sentTimeTv: TextView? = null
    private var commentCountTv: TextView? = null
    private var mContent: TextView? = null
    private var mPraise: TextView? = null
    private var mFlower: TextView? = null
    private var mCircleImageView: CircleImageBorderView? = null
    private var mName: TextView? = null
    private var mAge: TextView? = null
    private var mWatch: ImageView? = null
    private var mRv: RecyclerView? = null
    private var mPictureSingle: ImageView? = null
    private var photoList: MomentListItemBean.PhotoList? = null
    /*如果getIntent能获取到值证明是数据源中的位置,关闭这个页面时 setResult 再传回去。*/
    private var dataPosition = -1
    private var roleFlag: ImageView? = null
    private var contentId = ""
    private var thumbnail = ""
    private var videoUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moment_details)

        mMomentListItemBean = intent.getParcelableExtra<MomentListItemBean>("data")
        contentId = intent.getStringExtra("contentId") ?: ""
        dataPosition = intent.getIntExtra("dataPosition", -1)
        var userId = ""
        var useType = "3"
        var content_id = ""
        if (!TextUtils.isEmpty(contentId)) {
            content_id = contentId
        } else if (mMomentListItemBean != null) {
            content_id = mMomentListItemBean?.content_id ?: 0.toString()
        } else {
            Toast.makeText(this, "数据读取失败", Toast.LENGTH_SHORT).show()
            finish()
        }

        val loginBean: LoginBean? = SPUtils.getSelfInfo(this)
        loginBean?.let {
            userId = loginBean.data.m_id
            useType = SPUtils.ROLE_TYPE_DEF
        }


        val map = HashMap<String, Any>()
        map.put("use_id", userId)
        map.put("use_type", useType)
        map.put("content_id", content_id)
        post(Url.momentDetailas2, map, object : ReqCallback.Callback<String> {
            override fun success(result: String) {
                Loger.log(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val code = jsonObject.getString("code")
                    if ("200" == code) {
                        mMomentListItemBean = Gson().fromJson(jsonObject.getString("contents"), MomentListItemBean::class.java)
                    } else {
                        if (!TextUtils.isEmpty(contentId)) {
                            ToastUtil.show(this@MomentDetailsActivity, jsonObject.getString("tips"))
                            finish()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                initViews()
            }

            override fun failed(msg: String) {
                Loger.log(TAG, msg)
                if (!TextUtils.isEmpty(contentId)) {
                    ToastUtil.show(this@MomentDetailsActivity, resources.getString(R.string.error_network_block))
                    finish()
                }
                initViews()
            }
        })
    }

    private fun initViews() {
        bottomCommentEditRv = findViewById(R.id.activity_moment_details_comment_bottom_edit) as RelativeLayout?
        mBack = findViewById(R.id.bar_action_back) as ImageView
        mTitle = findViewById(R.id.bar_action_title) as TextView
        shareImg = findViewById(R.id.activity_moment_details_share) as ImageView
        addressTv = findViewById(R.id.activity_moment_details_address) as TextView
        sentTimeTv = findViewById(R.id.activity_moment_details_sendtime) as TextView?
        commentCountTv = findViewById(R.id.activity_moment_details_commentcount) as TextView?
        mContent = findViewById(R.id.activity_moment_details_conetnt) as TextView
        mVideoPlayView = findViewById(R.id.activity_moment_details_video) as VideoPlayView
        mThumbnail = findViewById(R.id.activity_moment_details_video_thumbnail) as ImageView
        mPlayIcon = findViewById(R.id.activity_moment_details_video_play_icon) as ImageView
        mPlay = findViewById(R.id.activity_moment_details_video_play) as RelativeLayout
        mProgressBar = findViewById(R.id.activity_moment_details_video_progress) as ProgressBar
        mPraise = findViewById(R.id.activity_moment_details_praise) as TextView
        mFlower = findViewById(R.id.activity_moment_details_flower) as TextView
        mCircleImageView = findViewById(R.id.activity_moment_details_portrait) as CircleImageBorderView
        mName = findViewById(R.id.activity_moment_details_name) as TextView
        mAge = findViewById(R.id.activity_moment_details_age) as TextView
        mWatch = findViewById(R.id.activity_moment_details_watch) as ImageView
        mPictureSingle = findViewById(R.id.activity_moment_details_single) as ImageView
        roleFlag = findViewById(R.id.activity_moment_details_role_flag) as ImageView
        mRv = findViewById(R.id.activity_moment_details_rv) as RecyclerView
        val manager = GridLayoutManager(this@MomentDetailsActivity, 3)
        manager.orientation = GridLayoutManager.VERTICAL
        mRv?.layoutManager = manager
        mRv?.addItemDecoration(GridlayoutDecoration())
        initData()
        initEvents()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "CommitTransaction")
    private fun initData() {

        if (!TextUtils.isEmpty(mMomentListItemBean?.content_type)) {
            when (mMomentListItemBean?.content_type) {
                "1" -> {
                    // 照片类型
                    initPictureData()
                }
                "2" -> {
                    // 视频类型
                    initVideoData()
                }
                "3" -> {
                    // 纯文本
                }
            }
        }

        mTabLayout = findViewById(R.id.activity_moment_details_tablayout) as TabLayout
        mBaseViewPager = findViewById(R.id.activity_moment_details_viewpager) as ViewPager

        /*加载花列表fragment*/
        supportFragmentManager.beginTransaction().replace(R.id.activity_moment_details_sentflower_container, FlowerFragment(mMomentListItemBean?.content_id ?: "")).commit()
        mFragments = ArrayList<Fragment>()
        mMomentListItemBean?.let {
            commentFragment = CommentFragment(mMomentListItemBean!!)
            commentFragment?.let {
                mFragments?.add(commentFragment!!)
                mTabLayout?.setupWithViewPager(mBaseViewPager)
                mBaseViewPager?.adapter = VpAdapter(supportFragmentManager)
            }
        }
        ImageLoad.bind(mCircleImageView, mMomentListItemBean?.head_photo)
        mName?.text = mMomentListItemBean?.names
        mTitle?.text = mMomentListItemBean?.names
        mAge?.text = mMomentListItemBean?.use_age
        mContent?.text = TextTools.Url.replaceUrlToText(mMomentListItemBean?.content)
        mContent?.movementMethod = LinkMovementMethod.getInstance()
        mPraise?.text = mMomentListItemBean?.zan_num
        commentCountTv?.text = mMomentListItemBean?.pin_num
        mFlower?.text = mMomentListItemBean?.gift_num

        if (!TextUtils.isEmpty(mMomentListItemBean?.send_addres)) {
            addressTv?.visibility = View.VISIBLE
            addressTv?.text = mMomentListItemBean?.send_addres
        }

        var sendtime = ""
        if (!TextUtils.isEmpty(mMomentListItemBean?.send_time)) {
            val timeDiff = DateUtils.getTimeDiff(mMomentListItemBean?.send_time)
            if (timeDiff > 0) {
                val minute = (timeDiff / 1000 / 60).toInt()
                try {
                    if (minute < 60) {
                        sendtime = minute.toString() + "分钟前"
                    } else if (minute < 24 * 60) {
                        sendtime = (minute / 60).toString() + "小时前"
                    } else if (minute < 7 * 24 * 60) {
                        sendtime = (minute / (24 * 60)).toString() + "天前"
                    } else {
                        val calendar = Calendar.getInstance()
                        calendar.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mMomentListItemBean?.send_time)
                        if (DateUtils.isToyear(mMomentListItemBean?.send_time)) {
                            sendtime = (calendar.get(Calendar.MONTH) + 1).toString() + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                        } else {
                            sendtime = calendar.get(Calendar.YEAR).toString() + "-" + calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                        }
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }
        sentTimeTv?.text = sendtime


        if ("1" == mMomentListItemBean?.zan_Y) {
            mPraise?.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_on, 0, 0, 0)
        } else {
            mPraise?.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_off, 0, 0, 0)
        }

        if ("2" == mMomentListItemBean?.content_sex) {
            mWatch?.visibility = View.GONE
            roleFlag?.visibility = View.VISIBLE
        } else {
            roleFlag?.visibility = View.GONE
            mWatch?.visibility = View.VISIBLE
            mAge?.setBackgroundResource(R.mipmap.lm_bg_age_man)
            if ("1" == mMomentListItemBean?.guanz_Y) {
                mWatch?.setImageResource(R.mipmap.lm_bg_watch_on)
            } else {
                mWatch?.setImageResource(R.mipmap.lm_bg_watch_off)
            }
        }
    }

    private fun initPictureData(): Boolean {
        if (TextUtils.isEmpty(mMomentListItemBean?.photos)) return false
        photoList = Gson().fromJson(mMomentListItemBean?.photos, MomentListItemBean.PhotoList::class.java)
        if (photoList?.list == null) return false
        if (photoList?.list?.size ?: 0 < 1) return false
        if (photoList?.list?.size == 1) {
            mPictureSingle?.visibility = View.VISIBLE
            mRv?.visibility = View.GONE
            ImageLoad.bind(mPictureSingle, photoList?.list?.get(0)?.url, 600, 600)
        } else {
            mPictureSingle?.visibility = View.GONE
            mRv?.visibility = View.VISIBLE
            mRv?.adapter = PictureAdapter(mRv!!, photoList!!.list)
        }
        return true
    }

    private fun initVideoData(): Boolean {
        if (TextUtils.isEmpty(mMomentListItemBean?.video_path)) return false
        try {
            val jsonObject = JSONObject(mMomentListItemBean?.video_path)
            if (!jsonObject.has("list")) return false
            val jsonArray = jsonObject.getJSONArray("list")
            if (jsonArray.length() < 1) return false
            val o = jsonArray.getJSONObject(0)
            thumbnail = o.getString("thumbnail")
            videoUrl = o.getString("video")
        } catch (e: JSONException) {
            e.printStackTrace()
            Loger.log(TAG, e.message)
            return false
        }
        mPlay?.visibility = View.VISIBLE
        if (!TextUtils.isEmpty(thumbnail)) {
            ImageLoad.bind(mThumbnail, thumbnail)
        } else {
            if (!TextUtils.isEmpty(videoUrl)) {
                Observable.create(ObservableOnSubscribe<Bitmap> { e -> e.onNext(PictureUtil.getNetworkVideoThumbnail(videoUrl)) }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Bitmap> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onNext(value: Bitmap) {
                                mThumbnail?.setImageBitmap(value)
                            }

                            override fun onError(e: Throwable) {}
                            override fun onComplete() {}
                        })
            }
        }
        return true
    }

    private fun videoReset() {
        if (mVideoPlayView != null) {
            mVideoPlayView?.destroy()
            mThumbnail?.visibility = View.VISIBLE
            mPlayIcon?.visibility = View.VISIBLE
            mProgressBar?.visibility = View.INVISIBLE
        }
    }

    private fun setDataCallback() {
        if (dataPosition > -1) {
            if (null != mMomentListItemBean) {
                val intent = Intent()
                intent.putExtra("data", mMomentListItemBean)
                intent.putExtra("dataPosition", dataPosition)
                setResult(RESULT_OK, intent)
            }
        }
    }

    override fun onBackPressed() {
        setDataCallback()
        super.onBackPressed()
    }

    private fun initEvents() {

        bottomCommentEditRv?.setOnClickListener {
            val loginBean: LoginBean? = SPUtils.getSelfInfo(this@MomentDetailsActivity)
            if (null == loginBean) {
                ToastUtil.show(this@MomentDetailsActivity, "您还未登录，请先登录")
                val intent = Intent()
                intent.action = "bbc.com.moteduan_lib2.login.LoginActivity"
                startActivity(intent)
                return@setOnClickListener
            }
            if (commentFragment == null) return@setOnClickListener
            if (!commentFragmentIsInit) return@setOnClickListener
            val commentReplyBean = CommentReplyBean()
            commentReplyBean.useId = loginBean?.data.m_id
            commentReplyBean.contentSex = ContRes.getSelfType()
            commentReplyBean.commentType = "1"
            commentReplyBean.contentId = mMomentListItemBean?.content_id
            commentReplyBean.toUseId = mMomentListItemBean?.use_id
            commentReplyBean.toUseSex = mMomentListItemBean?.content_sex
            commentFragment?.showEditDialog(commentReplyBean)
            CommentFragment.COMMENT_TYPE = CommentFragment.COMMENT_TYPE_1
        }

        // 播放视频
        mPlay?.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(videoUrl)) {
                ToastUtil.show(this@MomentDetailsActivity, "视频地址未找到,播放失败")
                return@OnClickListener
            }
            if (mVideoPlayView?.isPlaying!!) {
                videoReset()
            } else {
                videoReset()
                mProgressBar?.visibility = View.VISIBLE
                mPlayIcon?.visibility = View.INVISIBLE
                mVideoPlayView?.setPath(videoUrl)
                mVideoPlayView?.prepareAsync()
            }
        })

        mVideoPlayView?.setOnCompletionListener({ videoReset() })
        mVideoPlayView?.setOnPreparedListener({ mp ->
            mProgressBar?.visibility = View.INVISIBLE
            mThumbnail?.visibility = View.INVISIBLE
            mp.start()
        })
        mBack?.setOnClickListener({
            videoReset()
            setDataCallback()
            finish()
        })

        mCircleImageView?.setOnClickListener({
            if (!TextUtils.isEmpty(mMomentListItemBean?.content_sex) && mMomentListItemBean?.content_sex == "1") {
                startActivity(Intent("bbc.com.moteduan_lib2.UserInfoActivity").putExtra("userId", mMomentListItemBean?.use_id))
            }
        })

        mPictureSingle?.setOnClickListener({
            val intent = Intent(this@MomentDetailsActivity, WatchPictureActivity::class.java)
            val list = ArrayList<String>()
            list.add(photoList?.list?.get(0)?.url.toString())
            intent.putStringArrayListExtra("data", list)
            startActivity(intent)
        })

        mPraise?.setOnClickListener(View.OnClickListener {
            val loginBean = SPUtils.getSelfInfo(this@MomentDetailsActivity)
            if (null == loginBean) {
                ToastUtil.show(this@MomentDetailsActivity, "您还未登录，请先登录")
                val intent = Intent()
                intent.action = "bbc.com.moteduan_lib2.login.LoginActivity"
                startActivity(intent)
                return@OnClickListener
            }
            val map = HashMap<Any, Any>()
            map.put("content_id", mMomentListItemBean?.content_id!!)
            map.put("use_id", loginBean.data.m_id)
            map.put("use_type", ContRes.getSelfType())
            map.put("b_use_type", mMomentListItemBean?.content_sex!!)
            map.put("b_use_id", mMomentListItemBean?.use_id!!)
            post(Url.clickPraise, map, object : ReqCallback.Callback<String> {
                @SuppressLint("SetTextI18n")
                override fun success(backPamam: String) {
                    try {
                        val jsonObject = JSONObject(backPamam)
                        val code = jsonObject.getString("code")
                        if (TextUtils.equals("200", code)) {
                            mMomentListItemBean?.zan_Y = "1"
                            var i = Integer.parseInt(mMomentListItemBean!!.zan_num)
                            ++i
                            mMomentListItemBean!!.zan_num = i.toString()
                        } else if (TextUtils.equals("202", code)) {
                            mMomentListItemBean!!.zan_Y = "0"
                            var i = Integer.parseInt(mMomentListItemBean!!.zan_num)
                            if (i > 0) {
                                --i
                                mMomentListItemBean?.zan_num = i.toString()
                            }
                        }
                        mPraise?.text = mMomentListItemBean?.zan_num
                        if ("1" == mMomentListItemBean?.zan_Y) {
                            mPraise?.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_on, 0, 0, 0)
                        } else {
                            mPraise?.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_off, 0, 0, 0)
                        }
                        ToastUtil.show(this@MomentDetailsActivity, jsonObject.getString("tips"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                override fun failed(msg: String) {
                    Loger.log(TAG, msg)
                }
            })
        })


        mFlower?.setOnClickListener({
            //                LoginBean loginBean = SPUtils.getSelfInfo(MomentDetailsPictureActivity.this);
            //                if (null == loginBean) {
            //                    ToastUtil.show(MomentDetailsPictureActivity.this, "您还未登录，请先登录");
            //                    Intent intent = new Intent();
            //                    intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
            //                   startActivity(intent);
            //                    return;
            //                }
            //                Map map = new HashMap();
            //                map.put("content_id", mMomentListItemBean.getContent_id());
            //                map.put("use_id", loginBean.getData().getM_id());
            //                map.put("use_type", ContRes.getSelfType());
            //                map.put("b_use_type",mMomentListItemBean.getContent_sex());
            //                map.put("b_use_id",mMomentListItemBean.getUse_id());
            //                ReqUrl.post(Url.sendFlower, map, new ReqCallback.Callback<String>() {
            //                    @Override
            //                    public void success(String backPamam) {
            //                        try {
            //                            JSONObject jsonObject = new JSONObject(backPamam);
            //                            String code = jsonObject.getString("code");
            //                            if (!TextUtils.equals("200", code)) {
            //                                ToastUtil.show(MomentDetailsPictureActivity.this, jsonObject.getString("tips"));
            //                                return;
            //                            }
            //                            int i = Integer.parseInt(mMomentListItemBean.getGift_num());
            //                            ++i;
            //                            mMomentListItemBean.setGift_num(String.valueOf(i));
            //                            mFlower.setText("送花(" + mMomentListItemBean.getGift_num() + ")");
            //                        } catch (JSONException e) {
            //                            e.printStackTrace();
            //                        }
            //                    }
            //
            //                    @Override
            //                    public void failed(String msg) {
            //                        ToastUtil.show(MomentDetailsPictureActivity.this, msg);
            //                    }
            //                });
        })
        mWatch?.setOnClickListener(View.OnClickListener {
            val loginBean = SPUtils.getSelfInfo(this@MomentDetailsActivity)
            if (null == loginBean) {
                ToastUtil.show(this@MomentDetailsActivity, "您还未登录，请先登录")
                val intent = Intent()
                intent.action = "bbc.com.moteduan_lib2.login.LoginActivity"
                startActivity(intent)
                return@OnClickListener
            }
            val map = HashMap<Any, Any>()
            map.put("use_id", loginBean.data.m_id)
            map.put("use_type", ContRes.getSelfType())
            map.put("b_use_id", mMomentListItemBean?.use_id!!)
            map.put("b_use_type", mMomentListItemBean?.content_sex!!)
            post(Url.watchMoments, map, object : ReqCallback.Callback<String> {
                override fun success(backPamam: String) {
                    try {
                        val jsonObject = JSONObject(backPamam)
                        val code = jsonObject.getString("code")
                        if (TextUtils.equals("200", code)) {
                            mMomentListItemBean?.guanz_Y = "1"
                            mWatch?.setImageResource(R.mipmap.lm_bg_watch_on)
                        } else if (TextUtils.equals("202", code)) {
                            mMomentListItemBean!!.guanz_Y = "0"
                            mWatch?.setImageResource(R.mipmap.lm_bg_watch_off)
                        }
                        ToastUtil.show(this@MomentDetailsActivity, jsonObject.getString("tips"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                override fun failed(msg: String) {

                }
            })
        })


        shareImg?.setOnClickListener {
            showShare()
        }
    }

    /**
     * 分享好友
     */
    private fun showShare() {
        if(mMomentListItemBean == null){
            Loger.log(TAG,"mMomentListItemBean == empty")
            return
        }

        val oks = OnekeyShare()
        //关闭sso授权
//                oks.disableSSOWhenAuthorize();
        //        oks.addHiddenPlatform(WechatFavorite.NAME);
        //        oks.addHiddenPlatform(QQ.NAME);
        //        oks.addHiddenPlatform(QZone.NAME);
        //        oks.addHiddenPlatform(SinaWeibo.NAME);
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(mMomentListItemBean?.names)
        val url = Url.Moments.momentsDetailsShare+mMomentListItemBean?.content_id
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url)
        // text是分享文本，所有平台都需要这个字段
//        oks.setImagePath("file://android_asset/appicon.png")
        oks.setImageUrl(Url.LOGO_URL_REMOTE)
        oks.text = mMomentListItemBean?.content
        oks.setExecuteUrl(url)
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url)
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("")
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name))
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url)
        // 启动分享GUI
        oks.show(this@MomentDetailsActivity)
    }

    private inner class PictureAdapter(private val recyclerView: RecyclerView, private val picList: List<MomentListItemBean.PhotoListBean>) : BaseRvAdapter() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.lm_item_picture, parent, false)
            return PictureHoder(view, recyclerView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val h = holder as PictureHoder
            ImageLoad.bind(h.imageView, picList[position].url)
            h.imageView.setOnClickListener {
                val intent = Intent(this@MomentDetailsActivity, WatchPictureActivity::class.java)
                val list = picList.mapTo(ArrayList<String>()) { it.url }
                intent.putStringArrayListExtra("data", list)
                intent.putExtra("position", position)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return picList.size
        }
    }

    private inner class VpAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return mFragments?.get(position)!!
        }

        override fun getCount(): Int {
            return mFragments?.size!!
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }
    }

    @SuppressLint("ValidFragment")
    class FlowerFragment : BaseFragment {
        val TAG = "FlowerFragment"
        private var mRv: BaseRecycleView? = null
        private var mListItemBeen: MutableList<FlowerListBean.GiftBean.ListBean>? = null
        private var page = 1
        private var mTimeStrmp: Long = 0
        private var root: ViewGroup? = null

        constructor() : super()

        constructor(content_id: String) {
            val b = Bundle()
            b.putString("content_id", content_id)
            arguments = b
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater!!.inflate(R.layout.lm_fragment_send_flower_horizontal, container, false)

            mListItemBeen = ArrayList<FlowerListBean.GiftBean.ListBean>()
            root = view.findViewById(R.id.fragment_send_flower_root) as ViewGroup?
            mRv = view.findViewById(R.id.fragment_send_flower_rv) as BaseRecycleView
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            mRv?.layoutManager = manager
            mRv?.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect?.right = (9 * resources.displayMetrics.density + 0.5f).toInt()
                }
            })
            mRv?.adapter = SendFlowerAdapter()
            mRv?.addOnScrollListener(onScrollListener)
            initDatas(page)
            return view
        }

        private val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val manager = mRv?.layoutManager as LinearLayoutManager
                    if (manager.findLastCompletelyVisibleItemPosition() == mListItemBeen?.size!! - 1) {
                        ++page
                        initDatas(page)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        }

        private fun initDatas(pageNum: Int) {
            val map = HashMap<String, Any>()
            val content_id = if (arguments == null) "" else arguments.getString("content_id")
            map.put("content_id", content_id)
            map.put("pageSize", 10)
            map.put("pageNumber", pageNum)
            if (pageNum < 2) {
                map.put("timeStamp", if (mTimeStrmp < 1) System.currentTimeMillis() else mTimeStrmp)
            } else {
                map.put("timeStamp", mTimeStrmp)
            }
            post(Url.sendFlowerList, map, object : ReqCallback.Callback1<String> {
                override fun completed() {
                    mListItemBeen?.let {
                        if (mListItemBeen?.size ?: 0 > 0) {
                            root?.visibility = View.VISIBLE
                        }
                    }
                }

                override fun success(result: String) {
                    Loger.log(TAG, result)
                    val bean: FlowerListBean? = Gson().fromJson(result, FlowerListBean::class.java)
                    bean?.let {
                        if ("200" != bean.code) {
                            if ("220" == bean.code) {
                                // 让用户重新登录
                            } else if ("201" == bean.code) {
                                // 没数据
                                if (page > 1) {
                                    --page
                                }
                                return
                            }
                            ToastUtil.show(context, bean.tips)
                            return
                        }
                        if (pageNum < 2) {
                            mListItemBeen?.clear()
                            mTimeStrmp = if (bean.timeStamp > 0) bean.timeStamp else System.currentTimeMillis()
                        }
                        mListItemBeen?.addAll(bean.gift.list)
                        mRv?.adapter?.notifyDataSetChanged()
                    }


                }

                override fun failed(msg: String) {
                    ToastUtil.show(context, "网络忙")
                    Loger.log(TAG, msg)
                }
            })
        }

        private inner class SendFlowerAdapter : BaseRvAdapter() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.lm_item_send_flower2, parent, false)
                return SendFlowerHolder(view)
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val h = holder as SendFlowerHolder
                val bean = mListItemBeen?.get(position)
                ImageLoad.bind(h.circleImageView, bean?.head_photo)
                if (bean?.num ?: 0 > 99) {
                    h.num.text = "${99}"
                } else {
                    h.num.text = "${bean?.num}"
                }
            }

            override fun getItemCount(): Int {
                return mListItemBeen?.size?:0
            }
        }

        private inner class SendFlowerHolder(itemView: View) : BaseRvHolder(itemView) {
            var circleImageView: CircleImageBorderView = itemView.findViewById(R.id.item_send_flower_portrait) as CircleImageBorderView
            var num: TextView = itemView.findViewById(R.id.item_send_flower_flower) as TextView

        }
    }


    class CommentFragment : BaseFragment {
        private val TAG = "CommentFragment"
        private var mRv: RecyclerView? = null
        private var mListBeen: MutableList<CommentListBean.ListBean>? = null
        private var mMomentListItemBean: MomentListItemBean? = null
        private var mButtomEdit: RelativeLayout? = null
        private var page = 1
        private var mTimeStrmp: Long = 0
        private var dialogDataReq : DialogUtils.DataReqDialog? = null
        /**
         * COMMENT_TYPE_1 1级评论 COMMENT_TYPE_2 2级评论
         */
        companion object {
            var COMMENT_TYPE = -1
            val COMMENT_TYPE_1 = 1
            val COMMENT_TYPE_2 = 2
        }


        /**
         * 当发起2级评论时把2级数据列表赋值给mTempChildCommentList以便数据提交成功后
         * 通过这个引用来更新数据
         */
        private var mTempChildCommentList: MutableList<CommentListBean.CommentBean>? = null

        constructor() : super()

        constructor(bean: MomentListItemBean) {
            val b = Bundle()
            b.putParcelable("bean", bean)
            arguments = b
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater?.inflate(R.layout.lm_fragment_comment, container, false)
            dialogDataReq = DialogUtils.DataReqDialog(context)
            mListBeen = ArrayList<CommentListBean.ListBean>()
            mMomentListItemBean = if (arguments == null) MomentListItemBean() else arguments.getParcelable<Parcelable>("bean") as MomentListItemBean?
            mRv = view?.findViewById(R.id.lm_fragment_comment_rv) as RecyclerView
            mButtomEdit = view?.findViewById(R.id.lm_fragment_comment_bottom_edit) as RelativeLayout
            mButtomEdit?.setOnClickListener(buttomEditListenet)
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.VERTICAL
            mRv?.layoutManager = manager
            mRv?.adapter = RvAdapter()
            mRv?.addOnScrollListener(onScrollListener)
            loadData(page)
            initEvents()
            return view
        }

        override fun onResume() {
            super.onResume()
            commentFragmentIsInit = true
        }

        override fun onDestroy() {
            super.onDestroy()
            commentFragmentIsInit = false
        }


        private val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val manager = mRv?.layoutManager as LinearLayoutManager
                    if (manager.findLastCompletelyVisibleItemPosition() == mListBeen?.size!! - 1) {
                        loadData(++page)
                    }
                }
            }
        }

        private val buttomEditListenet = View.OnClickListener {
            val loginBean: LoginBean? = SPUtils.getSelfInfo(activity)
            if (null == loginBean) {
                ToastUtil.show(activity, "您还未登录，请先登录")
                val intent = Intent()
                intent.action = "bbc.com.moteduan_lib2.login.LoginActivity"
                activity.startActivity(intent)
                return@OnClickListener
            }
            val commentReplyBean = CommentReplyBean()
            commentReplyBean.useId = loginBean?.data.m_id
            commentReplyBean.contentSex = ContRes.getSelfType()
            commentReplyBean.commentType = "1"
            commentReplyBean.contentId = mMomentListItemBean?.content_id
            commentReplyBean.toUseId = mMomentListItemBean?.use_id
            commentReplyBean.toUseSex = mMomentListItemBean?.content_sex
            showEditDialog(commentReplyBean)
            COMMENT_TYPE = COMMENT_TYPE_1
        }

        fun showEditDialog(commentReplyBean: CommentReplyBean) {
            val view = LayoutInflater.from(context).inflate(R.layout.lm_comment_edit, null)
            val mPopupWindow = PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            mPopupWindow.setBackgroundDrawable(ColorDrawable(0))
            mPopupWindow.isFocusable = true
            val mCommentContent = view.findViewById(R.id.lm_fragment_comment_comment) as EditText
            val mCommentCommit = view.findViewById(R.id.lm_fragment_comment_commit) as ImageView
            mCommentCommit.setOnClickListener(View.OnClickListener {
                val commentContent = mCommentContent.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(commentContent)) {
                    ToastUtil.show(context, "评论内容不能为空")
                    return@OnClickListener
                }
                commentReplyBean.commentContent = commentContent
                sendComment(commentReplyBean)
                mPopupWindow.dismiss()
            })
            view.setOnClickListener { mPopupWindow.dismiss() }
            val toName = commentReplyBean.toName
            if (!TextUtils.isEmpty(toName)) {
                mCommentContent.hint = "回复$toName:"
            }
            mPopupWindow.showAtLocation(mCommentContent, Gravity.BOTTOM, 0, 0)
            SoftInputUtils.switchSoftInput(context)
        }

        fun sendComment(commentReplyBean: CommentReplyBean) {
            dialogDataReq?.show()
            val map = HashMap<String, Any>()
            map.put("comment_type", commentReplyBean.commentType)
            map.put("content_id", commentReplyBean.contentId)
            map.put("use_id", commentReplyBean.useId)
            map.put("content_sex", commentReplyBean.contentSex)
            map.put("comment", commentReplyBean.commentContent)
            map.put("pin_use_id", commentReplyBean.toUseId)
            map.put("pin_use_sex", commentReplyBean.toUseSex)
            map.put("parid", commentReplyBean.parid)
            ReqUrl.post(Url.sendComment, map, object : ReqCallback.Callback1<String> {
                override fun completed() {
                    dialogDataReq?.dismiss()
                }

                override fun success(result: String) {
                    try {
                        Loger.log(javaClass.simpleName, result)
                        val jsonObject = JSONObject(result)
                        val code = jsonObject.getString("code")
                        if ("200" != code) {
                            ToastUtil.show(context, jsonObject.getString("tips"))
                            if ("220" == code) {
                                // 让用户重新登录
                            }
                            return
                        }
                        when (COMMENT_TYPE) {
                            COMMENT_TYPE_1 -> {
                                val listBean = Gson().fromJson(jsonObject.getString("maps"), CommentListBean.ListBean::class.java)
                                mListBeen?.add(0, listBean)
                                mRv?.adapter?.notifyDataSetChanged()
                                mRv?.smoothScrollToPosition(0)
                            }
                            COMMENT_TYPE_2 -> {
                                val listBean2 = Gson().fromJson(jsonObject.getString("maps"), CommentListBean.ListBean::class.java)
                                mTempChildCommentList?.clear()
                                mTempChildCommentList?.addAll(listBean2.comment)
                                mRv?.adapter?.notifyDataSetChanged()
                            }
                            else -> {
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                override fun failed(msg: String) {
                    Loger.log(TAG,msg)
                    ToastUtil.show(context, context.resources.getString(R.string.error_network_block))
                }
            })

        }


        /**
         * @param pageNumber
         */
        private fun loadData(pageNumber: Int) {
            val map = HashMap<String, Any>()
            map.put("content_id", mMomentListItemBean!!.content_id)
            map.put("pageSize", 10)
            map.put("pageNumber", pageNumber)
            if (pageNumber < 2) {
                map.put("timeStamp", if (mTimeStrmp < 1) System.currentTimeMillis() else mTimeStrmp)
            } else {
                map.put("timeStamp", mTimeStrmp)
            }
            ReqUrl.post(Url.commentList, map, object : ReqCallback.Callback<String> {
                override fun success(result: String) {
                    Loger.log(TAG, result)
                    val bean = Gson().fromJson(result, CommentListBean::class.java)
                    if ("200" != bean.code) {
                        if ("220" == bean.code) {
                            // 让用户重新登录
                        } else if ("201" == bean.code) {
                            // 没数据
                            if (page > 1) {
                                --page
                            }
                            return
                        }
                        ToastUtil.show(context, bean.tips)
                        return
                    }

                    if (pageNumber < 2) {
                        mListBeen!!.clear()
                        mTimeStrmp = if (bean.timeStamp > 0) bean.timeStamp else System.currentTimeMillis()
                    }
                    mListBeen?.addAll(bean.list)
                    mRv?.adapter?.notifyDataSetChanged()
                }

                override fun failed(msg: String) {
                    ToastUtil.show(context, msg)
                }
            })
        }

        private fun initEvents() {


        }


        private inner class RvAdapter : BaseRvAdapter() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.lm_item_comment, parent, false)
                return RvHolder(view)
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val bean = mListBeen?.get(position)
                val h = holder as RvHolder
                ImageLoad.bind(h.portrait, bean?.conmmentOne?.hui_head_photo)
                h.name.text = bean?.conmmentOne?.hui_name
                h.time.text = bean?.conmmentOne?.comment_date?.trim { it <= ' ' }
                if (Build.VERSION.SDK_INT < 23) {
                    h.content.text = TextTools.ToDBC(bean?.conmmentOne?.content)
                } else {
                    h.content.text = bean?.conmmentOne?.content
                }
                val l = bean?.comment
                if (l != null && l.size > 0) {
                    h.rv.visibility = View.VISIBLE
                    h.rv.adapter = RvAdapterChild(bean.comment)
                } else {
                    h.rv.visibility = View.GONE
                }

                val huiSex = bean?.conmmentOne?.hui_sex
                if (huiSex == 1) {
                    h.roleFlag.visibility = View.GONE
                } else {
                    h.roleFlag.visibility = View.VISIBLE
                }
                h.portrait.setOnClickListener {
                    if (huiSex == 1) {
                        startActivity(Intent("bbc.com.moteduan_lib2.UserInfoActivity").putExtra("userId", bean.conmmentOne.hui_id))
                    }
                }
                h.mCommentBt.setOnClickListener {
                    val loginBean = SPUtils.getSelfInfo(activity)
                    if (null == loginBean) {
                        ToastUtil.show(activity, "您还未登录，请先登录")
                        val intent = Intent()
                        intent.action = "bbc.com.moteduan_lib2.login.LoginActivity"
                        activity.startActivity(intent)
                        return@setOnClickListener
                    }
                    if (loginBean.data.m_id == bean?.conmmentOne?.hui_id) {
                        return@setOnClickListener
                    }
                    val commentReplyBean = CommentReplyBean()
                    commentReplyBean.useId = loginBean.data.m_id
                    commentReplyBean.contentSex = ContRes.getSelfType()
                    commentReplyBean.commentType = "2"
                    commentReplyBean.contentId = mMomentListItemBean!!.content_id
                    commentReplyBean.parid = bean?.conmmentOne?.parid
                    commentReplyBean.toUseId = bean?.conmmentOne?.hui_id
                    commentReplyBean.toUseSex = bean?.conmmentOne?.hui_sex.toString() + ""
                    commentReplyBean.toName = bean?.conmmentOne?.hui_name
                    showEditDialog(commentReplyBean)
                    COMMENT_TYPE = COMMENT_TYPE_2
                    mTempChildCommentList = bean?.comment
                }
            }

            override fun getItemCount(): Int {
                return mListBeen!!.size
            }
        }

        private inner class RvHolder(itemView: View) : BaseRvHolder(itemView) {
            var portrait: CircleImageBorderView
            var name: TextView
            var time: TextView
            var content: TextView
            var rv: RecyclerView
            var mCommentBt: TextView
            var roleFlag: ImageView

            init {
                portrait = itemView.findViewById(R.id.lm_item_comment_portrait) as CircleImageBorderView
                name = itemView.findViewById(R.id.lm_item_comment_name) as TextView
                time = itemView.findViewById(R.id.lm_item_comment_time) as TextView
                content = itemView.findViewById(R.id.lm_item_comment_content) as TextView
                mCommentBt = itemView.findViewById(R.id.lm_item_comment_bt) as TextView
                roleFlag = itemView.findViewById(R.id.lm_item_comment_role_flag) as ImageView
                rv = itemView.findViewById(R.id.lm_item_comment_rv) as RecyclerView
                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                rv.layoutManager = manager
                rv.isNestedScrollingEnabled = false
            }
        }

        private inner class RvAdapterChild(private val listBeen: MutableList<CommentListBean.CommentBean>) : BaseRvAdapter() {
            val purpleSpan = ForegroundColorSpan(ContextCompat.getColor(this@CommentFragment.context, R.color.sr_purple))
            val purpleSpan1 = ForegroundColorSpan(ContextCompat.getColor(this@CommentFragment.context, R.color.sr_purple))
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.lm_item_comment_child, null)
                return RvHolderChild(view)
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val bean = listBeen[position]
                val h = holder as RvHolderChild
                val fromName = bean.hui_name
                val toName = bean.b_hui_name
                val content = SpannableStringBuilder()
                if (TextUtils.isEmpty(toName)) {
                    content.append(fromName)
                            .append(":")
                            .append(bean.content)
                } else {
                    content.append(fromName)
                            .append("回复")
                            .append(toName)
                            .append(":")
                            .append(bean.content)
                }
                val fromNameIndex = content.indexOf(fromName, 0, true)
                if (fromNameIndex != -1) content.setSpan(purpleSpan, fromNameIndex, fromNameIndex + fromName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val toNameIndex = content.indexOf(toName, fromNameIndex + fromName.length, true)
                if (toNameIndex != -1) content.setSpan(purpleSpan1, toNameIndex, toNameIndex + toName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (Build.VERSION.SDK_INT < 23) {
                    h.textView?.text = content
                } else {
                    h.textView?.text = content
                }
                h.textView?.setOnClickListener {
                    val loginBean = SPUtils.getSelfInfo(activity)
                    if (null == loginBean) {
                        ToastUtil.show(activity, "您还未登录，请先登录")
                        val intent = Intent()
                        intent.action = "bbc.com.moteduan_lib2.login.LoginActivity"
                        activity.startActivity(intent)
                        return@setOnClickListener
                    }
                    if (loginBean.data.m_id == bean.hui_id) {
                        return@setOnClickListener
                    }
                    val commentReplyBean = CommentReplyBean()
                    commentReplyBean.useId = loginBean.data.m_id
                    commentReplyBean.contentSex = ContRes.getSelfType()
                    commentReplyBean.commentType = "2"
                    commentReplyBean.contentId = mMomentListItemBean!!.content_id
                    commentReplyBean.parid = bean.parid
                    commentReplyBean.toUseId = bean.hui_id
                    commentReplyBean.toUseSex = bean.hui_sex.toString() + ""
                    commentReplyBean.toName = bean.hui_name
                    showEditDialog(commentReplyBean)
                    COMMENT_TYPE = COMMENT_TYPE_2
                    mTempChildCommentList = listBeen
                }
            }

            override fun getItemCount(): Int {
                return listBeen.size
            }
        }

        private inner class RvHolderChild(itemView: View) : BaseRvHolder(itemView) {
            var textView: TextView? = itemView.findViewById(R.id.lm_item_comment_child_text) as TextView

        }

    }
}
