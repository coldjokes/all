package com.exam.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
 
/**
 * 文件解压工具
 * 
 * @author Yifeng Wang  
 */

public class FileUnZipRarUtil {
	
	private static final String EXT_ZIP = "zip";
	private static final String EXT_RAR = "rar";
	private static final int BUFFER_SIZE = 1024;
	
	/**
	 * 解压
	 * @param fileName
	 * @param sourceFile
	 * @param toFolder
	 * @return
	 * @throws Exception
	 */
    public static void zipRarToFile(String fileName, String sourceFile, String toFolder) throws Exception {
        int pos = fileName.lastIndexOf(".");
        String extName = fileName.substring(pos + 1).toLowerCase();
        File pushFile = new File(sourceFile);
        File descFile = new File(toFolder);
        if (!descFile.exists()) {
            descFile.mkdirs();
        }
//        解压目的文件
//        String descDir = toFolder + fileName.substring(0, pos);
        //开始解压zip
        if (extName.equals(EXT_ZIP)) {
        	FileUnZipRarUtil.unZipFiles1(pushFile, toFolder);
        } else if (extName.equals(EXT_RAR)) {
            //开始解压rar
        	FileUnZipRarUtil.unRarFile(pushFile.getAbsolutePath(), toFolder);
        }
    }
 
    /**
     * 解压文件到指定目录
     *基于原生java
     * @param zipFile
     * @param descDir
     * @author isea533
     */
    public static void unZipFiles1(File sourceFile, String descDir) throws IOException {
    	if(!sourceFile.exists()) {
    		throw new RuntimeException(sourceFile.getPath() + "所指文件不存在");
    	}
    	// 开始解压
    	ZipFile zipFile = null;
    	try {
    		zipFile = new ZipFile(sourceFile, "GBK");
    		Enumeration<?> entries = zipFile.getEntries();
    		while (entries.hasMoreElements()) {
    			ZipEntry entry = (ZipEntry) entries.nextElement();
    			 System.out.println("解压" + entry.getName());
    			 // 如果是文件夹，就创建个文件夹
    			 if (entry.isDirectory()) {
    				 String dirPath = descDir + "/" + entry.getName();
    				 File dir = new File(dirPath);
    				 dir.mkdirs();
    			 } else {
    				// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
    				 File targetFile = new File(descDir + "/" + entry.getName());
    				// 保证这个文件的父文件夹必须要存在
    				 if(!targetFile.getParentFile().exists()){
    					 targetFile.getParentFile().mkdirs();
    				 }
    				 targetFile.createNewFile();
    				// 将压缩文件内容写入到这个文件中
    				 InputStream is = zipFile.getInputStream(entry);
    				 FileOutputStream fos = new FileOutputStream(targetFile);
    				 int len;
    				 byte[] buf = new byte[BUFFER_SIZE];
    				 while ((len = is.read(buf)) != -1) {
    					 fos.write(buf, 0, len);
    				 }
    				// 关流顺序，先打开的后关闭
    				 fos.close();
    				 is.close();
    			 }
    		}
    	} catch(Exception e) {
    		 throw new RuntimeException("unzip error from ZipUtils", e);
    	} finally {
    		if(zipFile != null){
    			zipFile.close();
    		}
    	}
    	
    }
    
    /**
     * 解压到指定目录
     *
     * @param zipPath
     * @param descDir
     * @author
     */
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        unZipFiles2(new File(zipPath), descDir);
    }
    /**
     * 解压文件到指定目录
     * 基于第三方ant包
     * @param sourceFile
     * @param descDir
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles2(File sourceFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(sourceFile);
        for (Enumeration entries = zip.getEntries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
            ;
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            //输出文件路径信息
            System.out.println(outPath);
 
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
    }
 
    /**
     * 根据原始rar路径，解压到指定文件夹下.
     *
     * @param srcRarPath       原始rar路径
     * @param dstDirectoryPath 解压到的文件夹
     */
    public static void unRarFile(String srcRarPath, String dstDirectoryPath) {
        if (!srcRarPath.toLowerCase().endsWith(".rar")) {
            System.out.println("非rar文件！");
            return;
        }
        File dstDiretory = new File(dstDirectoryPath);
        if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
            dstDiretory.mkdirs();
        }
        Archive a = null;
        try {
            a = new Archive(new File(srcRarPath));
            if (a != null) {
                a.getMainHeader().print(); // 打印文件信息.
                FileHeader fh = a.nextFileHeader();
                while (fh != null) {
                    if (fh.isDirectory()) { // 文件夹
                        File fol = new File(dstDirectoryPath + File.separator
                                + fh.getFileNameString());
                        fol.mkdirs();
                    } else { // 文件
                        File out = new File(dstDirectoryPath + File.separator
                                + fh.getFileNameString().trim());
                        try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
                            if (!out.exists()) {
                                if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = a.nextFileHeader();
                }
                a.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}