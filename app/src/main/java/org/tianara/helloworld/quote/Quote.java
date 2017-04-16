package org.tianara.helloworld.quote;


import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.tianara.helloworld.MainActivity;
import org.tianara.helloworld.R;
import org.tianara.helloworld.generic.NoConnectivityException;
import org.tianara.helloworld.settings.Settings;
import org.tianara.helloworld.web.WebParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Quote {
    // Static stuff
    public static List<Quote> displayed = new ArrayList<>();
    private static WebParser.category displayedCategory = WebParser.category.NEW;
    // Fields
    private int id;
    private String date;
    private String text;
    // Methods
    public int getId() { return id; }
    public String getDate() { return date; }
    public String getText() { return text; }
    public static WebParser.category getDisplayedCategory() { return displayedCategory; }
    // Constructor
    public Quote(int id, String date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    // Static functions
    public static Quote getRandom() {
        Quote q = null;
        List<Quote> l = new ArrayList<>();

        try {
            l = WebParser.GetSome(1);
            if (l.size() > 0)
                q = l.get(0);
            else
                q = null;
        }
        catch (IOException ex) { q = new Quote(0, "-", ex.getMessage()); ex.printStackTrace(); }

        return q;
    }
    public static void clear() {
        displayed.clear();
    }
    public static void prepare(int count) {
        try { WebParser.Prepare(count); }
        catch (IOException ex) { ex.printStackTrace(); }
        catch (NoConnectivityException ex) { ex.printStackTrace(); }
    }

    // Pull new quotes and populate the list in async task
    public static void populateWith(MainActivity home, WebParser.category category) {
        displayedCategory = category;

        LinearLayout ll = (LinearLayout) home.findViewById(R.id.mainLayout);
        ll.removeAllViews();
        ((ScrollView)home.findViewById(R.id.mainScroll)).scrollTo(0, 0);
        ((DrawerLayout)home.findViewById(R.id.drawer_layout)).closeDrawers();

        LayoutInflater inflater = LayoutInflater.from(ll.getContext());
        RelativeLayout quote = (RelativeLayout) inflater.inflate(R.layout.quote_loading, ll, false);
        ll.addView(quote);

        WebParser.site source;
        if (Settings.fromId(Settings.id.LANGUAGE).getIndex() == 0) {
            source = WebParser.site.BASH_ORG;
        }
        else {
            source = WebParser.site.BASH_IM;
        }
        WebParser.Setup(source, category);

        QuoteTaskPopulate task = new QuoteTaskPopulate();
        task.execute(home);
    }

    // Inflate the list with currently buffered quotes
    public static void populateBuffered(MainActivity home) {
        LinearLayout ll = (LinearLayout) home.findViewById(R.id.mainLayout);
        LayoutInflater inflater = LayoutInflater.from(ll.getContext());
        RelativeLayout quote;
        TextView quoteId, quoteDate, quoteText;

        // Remove all views
        ll.removeAllViews();
        // Iterate and add new views
        for (int i = 0; i < Quote.displayed.size(); i++) {
            quote = (RelativeLayout) inflater.inflate(R.layout.quote_layout, ll, false);

            Quote q = Quote.displayed.get(i);
            try {
                quoteId = ((TextView) quote.findViewById(R.id.quoteId));
                quoteId.setText("#" + String.valueOf(q.getId()));
                quoteId.setTextSize(TypedValue.COMPLEX_UNIT_SP, Settings.
                        fromId(Settings.id.FONT_SIZE_SMALL).getInt());

                quoteDate = ((TextView) quote.findViewById(R.id.quoteDate));
                quoteDate.setText(q.getDate());
                quoteDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, Settings.
                        fromId(Settings.id.FONT_SIZE_SMALL).getInt());

                quoteText = ((TextView) quote.findViewById(R.id.quoteText));
                quoteText.setText(q.getText());
                quoteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Settings.
                        fromId(Settings.id.FONT_SIZE_MAIN).getInt());

                ll.addView(quote);
            }
            catch (NullPointerException ex) { ex.printStackTrace(); }
        }
    }
}
