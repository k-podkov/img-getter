package com.kpodkov;

/**
 * Created by Kirill on 12/28/2015.
 */
public class urlParser {

    public static String getExtension (String string) {
        string = string.substring(string.lastIndexOf(".")+1,string.length());
        return string;
    }

    public static String getFileName (String string) {
        string = string.substring(string.lastIndexOf("/")+1,string.length());
        return string;
    }

    public static String cleanTitle (String string) {
        string = string.replace("<","");
        string = string.replace(">","");
        string = string.replace(":","");
        string = string.replace("\"","");
        string = string.replace("/","");
        string = string.replace("\\","");
        string = string.replace("|","");
        string = string.replace("?","");
        string = string.replace("*","");
        return string;
    }

}
