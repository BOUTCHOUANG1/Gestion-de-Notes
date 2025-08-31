package com.university.ManageNotes.util;

public class PeriodLabelUtil {
    private PeriodLabelUtil(){}
    public static String normalize(String period){
        if(period==null){
            return null;
        }
        String p = period.trim()
                .toUpperCase()
                .replace(" ", "")
                .replace("#", "");
        if(p.matches("^(CC_1|CC_2|SN_1|SN_2)")){
            return p;
        }
        return p;
    }
}
