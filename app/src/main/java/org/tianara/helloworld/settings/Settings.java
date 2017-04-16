package org.tianara.helloworld.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.tianara.helloworld.MainActivity;
import org.tianara.helloworld.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings {
    private static boolean saveEnabled = true;
    private static MainActivity home;
    // Static class
    private Settings() {}
    // Enumeration
    public enum id {
        LANGUAGE,
        COLOR_PALETTE,
        FONT_SIZE_MAIN,
        FONT_SIZE_SMALL,
        QUOTE_BUFFER_SIZE
    }
    // Static fields
    private static List<Setting> list = new ArrayList<>();
    // Public static functions
    // Initialize the settings
    public static void initialize(MainActivity activity) {
        home = activity;
        list.clear();
        // Language
        list.add(new Setting(id.LANGUAGE, "Source", 0, new ArrayList<>(Arrays.asList(
                "bash.org [English]",
                "bash.im [Russian]"
        ))));
        // Color palette
        list.add(new Setting(id.COLOR_PALETTE, "Color palette", 0, new ArrayList<>(Arrays.asList(
                "Classic",
                "Black",
                "Green",
                "Pink",
                "Dark Pink"
        ))));
        // Font size
        list.add(new Setting(id.FONT_SIZE_MAIN, "Main font size", 3, new ArrayList<>(Arrays.asList(
                "12",
                "14",
                "16",
                "18",
                "20",
                "22",
                "24"
        ))));
        list.add(new Setting(id.FONT_SIZE_SMALL, "Small font size", 2, new ArrayList<>(Arrays.asList(
                "12",
                "14",
                "16",
                "18",
                "20",
                "22",
                "24"
        ))));
        list.add(new Setting(id.QUOTE_BUFFER_SIZE, "Quotes to buffer", 0, new ArrayList<>(Arrays.asList(
                "50",
                "100",
                "150",
                "200",
                "250"
        ))));
    }
    // Return a raw list
    public static List<Setting> getList() { return list; }
    // Return a setting object from its id
    public static Setting fromId(id id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                return list.get(i);
            }
        }
        return null;
    }
    // Return a setting object from its position in the list
    public static Setting fromPosition(int position) {
        return list.get(position);
    }
    // Modify the setting object from its position
    public static void updateAt(int position, int value) {
        list.get(position).setValue(value);
    }
    // Save all the settings to the home preferences
    public static void saveAll() {
        if (saveEnabled) {
            SharedPreferences pref = home.getPreferences(0);
            SharedPreferences.Editor editor = pref.edit();
            for (id entry : id.values()) {
                editor.putInt(entry.toString(), fromId(entry).getIndex());
            }
            editor.commit();
        }
        else {
            saveEnabled = true;
        }
    }
    // Load all the settings from the home preferences
    public static void loadAll() {
        SharedPreferences pref = home.getPreferences(0);
        for (id entry : id.values()) {
            int val = pref.getInt(entry.toString(), fromId(entry).getDefault());
            fromId(entry).setValue(val);
        }
    }
    // Clear the preferences and disable the saving for the next activity close
    public static void clearAll() {
        SharedPreferences pref = home.getPreferences(0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        saveEnabled = false;
    }
    // Apply style
    public static void applyStyle(Activity activity, boolean isMain) {
        // Set the style
        if (Settings.fromId(Settings.id.COLOR_PALETTE).getString() == "Classic") {
            if (isMain) activity.setTheme(R.style.MainThemeLight_MainActivity);
            else activity.setTheme(R.style.MainThemeLight);
        }
        else if (Settings.fromId(Settings.id.COLOR_PALETTE).getString() == "Black") {
            if (isMain) activity.setTheme(R.style.MainThemeDark_MainActivity);
            else activity.setTheme(R.style.MainThemeDark);
        }
        else if (Settings.fromId(Settings.id.COLOR_PALETTE).getString() == "Green") {
            if (isMain) activity.setTheme(R.style.MainThemeGreen_MainActivity);
            else activity.setTheme(R.style.MainThemeGreen);
        }
        else if (Settings.fromId(Settings.id.COLOR_PALETTE).getString() == "Pink") {
            if (isMain) activity.setTheme(R.style.MainThemePink_MainActivity);
            else activity.setTheme(R.style.MainThemePink);
        }
        else if (Settings.fromId(Settings.id.COLOR_PALETTE).getString() == "Dark Pink") {
            if (isMain) activity.setTheme(R.style.MainThemePinkDark_MainActivity);
            else activity.setTheme(R.style.MainThemePinkDark);
        }
    }

    // Private static functions

}