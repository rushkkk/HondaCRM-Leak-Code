package vn.co.honda.hondacrm.btu.Interfaces.listener.transition;


public class TransferDataListenerObj {
    private IF_BluetoothTransferDataListener listener;
    private String classNameListener;

    public TransferDataListenerObj(IF_BluetoothTransferDataListener listener) {
        this.listener = listener;
        classNameListener = listener.getClass().getCanonicalName();
    }

    public IF_BluetoothTransferDataListener getListener() {
        return listener;
    }

    public String getClassNameListener() {
        return classNameListener;
    }
}
