
package com.gubo.pray

import dagger.*
import javax.inject.*
import android.content.*
import java.util.concurrent.*

import okhttp3.*
import okhttp3.logging.*

import retrofit2.*
import retrofit2.converter.gson.*
import retrofit2.adapter.rxjava2.*

import com.google.gson.*
import com.gubo.pray.api.FetchCommunitys

import gubo.slipwire.*
import com.gubo.pray.home.*

@Module
class AppModule( val application:PrayApplication )
{
    @Provides
    @Singleton
    fun provideApplicationContext() : Context = application.applicationContext;

    @Provides
    @Singleton
    fun provideEventBus() : EventBus = EventBus()

    @Provides
    @Singleton
    fun provideHomePresenter() : HomePresenter = HomePresenter()

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        class Logging : HttpLoggingInterceptor.Logger {
            override fun log( m:String ) {
                android.util.Log.d( "HTTP",m )
            }
        }

        val loggingInterceptor:HttpLoggingInterceptor = HttpLoggingInterceptor( Logging() )
        loggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY )

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(15000, TimeUnit.MILLISECONDS )
                .addInterceptor( loggingInterceptor )
                .build()

        val gson = GsonBuilder().setPrettyPrinting().create()

        val retrofit:Retrofit = Retrofit.Builder()
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                .addConverterFactory( GsonConverterFactory.create( gson ) )
                .baseUrl( "https://api.prayclient.com" )
                .client( okHttpClient )
                .build()

        return retrofit
    }
}

/**
 * Created by jeffschulz on 5/4/18.
 */
@Singleton
@Component( modules = arrayOf( AppModule::class ) )
interface AppComponent
{
    fun inject( homeActivity:HomeActivity )
    fun inject( homeFragment:HomeFragment )
    fun inject( homePresenter:HomePresenter )
    fun inject( fetchCommunitys:FetchCommunitys )
}