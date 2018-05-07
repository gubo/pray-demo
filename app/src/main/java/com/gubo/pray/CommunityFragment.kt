
package com.gubo.pray

import android.os.*
import android.view.*

/**
 * Created by jeffschulz on 5/4/18.
 */
class CommunityFragment : android.support.v4.app.Fragment()
{
    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
    }

    override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?, savedInstanceState:Bundle? ) : View? {
        val view = inflater?.inflate( R.layout.communityfragment,container,false );
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

