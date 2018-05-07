
package com.gubo.pray

import android.os.*
import android.content.*
import android.support.v7.app.*

import gubo.slipwire.*

/**
 * Created by jeffschulz on 5/4/18.
 */
class LaunchActivity : AppCompatActivity()
{
    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
        dbg("LaunchActivity.onCreate" )

        val intent = Intent(this,HomeActivity::class.java )
        intent.putExtra( "idiom","launch" )
        startActivity( intent )
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbg("LaunchActivity.onDestroy" )
    }
}