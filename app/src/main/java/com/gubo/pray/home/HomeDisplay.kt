
package com.gubo.pray.home

import com.gubo.pray.*
import gubo.slipwire.*

/**
 * Created by jeffschulz on 5/4/18.
 */
interface HomeDisplay : Display,DataSink<Community>
{
    interface Listener
    {
        fun onCommunity( community:Community )
    }

    var listener: HomeDisplay.Listener?
    var firstVisiblePosition: Int
}