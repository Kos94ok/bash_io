package org.tianara.helloworld.sidedrawer;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.tianara.helloworld.MainActivity;
import org.tianara.helloworld.SettingsActivity;
import org.tianara.helloworld.VersionActivity;
import org.tianara.helloworld.quote.Quote;
import org.tianara.helloworld.settings.Settings;
import org.tianara.helloworld.web.WebParser;

public class DrawerItemClickListener implements ListView.OnItemClickListener {
    // Data fields
    private MainActivity home;
    private int separatorClicks;
    // Constructor
    public DrawerItemClickListener(MainActivity home) {
        this.home = home;
        separatorClicks = 0;
    }
    // Populate the list
    private void populateList(WebParser.category category) {
        Quote.populateWith(home, category);
    }
    // Onclick
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String objectId = ((DrawerItem)parent.getItemAtPosition(position)).getId();
        switch (objectId)
        {
            case "new":
                populateList(WebParser.category.NEW);
                break;
            case "top":
                populateList(WebParser.category.TOP);
                break;
            case "random":
                populateList(WebParser.category.RANDOM);
                break;
            case "abyss":
                populateList(WebParser.category.ABYSS);
                break;
            case "settings":
                home.swapActivity(SettingsActivity.class);
                break;
            case "version":
                home.swapActivity(VersionActivity.class);
                break;
            case "clearTemp":
                Settings.clearAll();
                Settings.loadAll();
                break;
            case "quit":
                home.finish();
                break;
            case "separator":
                separatorClicks += 1;
                if (separatorClicks == 10) {
                    Toast.makeText(home, "Please stop clicking that.", Toast.LENGTH_LONG).show();
                }
                else if (separatorClicks == 50) {
                    Toast.makeText(home, "There is no functionality here.", Toast.LENGTH_LONG).show();
                }
                else if (separatorClicks == 125) {
                    Toast.makeText(home, "I just can't make it unclickable :(", Toast.LENGTH_LONG).show();
                }
                else if (separatorClicks == 225) {
                    Toast.makeText(home, "Actually I can, but I don't want to.", Toast.LENGTH_LONG).show();
                }
                else if (separatorClicks == 350) {
                    Toast.makeText(home, "Why do you keep doing it?", Toast.LENGTH_LONG).show();
                }
                else if (separatorClicks == 500) {
                    Toast.makeText(home, "I have better things to spend my time on. Hope you have as well.", Toast.LENGTH_LONG).show();
                    Toast.makeText(home, "It was 500 clicks by the way.", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
