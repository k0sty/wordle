package web.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import trie.PotentialWordsWrapper;
import trie.WordWrapper;
import java.util.SortedSet;

public class Serializer {

    /*
    * This class containing all custom serialization for getting into web
    * friendly or JSON form
    */

    public static String createJSONString(PotentialWordsWrapper potentialWordsWrapper) {

        JSONArray arrayWords = new JSONArray();

        for (WordWrapper currentWrappedWord : potentialWordsWrapper.getWordsSet()) {
            arrayWords.put(currentWrappedWord.getWord());
        }

        JSONObject returnJSONObj = new JSONObject();
        returnJSONObj.put("potentialWords", arrayWords);

        return returnJSONObj.toString();
    }

}