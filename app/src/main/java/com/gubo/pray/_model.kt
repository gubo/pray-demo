
package com.gubo.pray

/**
 * Created by jeffschulz on 5/4/18.
 */

data class Community(
  val id : String,
  val name: String,
  val global: Boolean = true,

  val type: String? = null,
  val address: String? = null,
  val leader: String? = null,
  val title: String? = null,
  val description: String? = null,
  val imageURL: String? = null,
  val thumbnailURL: String? = null,
  val status: String? = null
)
{
    override fun equals( other:Any? ): Boolean {
        if ( other is Community ) {
            return ( this.id == other.id )
        }
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
