package com.honda.connmc.View.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Interfaces.listener.transition.IF_BluetoothTransferDataListener;
import com.honda.connmc.Model.ItemRawData;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.R;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.View.adapter.RawDataAdapter;
import com.honda.connmc.View.base.BaseFragment;

public class RawDataFragment extends BaseFragment implements IF_BluetoothTransferDataListener, RawDataAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RawDataAdapter mAdapter;

    @Override
    protected int getIdLayout() {
        return R.layout.raw_data_frag;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new RawDataAdapter(getActivity());
        mAdapter.setOnItemClick(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onBackPressedFragment() {
        if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void setActionForView() {
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BluetoothManager.getInstance().setMessageListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        BluetoothManager.getInstance().unRegisterMessageListener(this);
        super.onDestroyView();
    }

    @Override
    public void onReceiveDataFromBTU(VehicleMessage message) {

    }

    @Override
    public void onReceivedRawData(String rawData) {
        LogUtils.d("Received raw data: " + rawData);
        getActivity().runOnUiThread(() -> {
            mAdapter.addData(new ItemRawData(rawData, false));
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        });
    }

    @Override
    public void onSendMessageFail() {

    }

    @Override
    public void onItemClick(String rawData) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy", rawData);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copy to Clipboard!", Toast.LENGTH_SHORT).show();
    }
}
