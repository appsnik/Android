package co.appsnik.chuck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

class MyBroadcastReceiver extends BroadcastReceiver {
    final static private String TAG = MyBroadcastReceiver.class.getSimpleName();
    final static IntentFilter intentfilter = new IntentFilter();

    static {
        intentfilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(intentfilter.getAction(0))) {
            boolean connected = isNetworkConnected(context);
            ConnectivityChangeObservable.instance().notify(connected);
            Log.i(TAG, "isNetworkConnected=" + connected);
        } else {
            Log.d(TAG, "Unrecognized intent - " + intent.getAction());
        }
    }

    private boolean isNetworkConnected(final Context context) {
        ConnectivityManager conmgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = conmgr.getActiveNetworkInfo();
        return (netinfo != null && netinfo.isConnected());
    }
}
