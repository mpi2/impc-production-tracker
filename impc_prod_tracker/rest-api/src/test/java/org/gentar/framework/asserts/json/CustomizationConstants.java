package org.gentar.framework.asserts.json;

public class CustomizationConstants
{
    public static final String URL_PATTERN = "https?:(.)+";

    public static final String TPN_PATTERN = "TPN:\\d{1,10}";
    public static final String PIN_PATTERN = "PIN:\\d{1,10}";
    public static final String TPO_PATTERN = "TPO:\\d{1,12}";
    public static final String MIN_PATTERN = "MIN:\\d{1,12}";
    public static final String PSN_PATTERN = "PSN:\\d{1,12}";
    public static final String TPN_URL_PATTERN = URL_PATTERN + TPN_PATTERN;
    public static final String PIN_URL_PATTERN = URL_PATTERN + PIN_PATTERN;
    public static final String MIN_URL_PATTERN = URL_PATTERN + MIN_PATTERN;
    public static final String PSN_URL_PATTERN = URL_PATTERN + PSN_PATTERN;
}
