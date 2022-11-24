package vn.co.honda.hondacrm.ui.fragments.newsDetailed.presenter;

import android.os.Bundle;
import android.widget.ImageView;

import vn.co.honda.hondacrm.ui.fragments.common.IBasePresenter;
import vn.co.honda.hondacrm.ui.fragments.newsDetailed.views.INewsDetailedFragmentView;

/**
 * @author CuongNV31
 */
public interface INewsDetailedFragmentPresenter extends IBasePresenter<INewsDetailedFragmentView> {

    void restoreArguments(Bundle arguments);

    void setNewsData();

    void loadNewsImg(ImageView newsImg);
}
