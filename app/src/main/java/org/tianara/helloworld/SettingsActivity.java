package org.tianara.helloworld;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.tianara.helloworld.settings.Setting;
import org.tianara.helloworld.settings.Settings;
import org.tianara.helloworld.settings.SettingsItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    public Boolean colorPaletteSwap = false;
    ArrayList<Integer> oldSettingsIndexes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Settings.applyStyle(this, false);

        setContentView(R.layout.activity_settings);
        // Save old settings
        if (savedInstanceState != null)
            oldSettingsIndexes = savedInstanceState.getIntegerArrayList("oldSettingsIndexes");

        if (oldSettingsIndexes == null) {
            oldSettingsIndexes = new ArrayList<>();
            for (Setting item : Settings.getList()) {
                oldSettingsIndexes.add(item.getIndex());
            }
        }
        // Update content
        updateContent();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("oldSettingsIndexes", oldSettingsIndexes);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save settings
        Settings.saveAll();
    }

    public void updateContent() {
        List<Map<String, String>> listData = new ArrayList<>();
        Map<String, String> listItem;
        for (Setting item : Settings.getList()) {
            listItem = new HashMap<>(2);

            listItem.put("name", item.getName());
            listItem.put("value", item.getString());

            listData.add(listItem);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listData,
                R.layout.list_item,
                new String[] {"name", "value"},
                new int[] {R.id.text1,
                        R.id.text2});
        ((ListView)findViewById(R.id.settings_list)).setAdapter(adapter);
        ((ListView)findViewById(R.id.settings_list)).setOnItemClickListener(new SettingsItemClickListener(this));
    }

    private void saveResultIntent() {
        Intent intent = new Intent();
        // Determine if any change is necessary
        boolean settingsSwapped = false;
        // If the color palette was swapped, return
        List<Setting> settingsList = Settings.getList();
        for (int i = 0; i < settingsList.size(); i++) {
            if (settingsList.get(i).getIndex() != oldSettingsIndexes.get(i)) {
                settingsSwapped = true;
                break;
            }
        }
        // If the settings are swapped...
        if (settingsSwapped) {
            intent.putExtra("settingsChanged", true);
        }
        // Send the result
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveResultIntent();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        saveResultIntent();
        this.finish();
    }
}
