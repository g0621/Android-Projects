package com.example.gyan.echo.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.gyan.echo.Activities.MainActivity
import com.example.gyan.echo.Fragments.AboutUsFragment
import com.example.gyan.echo.Fragments.FavoriteFragmnet
import com.example.gyan.echo.Fragments.MainScreenFragment
import com.example.gyan.echo.Fragments.SettingsFragment
import com.example.gyan.echo.R

class NavigationDrawerAdapter(_contentList: ArrayList<String>,_getImages: IntArray,context: Context): RecyclerView.Adapter<NavigationDrawerAdapter.NavViewHolder>(){
    var contentList: ArrayList<String>?= null
    var getImage: IntArray?= null
    var mContext: Context?= null

    init {
        this.contentList = _contentList
        this.getImage = _getImages
        this.mContext = context
    }

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {
        holder?.text_GET?.setText(contentList?.get(position))
        holder?.icon_GET?.setBackgroundResource(getImage?.get(position) as Int)
        holder?.content_GET?.setOnClickListener({
            if (position == 0){
                val mainScreenFrag = MainScreenFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment,mainScreenFrag,"MainFromNav")
                        .addToBackStack("MainFromNav")
                        .commit()
            }else if(position == 1){
                val faviorateFrag = FavoriteFragmnet()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment,faviorateFrag,"FavFromNav")
                        .addToBackStack("FavFroNav")
                        .commit()
            }else if(position == 2){
                val settingFrag = SettingsFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment,settingFrag,"SettFromNav")
                        .addToBackStack("SettFromNav")
                        .commit()
            }else{
                val aboutFrag = AboutUsFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment,aboutFrag,"AbtFromNAv")
                        .addToBackStack("AbtFromNav")
                        .commit()
            }
            MainActivity.Statified.drawerLayout?.closeDrawers()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        var itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_navigation_drawer,parent,false)
        val returnThis = NavViewHolder(itemView)
        return returnThis
    }

    override fun getItemCount(): Int {
        return contentList?.size as Int
    }

    class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        var icon_GET: ImageView?= null
        var text_GET: TextView?= null
        var content_GET: RelativeLayout?= null

        init {
            icon_GET = itemView?.findViewById(R.id.icon_navdrawer)
            text_GET = itemView?.findViewById(R.id.text_navbar)
            content_GET = itemView?.findViewById(R.id.nav_drawer_item_holder)
        }
    }
}