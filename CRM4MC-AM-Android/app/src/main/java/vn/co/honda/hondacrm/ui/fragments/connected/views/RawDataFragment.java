package vn.co.honda.hondacrm.ui.fragments.connected.views;

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

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Interfaces.listener.transition.IF_BluetoothTransferDataListener;
import vn.co.honda.hondacrm.btu.Model.ItemRawData;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseFragment;
import vn.co.honda.hondacrm.ui.adapters.connected.RawDataAdapter;


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
