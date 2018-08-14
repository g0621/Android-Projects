package com.example.gyan.echo.Fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.example.gyan.echo.R


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {


    var myActivity: Activity? = null
    var switch: Switch? = null

    object Statified{
        var MY_PREF_NAME = "ShakeFeature"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_settings, container, false)
        activity!!.title = "Settings"
        switch = view?.findViewById(R.id.switchShake)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val pref = myActivity?.getSharedPreferences(Statified.MY_PREF_NAME,Context.MODE_PRIVATE)
        val isAllowed = pref?.getBoolean("feature",false)
        switch?.isChecked = isAllowed as Boolean
        switch?.setOnCheckedChangeListener({compoundButton, b ->
            if (b){
                val editor = myActivity?.getSharedPreferences(Statified.MY_PREF_NAME,Context.MODE_PRIVATE)?.edit()
                editor?.putBoolean("feature",true)?.apply()
            }else{
                val editor = myActivity?.getSharedPreferences(Statified.MY_PREF_NAME,Context.MODE_PRIVATE)?.edit()
                editor?.putBoolean("feature",false)?.apply()
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        var item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
    }

}// Required empty public constructor
