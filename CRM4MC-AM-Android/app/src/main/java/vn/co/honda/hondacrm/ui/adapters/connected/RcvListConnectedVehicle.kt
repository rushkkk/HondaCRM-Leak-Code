package vn.co.honda.hondacrm.ui.adapters.connected

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_list_connected_vehicle.view.*
import vn.co.honda.hondacrm.R
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity
import vn.co.honda.hondacrm.utils.StateConnectPre


/**
 * Created by TienTM13 on 20/06/2019.
 */

class RcvListConnectedVehicle(private val items: List<VehicleEntity>?, private val context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_connected_vehicle, viewGroup, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).tvName.text = items?.get(position)?.vehicleName
        holder.ivAva.setImageResource(R.drawable.ic_mortor_symbol)
        holder.tvLicense.text = items?.get(position)?.licensePlate
        holder.tvVinCode.text = items?.get(position)?.vinId
        holder.tvVinCode.text = "***" + items?.get(position)?.vinId?.substring(10, 17)
        holder.itemView.setOnClickListener {
            if (onItemClickedListener != null) {
                onItemClickedListener!!.onItemClick(items?.get(position)!!)
            }
        }

        if (StateConnectPre.getInstance(holder.itemView.context).getState(items?.get(position)?.vinId) == 1) {
            holder.ivBluetooth.setImageResource(R.drawable.ic_connected)
        } else {
            holder.ivBluetooth.setImageResource(R.drawable.ic_connect)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAva: ImageView = view.iv_item_list_connected_thumb
        val tvName: TextView = view.tv_item_list_connected_name
        val tvLicense: TextView = view.tv_item_list_connected_license
        val tvVinCode: TextView = view.tv_item_list_connected_vin_code
        val ivBluetooth: ImageView = view.iv_item_list_connected_bluetooth_status
    }

    // interface clicked for row
    interface OnItemClickedListener {
        fun onItemClick(vehicleEntity: VehicleEntity)
    }

    private var onItemClickedListener: OnItemClickedListener? = null
    fun setOnItemClickedListener(onItemClickedListener: OnItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener
    }
}