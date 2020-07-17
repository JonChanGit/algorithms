package cn.com.jonpad.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhouwq
 * @Description:
 * @date 2019/11/27.
 */
public class FileUtil {

    /**
     * 保存生成的文件
     *
     * @param content 保存的内容
     * @param filePath
     * @throws IOException
     */
    public static void saveToFile(String content, String filePath) throws IOException {
        FileOutputStream outputStream = null;
        FileChannel outputStreamChannel = null;
        try {
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }
            outputStream = new FileOutputStream(filePath);
            outputStreamChannel = outputStream.getChannel();
            ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(content);

            int length = 0;
            while ((length = outputStreamChannel.write(byteBuffer)) != 0) {
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (outputStreamChannel != null) {
                outputStreamChannel.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 统计相应文件的行数
     * @param absolutePath
     * @return
     */
    public static Integer lineNumber(String absolutePath) throws Exception{
        //定义字符流读取文件
        FileReader fileReader = new FileReader(absolutePath);
        //从字节流中升级为字符流，方便按行读取。
        BufferedReader bufferedReader= new BufferedReader(fileReader);
        int index = 0;
        try {
            while (bufferedReader.readLine()!=null){
                index++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fileReader != null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return index;
        }
    }



    /**
     * 格式化路径
     * @param filePath 原始路径
     * @return 格式化后的路径
     */
    public static String formatFilePath(String filePath) {

        if (filePath == null || filePath.trim().equals("")) {
            return "/";
        }

        // 将反斜杠\全部替换成斜杠/
        filePath = filePath.replaceAll("[\\\\/]+", "/");

        if (!filePath.startsWith("/") && !filePath.contains(":")) {
            filePath = "/" + filePath;
        }

        if (filePath.endsWith("/") && filePath.length() > 1) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }

        return filePath;
    }


    /**
     * 获取相对路径
     * @param filePath 被比较的路径
     * @param parentPath 父路径
     * @return 返回路径相对父路径的子路径
     */
    public static String getRelativePath(String filePath, String parentPath) {
        filePath = formatFilePath(filePath);
        parentPath = formatFilePath(parentPath);

        if (filePath.equals(parentPath)) {
            return "";
        }
        if (filePath.startsWith(parentPath)) {
            return filePath.substring(parentPath.length() + (parentPath.endsWith("/") ? 0 : 1));
        }
        return filePath;
    }



    /**
     * 获取父路径
     * @param filePath
     * @return
     */
    public static String getParentPath(String filePath) {
        filePath =  formatFilePath(filePath);
        if (!filePath.equals("/")) {
            if (filePath.endsWith("/")) {
                filePath = filePath.substring(0, filePath.length() - 1);
            }
            int idx = filePath.lastIndexOf('/');
            if (idx != -1) {
                return filePath.substring(0, idx);
            }
        }
        return null;
    }


    /**
     * 获取文件名（不包含路径）
     * @param filePath
     * @return
     */
    public static String getFileSimpleName(String filePath) {
        filePath = formatFilePath(filePath);
        int idx = filePath.lastIndexOf('/');

        if (idx == -1) {
            return filePath;
        } else {
            return filePath.substring(idx + 1);
        }
    }

    /**
     * 获取文件类型名
     * @param filename 文件名
     * @return
     */
    public static String getFileTypeName(String filename) {
        int idx = filename.lastIndexOf(".");
        if (idx <= 0) {
            return "";
        }
        return filename.substring(idx + 1);
    }

    /**
     * 获取文件名（不含后缀）
     * @return
     */
    public static String getFileName(String fileName){

        return fileName.substring(0,fileName.lastIndexOf("."));
    }
    /**
     * 创建临时文件
     * @return
     * @throws IOException
     */
    public static File createTempFile(String path, String prefix) throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return File.createTempFile(prefix, null, dir);
    }


    /**
     * 递归删除文件
     * @param file 要删除的文件或文件夹
     */
    public static void deleteFile(File file) {
//        LOGGER.info("删除文件：" + file.getAbsolutePath());
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                deleteFile(subFile);
            }
        }
        file.delete();
    }


    /**
     * 递归复制文件夹
     * @param srcDir 源文件夹
     * @param destDir 目标文件夹
     */
/*    public static void copyDirContent(File srcDir, File destDir, boolean isRecursive) throws Exception {
        List<FileInfo> fileList = listFileInfo(srcDir, isRecursive);
        for (FileInfo fileInfo : fileList) {
            String relativePath = getRelativePath(fileInfo.getFilePath(), srcDir.getAbsolutePath());
            copyFile(new File(fileInfo.getFilePath()), new File(destDir + "/" + relativePath));
        }
    }*/


    /**
     * 复制单个文件
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @throws Exception
     */
/*    public static void copyFile(File srcFile, File destFile) throws Exception {
        if (srcFile.isDirectory()) {
            destFile.mkdirs();
        } else {
            writeFile(new FileInputStream(srcFile), destFile, true);
        }
    }*/

    /**
     * 将输入流内容写到文件
     * @param in 输入流
     * @param destFile 目标文件
     * @param isCloseInputStream 是否关闭输入流
     * @throws Exception
     */
    public static void writeFile(InputStream in, File destFile, boolean isCloseInputStream) throws Exception {
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        byte[] buffer = new byte[10240];
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(destFile));
            try {
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            if (isCloseInputStream) {
                in.close();
            }
        }
    }



    /**
     * 查看文件最后多少行内容
     * @param filename 文件名
     * @param lastLine 最后多少行
     * @return
     * @throws Exception
     */
    public static String tailFile(String filename, int lastLine) throws Exception {
        RandomAccessFile file = new RandomAccessFile(filename, "r");

        try {
            if (file.length() == 0 || lastLine <= 0) {
                return "";
            }
            // 最多读取100KB内容
            int maxReadLen = 1024 * 100;

            long pos = file.length();
            int length = 0;
            int line = 0;
            while (pos > 0 && length <= maxReadLen) {
                length++;
                pos--;
                file.seek(pos);
                if (file.readByte() == '\n' && length != 1 && ++line >= lastLine) {
                    break;
                }
            }

            byte[] buffer = new byte[length];
            if (pos == 0) {
                file.seek(0);
            }
            int readLen = file.read(buffer);

            return new String(buffer, 0, readLen);
        } finally {
            file.close();
        }
    }

    /**
     * 递归删除
     * @param path
     * @return
     */
    public static boolean delete(String path){
        File file = new File(path);
        if(!file.exists()){
            return false;
        }
        if(file.isFile()){
            return file.delete();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if(f.isFile()){
                if(!f.delete()){
                    return false;
                }
            }else{
                if(!delete(f.getAbsolutePath())){
                    return false;
                }
            }
        }
        return file.delete();
    }
}
