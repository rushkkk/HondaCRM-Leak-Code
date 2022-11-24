package vn.co.honda.hondacrm.di.newsDetail;

import dagger.Subcomponent;
import vn.co.honda.hondacrm.ui.fragments.newsDetailed.views.NewsDetailedFragment;

/**
 * @author CuongNV31
 */
@Subcomponent(modules = {NewsDetailedModule.class})
@NewsDetailedScope
public interface NewsDetailedComponent {

    void inject(NewsDetailedFragment fragment);
}
