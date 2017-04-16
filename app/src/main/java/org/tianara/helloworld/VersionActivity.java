package org.tianara.helloworld;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.tianara.helloworld.settings.Settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class VersionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.applyStyle(this, false);
        setContentView(R.layout.activity_version);

        SimpleDateFormat formatter = new SimpleDateFormat("DD.MM.yyyy, HH:mm:ss");
        String compileDate = formatter.format(BuildConfig.TIMESTAMP) + " GMT";
        ((TextView)findViewById(R.id.compile_date)).setText(compileDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
