package vn.co.honda.hondacrm.ui.fragments.connected.local.roomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import vn.co.honda.hondacrm.ui.fragments.connected.local.dao.InfoVehicleDao;
import vn.co.honda.hondacrm.ui.fragments.connected.local.dao.VehicleDao;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.InfoVehicle;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;

/**
 * Created by TienTM13 on 15/07/2019.
 */

@Database(entities = {VehicleEntity.class, InfoVehicle.class}, version = 1, exportSchema = false)
public abstract class VehicleRoomDb extends RoomDatabase {

    private static VehicleRoomDb INSTANCE;
    private static final String DATABASE_NAME = "connected_vehicle_db";

    public abstract VehicleDao daoAccess();

    public abstract InfoVehicleDao infoVehicleDao();

    public static VehicleRoomDb getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VehicleRoomDb.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}