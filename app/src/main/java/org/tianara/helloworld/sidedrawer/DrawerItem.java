package org.tianara.helloworld.sidedrawer;


public class DrawerItem {
    // Data fields
    private String mId;
    private String mText;
    private boolean mHeader;
    private boolean mSeparator;
    // Public functions
    public String getId() { return mId; }
    public String getText() { return mText; }
    public boolean isHeader() { return mHeader; }
    public boolean isSeparator() { return mSeparator; }
    public void setText(String text) { mText = text; }

    // Constructors
    public DrawerItem() {
        this("none", "No text", NORMAL);
    }
    public DrawerItem(String text, int type) {
        this("none", text, type);
    }
    public DrawerItem(String id, String text, int type) {
        this.mId = id;
        this.mText = text;
        if (type == SEPARATOR) { MakeSeparator(); }
        if (type == HEADER) { mHeader = true; }
    }
    // Private functions
    private void MakeSeparator() {
        this.mId = "separator";
        this.mText = null;
        this.mSeparator = true;
    }

    // Enumeration
    public static final int NORMAL = 0;
    public static final int SEPARATOR = 1;
    public static final int HEADER = 2;
    public static int getTypeCount() { return 3; }
    // Static functions
    public static DrawerItem Separator() {
        DrawerItem item = new DrawerItem();
        item.MakeSeparator();
        return item;
    }
}
