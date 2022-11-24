package vn.co.honda.hondacrm.navigator.bus;

import vn.co.honda.hondacrm.net.model.user.UserProfile;

/**
 * Implement send message using otto.
 *
 * @author CuongNV31
 */
public class Events {

    public static class FragmentActivityMessage {

        private int size;
        private boolean isAdd = false;

        public FragmentActivityMessage(int size, boolean isAdd) {
            this.size = size;
            this.isAdd = isAdd;
        }

        public FragmentActivityMessage(int size) {
            this.size = size;
        }

        public int getMessage() {
            return size;
        }

        public boolean getAdded() {
            return isAdd;
        }
    }

    public static class ActivityFragmentMessage {

        private String message;

        private int type;
        private int id;
        private int exit;


        public ActivityFragmentMessage(String message) {
            this.message = message;
        }



        public ActivityFragmentMessage(String message, int type) {
            this.message = message;
            this.type = type;

        }

        public ActivityFragmentMessage(String message, int type, int exit) {
            this.message = message;
            this.type = type;
            this.exit = exit;
        }

        public int getExit() {
            return exit;
        }

        public void setExit(int exit) {
            this.exit = exit;
        }

        public int getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ActivityProfleMessage {

        public ActivityProfleMessage(UserProfile profile) {
            this.profile = profile;
        }
        public UserProfile getProfile() {
            return profile;
        }

        public void setProfile(UserProfile profile) {
            this.profile = profile;
        }

        private UserProfile profile;
    }
}
