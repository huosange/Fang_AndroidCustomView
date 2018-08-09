package com.custom.fang_androidcustomview.utils;

import com.custom.fang_androidcustomview.bean.UserBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<UserBean>{

    @Override
    public int compare(UserBean o1, UserBean o2) {
        return o1.getSortLetters().compareTo(o2.getSortLetters());
    }
}
