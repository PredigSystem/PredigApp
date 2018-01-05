package predigsystem.udl.org.predigsystem.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by Pau on 5/1/18.
 */

public abstract class NetworkManager {

    public static boolean checkConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        boolean isWiFi = activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String connectionType = prefs.getString("prefNetwork", "");

        return (isConnected && !connectionType.equals("1")) || (isConnected && isWiFi);
    }
}
