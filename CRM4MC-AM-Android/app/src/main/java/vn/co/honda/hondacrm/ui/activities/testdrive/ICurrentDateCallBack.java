package vn.co.honda.hondacrm.ui.activities.testdrive;

import java.util.List;

import vn.co.honda.hondacrm.net.model.booking_service.SelectedTimeInDay;

public interface ICurrentDateCallBack {
    void getDate(String date, List<SelectedTimeInDay> slotOfDays);
}
