package vn.co.honda.hondacrm.net.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.util.Map;

public class SessionStore {
    private static final String HEADER_NETWORK = "HEADER_NETWORK";
    private static SessionStore sSessionStore;
    private static Editor sEditor;

    /**
     * get singleton instance
     *
     * @return current apiclient
     */
    public static synchronized SessionStore getInstance(Context context) {
        sEditor = context.getSharedPreferences(HEADER_NETWORK, Context.MODE_PRIVATE).edit();
        if (sSessionStore == null) {
            sSessionStore = new SessionStore();
        }
        return sSessionStore;
    }

    /**
     * get singleton instance
     *
     * @return current apiclient
     */
    public static synchronized SessionStore getInstance() {
        if (sSessionStore == null) {
            sSessionStore = new SessionStore();
        }
        return sSessionStore;
    }

    public void addHeader(String key, String value) {
        sEditor.putString(key, value);
        sEditor.apply();
    }

    public void removeHeader(String key) {
        sEditor.remove(key);
        sEditor.apply();
    }

    public void clearHeader() {
        sEditor.clear();
        sEditor.apply();
    }

    public void addAuth(String name, String pass){
        String userCredentials = String.format("%s:%s", name, pass);
        String basicAuth = String.format("%s%s", "Basic ", Base64.encodeToString(userCredentials.getBytes(), Base64.DEFAULT));
        sEditor.putString("Authorization", basicAuth);
        sEditor.apply();
    }

    public Map getHeader(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(HEADER_NETWORK, Context.MODE_PRIVATE);
        return savedSession.getAll();
    }

}
