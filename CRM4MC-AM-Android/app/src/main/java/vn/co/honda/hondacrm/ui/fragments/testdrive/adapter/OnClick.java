package vn.co.honda.hondacrm.ui.fragments.testdrive.adapter;

import com.google.android.gms.maps.GoogleMap;

import vn.co.honda.hondacrm.net.model.dealer.Dealers;

public interface OnClick {
    void onMapReady(GoogleMap googleMap);

    void onClickGroup(int position, Dealers dealer);

    void onClickBooking(Dealers dealers);
}