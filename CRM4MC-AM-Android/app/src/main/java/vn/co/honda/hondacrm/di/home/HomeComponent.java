package vn.co.honda.hondacrm.di.home;

import dagger.Subcomponent;
import vn.co.honda.hondacrm.ui.fragments.home.views.HomeFragment;

/**
 * @author CuongNV31
 */
@Subcomponent(modules = {HomeModule.class})
@HomeScope
public interface HomeComponent {

    void inject(HomeFragment fragment);

   // void inject(vn.co.honda.hondacrm.ui.fragments.dealers.views.DealerFragment homeFragment);
}
