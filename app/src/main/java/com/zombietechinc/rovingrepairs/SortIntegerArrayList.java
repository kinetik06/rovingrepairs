package com.zombietechinc.rovingrepairs;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by User on 8/29/2017.
 */

public class SortIntegerArrayList {
    private ArrayList<Integer> integerArrayList;
    public SortIntegerArrayList (ArrayList<Integer> integerArrayList) {
        this.integerArrayList = integerArrayList;
    }
        public ArrayList<Integer> getArrayList() {
            return this.integerArrayList;
        }
        public ArrayList<Integer> sortAscending() {
            Collections.sort(this.integerArrayList);
            return this.integerArrayList;
        }
        public ArrayList<Integer> sortDescending() {
            Collections.sort(this.integerArrayList, Collections.reverseOrder());
            return this.integerArrayList;
    }
}
