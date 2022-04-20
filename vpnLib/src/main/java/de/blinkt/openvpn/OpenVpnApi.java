package de.blinkt.openvpn;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;

import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VPNLaunchHelper;

public class OpenVpnApi {

    private static final String TAG = "OpenVpnApi";

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public static void startVpn(Context context, String inlineConfig, String sCountry, String userName, String pw) throws RemoteException {
        if (TextUtils.isEmpty(inlineConfig)) throw new RemoteException("config is empty");
        startVpnInternal(context, inlineConfig, sCountry, userName, pw);
    }

    static void startVpnInternal(Context context, String inlineConfig, String sCountry, String userName, String pw) throws RemoteException {
        ConfigParser cp = new ConfigParser();
        try {
            cp.parseConfig(new StringReader(inlineConfig));
            VpnProfile vp = cp.convertProfile();
            Log.d(TAG, "startVpnInternal: ==============" + cp + "\n" + vp);
            vp.mName = sCountry;
            if (vp.checkProfile(context) != de.blinkt.openvpn.R.string.no_error_found) {
                throw new RemoteException(context.getString(vp.checkProfile(context)));
            }
            vp.mProfileCreator = context.getPackageName();
            vp.mUsername = userName;
            vp.mPassword = pw;
            vp.mAllowedAppsVpnAreDisallowed = true;
            if(PreferenceManager.getDefaultSharedPreferences(context).getStringSet("not_allowed_apps", new HashSet())!=null)
                vp.mAllowedAppsVpn = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet("not_allowed_apps", new HashSet());


            ProfileManager.setTemporaryProfile(context, vp);
            VPNLaunchHelper.startOpenVpn(vp, context);
        } catch (IOException | ConfigParser.ConfigParseError e) {
            throw new RemoteException(e.getMessage());
        }
    }
}
