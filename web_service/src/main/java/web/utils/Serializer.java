package web.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import trie.WordWrapper;
import java.util.SortedSet;

public class Serializer {

    /*
    * This class containing all custom serialization for getting into web
    * friendly or JSON form
    */

    public static String createJSONString(SortedSet<WordWrapper> set) {
        // for output of trie.Trie.generatePotentialWords()

        JSONArray arrayWords = new JSONArray();

        Iterator value = set.iterator();
        while (value.hasNext()) {
            arrayWords.put(value.next());
        }

        JSONObject returnJSONObj = new JSONObject();
        returnJSONObj.put("potentialWords", arrayWords);

        return returnJSONObj.toString();
    }

}