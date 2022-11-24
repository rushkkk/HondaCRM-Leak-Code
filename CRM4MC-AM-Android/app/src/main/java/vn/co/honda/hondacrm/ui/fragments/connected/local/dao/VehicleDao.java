package vn.co.honda.hondacrm.ui.fragments.connected.local.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;

/**
 * Created by TienTM13 on 15/07/2019.
 */

@Dao
public interface VehicleDao {

    @Insert
    void insertOnlySingleVehicle(VehicleEntity vehicleEntity);

    @Query("SELECT * FROM connected_vehicle")
    List<VehicleEntity> fetchAllVehicle();

    @Update
    void updateVehicle(VehicleEntity Vehicles);

    @Delete
    void deleteVehicle(VehicleEntity Vehicles);

    @Query("DELETE FROM connected_vehicle")
    void clearTable();

    @Query("SELECT * FROM connected_vehicle WHERE vinId = :vin")
    VehicleEntity fetchVehicleByVin(String vin);
}

