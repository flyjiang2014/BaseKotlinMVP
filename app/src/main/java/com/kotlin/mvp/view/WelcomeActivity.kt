package com.kotlin.mvp.view

import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kotlin.mvp.R
import com.kotlin.mvp.app.Constants
import com.kotlin.mvp.base.SimpleActivity
import com.kotlin.mvp.utils.SharepreferenceUtil
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*
/**
 * 文件说明：首次打开的引导页
 */
class WelcomeActivity : SimpleActivity() {

    private lateinit var mIndicators: Array<ImageView?>
    private var views = ArrayList<View>()
    override fun setBaseContentView(): Int {
        setIsShowTitle(false)
        setIsNotFillSysState(false)
        setStatusColor(R.color.transparent)
        return R.layout.activity_welcome
    }

    override fun init() {
        val layout = LayoutInflater.from(this)
        val view1 = layout.inflate(R.layout.welcome_a, null)
        val view2 = layout.inflate(R.layout.welcome_b, null)
        val view3 = layout.inflate(R.layout.welcome_c, null)
        val view4 = layout.inflate(R.layout.welcome_d, null)
        val btnWelCome = view4.findViewById<Button>(R.id.btn_welcome)
        views = ArrayList()
        views.add(view1)
        views.add(view2)
        views.add(view3)
        views.add(view4)
        mIndicators = arrayOfNulls(views.size)
        // 设置下标
        for (i in views.indices) {
            val imageView = ImageView(this)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            if (i != 0) {
                params.leftMargin = 25
            }
            imageView.layoutParams = params
            mIndicators[i] = imageView
            if (i == 0) {
                mIndicators[i]!!.setBackgroundResource(R.drawable.icon_welcome_checked)
            } else {
                mIndicators[i]!!.setBackgroundResource(R.drawable.icon_welcome_unchecked)
            }
            ll_indicator.addView(imageView)
            viewPage.addOnPageChangeListener(MyOnPageChangeListener())
            btnWelCome.setOnClickListener {
                SharepreferenceUtil.saveBoolean(Constants.APP_FIRST_IN, true)
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish()
            }
        }
        // 填充ViewPager的数据适配器
        val mPagerAdapter: PagerAdapter = object : PagerAdapter() {
            override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
                return arg0 === arg1
            }

            override fun getCount(): Int {
                return views.size
            }

            override fun destroyItem(container: View, position: Int, `object`: Any) {
                (container as ViewPager).removeView(views[position])
            }

            override fun instantiateItem(container: View, position: Int): Any {
                (container as ViewPager).addView(views[position])
                return views[position]
            }
        }
        viewPage!!.adapter = mPagerAdapter
    }

    inner class MyOnPageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(arg0: Int) {}
        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageSelected(arg0: Int) {
            ll_indicator.removeAllViews()
            for (i in views.indices) {
                val imageView = ImageView(mContext)
                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                if (i != 0) {
                    params.leftMargin = 25
                }
                imageView.layoutParams = params
                mIndicators[i] = imageView
                if (i == arg0) {
                    mIndicators[i]!!.setBackgroundResource(R.drawable.icon_welcome_checked)
                } else {
                    mIndicators[i]!!.setBackgroundResource(R.drawable.icon_welcome_unchecked)
                }
                if (arg0 < views.size - 1) {
                    ll_indicator.addView(imageView)
                }
            }
        }
    }
}