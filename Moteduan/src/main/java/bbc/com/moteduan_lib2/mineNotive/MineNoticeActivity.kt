package bbc.com.moteduan_lib2.mineNotive

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import bbc.com.moteduan_lib.R

class MineNoticeActivity : AppCompatActivity() {
    var back : ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_notice)
        back = findViewById(R.id.back) as ImageView?
        back?.setOnClickListener { finish() }
        supportFragmentManager.beginTransaction().replace(R.id.activity_mine_notice_frame,MineNoticeListFragment()).commit()
    }
}
