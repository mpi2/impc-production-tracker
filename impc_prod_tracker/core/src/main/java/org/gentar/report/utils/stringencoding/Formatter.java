package org.gentar.report.utils.stringencoding;

public class Formatter {

    public static String cleanNewLines(String inputString){
        return inputString.replaceAll("\\r\\n", "\n");
    }

    public static String replaceInternalNewLines(String inputString){
        return inputString.replaceAll("\\n", "++n++");
    }

    public static String replaceInternalLineFeeds(String inputString){
        return inputString.replaceAll("\\r", "++n++");
    }

    public static String threePrime(String inputString){
        return inputString.replace("3’", "3'");
    }

    public static String fivePrime(String inputString){
        return inputString.replace("5’", "5'");
    }

    public static String oddCharacters(String inputString){
        return inputString
                .replace("\uF0E0", "++n++")
                .replace("–", "-")
                .replace("•\t", "")
                .replace("•", "")
                .replace("…", "...");
    }

    public static String clean(String inputString){
        return
                Formatter.oddCharacters(
                         Formatter.fivePrime(
                            Formatter.threePrime(
                                Formatter.replaceInternalLineFeeds(
                                    Formatter.replaceInternalNewLines(
                                        Formatter.cleanNewLines(inputString)
                                    )
                                )
                            )
                        )
                );
    }
}
