
package com.gubo.pray.home

import android.net.*
import android.view.*
import android.databinding.DataBindingUtil

import com.gubo.pray.*
import gubo.slipwire.*
import com.gubo.pray.databinding.CommunityBinding

/**
 * Created by jeffschulz on 5/4/18.
 */
class HomeAdapter( var view:View,var dataSource:DataSource<Community> ) : HomeDisplay
{
    private var linearLayoutManager : android.support.v7.widget.LinearLayoutManager? = null
    private var recyclerView : android.support.v7.widget.RecyclerView? = null
    private var _listener: HomeDisplay.Listener? = null

    private val communityAdapter = CommunityAdapter()

    private var _pageDelta = 5
    private var _pageSize = 25

    override var pageDelta:Int
        get() = _pageDelta
        set(value) { _pageDelta = value }

    override var pageSize:Int
        get() = _pageSize
        set(value) { _pageSize = value }

    init {
        linearLayoutManager = android.support.v7.widget.LinearLayoutManager( view.context,android.support.v7.widget.LinearLayoutManager.VERTICAL,false )

        communityAdapter.dataSource = dataSource

        recyclerView = view?.findViewById<android.support.v7.widget.RecyclerView>( R.id.homefragmentrecyclerview )
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.adapter = communityAdapter
        recyclerView?.setHasFixedSize( true )

        setupScrolling()
    }

    override fun release() {
        dbg("HomeAdapter.release" )
    }

    override fun setItemCount( count:Int ) {
        val positionStart = communityAdapter?.itemCount ?: 0
        val itemCount = ( count - positionStart )
        communityAdapter?.itemCount = count
        communityAdapter?.notifyItemRangeInserted( positionStart,itemCount )
    }

    override fun setPosition( position:Int ) {
        recyclerView?.scrollToPosition( position )
    }

    override var listener:HomeDisplay.Listener?
        get() = _listener
        set( value ) { _listener = value }

    override var firstVisiblePosition:Int
        get() = linearLayoutManager?.findFirstVisibleItemPosition() ?: 0
        set( value ) {}

    private fun setupScrolling() {
        val listener = object : android.support.v7.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled( recyclerView:android.support.v7.widget.RecyclerView?, dx:Int,dy:Int ) {
                val childCount = linearLayoutManager?.childCount ?: 0
                val itemCount = linearLayoutManager?.itemCount ?: 0
                val firstVisibleItem = linearLayoutManager?.findFirstVisibleItemPosition() ?: 0
                val reach = ( firstVisibleItem + childCount )
                val delta = Math.abs( reach - itemCount )
                if ( delta < _pageDelta ) {
                    dataSource?.getReadyFor( itemCount,_pageSize )
                }
            }
        }
        recyclerView?.addOnScrollListener( listener )
    }

    private inner class CommunityHolder( view:View ) : android.support.v7.widget.RecyclerView.ViewHolder( view ) {
        fun bind( community:Community ) {
            val binding:CommunityBinding = DataBindingUtil.bind( itemView )
            binding.community = community

            val communityImageView = itemView.findViewById<android.widget.ImageView>( R.id.communityimageview )
            communityImageView.setOnClickListener { _listener?.onCommunity( community ) }

            communityImageView.setImageDrawable( null )
            val uri = Uri.parse( community.imageURL )
            com.squareup.picasso.Picasso.get()
                    .load( uri )
                    //.transform( com.gubo.pray.view.RoundedCornersTransform() )
                    .into( communityImageView )
        }
    }

    private inner class CommunityAdapter : android.support.v7.widget.RecyclerView.Adapter<CommunityHolder>()
    {
        var dataSource : DataSource<Community>? = null

        private var itemCount = 0

        override fun getItemCount(): Int {
            return itemCount
        }

        fun setItemCount( itemCount:Int ) {
            this.itemCount = itemCount
        }

        override fun onCreateViewHolder( parent:ViewGroup?,viewType:Int ) : CommunityHolder {
            val view = LayoutInflater.from( parent?.getContext() ).inflate( R.layout.community,parent,false )
            return CommunityHolder( view )
        }

        override fun onBindViewHolder( holder:CommunityHolder?,position:Int ) {
            val community = dataSource?.getDataFor( position ) ?: Community( id="0",name="" )
            holder?.bind( community )
        }
    }
}