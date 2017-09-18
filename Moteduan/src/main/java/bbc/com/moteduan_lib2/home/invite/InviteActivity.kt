package bbc.com.moteduan_lib2.home.invite

import android.os.Bundle
import bbc.com.moteduan_lib.R
import wei.toolkit.BaseActivity

class InviteActivity : BaseActivity() {
    var url: String? = ""
    var title: String? = ""
    var dataType: Int? = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)
        url = intent.getStringExtra("url")
        title = intent.getStringExtra("title")
        dataType = intent.getIntExtra("dataType", -1)
        val inviteFragment = InviteFragment(dataType ?: -1, title, url)
        supportFragmentManager.beginTransaction().replace(R.id.activity_invite, inviteFragment).commit()
    }
}
