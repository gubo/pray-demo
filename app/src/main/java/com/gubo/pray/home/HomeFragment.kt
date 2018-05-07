
package com.gubo.pray.home

import android.os.*
import android.view.*
import javax.inject.*

import com.gubo.pray.*

/**
 * Created by jeffschulz on 5/4/18.
 */
class HomeFragment : android.support.v4.app.Fragment()
{
    @Inject lateinit var homePresenter : HomePresenter

    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )

        PrayApplication.appComponent.inject(this )
    }

    override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?, savedInstanceState:Bundle? ) : View? {
        val view = inflater?.inflate( R.layout.homefragment,container,false );
        if ( view != null ) {
            homePresenter.bind( HomeAdapter( view,homePresenter ) )
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homePresenter.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        val changingConfigurations = activity?.isChangingConfigurations ?: false
        if ( !changingConfigurations ) {
            homePresenter.release()
        }
    }
}
