package com.vjudge.ecnuvj.tool;

import com.vjudge.ecnuvj.bean.Description;

import java.util.Comparator;

public class DescriptionComparator implements Comparator<Description> {
    public int compare(Description o1, Description o2) {
        return o1.getUpdateTime().compareTo(o2.getUpdateTime()) < 0 ? -1 : 1;
    }
}
