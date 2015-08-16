package com.demien.words;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;

/**
 * Created by dmitry on 02.04.15.
 */
public class WordTranslator {

    static enum BeginEnd {BEGIN, END};

    private final String WORD_PATTERN="#WORD#";
    private final String TRANSLATION_URL_PATTERN="http://pl.wiktionary.org/w/index.php?title="+WORD_PATTERN+"&printable=yes";
    //private final String[] translationBeginElements={"rosyjski", "title=\""};
    //private final String[] translationEndElements={"\""};
    private final String[] translationBeginElements={"<li>angielski", ")"};
    private final String[] translationEndElements={"</li>"};


    private final String[] exampleBeginElements={"przykłady:", "<dd>(1.1)"};
    private final String[] exampleEndElements={"</dd>"};

    private final String[] cutPatterns={";", ".", "["};



    public String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void log(String message) {
        int trashHold=3000;
        if (message.length()>trashHold ) {
            message=message.substring(1, trashHold);
        }
        //System.out.println(message);
    }

    public static String getPlainText(String html) throws Exception {
        Document doc = Jsoup.parse(html);
        String text = doc.body().text(); // "An example link"
        return text;
    }

    /*
    public int getPatternPosition(String page, int startPosition) {

    }
    */

    public int getArrayPosition(String page, String[] array, int startPos, BeginEnd beginEnd) {
        int pos=-1;
        String lastArrayElement="";
        for (String s:array) {
            pos=page.indexOf(s, startPos);
            if (pos==-1) break;
            startPos=pos;
            lastArrayElement=s;
        }
        if (pos>-1) {
            if (beginEnd == BeginEnd.END) {
                pos = pos + lastArrayElement.length();
            }
        }

        return pos;
    }

    public String cutByPatterns(final String s) {
        String result=s;
        for (String pattern:cutPatterns) {
            int index=result.indexOf(pattern);
            if (index>0) {
             result=result.substring(0, index);
            }
        }
        return result;
    }

    public String getTextFromPage(String page, String[] beginElements, String[] endElements) throws Exception {
        String result="NULL";

        int startPos=getArrayPosition(page, beginElements, 0, BeginEnd.END);
        if (startPos>-1) {
            int endPos = getArrayPosition(page, endElements, startPos, BeginEnd.BEGIN);

            if ((startPos > 0) && (endPos > 0)) {
                result = page.substring(startPos, endPos);
            }
        }

        result=getPlainText(result);
        result=cutByPatterns(result);

        return result;
    }

    public String getTranslationAndExample(String word) throws Exception {
        String uri = TRANSLATION_URL_PATTERN.replace(WORD_PATTERN, word);
        log(uri);
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        //connection.setRequestProperty("Accept", "application/xml");

        //JAXBContext jc = JAXBContext.newInstance(Customer.class);
        InputStream is = connection.getInputStream();

        String page=convertStreamToString(is);
        //log(getPlainText(page));
        connection.disconnect();

        String translation=getTextFromPage(page, translationBeginElements, translationEndElements);
        String example=getTextFromPage(page, exampleBeginElements, exampleEndElements);


        return translation+"#"+example;
    }

    public static void main(String[] args) throws Exception {
        //String s=new WordTranslator().getTranslationAndExample("który");
        String s=new WordTranslator().getTranslationAndExample("widać");
        log(s);

    }
}
