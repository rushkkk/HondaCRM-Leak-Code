package vn.co.honda.hondacrm.ui.fragments.testdrive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.dealer.District;

public class SpinnerDistrictAdapter extends ArrayAdapter<District> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<District> items;

    public SpinnerDistrictAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        items = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.spinner_item_province, parent, false);

        TextView offTypeTv = view.findViewById(R.id.name);

        District offerData = items.get(position);

        offTypeTv.setText(offerData.getNameVi());

        return view;
    }
}