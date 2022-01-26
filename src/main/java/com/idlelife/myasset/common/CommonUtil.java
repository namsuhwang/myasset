package com.idlelife.myasset.common;

public class CommonUtil {
    public static boolean isNullEmpty(String str){
        boolean result;
        if(str == null || str.isBlank()){
            result = true;
        }else{
            result = false;
        }
        return result;
    }

}
