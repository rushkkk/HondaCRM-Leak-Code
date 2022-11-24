package vn.co.honda.hondacrm.ui.customview;

/**
 * Home Listener.
 *
 * @author CuongNV31
 */
public interface HomeListener {
    void showHomeFragment();

    void showProductsFragment();

    void showConnectedFragment();

    void showVehiclesFragment();

    void showDealers();

    void setTextHeader(@HomeStatus int status);
}
