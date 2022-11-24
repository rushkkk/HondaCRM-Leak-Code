package vn.co.honda.hondacrm.ui.fragments.booking_service.presenter;

import vn.co.honda.hondacrm.ui.fragments.booking_service.views.ISelectTimeSlotFragmentView;
import vn.co.honda.hondacrm.ui.fragments.common.BasePresenter;

public class SelectTimeSlotFragmentPresenter extends BasePresenter<ISelectTimeSlotFragmentView> {

    private ISelectTimeSlotFragmentView mSelectTimeSlotFragmentView;

    public SelectTimeSlotFragmentPresenter(ISelectTimeSlotFragmentView mSelectTimeSlotFragmentView) {
        this.mSelectTimeSlotFragmentView = mSelectTimeSlotFragmentView;
    }

    @Override
    public void bindView(ISelectTimeSlotFragmentView iSelectTimeSlotFragmentView) {

    }

    @Override
    public void initStartViewActions() {

    }
}
