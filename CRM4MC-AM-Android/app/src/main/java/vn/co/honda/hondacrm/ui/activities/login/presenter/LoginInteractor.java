package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.content.Context;

import vn.co.honda.hondacrm.ui.activities.login.model.User;


public interface LoginInteractor {
    public void login(User user, OnLoginFinishedListener listener, Context context,int delay);
}