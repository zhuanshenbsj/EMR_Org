package com.cloud.emr.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * 
 * 项目名称：EMR 类名称：FileUtil 类描述：暂无 创建人：lw 创建时间：2014-3-25 上午09:57:11 修改人：lw
 * 修改时间：2014-3-25 上午09:57:11 修改备注：
 * 
 * @version
 */
public class FileUtil {
	private static int cacheSize = 8192;
	/**
	 * The number of bytes in a kilobyte.
	 */
	public static final long ONE_KB = 1024;

	/**
	 * The number of bytes in a megabyte.
	 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/**
	 * The file copy buffer size (30 MB)
	 */
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

	/**
	 * The number of bytes in a gigabyte.
	 */
	public static final long ONE_GB = ONE_KB * ONE_MB;

	/**
	 * The number of bytes in a terabyte.
	 */
	public static final long ONE_TB = ONE_KB * ONE_GB;

	/**
	 * The number of bytes in a petabyte.
	 */
	public static final long ONE_PB = ONE_KB * ONE_TB;

	/**
	 * The number of bytes in an exabyte.
	 */
	public static final long ONE_EB = ONE_KB * ONE_PB;

	/**
	 * The number of bytes in a zettabyte.
	 */
	public static final BigInteger ONE_ZB = BigInteger.valueOf(ONE_KB)
			.multiply(BigInteger.valueOf(ONE_EB));

	/**
	 * The number of bytes in a yottabyte.
	 */
	public static final BigInteger ONE_YB = ONE_ZB.multiply(BigInteger
			.valueOf(ONE_EB));

	/**
	 * An empty array of type <code>File</code>.
	 */
	public static final File[] EMPTY_FILE_ARRAY = new File[0];

	/**
	 * The UTF-8 character set, used to decode octets in URLs.
	 */
	private static final Charset UTF8 = Charset.forName("UTF-8");
	/**
	 * 默认的过滤器
	 */
	private static FilenameFilter filter = null;

	/**
	 * 把long个字节的文件大小转换成为可读性强的文件大小版本
	 * 
	 * @param size
	 * @return
	 */
	public static String byteCountToDisplaySize(long size) {
		String displaySize;
		// if (size / ONE_EB > 0) {
		// displaySize = String.valueOf(size / ONE_EB) + " EB";
		// } else if (size / ONE_PB > 0) {
		// displaySize = String.valueOf(size / ONE_PB) + " PB";
		// } else if (size / ONE_TB > 0) {
		// displaySize = String.valueOf(size / ONE_TB) + " TB";
		// } else
		if (size / ONE_GB > 0) {
			displaySize = String.valueOf(size / ONE_GB) + " GB";
		} else if (size / ONE_MB > 0) {
			displaySize = String.valueOf(size / ONE_MB) + " MB";
		} else if (size / ONE_KB > 0) {
			displaySize = String.valueOf(size / ONE_KB) + " KB";
		} else {
			displaySize = String.valueOf(size) + " bytes";
		}
		return displaySize;
	}

	public static File createFileIfNoExists(String path) throws IOException {
		return createFileIfNoExists(new File(path));
	}

	public static File createFileIfNoExists(File file) throws IOException {
		if (file == null || file.exists())
			return null;
		makeDir(file.getParentFile());
		boolean b = file.createNewFile();
		if (b)
			return file;
		else
			return null;
	}

	public static boolean createNewFile(String fileName) throws IOException {
		return createNewFile(new File(fileName));
	}

	/**
	 * 创建一个文件（包括父类） 创建新文件，如果父目录不存在，也一并创建。可接受 null 参数
	 * 
	 * @param f
	 *            文件对象
	 * @return false，如果文件已存在。 true 创建成功
	 * @throws IOException
	 */
	public static boolean createNewFile(File file) throws IOException {
		return createFileIfNoExists(file) == null;
	}

	// public static void main(String[] args) throws Exception {
	// // System.out.println(new File("E:\\img\\tes\\te.txt").isFile());
	// System.out.println(getFileSize(new File("E:\\桌面\\")));
	// // for (File f:new File("E:\\桌面\\detail.jsp").listFiles()){
	// // System.out.println(f.getAbsolutePath());
	// // }
	// }

	/**
	 * 创建新目录，如果父目录不存在，也一并创建。可接受 null 参数
	 * 
	 * @param dir
	 *            目录对象
	 * @return false，如果目录已存在。 true 创建成功
	 * @throws IOException
	 */
	public static boolean makeDir(File file) {
		if (file == null || file.exists())
			return false;
		return file.mkdirs();
	}

	public static long getFileSize(String fileName) throws Exception {
		return getFileSize(new File(fileName));
	}

	/**
	 * 文件或者目录的大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) throws Exception {
		if (isFile(f))
			return f.length();
		long size = 0;
		if (!isDirectory(f))
			return size;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++)
			if (flist[i].isDirectory())
				size = size + getFileSize(flist[i]);
			else
				size = size + flist[i].length();
		return size;
	}

	/**
	 * 文件对象是否是目录，可接受 null
	 */
	public static boolean isDirectory(File f) {
		if (null == f)
			return false;
		if (!f.exists())
			return false;
		if (!f.isDirectory())
			return false;
		return true;
	}

	/**
	 * 文件对象是否是文件，可接受 null
	 */
	public static boolean isFile(File f) {
		return null != f && f.exists() && f.isFile();
	}

	/**
	 * 删除文件或者目录（文件夹）
	 * 
	 * @param f
	 * @return
	 */
	public static boolean delete(File f, FilenameFilter filter) {
		boolean bo = false;
		if (isFile(f))
			bo = f.delete();
		else if (isDirectory(f))
			bo = deleteDir(f, filter);
		return bo;
	}

	/**
	 * 删除一个目录包括目录下所有东西 如果不是目录或为null则返回false
	 * 
	 * @param f
	 * @return
	 */
	public static boolean deleteDir(File f, FilenameFilter filter) {
		if (!isDirectory(f))
			return false;
		boolean re = false;
		File[] fs = f.listFiles(filter);
		if (null != fs) {
			if (fs.length == 0)
				return f.delete();
			for (File file : fs) {
				if (file.isFile())
					re |= deleteFile(file);
				else
					re |= deleteDir(file, filter);
			}
			re |= f.delete();
		}
		return re;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param f
	 * @return
	 */
	public static boolean deleteFile(File f) {
		if (isFile(f))
			return f.delete();
		return false;
	}

	/**
	 * 
	 * 复制文件（不包括目录） 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源文件
	 * @param target目标文件
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(File src, File target) throws IOException {
		return copyFile(src, target, cacheSize);
	}

	/**
	 * 复制文件（不包括目录） 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源文件
	 * @param target目标文件
	 * @param cacheSize缓存大小
	 * @return 是否读取成功
	 * @throws IOException
	 */
	public static boolean copyFile(File src, File target, int cacheSize)
			throws IOException {
		if (!isFile(src) || target == null)
			return false;
		if (!target.exists())
			if (!createNewFile(target))
				return false;
		// 缓冲流
		BufferedInputStream inBuff = new BufferedInputStream(
				new FileInputStream(src));
		BufferedOutputStream outBuff = new BufferedOutputStream(
				new FileOutputStream(target));
		byte[] buff = new byte[cacheSize];
		int len = 0;
		while ((len = inBuff.read(buff)) != -1)
			outBuff.write(buff, 0, len);
		IOUtil.close(inBuff);
		IOUtil.flush(outBuff);
		IOUtil.close(outBuff);
		return target.setLastModified(src.lastModified());
	}

	/**
	 * 复制目录 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源目录
	 * @param target目标目录
	 * @return 是否读取成功
	 * @throws IOException
	 */
	public static boolean copyDir(File src, File target) throws IOException {
		return copyDir(src, target, cacheSize);
	}

	/**
	 * 复制目录 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源目录
	 * @param target目标目录
	 * @param cacheSize缓存大小
	 * @return 是否读取成功
	 * @throws IOException
	 */
	public static boolean copyDir(File src, File target, int cacheSize)
			throws IOException {
		if (src == null || target == null || !src.exists())
			return false;
		if (!src.isDirectory())
			throw new IOException(src.getAbsolutePath()
					+ " should be a directory!");
		if (!target.exists())
			if (!makeDir(target))
				return false;
		boolean re = true;
		File[] files = src.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isFile())
					re &= copyFile(f, new File(target.getAbsolutePath() + "/"
							+ f.getName()), cacheSize);
				else
					re &= copyDir(f, new File(target.getAbsolutePath() + "/"
							+ f.getName()), cacheSize);
			}
		return re;
	}

	/**
	 * 复制文件或者目录 至于复制文件还是目录取决于源文件src 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src源目录
	 * @param target目标目录
	 * @param cacheSize缓存大小
	 * @return 是否读取成功
	 * @throws IOException
	 */
	public static boolean copy(File src, File target, int cacheSize)
			throws IOException {
		if (src == null || target == null || !src.exists())
			return false;
		boolean bo = false;
		if (src.isFile()) {
			bo |= copyFile(src, target, cacheSize);
		} else if (src.isDirectory()) {
			bo |= copyDir(src, target, cacheSize);
		}
		return bo;
	}

	public static boolean copy(File src, File target) throws IOException {
		return copy(src, target, cacheSize);
	}

	/**
	 * 移动一个文件 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @param cacheSize
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(File src, File target, int cacheSize)
			throws IOException {
		if (!isFile(src) || target == null)
			return false;
		makeDir(target.getParentFile());
		boolean bo = src.renameTo(target);
		if (!bo) {
			bo |= copyFile(src, target, cacheSize);
			bo |= deleteFile(src);
		}
		return bo;
	}

	/**
	 * 移动一个文件 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(File src, File target) throws IOException {
		return moveFile(src, target, cacheSize);
	}

	/**
	 * 移动一个文件夹（目录） 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @param cacheSize
	 * @return
	 * @throws IOException
	 */
	public static boolean moveDir(File src, File target, int cacheSize)
			throws IOException {
		if (!isDirectory(src) || target == null)
			return false;
		makeDir(target.getParentFile());
		boolean bo = src.renameTo(target);
		if (!bo) {
			bo |= copyDir(src, target, cacheSize);
			bo |= deleteDir(src, filter);
		}
		return bo;
	}

	/**
	 * 移动一个文件夹（目录） 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static boolean moveDir(File src, File target) throws IOException {
		return moveDir(src, target, cacheSize);
	}

	/**
	 * 移动一个文件夹或者目录 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @param cacheSize
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File src, File target, int cacheSize)
			throws IOException {
		if (src == null || target == null || !src.exists())
			return false;
		if (src.isFile()) {
			return moveFile(src, target, cacheSize);
		} else {
			return moveDir(src, target, cacheSize);
		}
	}

	/**
	 * 移动一个文件夹或者目录 默认如果目标父目录不存在，也一并创建
	 * 
	 * @param src
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File src, File target) throws IOException {
		return move(src, target, cacheSize);
	}

	/**
	 * 将文件改名
	 * 
	 * @param src
	 *            文件
	 * @param newName
	 *            新名称
	 * @return 改名是否成功
	 */
	public static boolean rename(File src, String newName) {
		if (src == null || newName == null)
			return false;
		if (src.exists()) {
			File newFile = new File(src.getParent() + "/" + newName);
			if (newFile.exists())
				return false;
			makeDir(newFile.getParentFile());
			return src.renameTo(newFile);
		}
		return false;
	}

	/**
	 * 修改路径
	 * 
	 * @param path
	 *            路径
	 * @param newName
	 *            新名称
	 * @return 新路径
	 */
	public static String renamePath(String path, String newName) {
		if (!StringUtil.isBlank(path)) {
			int pos = path.replace('\\', '/').lastIndexOf('/');
			if (pos > 0)
				return path.substring(0, pos) + "/" + newName;
		}
		return newName;
	}

	/**
	 * @param path
	 *            路径
	 * @return 父路径
	 */
	public static String getParent(String path) {
		if (StringUtil.isBlank(path))
			return path;
		int pos = path.replace('\\', '/').lastIndexOf('/');
		if (pos > 0)
			return path.substring(0, pos);
		return "/";
	}

	/**
	 * @param path
	 *            全路径
	 * @return 文件或者目录名
	 */
	public static String getName(String path) {
		if (!StringUtil.isBlank(path)) {
			int pos = path.replace('\\', '/').lastIndexOf('/');
			if (pos > 0)
				return path.substring(pos + 1);
		}
		return path;
	}

	/**
	 * 精确比较两个文件是否相等
	 * 
	 * @param f1
	 *            文件1
	 * @param f2
	 *            文件2
	 * @return 是否相等
	 */
	public static boolean isEquals(File f1, File f2) {
		if (!f1.isFile() || !f2.isFile())
			return false;
		InputStream ins1 = null;
		InputStream ins2 = null;
		try {
			ins1 = new BufferedInputStream(new FileInputStream(f1));
			ins2 = new BufferedInputStream(new FileInputStream(f2));
			return IOUtil.equals(ins1, ins2);
		} catch (IOException e) {
			return false;
		} finally {
			IOUtil.close(ins1);
			IOUtil.close(ins2);
		}
	}

	/**
	 * 文件读写流
	 */
	/**
	 * 读取 UTF-8 文件全部内容
	 * 
	 * @param path
	 *            文件路径
	 * @return 文件内容
	 */
	public static String read(String path) {
		return null;
	}

	/**
	 * 读取 UTF-8 文件全部内容
	 * 
	 * @param f
	 *            文件
	 * @return 文件内容
	 */
	public static String read(File f) {
		try {
			return StreamUtil.read(StreamUtil.fileInr(f)).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将内容写到一个文件内，内容对象可以是：
	 * <ul>
	 * <li>InputStream - 按二进制方式写入
	 * <li>byte[] - 按二进制方式写入
	 * <li>Reader - 按 UTF-8 方式写入
	 * <li>其他对象被 toString() 后按照 UTF-8 方式写入
	 * </ul>
	 * 
	 * @param path
	 *            文件路径，如果不存在，则创建
	 * @param obj
	 *            内容对象
	 */
	public static void write(String path, Object obj) {
		if (null == path || null == obj)
			return;
		try {
			write(FileUtil.createFileIfNoExists(path), obj);
		} catch (IOException e) {
			throw Lang.wrapThrow(e);
		}
	}

	/**
	 * 将内容写到一个文件内，内容对象可以是：
	 * 
	 * <ul>
	 * <li>InputStream - 按二进制方式写入
	 * <li>byte[] - 按二进制方式写入
	 * <li>Reader - 按 UTF-8 方式写入
	 * <li>其他对象被 toString() 后按照 UTF-8 方式写入
	 * </ul>
	 * 
	 * @param f
	 *            文件
	 * @param obj
	 *            内容
	 */
	public static void write(File f, Object obj) {
		if (null == f || null == obj)
			return;
		if (f.isDirectory())
			throw Lang.makeThrow("Directory '%s' can not be write as File", f);

		try {
			// 保证文件存在
			if (!f.exists())
				FileUtil.createNewFile(f);
			// 输入流
			if (obj instanceof InputStream) {
				StreamUtil.writeAndClose(StreamUtil.fileOut(f),
						(InputStream) obj);
			}
			// 字节数组
			else if (obj instanceof byte[]) {
				StreamUtil.writeAndClose(StreamUtil.fileOut(f), (byte[]) obj);
			}
			// 文本输入流
			else if (obj instanceof Reader) {
				StreamUtil.writeAndClose(StreamUtil.fileOutw(f), (Reader) obj);
			}
			// 其他对象
			else {
				StreamUtil
						.writeAndClose(StreamUtil.fileOutw(f), obj.toString());
			}
		} catch (IOException e) {
			throw Lang.wrapThrow(e);
		}
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param klass
	 *            参考的类， -- 会用这个类的 ClassLoader
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, Class<?> klass,
			String enc) {
		File f = new File(path);
		if (f.exists())
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e1) {
				return null;
			}
		if (null != klass) {
			InputStream ins = klass.getClassLoader().getResourceAsStream(path);
			if (null == ins)
				ins = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(path);
			if (null != ins)
				return ins;
		}
		return ClassLoader.getSystemResourceAsStream(path);
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, String enc) {
		return findFileAsStream(path, FileUtil.class, enc);
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param klass
	 *            参考的类， -- 会用这个类的 ClassLoader
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, Class<?> klass) {
		return findFileAsStream(path, klass, Encoding.defaultEncoding());
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path) {
		return findFileAsStream(path, FileUtil.class,
				Encoding.defaultEncoding());
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param klassLoader
	 *            参考 ClassLoader
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, ClassLoader klassLoader, String enc) {
		if (null == path)
			return null;
		return new File(path);
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param enc
	 *            文件路径编码
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, String enc) {
		return findFile(path, FileUtil.class.getClassLoader(), enc);
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param klassLoader
	 *            使用该 ClassLoader进行查找
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, ClassLoader klassLoader) {
		return findFile(path, klassLoader, Encoding.defaultEncoding());
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path) {
		return findFile(path, FileUtil.class.getClassLoader(),
				Encoding.defaultEncoding());
	}

	/**
	 * 在文件尾追加数据(关闭流)
	 * 
	 * @param path
	 * @param tempByte
	 */
	public static boolean skipFileAndClose(String path, byte[] tempByte) {
		return skipFileAndClose(new File(path), tempByte);
	}

	/**
	 * 在文件尾追加数据(关闭流)
	 * 
	 * @param file
	 * @param tempByte
	 */
	public static boolean skipFileAndClose(File file, byte[] tempByte) {
		return IOUtil.close(skipFile(file, tempByte));
	}

	/**
	 * 在文件尾追加数据(不关闭流)
	 * 
	 * @param fileName
	 * @param tempByte
	 */
	public static RandomAccessFile skipFile(String fileName, byte[] tempByte) {
		return skipFile(new File(fileName), tempByte);
	}

	/**
	 * 在文件尾追加数据(不关闭流)
	 * 
	 * @param file
	 * @param tempByte
	 * @return
	 */
	public static RandomAccessFile skipFile(File file, byte[] tempByte) {
		if (!isFile(file) || tempByte == null) {
			System.out.println("文件不存在，或者byte字符串为null");
			return null;
		}
		// 打开一个随机访问文件流，按读写方式 如果该文件尚不存在，则尝试创建该文件
		// "rw"可以查aip知道意思 读写的意思
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(file, "rw");
			skipFile(randomFile, tempByte, randomFile.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return randomFile;
	}

	public static boolean skipFileAndClose(String fileName, byte[] tempByte,
			long seek) {
		return IOUtil.close(skipFile(new File(fileName), tempByte, seek));
	}

	public static RandomAccessFile skipFile(String fileName, byte[] tempByte,
			long seek) {
		return skipFile(new File(fileName), tempByte, seek);
	}

	/**
	 * 跳到某个地方添加数据不关闭流
	 * 
	 * @param file
	 * @param tempByte
	 * @param seek
	 *            =跳转到的字节数
	 * @return
	 */
	public static RandomAccessFile skipFile(File file, byte[] tempByte,
			long seek) {
		if (!isFile(file) || tempByte == null) {
			System.out.println("文件不存在，或者byte字符串为null");
			return null;
		}
		// 打开一个随机访问文件流，按读写方式 如果该文件尚不存在，则尝试创建该文件
		// "rw"可以查aip知道意思 读写的意思
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(file, "rw");
			skipFile(randomFile, tempByte, seek);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return randomFile;
	}

	public static boolean skipFileAndClose(File file, byte[] tempByte, long seek) {
		return IOUtil.close(skipFile(file, tempByte, seek));
	}

	/**
	 * 跳到某个地方添加数据不关闭流
	 * 
	 * @param randomFile
	 * @param tempByte
	 * @param seek跳转到的地方
	 * @return
	 */
	public static RandomAccessFile skipFile(RandomAccessFile randomFile,
			byte[] tempByte, long seek) {
		try {
			// 将写文件指针移到文件尾。
			randomFile.seek(seek);
			randomFile.write(tempByte);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return randomFile;
	}

	/**
	 * 跳到某个地方添加数据关闭流
	 * 
	 * @param randomFile
	 * @param tempByte
	 * @param seek
	 */
	public static boolean skipFileAndClose(RandomAccessFile randomFile,
			byte[] tempByte, long seek) {
		return IOUtil.close(skipFile(randomFile, tempByte, seek));
	}

	/**
	 * 大文件处理 写入的文件从position开始 （修改tempBytes个字节的文本）
	 * 
	 * @param file
	 * @param position内存映射的起始位置
	 * @param size
	 *            =内存映射的大小
	 * @param tempBytes
	 *            =要传入的byte[]
	 * @return
	 */
	public static MappedByteBuffer mappedByteBuffer(File file, int position,
			int size, byte[] tempBytes) {
		if (tempBytes == null || !isFile(file) || size < tempBytes.length)
			return null;
		MappedByteBuffer out = null;
		try {
			out = new RandomAccessFile(file, "rw").getChannel().map(
					FileChannel.MapMode.READ_WRITE, position, size);
			out.put(tempBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(deleteDir(new File("G:\\测试 - 副本")));
		// System.out.println(isDirectory(new File("G:\\测试 - 副本")));
		// System.out.println(move(new File("G:\\新建文件夹\\test.sql"), new File(
		// "E:\\新建文件夹副本\\ttt.sql")));
		// System.out.println();
		// System.out.println(new File("E:\\系统").listFiles(filter).length);
		// skipFileAndClose("E:\\测试.txt", "2222".getBytes(),2111L);
		// System.out.println(getFileSize("E:\\array.txt"));
	}
}
