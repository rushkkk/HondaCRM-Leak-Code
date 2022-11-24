package vn.co.honda.hondacrm.di.news;

import dagger.Subcomponent;
import vn.co.honda.hondacrm.ui.fragments.news.views.NewsFragment;

/**
 * @author CuongNV31
 */
@Subcomponent(modules = {NewsModule.class})
@NewsScope
public interface NewsComponent {

    void inject(NewsFragment fragment);
}
