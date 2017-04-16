package org.tianara.helloworld.quote;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import org.tianara.helloworld.MainActivity;
import org.tianara.helloworld.R;
import org.tianara.helloworld.settings.Settings;

public class QuoteTaskPopulate extends AsyncTask<MainActivity, Void, Void> {

    MainActivity home;
    @Override
    protected Void doInBackground(MainActivity... params) {
        for (MainActivity home : params) {
            Quote.prepare(Settings.fromId(Settings.id.QUOTE_BUFFER_SIZE).getInt());
            this.home = home;
        }
        return null;
    }

    protected void onProgressUpdate(Void... progress) {

    }

    protected void onPostExecute(Void result) {
        Quote.clear();
        for (int i = 0; i < Settings.fromId(Settings.id.QUOTE_BUFFER_SIZE).getInt(); i++) {
            Quote q = Quote.getRandom();
            if (q != null) {
                Quote.displayed.add(q);
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(home).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Unable to connect to server.\nPlease check your Internet" +
                        " connectivity and try again");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
        }
        Quote.populateBuffered(home);
        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout)home.findViewById(R.id.swipeRefresh);
        if (swipeLayout.isRefreshing())
            swipeLayout.setRefreshing(false);
    }
}
