package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        int bottom = 0;
        int top = words.size() - 1;
        Log.d("Debug", "Word In " + prefix);
        int middle;
        if(prefix.isEmpty()) {
            int randomNum = random.nextInt((words.size() - 1));
            Log.d("Debug", "Word Out Empty " + words.get(randomNum));
            return words.get(randomNum);
        } else {
            while(bottom <= top) {
                middle = bottom + (top - bottom) / 2;
/*                Log.d("Debug", "Value of Top: " + top);
                Log.d("Debug", "Value of Middle: " + middle);
                Log.d("Debug", "Value of Bottom: " + bottom);
                Log.d("Debug", "Substring: " + words.get(middle).substring(0, length));
                Log.d("Debug", "Length: " + length);*/
                if(words.get(middle).startsWith(prefix)) {
                    return words.get(middle);
                } else if (words.get(middle).compareTo(prefix) > 0) {
                    top = middle - 1;

/*                    Log.d("Debug", "Less than " + words.get(middle));
                    Log.d("Debug", "Value " + words.get(middle).compareTo(prefix));*/
                }
                else {
                    bottom = middle + 1;
/*                    Log.d("Debug", "Greater Than " + words.get(middle));
                    Log.d("Debug", "Value " + words.get(middle).compareTo(prefix));*/
                }
            }
        }

/*        Log.d("Debug", "Word Out Null ");*/
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
