package org.tianara.helloworld;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.tianara.helloworld.generic.GenericTouchListener;
import org.tianara.helloworld.profile.Profile;
import org.tianara.helloworld.quote.Quote;
import org.tianara.helloworld.settings.Settings;
import org.tianara.helloworld.sidedrawer.DrawerItemClickListener;
import org.tianara.helloworld.sidedrawer.DrawerAdapter;
import org.tianara.helloworld.sidedrawer.DrawerItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize settings
        Settings.initialize(this);
        // Load settings
        Settings.loadAll();

        Settings.applyStyle(this, true);

        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Initialize side drawer
        ArrayList<DrawerItem> itemList = new ArrayList<DrawerItem>();
        itemList.add(new DrawerItem("Category", DrawerItem.HEADER));
        itemList.add(new DrawerItem("new", "New", DrawerItem.NORMAL));
        itemList.add(new DrawerItem("top", "Top", DrawerItem.NORMAL));
        itemList.add(new DrawerItem("random", "Random", DrawerItem.NORMAL));
        itemList.add(DrawerItem.Separator());
        if (Settings.fromId(Settings.id.LANGUAGE).getIndex() == 1) {
            itemList.add(new DrawerItem("abyss", "Abyss", DrawerItem.NORMAL));
        }
        itemList.add(new DrawerItem("Preferences", DrawerItem.HEADER));
        itemList.add(new DrawerItem("settings", "Settings", DrawerItem.NORMAL));
        //itemList.add(new DrawerItem("profile", "Profile", DrawerItem.NORMAL));
        itemList.add(DrawerItem.Separator());
        itemList.add(new DrawerItem("version", "Version", DrawerItem.NORMAL));

        ListView mDrawerList;
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this));

        // Clarity
        LinearLayout quoteList = (LinearLayout) findViewById(R.id.mainLayout);
        LayoutInflater inflater = LayoutInflater.from(quoteList.getContext());
        RelativeLayout quote = (RelativeLayout) inflater.inflate(R.layout.quote_clarity, quoteList, false);
        quoteList.addView(quote);

        // On swipe update
        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        ScrollView mainScroll = (ScrollView)findViewById(R.id.mainScroll);
        // Load stuff
        if (savedInstanceState != null) {
            boolean isInitialized = savedInstanceState.getBoolean("initialized", false);
            if (isInitialized) {
                Quote.populateBuffered(this);
                double scrollVal = savedInstanceState.getDouble("main_scroll_y");
                //scroll.scrollTo(0, (int) Math.round(scrollVal * scroll.getHeight()));
                Log.d("Scroll", String.valueOf(scrollVal * mainScroll.getHeight()));
                //scroll.scrollTo(0, (int)Math.round(scrollVal));
            }
        }
    }
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        ScrollView scroll = (ScrollView) findViewById(R.id.mainScroll);
        outState.putBoolean("initialized", true);
        outState.putDouble("main_scroll_y", ((double)scroll.getScrollY()) / ((double)scroll.getHeight()));
        //Log.d("Scroll", String.valueOf((double)scroll.getScrollY() / ((double)scroll.getHeight())));
        //outState.putDouble("main_scroll_y", scroll.getScrollY());

        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        int firstVisibleQuote = -1;
        int lastVisibleQuote = -1;
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            boolean isVisible = isViewVisible(mainLayout.getChildAt(i));
            if (isVisible && firstVisibleQuote == -1) {
                firstVisibleQuote = i;
            }
            if (!isVisible && firstVisibleQuote != -1 && lastVisibleQuote == -1) {
                lastVisibleQuote = i - 1;
                break;
            }
        }
        if (firstVisibleQuote != -1 && lastVisibleQuote != -1) {
            Profile.quotesReadTotal += lastVisibleQuote;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save settings
        Settings.saveAll();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void swapActivity(Class cl) {
        Intent intent = new Intent(this, cl);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) { return; }

        boolean settingsChanged = data.getBooleanExtra("settingsChanged", false);
        if (settingsChanged)
            this.recreate();
    }

    public void onRefresh() {
        //Toast.makeText(this, "Refreshin'", Toast.LENGTH_SHORT).show();

        Quote.populateWith(this, Quote.getDisplayedCategory());
    }

    // Check if the view is visible on screen
    private boolean isViewVisible(View view) {
        Rect scrollBounds = new Rect();
        ((ScrollView)findViewById(R.id.mainScroll)).getDrawingRect(scrollBounds);

        float top = view.getY();
        float bottom = top + view.getHeight();

        if (scrollBounds.top < top && scrollBounds.bottom > bottom) {
            return true;
        } else {
            return false;
        }
    }
    // Get the quote view from index
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
