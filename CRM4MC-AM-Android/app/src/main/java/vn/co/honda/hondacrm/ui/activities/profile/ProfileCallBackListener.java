package vn.co.honda.hondacrm.ui.activities.profile;

import vn.co.honda.hondacrm.net.model.user.UserProfile;

public interface ProfileCallBackListener {
    public void sendUserProfile(UserProfile userProfile);
}
