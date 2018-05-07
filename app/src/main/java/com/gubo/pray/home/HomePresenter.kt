
package com.gubo.pray.home

import javax.inject.*
import java.util.concurrent.*

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

import com.gubo.pray.*
import com.gubo.pray.api.FetchCommunitys
import gubo.slipwire.*

val mockimages = arrayOf(
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRM7_A_Iw9bKrP33Y1Z5buzQyAcZw7L5rhwRk3TDQVl_vMInhLO",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr8MEi4y7_6d4eenZ9ZJynQq35SbJQsSYb_FA5hDNZftUCTEdc",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-ZUbPdJKmcn4P3uGIwfgjIqDAI-hCIkH92yo2kXzR3xjBgdSZmg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlw4HvMH6CskL5ljNGuqR_FxerEmDgXs2Zr4rINAvPObleKG7u",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeSfsoVUAQDaBII5Wo4hNtPcG1GNw8I-m8CBXR2X151RLo2DqJ"
)

/**
 * Created by jeffschulz on 5/4/18.
 */
class HomePresenter : Presenter<HomeDisplay>,DataSource<Community>,HomeDisplay.Listener
{
    private val communitys = mutableListOf<Community>()

    private var display: HomeDisplay? = null
    private var firstVisiblePosition = 0
    private var total = 2500 // TODO: need total field returned from server

    @Inject lateinit var eventbus : EventBus

    init {
        PrayApplication.appComponent.inject(this )
    }

    override fun bind( d:HomeDisplay ) {
        display?.release()

        display = d
        display?.listener = this
        display?.pageDelta = 15
        display?.pageSize = 50
        display?.setItemCount( communitys.size )
        display?.setPosition( firstVisiblePosition )

        if ( communitys.isEmpty() ) fetch(0,100 )
    }

    override fun unbind() {
        firstVisiblePosition = display?.firstVisiblePosition ?: 0

        display?.listener = null
        display?.release()
        display = null

        dbg("HomePresenter.unbind" )
    }

    override fun release() {
        display?.listener = null
        display?.release()
        display = null

        communitys.clear()

        dbg("HomePresenter.release" )
    }

    override fun getDataFor( position:Int ):Community {
        try {
            return communitys[ position ]
        } catch ( x:Throwable ) {
            dbg( x )
        }
        return Community( id = "0",name = "" )
    }

    class Range( val start:Int,val count:Int ) {
        fun same( start:Int,count:Int ) : Boolean {
            return (this.start == start) && (this.count == count)
        }
    }

    private var readyForRange = Range(-1,-1 )

    override fun getReadyFor( start:Int,count:Int ) {
        val s = Math.max( start,0 )
        val c = Math.max( count,0 )
        if ( readyForRange.same( s,c ) ) { return }

        readyForRange = Range( s,c )
        val reach = Math.min( ( s + c ),total )
        if ( reach >= communitys.size ) {
            fetch( s,c )
        }
    }

    override fun requestRefresh() {}

    override fun onCommunity( community:Community ) {
        dbg("onCommunity ${community.id}" )
    }

    private fun fetch( start:Int, count:Int ) {
        FetchCommunitys().fetch( start,count )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(
                        { c -> onNext( c ) },
                        { e -> onError() },
                        { onComplete() }
                )
    }

    private fun onNext( c:Community ) {
        val indexof = communitys.indexOf( c )
        when ( indexof ) {
            -1 -> { communitys.add( c ) }
            else -> { communitys[ indexof ] = c }
        }
    }

    private fun onComplete() {
        display?.setItemCount( communitys.size )
    }

    private fun onError() {
        dbg("oops failed to fetch communitys" )
    }
}