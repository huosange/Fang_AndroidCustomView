package com.custom.fang_androidcustomview.utils;

/**
 * Java汉字转换为拼音
 */
public class CharacterParser {

    /**
     * 汉字转成ASCII码
     * @param chs
     * @return
     */
    private int getChsAscii(String chs){
        int asc=0;
        try{
            byte[] bytes=chs.getBytes("gb2312");
            if(bytes==null||bytes.length>2||bytes.length<=0){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return asc;
    }
}
