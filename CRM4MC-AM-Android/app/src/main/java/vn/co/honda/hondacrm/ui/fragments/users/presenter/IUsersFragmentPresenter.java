package vn.co.honda.hondacrm.ui.fragments.users.presenter;

import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.ui.fragments.common.IBasePresenter;
import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;
import vn.co.honda.hondacrm.ui.fragments.users.views.IUsersFragmentView;

/**
 * @author CuongNV31
 */
public interface IUsersFragmentPresenter extends IBasePresenter<IUsersFragmentView> {

    void observeNewsAdapterItemClick(PublishSubject<UserModel> subject);

    void loadNews();
}
