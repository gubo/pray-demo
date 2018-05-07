
package com.gubo.pray.view

import android.graphics.*

/**
 * Created by jeffschulz on 5/4/18.
 * https://stackoverflow.com/questions/30704581/make-imageview-with-round-corner-using-picasso
 */
class RoundedCornersTransform : com.squareup.picasso.Transformation
{
    override fun key():String  = "RoundedCornersTransform"

    override fun transform( source:Bitmap ) : Bitmap {
        val size = Math.min( source.getWidth(),source.getHeight() )

        val x = ( source.getWidth() -  size ) / 2
        val y = ( source.getHeight() - size ) / 2

        val squaredBitmap = Bitmap.createBitmap( source, x,y,size,size )
        if ( squaredBitmap != source ) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap( size,size, source.getConfig() )

        val canvas = Canvas( bitmap )
        val paint = Paint()
        val shader = BitmapShader( squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP )
        paint.setShader( shader )
        paint.setAntiAlias( true )

        val r = size / 8f

        val left = 0F
        val top = 0F
        val right : Float = 0F + source.getWidth()
        val bottom : Float = 0F + source.getHeight()

        canvas.drawRoundRect( RectF( left,top,right,bottom  ), r,r, paint )
        squaredBitmap.recycle()

        return bitmap
    }
}