package org.gentar.report.utils.stringencoding;

public class Formatter {

    public static String cleanNewLines(String inputString){
        return inputString.replaceAll("\\r\\n", "\n");
    }

    public static String clean(String inputString){
        return Formatter.cleanNewLines(inputString);
    }
}
