package com.cloud.emr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadFileUtil {
	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 * @param fileName 指定的文件目录
	 * @return
	 */
    public static String readFileByLines(String fileName) {
    	String re_str = "";
        File file = new File(fileName);
        if(!file.exists())
        	return "";
        BufferedReader reader = null;
        try {
        	String tempString = null;
            reader = new BufferedReader(new FileReader(file));
            while ((tempString = reader.readLine()) != null) {
            	re_str += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
            }
        }
        return re_str;
    }
}
