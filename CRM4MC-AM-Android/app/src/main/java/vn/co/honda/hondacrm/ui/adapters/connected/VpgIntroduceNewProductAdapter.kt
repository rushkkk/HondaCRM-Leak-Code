package vn.co.honda.hondacrm.ui.adapters.connected

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import vn.co.honda.hondacrm.R
import vn.co.honda.hondacrm.ui.fragments.connected.models.VehicleIntroduce


/**
 * Created by TienTM13 on 22/06/2019.
 */

class VpgIntroduceNewProductAdapter(private val someList: ArrayList<VehicleIntroduce>) : PagerAdapter() {

    override fun isViewFromObject(collection: View, p1: Any): Boolean {
        return collection === p1 as View
    }

    override fun getCount(): Int {
        return someList.size
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = collection.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_intro_new_product, collection, false)

        val itemImg = view.findViewById(R.id.iv_intro_image) as ImageView
        val itemTitle = view.findViewById(R.id.tv_intro_title) as TextView
        val itemContent = view.findViewById(R.id.tv_intro_content) as TextView

        try {
            itemImg.setImageResource(someList[position].imgVehicle)
            itemTitle.text = someList[position].name
            itemContent.text = someList[position].content
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        (collection as ViewPager).addView(view, 0)
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, p1: Any) {
        (collection as ViewPager).removeView(p1 as View)
    }
}