package vn.co.honda.hondacrm.ui.fragments.drawer.interfaces;

import vn.co.honda.hondacrm.ui.adapters.drawer.Menu;

public interface IDrawerListener {
    public void onSocialNetWorkClick(int type);

    public void onItemMenuClick(Menu menu);

    public void onHeaderClick();
}
