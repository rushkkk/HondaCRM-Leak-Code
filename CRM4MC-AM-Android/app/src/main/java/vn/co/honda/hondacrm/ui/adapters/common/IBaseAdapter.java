package vn.co.honda.hondacrm.ui.adapters.common;

import java.util.ArrayList;

import rx.subjects.PublishSubject;

/**
 * @author CuongNV31
 */
public interface IBaseAdapter<TypeData, ClickData> {

    void setData(ArrayList<TypeData> data);

    ArrayList<TypeData> getData();

    TypeData getItem(final int position);

    PublishSubject<ClickData> observeItemClick();
}


