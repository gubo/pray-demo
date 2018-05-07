
package com.gubo.pray.api

import android.text.Html
import javax.inject.*

import io.reactivex.*
import io.reactivex.functions.*
import io.reactivex.schedulers.*

import retrofit2.*
import retrofit2.http.*

import com.gubo.pray.*
import gubo.slipwire.dbg
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by jeffschulz on 5/7/18.
 */
class FetchCommunitys
{
    private data class Response( val status:Int? )

    @Inject lateinit var retrofit:Retrofit

    init {
        PrayApplication.appComponent.inject(this )
    }

    private interface get
    {
        @GET( "/communitys" )
        fun get(
                @Query( "userid" ) userid:String
        ) :Observable<Response>
    }

    /*
     * https://randomuser.me/api?page=0&results=100
     */
    private data class Name( val first:String?,val last:String? )
    private data class Location( val street:String?,val city:String?,val state:String? )
    private data class RandomUser( val name:Name?,val location:Location? )
    private data class RandomUserResponse( val results:List<RandomUser>? )
    private interface getRandomUsers
    {
        @GET( "api" )
        fun get(
                @Query( "page" ) page:Int,
                @Query( "results" ) results:Int
        ) :Observable<RandomUserResponse>
    }

    fun fetch( start:Int,count:Int ) : Observable<Community> {
        var id = start;

        val mapToRandomUser = Function<RandomUserResponse,Observable<RandomUser>> {
            response -> Observable.fromIterable(response.results ?: listOf<RandomUser>() )
        }

        val mapToCommunity = Function<RandomUser,Observable<Community>> {
            ru -> Observable.just(
                Community(
                        id = "${id}",
                        name = "${ru.name?.first} ${ru.name?.last}",
                        address = "${ru.location?.street} \n ${ru.location?.city} ${ru.location?.state}",
                        imageURL = "https://picsum.photos/512/384?image=${100+(id++)}"
                )
            )
        }

        /*
         * https://randomuser.me/documentation
         * https://picsum.photos/
         */

        return retrofit.newBuilder().baseUrl("https://randomuser.me" )
                .build()
                .create( getRandomUsers::class.java )
                .get(0,count )
                .subscribeOn( Schedulers.io() )
                .observeOn( Schedulers.computation() )
                .concatMap( mapToRandomUser )
                .filter { randomUser ->  randomUser != null }
                .concatMap( mapToCommunity )
    }
}