package vn.co.honda.hondacrm.ui.fragments.common;

/**
 * @author CuongNV31
 */
public interface IBasePresenter<TypeView> {

    void bindView(TypeView view);

    void unbindView();

    void initStartViewActions();
}
