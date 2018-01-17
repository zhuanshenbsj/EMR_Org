package com.cloud.emr.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CreateFileUtil{
	public static boolean CreateFile(String destFileName) {
	    File file = new File(destFileName);
	    if (file.exists()) {
	     System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
	     return false;
	    }
	    if (destFileName.endsWith(File.separator)) {
	     System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
	     return false;
	    }
	    if (!file.getParentFile().exists()) {
	     System.out.println("目标文件所在路径不存在，准备创建...");
	     if (!file.getParentFile().mkdirs()) {
	      System.out.println("创建目录文件所在的目录失败！");
	      return false;
	     }
	    }

	    // 创建目标文件
	    try {
	     if (file.createNewFile()) {
	      System.out.println("创建单个文件" + destFileName + "成功！");
	      return true;
	     } else {
	      System.out.println("创建单个文件" + destFileName + "失败！");
	      return false;
	     }
	    } catch (IOException e) {
	     e.printStackTrace();
	     System.out.println("创建单个文件" + destFileName + "失败！");
	     return false;
	    }
	}



	public static boolean createDir(String destDirName) {
	    File dir = new File(destDirName);
	    if(dir.exists()) {
	     return false;
	    }
	    if(!destDirName.endsWith(File.separator))
	     destDirName = destDirName + File.separator;
	    // 创建单个目录
	    if(dir.mkdirs()) {
	     System.out.println("创建目录" + destDirName + "成功！");
	     return true;
	    } else {
	     System.out.println("创建目录" + destDirName + "失败！");
	     return false;
	    }
	}



	public static String createTempFile(String prefix, String suffix, String dirName) {
	    File tempFile = null;
	    try{
	    if(dirName == null) {
	     // 在默认文件夹下创建临时文件
	     tempFile = File.createTempFile(prefix, suffix);
	     return tempFile.getCanonicalPath();
	    }
	    else {
	     File dir = new File(dirName);
	     // 如果临时文件所在目录不存在，首先创建
	     if(!dir.exists()) {
	      if(!CreateFileUtil.createDir(dirName)){
	       System.out.println("创建临时文件失败，不能创建临时文件所在目录！");
	       return null;
	      }
	     }
	     tempFile = File.createTempFile(prefix, suffix, dir);
	     return tempFile.getCanonicalPath();
	    }
	    } catch(IOException e) {
	     e.printStackTrace();
	     System.out.println("创建临时文件失败" + e.getMessage());
	     return null;
	    }
	}
	
	/**
	 * copy Directiory
	 * @param sourceDir
	 * @param targetDir
	 * @throws IOException
	 */
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }
	 /**
     * copy Dir
     * @param sourceDirPath
     * @param targetDirPath
     * @throws IOException
     */
    public static void copyDir(String sourceDirPath, String targetDirPath) throws IOException {
        // 创建目标文件夹
        (new File(targetDirPath)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDirPath)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 复制文件
                String type = file[i].getName().substring(file[i].getName().lastIndexOf(".") + 1);

                if (type.equalsIgnoreCase("txt"))
                    copyFile(file[i], new File(targetDirPath + file[i].getName()), "UTF-8","GBK");
                else
                    copyFile(file[i], new File(targetDirPath + file[i].getName()));
            }
            if (file[i].isDirectory()) {
                // 复制目录
                String sourceDir = sourceDirPath + File.separator + file[i].getName();
                String targetDir = targetDirPath + File.separator + file[i].getName();
                copyDirectiory(sourceDir, targetDir);
            }
        }
    }
    /**
     * read File
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    @SuppressWarnings("unused")
	public static String readFile(File sourceFile) throws IOException {
    	BufferedInputStream inBuff = null;
    	String re_value="";
    	List<byte[]> srcArrays = new ArrayList<byte[]>();
    	try {
    		// 新建文件输入流并对它进行缓冲
    		inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
    		byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
            	srcArrays.add(b);      
            }
    		re_value = new String(byteCopy(srcArrays),"ISO-8859-1");
    	} finally {
    		// 关闭流
    		if (inBuff != null)
    			inBuff.close();
    	}
    	return re_value;
    }
   /**
    * combine arrays
    * @param srcArrays
    * @return combine byte
    */
    public static byte[] byteCopy(List<byte[]> srcArrays) {
     int len = 0;
     for (byte[] srcArray:srcArrays) {
      len+= srcArray.length;
     }
        byte[] destArray = new byte[len];   
        int destLen = 0;
        for (byte[] srcArray:srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);   
            destLen += srcArray.length;   
        }   
        return destArray;
    }  
    /**
     * copy File
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
    /**
     * copy File
     * @param srcFileName
     * @param destFileName
     * @param srcCoding
     * @param destCoding
     * @throws IOException
     */
    public static void copyFile(File srcFileName, File destFileName, String srcCoding, String destCoding) throws IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileName), srcCoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFileName), destCoding));
            char[] cbuf = new char[1024 * 5];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } finally {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        }
    }
    /**
     * delete file or directory
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
            }
        }
    }
    /** 
     * rename file name
     * @param resFilePath 源文件路径 
     * @param newFileName 重命名 
     * @return 操作成功标识 
     */ 
    public  static boolean renameFile(String resFilePath, String newFileName) { 
        String newFilePath = new File(resFilePath).getParent()+ "/" + newFileName; 
        File resFile = new File(resFilePath); 
        File newFile = new File(newFilePath); 
        return resFile.renameTo(newFile); 
    }
    public static void main(String[] args) {
    	String file_name = "D:\\APP\\PHR\\sportDetail\\15910858533";
    	File file = new File(file_name);
    	if(file.exists())
		System.out.println(renameFile("D:\\APP\\PHR\\sportDetail\\15910858533","15910858512"));
	}
}