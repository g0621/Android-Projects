package com.example.gyan.echo.Fragments


import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.gyan.echo.R
import com.github.florent37.viewanimator.ViewAnimator
import de.hdodenhof.circleimageview.CircleImageView


/**
 * A simple [Fragment] subclass.
 */
class AboutUsFragment : Fragment() {

    var dev_Image: CircleImageView? = null
    var dev_name: TextView? = null
    var dev_mail: TextView? = null
    var dev_phone: TextView? = null
    var main_bg: LinearLayout? = null
    var animationDrawable: AnimationDrawable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_about_us, container, false)
        activity!!.title = "About Us"
        dev_Image = view?.findViewById(R.id.dev_pic)
        dev_name = view?.findViewById(R.id.dev_name)
        dev_mail = view?.findViewById(R.id.dev_mail)
        dev_phone = view?.findViewById(R.id.dev_phone)
        main_bg = view?.findViewById(R.id.main_bg)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        animationDrawable = main_bg?.background as AnimationDrawable
        animationDrawable?.setEnterFadeDuration(4500)
        animationDrawable?.setExitFadeDuration(4500)
        animationDrawable?.start()
        ViewAnimator.animate(dev_Image)
                .translationY(-1000f, 0f)
                .alpha(0f,1f)
                .andAnimate(dev_name)
                .dp().translationX(-50f, 0f)
                .andAnimate(dev_mail)
                .dp().translationX(50f,0f)
                .andAnimate(dev_phone)
                .dp().translationX(-50f,0f)
                .decelerate()
                .duration(2000)
                .thenAnimate(dev_Image)
                .scale(1f, 0.5f, 1f)
                .accelerate()
                .duration(1000)
                .start()
    }
    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        var item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
    }

}// Required empty public constructor
