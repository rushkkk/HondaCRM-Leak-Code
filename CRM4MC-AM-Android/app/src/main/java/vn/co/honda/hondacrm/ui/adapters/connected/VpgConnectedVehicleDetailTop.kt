package vn.co.honda.hondacrm.ui.adapters.connected

import android.content.Context
import android.content.res.Resources
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import vn.co.honda.hondacrm.R
import vn.co.honda.hondacrm.ui.fragments.connected.models.Vehicle
import vn.co.honda.hondacrm.utils.Constants


/**
 * Created by TienTM13 on 06/06/2019.
 */

class VpgConnectedVehicleDetailTop(private val someList: ArrayList<Vehicle>, private val res: Resources, private val context: Context) : PagerAdapter() {
    override fun isViewFromObject(collection: View, p1: Any): Boolean {
        return collection === p1 as View
    }

    override fun getCount(): Int {
        return someList.size
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = collection.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_connected_vehicle_detail_top, collection, false)
        val itemName = view.findViewById(R.id.tv_item_connected_vehicle_detail_top_name) as TextView
        val btnBookService = view.findViewById(R.id.btn_item_connected_vehicle_detail_top_service) as TextView
        val btnConnectNow = view.findViewById(R.id.btn_item_connected_vehicle_detail_top_connect_now) as TextView
        val itemLicense = view.findViewById(R.id.tv_item_connected_vehicle_detail_top_license) as TextView
        val itemVinCode = view.findViewById(R.id.tv_item_connected_vehicle_detail_top_vin_code) as TextView
        val itemStatusTitle =
                view.findViewById(R.id.tv_item_connected_vehicle_detail_top_status_title) as TextView
        val itemStatusImg = view.findViewById(R.id.iv_item_connected_vehicle_detail_top_status_img) as ImageView
        val tvChangeOil = view.findViewById(R.id.tv_item_connected_vehicle_detail_top_change_oil) as TextView
//        val ivChangOilDetail = view.findViewById(R.id.iv_item_connected_vehicle_detail_top_change_oil_detail) as ImageView
        val ivChangOilImg = view.findViewById(R.id.iv_item_connected_vehicle_detail_top_change_oil_img) as ImageView
        val clOil = view.findViewById(R.id.cl_connect_oil) as ConstraintLayout

        clOil.setOnClickListener {
            if (onItemClickedListener != null) {
                onItemClickedListener!!.onItemClick(position, Constants.ITEM_CHANGE_OIL)
            }
        }

        btnBookService.setOnClickListener {
            if (onItemClickedListener != null) {
                onItemClickedListener!!.onItemClick(position, Constants.ITEM_BOOK_SERVICE)
            }
        }
        btnConnectNow.setOnClickListener {
            if (onItemClickedListener != null) {
                onItemClickedListener!!.onItemClick(position, Constants.ITEM_CONNECT_NOW)
            }
        }
        try {
            itemName.text = someList[position].name
            itemLicense.text = someList[position].license_plate
            itemVinCode.text = someList[position].vin

            // if connected or not
            if (someList[position].isConnected) {
                itemStatusTitle.text = res.getString(R.string.connect_is_connected)
                itemStatusImg.setImageResource(R.drawable.ic_connected)
                itemName.setTextColor(ContextCompat.getColor(context, R.color.colorRedDark2))
                btnConnectNow.visibility = View.GONE
            } else {
                itemStatusTitle.text = res.getString(vn.co.honda.hondacrm.R.string.connect_is_disconnected)
                itemStatusImg.setImageResource(R.drawable.ic_bluetooth_gray)
                itemName.setTextColor(ContextCompat.getColor(context, R.color.black))
                btnConnectNow.visibility = View.VISIBLE
            }

            // if need change oil
            if (someList[position].isNeedChangeOil) {
                tvChangeOil.setTextColor(ContextCompat.getColor(context, R.color.colorRedDark2))
                ivChangOilImg.setImageResource(R.drawable.ic_change_oil_red)
            } else {
                tvChangeOil.setTextColor(ContextCompat.getColor(context, R.color.black))
                ivChangOilImg.setImageResource(R.drawable.ic_change_oil_black)
            }

        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        (collection as ViewPager).addView(view, 0)
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, p1: Any) {
        (collection as ViewPager).removeView(p1 as View)
    }

    // interface clicked for row
    interface OnItemClickedListener {
        fun onItemClick(pos: Int, type: String)
    }

    private var onItemClickedListener: OnItemClickedListener? = null
    fun setOnItemClickedListener(onItemClickedListener: OnItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener
    }
}