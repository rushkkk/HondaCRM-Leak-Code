package vn.co.honda.hondacrm.ui.activities.booking_service;

import java.util.Date;
import java.util.List;

import vn.co.honda.hondacrm.net.model.booking_service.SelectedTimeInDay;

public interface IControlCalendarCallBack {
    void onItemClick(Date content);
}
