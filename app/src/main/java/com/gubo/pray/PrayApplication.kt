
package com.gubo.pray

import android.os.*
import android.app.*

import gubo.slipwire.*


/**
 * Created by jeffschulz on 5/4/18.
 */
class PrayApplication : Application()
{
    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        val versionCode = packageManager.getPackageInfo( packageName,0 ).versionCode
        val versionName = packageManager.getPackageInfo( packageName,0 ).versionName

        if ( com.gubo.pray.BuildConfig.DEBUG ) {
            dbg("PRAY V$versionName($versionCode) DEBUG" )
            StrictMode.setThreadPolicy( StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectAll()
                    .penaltyLog()
                    .build() )
            StrictMode.setVmPolicy( StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build() )
        } else {
            dbg("PRAY V$versionName($versionCode) RELEASE" )
        }

        if ( com.squareup.leakcanary.LeakCanary.isInAnalyzerProcess(this ) ) {
            return
        }
        com.squareup.leakcanary.LeakCanary.install( this )

        appComponent = DaggerAppComponent.builder()
                .appModule( AppModule( this ) )
                .build()
    }
}