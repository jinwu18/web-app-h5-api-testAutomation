package com.automation.utils;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.automation.listener.Log4jLogger;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

public class FileUtil {
	
	public static boolean flag = false;
	public static File file = null;
	
	Log4jLogger logger = new Log4jLogger();
	/**
	 * push a file to remote server through smb protocol.
	 * @param remoteUrl:remote server address, it likes smb://ip/folder
	 * @param localFilePath:local file path, this file cannot be folder.
	 * @param username:server machine's username
	 * @param password:server machine's password
	 */
	public void smbPut(String remoteUrl, String localFilePath, String username, String password) {
		InputStream in = null;
		OutputStream out = null;
		File localFile = new File(localFilePath);
		String fileName = localFile.getName();
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(
				"", username, password);
		SmbFile remoteFile;
		try {
			FileInputStream fs = new FileInputStream(localFile);
			int totalSize = fs.available();
			remoteFile = new SmbFile(remoteUrl + "/" + fileName, authentication);
			in = new BufferedInputStream(fs);
			out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
			byte[] buffer = new byte[totalSize];
			in.read(buffer, 0, totalSize);
			out.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				logger.error("Cannot publish this report onlie, detail:"
						+ e.getMessage());
			}
		}
	}

	/**
	 * push a file/folder to remote server through smb protocol.
	 * @param remoteUrl:remote server address, it likes smb://ip/folder
	 * @param localFilePath:local file path, this file can be file/folder.
	 * @param username:server machine's username
	 * @param password:server machine's password
	 */
	public void smbPutFolder(String remoteUrl, String folderPath,
			String username, String password) {
		File localFile = new File(folderPath);
		String fileName = localFile.getName();
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(
				"", username, password);
		SmbFile remoteFile;
		try {
			SmbFile remoteRoot = new SmbFile(remoteUrl, authentication);
			if(!remoteRoot.exists()) {
				remoteRoot.mkdirs();
			}
			remoteFile = new SmbFile(remoteUrl + "/" + fileName, authentication);
			if(localFile.isDirectory()) {
				if(!remoteFile.exists()) {
					remoteFile.mkdir();
				}
				for (File file : localFile.listFiles()) {
					// SmbFile remoteFile = new SmbFile(remoteUrl+"/"+fileName);
					if (file.isDirectory()) {
						smbPutFolder(remoteUrl + "/" + fileName, file.getPath(),
								username, password);
					} else {
						smbPut(remoteUrl + "/" + fileName, file.getPath(), username, password);
					}
				}
			} else {
				smbPut(remoteUrl + "/" + fileName, folderPath, username, password);
			}

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (SmbException e) {
			e.printStackTrace();
		}
	}
	
	public boolean DeleteFolder(String sPath) { 
	    flag = false;  
    	try{
    	    file = new File(sPath);  
    	    // 判断目录或文件是否存在  
    	    if (!file.exists()) {  // 不存在返回 false  
    	    	flag = false; 
    	    } else {  
    	        // 判断是否为文件  
    	        if (file.isFile()) {  // 为文件时调用删除文件方法  
    	        	flag =  deleteFile(sPath);  
    	        } else {  // 为目录时调用删除目录方法  
    	        	flag =  deleteDirectory(sPath);  
    	        }  
    	    }  
    	}catch(Throwable e){
    		e.printStackTrace();
    		logger.error(e);
    	}
    	return flag;
	}  
	
    /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public boolean deleteFile(String sPath) {  
    	flag = false;  
    	try{
            
            file = new File(sPath);  
            // 路径为文件且不为空则进行删除  
            if (file.isFile() && file.exists()) {  
                file.delete();  
                flag = true;  
            } else {
            	String[] filePathArray = sPath.split("\\\\");
            	String fileName = filePathArray[filePathArray.length-1];
            	String filePath = sPath.replace(fileName, "");
            	File[] fileList = fileListGet(filePath);
            	for(File file:fileList){
            		if(file.getName().contains(fileName + ".")){
            			file.delete();
            		}
            	}
            }
    	}catch(Throwable e){
    		e.printStackTrace();
    		logger.error(e);
    	}
    	return flag;  
    }  
	
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public boolean deleteDirectory(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    flag = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            flag = deleteFile(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    } 
	} 
	
	public File[] fileListGet(String folderPath) {
		File folder = new File(folderPath);
		return folder.listFiles();
	}
	
	public List<String> allDirPathList = new ArrayList<String>();
    /**
     * 获取本地路径所有文件路径
     * @param testFilePath
     * @throws IOException
     */
    public List<String> allDirListGet(String testFilePath) throws IOException { 	
    	File[] fileArray = (new File(testFilePath)).listFiles();
    	for(int i=0; i<fileArray.length; i++){
    		if(fileArray[i].isDirectory()){
    			dirListGet(fileArray[i].getAbsolutePath());
    			allDirPathList.add(fileArray[i].getAbsolutePath());
    		}
    	}
    	return allDirPathList;
    }
	
    /**
     * 获取本地路径所有文件路径
     * @param testFilePath
     * @throws IOException
     */
    public List<String> dirListGet(String testFilePath) throws IOException { 	
    	File[] fileArray = (new File(testFilePath)).listFiles();
    	List<String> dirPathList = new ArrayList<String>();
    	for(int i=0; i<fileArray.length; i++){
    		if(fileArray[i].isDirectory()){
    			dirListGet(fileArray[i].getAbsolutePath());
    			dirPathList.add(fileArray[i].getAbsolutePath());
    		}
    	}
    	return dirPathList;
    }
	
	public List<String> localFilePathList = new ArrayList<String>();
    /**
     * 获取本地路径所有文件路径
     * @param testFilePath
     * @throws IOException
     */
    public List<String> dirFileListGet(String testFilePath) throws IOException { 	
    	File[] fileArray = (new File(testFilePath)).listFiles();
    	for(int i=0; i<fileArray.length; i++){
    		if(fileArray[i].isDirectory()){
    			dirFileListGet(fileArray[i].getAbsolutePath());
    		}else{
    			String fileFullPath = fileArray[i].getAbsolutePath();
    			localFilePathList.add(fileFullPath);
    			System.out.println("本地文件：" + fileFullPath);
    		}
    	}
    	return localFilePathList;
    }
	/** 
	 * 获取指定文件目录下文件个数
	 * @param   sPath 目录的文件路径 
	 * @return  返回目录下文件个数 
	 */  
	public int fileListSize(String sPath) {
		int fileNumber = 0;
		try{
		    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
		    if (!sPath.endsWith(File.separator)) {  
		        sPath = sPath + File.separator;  
		    }  
		    File dirFile = new File(sPath);  
		    //如果dir对应的文件不存在，或者不是一个目录，则退出  
		    if (!dirFile.exists() || !dirFile.isDirectory()) {  
		        return fileNumber;  
		    } else {
		    	fileNumber = dirFile.listFiles().length;
		    } 
		}catch (Exception e) {
    		e.printStackTrace();
    		logger.error(e);
		}
		return fileNumber;
	} 
	
    /** 
     * 递归删除某目录下的所有文件及子目录下所有文件，保留该目录
     * @param   sPath    所需清空文件的根目录 
     * @author Jerry Qi  Febr 16, 2017
     * @return 删除成功返回true，否则返回false 
     */  
    public boolean deleteFilesUnderFolder(String sPath) {  
    	try {
            //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
    	    if (!sPath.endsWith(File.separator)) {  
    	        sPath = sPath + File.separator;  
    	    }  
    	    File dirFile = new File(sPath);  
    	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
    	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
    	        return false;  
    	    }  
    	    flag = true;  
    	    //删除文件夹下的所有文件(包括子目录)  
    	    File[] files = dirFile.listFiles();  
    	    for (int i = 0; i < files.length; i++) {  
    	        //删除子文件  
    	        if (files[i].isFile()) {  
    	            flag = deleteFile(files[i].getAbsolutePath());  
    	            if (!flag) break;  
    	        } //删除子目录  
    	        else {  
    	            flag = deleteDirectory(files[i].getAbsolutePath());  
    	            if (!flag) break;  
    	        }  
    	    }
		} catch (Exception e) {
    		e.printStackTrace();
    		logger.error(e);
		}
 
	    if (!flag) return false;  
	    else {
			return true;
		}
    }  
	
	public void folderCreate(String folderName) {
    	try{
    		String[] folderArray = folderName.split("\\\\");
    		String tempFolder = null;
    		for(int i=0; i<folderArray.length; i++){
    			if(i==0){
    				tempFolder = folderArray[0];
    			}else{
    				tempFolder += "\\" + folderArray[i];
    			}
    			if(!(new File(tempFolder).exists())){
    				(new File(tempFolder)).mkdirs();
    			}
    		}
//    		File folder = new File(folderName);
//    		if(!folder.exists()){
//    			folder.mkdir();
//    		}
    	}catch(Throwable e){
    		e.printStackTrace();
    		logger.error(e);
    	}

	}
	
	/**
	 * 删除指定目录下，excludeList以外的所有文件及文件夹
	 * @param filePath
	 * @param excludeList
	 */
	public void deleteAll(String sPath, List<String> excludeList){
    	try{
    	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
    	    if (!sPath.endsWith(File.separator)) {  
    	        sPath = sPath + File.separator;  
    	    }  
    	    File dirFile = new File(sPath);  
    	    //删除文件夹下的所有文件(包括子目录)  
    	    File[] files = dirFile.listFiles();  
    	    for (int i = 0; i < files.length; i++) {  
    	    	//删除子目录  
    	    	String subFolderName = files[i].toString().replace(sPath, "");
    	    	if(!excludeList.contains(subFolderName)){
    	    		deleteDirectory(files[i].getAbsolutePath()); 
    	    		logger.info("删除文件夹" + subFolderName);
    	    	}
    	    }  
    	}catch(Throwable e){
    		e.printStackTrace();
    		logger.error(e);
    	}

	}
	
	public void deleteAll(String filePath){
    	try{
    		File file = new File(filePath);
    		if (!file.isDirectory()) {
    			file.delete();
//    			System.out.println("file deleted:" + file.getName());
    		} else {
//    			System.out.println("folder find:" + filePath);
    			String[] fList = file.list();
    			for (String f : fList) {
//    				System.out.println("file find:" + f);
    				deleteAll(filePath + "\\" + f);
    			}
//    			System.out.println("folder deleted:" + filePath);
    			file.delete();
    		}
    	}catch(Throwable e){
    		e.printStackTrace();
    		logger.error(e);
    	}

	}
	
	/**
	 * 删除路径下含有匹配文件名的文件,不区分大小写
	 * @param path 文件路径， 如 "D:\\autoItDownload"
	 * @param partFileName 部分文件名
     * @author Jerry Qi  08/28/2017
	 * @return Boolean
	 */
	 public Boolean delFileByPartName(String path,String partFileName){
		Boolean result = false;
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		File file = new File(path);
		File[] tempFile = file.listFiles();
		for (int i = 0; i < tempFile.length; i++) {
			String fileName = tempFile[i].getName();
			if (fileName.toLowerCase().contains(partFileName.toLowerCase())) {
				Boolean deleted = deleteFile(path + fileName);
				if (deleted) {
					System.out.println("文件" + fileName + "删除成功");
					result = true;
				} else {
					System.out.println("文件" + fileName + "删除失败");
				}
			}
		}
		return result;
	}

    public void fileCopy(String from, String to) {
    	try{
        	File fromFile = new File(from);
        	if(fromFile.exists() && fromFile.isFile()){
                FileInputStream fi = null;
                FileOutputStream fo = null;
                FileChannel in = null;
                FileChannel out = null;
                try {
                    fi = new FileInputStream(new File(from));
                    fo = new FileOutputStream(new File(to));
                    in = fi.getChannel();
                    out = fo.getChannel();
                    in.transferTo(0, in.size(), out);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fi.close();
                        in.close();
                        fo.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        	}
    	}catch(Exception ex){
    		
    	}
    }

    public boolean fileExistedChk(String filePath){
    	File file = new File(filePath);
    	return file.exists();
    }
    
	public void copyAll(String filePath, String toFolder){
    	try{
    		File file = new File(filePath);
    		if (!file.isDirectory()) {
    			fileCopy(filePath, toFolder + File.separator + file.getName());
//    			System.out.println("file copy:" + file.getName());
    		} else {
//    			System.out.println("folder find:" + filePath);
    			String newFolderPath = toFolder + File.separator + file.getName();
    			File newFolder = new File(newFolderPath);
    			newFolder.mkdir();
    			String[] fList = file.list();
    			for (String f : fList) {
//    				System.out.println("file find:" + f);
    				copyAll(filePath + File.separator + f, newFolderPath);
    			}
    		}
    	}catch(Throwable e){
    		e.printStackTrace();
    		logger.error(e);
    	}

	}

	public File doZip(String sourceDir, String zipFilePath) throws IOException {
		File file = new File(sourceDir);
		File zipFile = new File(zipFilePath);
		ZipOutputStream zos = null;
		try {
			OutputStream os = new FileOutputStream(zipFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			zos = new ZipOutputStream(bos);
			String basePath = null;

			if (file.isDirectory()) {
				basePath = file.getPath();
			} else {
				basePath = file.getParent();
			}
			zipFile(file, basePath, zos);
		} finally {
			if (zos != null) {
				zos.closeEntry();
				zos.close();
			}
		}
		return zipFile;
	}

	private void zipFile(File source, String basePath, ZipOutputStream zos) throws IOException {
		InputStream is = null;
		String pathName;
		byte[] buf = new byte[1024];
		int length = 0;
		try {
			File[] files = null;
			if (source.isDirectory()) {
				files = source.listFiles();
			} else {
				files = new File[1];
				files[0] = source;
			}
			
			for (File file : files) {
				if (file.isDirectory()) {
					pathName = file.getPath().substring(basePath.length() + 1)
							+ "/";
					zos.putNextEntry(new ZipEntry(pathName));
					zipFile(file, basePath, zos);
				} else {
					pathName = file.getPath().substring(basePath.length() + 1);
					is = new FileInputStream(file);
					@SuppressWarnings("resource")
					BufferedInputStream bis = new BufferedInputStream(is);
					zos.putNextEntry(new ZipEntry(pathName));
					while ((length = bis.read(buf)) > 0) {
						zos.write(buf, 0, length);
					}
				}
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}

	}
	
	public String fileSizeGet(String filePath) throws IOException {
		File newFile = new File(filePath);
		String fileSize = null;
		if(newFile.exists()){
			FileInputStream fis = new FileInputStream(newFile);
			fileSize = String.valueOf(newFile.length());
			fis.close();
		}
		return fileSize;
	}
	
	public byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for(int i=0; i<b.length; i++){
			b[i] = new Long(temp & 0xff).byteValue();
		}
		return b;
	}
	
	/**
	 * 把上传路径写入文件
	 * @param uploadFile
	 * @param iniFilePath
	 * @throws IOException
	 */
	public void fileCreate(String filePath) throws IOException {
		try{
			File newFile = new File(filePath);
			if(!newFile.exists()){
				newFile.createNewFile();
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 把上传路径写入文件
	 * @param uploadFile
	 * @param iniFilePath
	 * @throws IOException
	 */
	public void iniFileWrite(String uploadFile, String iniFilePath) throws IOException {
		BufferedWriter fileBuffer = null;
		try{
			fileBuffer = new BufferedWriter(new FileWriter(iniFilePath));
			fileBuffer.write("[uploadFilePath]" + System.getProperty("line.separator"));
			fileBuffer.write("uploadFilePath=" + uploadFile);
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			fileBuffer.flush();
			fileBuffer.close();
		}
	}	
    
	public void fileWrite(String filePath, String fileContent) throws IOException {
		BufferedWriter fileBuffer = new BufferedWriter(new FileWriter(filePath));;
		try{
			fileBuffer.write(fileContent);
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			fileBuffer.flush();
			fileBuffer.close();
		}
	}
	
	public List<String> readFileByLine(String path) {
		List<String> strList = new ArrayList<String>();
		try{
			File file = new File(path);
			BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			int i=1;
			while((temp = reader.readLine())!=null){
				System.out.println("line" + i + ":" + temp);
				strList.add(temp);
				i++;
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return strList;
	}
	
	public String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	/**
	 * 获取文件夹下最新的文件名全路径
	 * @param pathName 文件夹路径，如 D:\Downloads
	 * @return String 
	 * @throws IOException
	 */
	public String getLatestFileInFolder(String pathName) throws IOException {
	    // 设置日期转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");            

        File file = new File(pathName);
        // 定义文件修改时间
        long modify_time = 0;
        long tmp = 0;
        String fileName = "";
		try {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					modify_time = Long.parseLong(sdf.format(new Date(files[i]
							.lastModified())));
					if (modify_time > tmp) {
						fileName = files[i].getName();
					}
					tmp = modify_time;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
        return pathName + File.separator + fileName;
	}

	
	public static void main(String[] args) throws IOException {
		//todo
	}
}
