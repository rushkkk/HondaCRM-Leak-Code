package vn.co.honda.hondacrm.di.users;

import dagger.Subcomponent;
import vn.co.honda.hondacrm.ui.fragments.users.views.UsersFragment;

/**
 * @author CuongNV31
 */
@Subcomponent(modules = {UsersModule.class})
@UsersScope
public interface UsersComponent {

    void inject(UsersFragment fragment);
}
