package vn.co.honda.hondacrm.ui.fragments.connected.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import vn.co.honda.hondacrm.ui.fragments.connected.local.model.InfoVehicle;

@Dao
public interface InfoVehicleDao {
    @Insert
    void insertOnlySingleVehicle(InfoVehicle infoVehicle);

    @Query("SELECT * FROM info_vehicle_table")
    List<InfoVehicle> fetchAllVehicle();

    @Update
    void updateVehicle(InfoVehicle infoVehicle);

    @Delete
    void deleteVehicle(InfoVehicle infoVehicle);

    @Query("DELETE FROM info_vehicle_table")
    void clearTable();
}
