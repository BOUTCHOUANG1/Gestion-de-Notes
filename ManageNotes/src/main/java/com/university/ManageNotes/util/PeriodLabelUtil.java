package com.university.ManageNotes.util;

public class PeriodLabelUtil {
    private PeriodLabelUtil(){}
    public static String normalize(String period){
        if(period==null){return null;}
        String p = period.trim().toUpperCase().replace(" ", "").replace("#", "");
        if(p.matches("^(CC|SN)[12]$")){
            return p;
        }
        return p;
    }
}
