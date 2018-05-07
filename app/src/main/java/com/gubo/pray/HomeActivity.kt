
package com.gubo.pray

import android.os.*
import android.view.*
import javax.inject.*
import android.support.v7.app.*

import com.gubo.pray.home.*

import gubo.slipwire.*

/**
 * Created by jeffschulz on 5/4/18.
 */
class HomeActivity : AppCompatActivity(),
        android.support.design.widget.NavigationView.OnNavigationItemSelectedListener,
        android.support.v4.view.ViewPager.OnPageChangeListener
{
    @Inject lateinit var eventBus:EventBus

    private var appBarLayout: android.support.design.widget.AppBarLayout? = null
    private var homeButton: android.support.v7.widget.AppCompatButton? = null
    private var communityButton: android.support.v7.widget.AppCompatButton? = null
    private var chatButton: android.support.v7.widget.AppCompatButton? = null
    private var giveButton: android.support.v7.widget.AppCompatButton? = null
    private var viewPager: android.support.v4.view.ViewPager? = null
    private var appBarLayoutIsExpanded = false

    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
        dbg("HomeActivity.onCreate" )

        PrayApplication.appComponent.inject(this )

        setContentView( R.layout.home )
        configure()
    }

    override fun onCreateOptionsMenu( menu: Menu? ): Boolean {
        menuInflater.inflate( R.menu.home,menu )
        return true
    }

    override fun onOptionsItemSelected( item: MenuItem? ): Boolean {
        val id = item?.itemId ?: -1

        when ( id ) {
            R.id.action_search -> {
                appBarLayout?.setExpanded( !appBarLayoutIsExpanded )
                return true
            }
            R.id.action_settings -> {
                return true
            }
        }

        return super.onOptionsItemSelected( item )
    }

    override fun onNavigationItemSelected( item:MenuItem ) : Boolean {
        val drawer = findViewById<android.support.v4.widget.DrawerLayout>( R.id.homedrawerlayout )
        drawer.closeDrawer( android.support.v4.view.GravityCompat.START )
        return true;
    }

    override fun onPageScrollStateChanged( state:Int ) {}
    override fun onPageScrolled( position:Int, positionOffset:Float, positionOffsetPixels:Int ) {}

    override fun onPageSelected( position:Int ) {
        homeButton?.isSelected = false
        communityButton?.isSelected = false
        chatButton?.isSelected = false
        giveButton?.isSelected = false
        when ( position ) {
            0 -> homeButton?.isSelected = true
            1 -> communityButton?.isSelected = true
            2 -> chatButton?.isSelected = true
            3 -> giveButton?.isSelected = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dbg("HomeActivity.onDestroy" )
    }

    private fun configure() {
        val toolbar = findViewById<android.support.v7.widget.Toolbar>( R.id.hometoolbar )
        toolbar.title = resources.getString( R.string.app_name )
        setSupportActionBar( toolbar )

        val drawer = findViewById<android.support.v4.widget.DrawerLayout>( R.id.homedrawerlayout )
        val toggle = ActionBarDrawerToggle( this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close )
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<android.support.design.widget.NavigationView>( R.id.homenavview )
        navigationView.setNavigationItemSelectedListener( this )

        appBarLayout = findViewById<android.support.design.widget.AppBarLayout>( R.id.homeappbar )
        appBarLayout?.addOnOffsetChangedListener { appBarLayout, verticalOffset -> appBarLayoutIsExpanded = (verticalOffset == 0) }

        homeButton = findViewById<android.support.v7.widget.AppCompatButton>( R.id.homehomebutton )
        communityButton = findViewById<android.support.v7.widget.AppCompatButton>( R.id.homecommunitybutton )
        chatButton = findViewById<android.support.v7.widget.AppCompatButton>( R.id.homechatbutton )
        giveButton = findViewById<android.support.v7.widget.AppCompatButton>( R.id.homegivebutton )

        homeButton?.setOnClickListener( { viewPager?.setCurrentItem( 0 ) } )
        communityButton?.setOnClickListener( { viewPager?.setCurrentItem( 1 ) } )
        chatButton?.setOnClickListener( { viewPager?.setCurrentItem( 2 ) } )
        giveButton?.setOnClickListener( { viewPager?.setCurrentItem( 3 ) } )

        val homeFragmentStatePagerAdapter = HomeFragmentStatePagerAdapter( supportFragmentManager )
        viewPager = findViewById<android.support.v4.view.ViewPager>( R.id.homeviewpager )
        viewPager?.setAdapter( homeFragmentStatePagerAdapter )
        viewPager?.addOnPageChangeListener(this )
        viewPager?.setOffscreenPageLimit( 4 )
        viewPager?.setCurrentItem( 0 )

        homeButton?.isSelected = true
    }

    private class HomeFragmentStatePagerAdapter( fragmentManager:android.support.v4.app.FragmentManager ) :
            android.support.v4.app.FragmentStatePagerAdapter( fragmentManager )
    {
        override fun getCount(): Int {
            return 4
        }

        override fun getItem( position : Int ) : android.support.v4.app.Fragment? {
            var fragment: android.support.v4.app.Fragment? = null
            when ( position ) {
                0 -> fragment = HomeFragment()
                1 -> fragment = CommunityFragment()
                2 -> fragment = ChatFragment()
                3 -> fragment = GiveFragment()
            }
            return fragment
        }
    }

}