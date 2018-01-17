package com.cloud.emr.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class PinyinUtil {
	 private static HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();    
	   
	    //初始化信息  
	    public static void init() throws IOException{  
	      
	        spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);    //小写
	   //     spellFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);    //大写
	        spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   //不加声调
	  //     spellFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER); //加声调
	       spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);   
	    
	    }  
	    // 判断字符串是否包含有中文   
	    public static boolean isChinese(String str) {    
	        String regex = "[\\u4e00-\\u9fa5]";    
	        Pattern pattern = Pattern.compile(regex);    
	        Matcher matcher = pattern.matcher(str);    
	        return matcher.find();    
	    }    
	    //使用PinYin4j.jar将汉字转换为拼音  
	    public static String chineneToSpell(String hanzi){  
	        return PinyinHelper.toHanyuPinyinString(hanzi , spellFormat ,"");  
	    }
	    
	    public static String pinyin2searchCondition(String pinyin)
	    {
			String ends="";
			if(pinyin!=null&&!pinyin.equalsIgnoreCase("")&&!isChinese(pinyin))
			{
				char[] c=pinyin.toCharArray();
				for(int i=0;i<c.length;i++){	
					if(i==c.length-1){
						ends=ends+"%"+c[i]+"%";
					}else{
						ends=ends+"%"+c[i];
					}
				}
			}
			return ends;
	    }
	    
}
