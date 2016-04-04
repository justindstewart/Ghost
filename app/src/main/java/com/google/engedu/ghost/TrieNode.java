package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode currentNode = this;
        int i;
        String key;

        for(i = 0; i < s.length(); ++i) {
            key = "" + s.charAt(i);
            if(!currentNode.children.containsKey(key)) {
                currentNode.children.put(key, new TrieNode());
            }
            currentNode = currentNode.children.get(key);
        }

        currentNode.isWord = true;
    }

    public boolean isWord(String s) {
        TrieNode currentNode = this;
        int i;
        String key;
        boolean found = false;
        Log.d("Debug", "Bool: " + currentNode.isWord);
        for(i = 0; i < s.length(); ++i) {
            key = "" + s.charAt(i);
            if(currentNode.children.containsKey(key)) {
                currentNode = currentNode.children.get(key);
            }
            else {
                found = false;
                break;
            }
        }

        if(currentNode.isWord) {
            found = true;
        }
        Log.d("Debug", "Bool return: " + currentNode.isWord);
        return found;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode currentNode = this;
        int i;
        Set keyList;
        String key;
        String word = "";

        Log.d("Debug", "Starting getAnyWord");
        if (!s.isEmpty()) {
            for (i = 0; i < s.length(); ++i) {
                Log.d("Debug", "getAnyWord: For Loop");
                key = "" + s.charAt(i);
                Log.d("Debug", "getAnyWord key: " + key);
                word += key;
                if (currentNode.children.containsKey(key)) {
                    currentNode = currentNode.children.get(key);
                } else {
                    return null;
                }

            }
        }

        while (!currentNode.isWord) {
            Log.d("Debug", "getAnyWord while: " + word);
            keyList = currentNode.children.keySet();
            key = keyList.toArray()[new Random().nextInt(keyList.size())].toString();
            word += key;
            currentNode = currentNode.children.get(key);
            Log.d("Debug", "getAnyWord while key: " + key);
            Log.d("Debug", "getAnyWord while bool: " + currentNode.isWord);
        }


        return word;
    }

    public Set cleanList(TrieNode node) {
        Set list;

        list = node.children.keySet();

        for(int i = 0; i < list.size(); ++i){
            if(node.children.get(list.toArray()[i]).isWord) {
                list.remove(i);
            }
        }

        return list;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode currentNode = this;
        int i;
        Set keyList;
        String key;
        String word = "";
        Set cleanList;

        Log.d("Debug", "Starting getAnyWord");
        if (!s.isEmpty()) {
            for (i = 0; i < s.length(); ++i) {
                Log.d("Debug", "getAnyWord: For Loop");
                key = "" + s.charAt(i);
                Log.d("Debug", "getAnyWord key: " + key);
                word += key;
                if (currentNode.children.containsKey(key)) {
                    currentNode = currentNode.children.get(key);
                } else {
                    return null;
                }

            }
        }

        while (!currentNode.isWord) {
            keyList = currentNode.children.keySet();
            cleanList = cleanList(currentNode);
            if(cleanList.size() == keyList.size()) {
                key = keyList.toArray()[new Random().nextInt(keyList.size())].toString();
            } else {
                key = cleanList.toArray()[new Random().nextInt(keyList.size())].toString();
            }

            word += key;
            currentNode = currentNode.children.get(key);
        }


        return word;
    }
}
