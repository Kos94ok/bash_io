package org.tianara.helloworld.web;

import android.util.Log;

import org.apache.commons.lang3.NotImplementedException;
import org.tianara.helloworld.generic.NoConnectivityException;
import org.tianara.helloworld.quote.Quote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class WebParser {
    // No instantiations allowed
    private WebParser() {}

    // Enumeration
    public enum site {
        BASH_IM,
        BASH_ORG,
    }

    public enum category {
        NEW,
        TOP,
        RANDOM,
        ABYSS,
    }

    // Fields
    private static site currentSite = site.BASH_IM;
    private static category currentCategory = category.RANDOM;
    private static Queue<Quote> parsedQueue = new ArrayDeque<>();

    // Private methods
    private static void ParseSome() throws MalformedURLException, NoConnectivityException {
        // Anchor strings
        String dateStartTag, dateEndTag, idStartTag, idEndTag, textStartTag, textEndTag;
        // Determine the url
        String url = null, encoding = null;
        if (currentSite == site.BASH_IM) {
            // Main info
            url = "http://bash.im";
            encoding = "windows-1251";
            // Anchor strings
            dateStartTag = "<span class=\"date\">";
            dateEndTag = "</span>";
            idStartTag = "class=\"id\">";
            idEndTag = "</a>";
            textStartTag = "<div class=\"text\">";
            textEndTag = "</div>";
            // Categories
            if (currentCategory == category.TOP) { url += "/best"; }
            else if (currentCategory == category.RANDOM) { url += "/random"; }
            else if (currentCategory == category.ABYSS) {
                url += "/abyss";
                idStartTag = "<span class=\"id\">";
                idEndTag = "</span>";
            }
        }
        else if (currentSite == site.BASH_ORG) {
            // Main info
            url = "http://bash.org";
            encoding = "windows-1251";
            // Anchor strings
            dateStartTag = null;
            dateEndTag = null;
            idStartTag = "<a href=\"?";
            idEndTag = "\"";
            textStartTag = "<p class=\"qt\">";
            textEndTag = "</p>";
            // Categories
            if (currentCategory == category.NEW) { url += "/?latest"; }
            else if (currentCategory == category.TOP) { url += "/?top"; }
            else if (currentCategory == category.RANDOM) { url += "/?random"; }
        }
        else {
            throw new NotImplementedException("Website not supported");
        }
        // Get the page and setup some strings
        String page = WebClient.get(new URL(url), encoding);

        int lastTag = 0;
        // Until we have something else to parse
        while (page.indexOf(textStartTag, lastTag) != -1) {
            // Find the date tag positions
            int continueFrom = lastTag;
            Log.d("tag", String.valueOf(continueFrom));
            String date = null;
            if (dateStartTag != null && dateEndTag != null) {
                int dateStart = page.indexOf(dateStartTag, continueFrom) + dateStartTag.length();
                int dateEnd = page.indexOf(dateEndTag, dateStart);
                continueFrom = dateEnd;
                // Get the text substring
                date = page.substring(dateStart, dateEnd);
            }

            // Find the id tag positions
            int idStart = page.indexOf(idStartTag, continueFrom) + idStartTag.length();
            int idEnd = page.indexOf(idEndTag, idStart);
            continueFrom = idEnd;
            // Get the id substring
            String id = page.substring(idStart, idEnd);

            // Find the text tag positions
            int textStart = page.indexOf(textStartTag, continueFrom) + textStartTag.length();
            int textEnd = page.indexOf(textEndTag, textStart);
            continueFrom = textEnd;
            // Get the text substring
            String text = page.substring(textStart, textEnd);

            // Push the new element
            parsedQueue.add(new Quote(Integer.parseInt(id.substring(1)), date, text));
            // Update next search position
            lastTag = continueFrom + textEndTag.length();
        }
    }

    // Public methods
    // Setup the class to the new website and/or category
    public static void Setup(WebParser.site site, WebParser.category category) {
        if (currentSite == site && currentCategory == category)
            return;

        currentSite = site;
        currentCategory = category;
        parsedQueue.clear();
    }

    // Get some quotes from the current site and category
    public static ArrayList<Quote> GetSome(int howMany) throws IOException {
        ArrayList<Quote> returnList = new ArrayList<Quote>();
        for (int i = 0; i < Math.min(howMany, parsedQueue.size()); i++) {
            returnList.add(parsedQueue.poll());
        }
        return returnList;
    }

    // Prepare some quotes in advance
    public static void Prepare(int howMany) throws IOException, NoConnectivityException {
        while (parsedQueue.size() < howMany) {
            ParseSome();
        }
    }
}
