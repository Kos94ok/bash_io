package org.tianara.helloworld.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.tianara.helloworld.R;
import org.tianara.helloworld.SettingsActivity;


public class SettingsItemClickListener implements ListView.OnItemClickListener {

    private SettingsActivity home;
    private int lastClickedPosition;
    private AlertDialog alert;

    public SettingsItemClickListener(SettingsActivity home) {
        this.home = home;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        lastClickedPosition = position;

        String[] valueList = Settings.fromPosition(position).getValues().toArray(
                new String[Settings.fromPosition(position).getValues().size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(home);
        builder.setTitle(Settings.fromPosition(position).getName());
        //builder.setIcon(R.drawable.ic_drawer);
        builder.setSingleChoiceItems(valueList, Settings.fromPosition(position).getIndex(),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Settings.updateAt(lastClickedPosition, item);
                alert.dismiss();

                if (Settings.fromPosition(position).getId() == Settings.id.COLOR_PALETTE) {
                    //home.colorPaletteSwap = true;
                    home.recreate();
                }
                else {
                    home.updateContent();
                }
            }
        });
        alert = builder.create();

        alert.show();
    }
}
