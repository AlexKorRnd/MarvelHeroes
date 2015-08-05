package com.growapp.marvelheroes.data;

import java.util.ArrayList;

/**
 * Created by Алексей on 04.08.2015.
 */
public class CharacterDataContainer {
    int offset;
    int limit;
    int total;
    int count;
    ArrayList<Character> results;


    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Character> getResults() {
        return results;
    }

    public void setResults(ArrayList<Character> results) {
        this.results = results;
    }
}
