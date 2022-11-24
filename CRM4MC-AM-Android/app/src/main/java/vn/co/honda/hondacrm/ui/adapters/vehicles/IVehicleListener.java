package vn.co.honda.hondacrm.ui.adapters.vehicles;

import android.graphics.Bitmap;

import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;

public interface IVehicleListener {
    public void editVehicle(Vehicle vehicle);
    public void deleteVehicle(Vehicle vehicle);
    public void onItemClick(String vin);
}
