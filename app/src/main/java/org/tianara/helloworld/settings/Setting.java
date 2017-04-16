package org.tianara.helloworld.settings;

import java.util.List;

public class Setting {
    // Private fields
    private Settings.id id;
    private String name;
    private int valueIndex;
    private int defaultIndex;
    private List<String> possibleValues;
    // Public functions
    public Settings.id getId() { return id; }
    public String getName() { return name; }
    public int getIndex() { return valueIndex; }
    public int getDefault() { return defaultIndex; }
    public int getInt() { return Integer.parseInt(possibleValues.get(valueIndex)); }
    public double getDouble() { return Double.parseDouble(possibleValues.get(valueIndex)); }
    public String getString() { return possibleValues.get(valueIndex); }
    public List<String> getValues() { return possibleValues; }
    public void setValue(int index) { valueIndex = index; }

    // Constructors
    public Setting(Settings.id id, String name, int valueIndex, List<String> possibleValues) {
        this.id = id;
        this.name = name;
        this.valueIndex = valueIndex;
        this.defaultIndex = valueIndex;
        this.possibleValues = possibleValues;
    }
}
