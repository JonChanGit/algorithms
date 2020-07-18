package cn.com.jonpad.util;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * @author zhouwq
 * @Description: 解压缩工具类
 * @date 2019/11/27.
 */
public class CompressUtil {
    public static final String DECOMPRESS_TEMP_DIR = ".[tmp]";
    /**
     * 支持的压缩文件类型
     */
    private static Set<String> supportedCompressTypes = new HashSet<String>(Arrays.asList("zip", "gzip", "gz", "tar", "rar", "bz2"));


    /**
     * 是否是可支持的压缩文件类型
     * @param filename 文件名
     * @return
     */
    public static boolean isSupportedCompressFile(String filename) {
        return supportedCompressTypes.contains(FileUtil.getFileTypeName(filename).toLowerCase());
    }

    static public List<File> decompressFile(File file, String fileNameEncoding) {
        List<File> result = new ArrayList<>();
        if (!file.isFile()) {
            return result;
        }
        //判断是否为支持的压缩类型
        if (!CompressUtil.isSupportedCompressFile(file.getName())) {
            result.add(file);
            return result;
        }

        // 解压文件临时目录
        String tempDir = file.getAbsolutePath() + DECOMPRESS_TEMP_DIR;
        try
        {
            new File(tempDir).mkdir();
        }
        catch (Exception e)
        {

        }
        List<File> subFileList = null;
        try {
            subFileList = CompressUtil.decompressFile(file, tempDir, fileNameEncoding);
            // 解压完毕后删除该压缩文件，释放磁盘空间
            if (file.getAbsolutePath().contains(DECOMPRESS_TEMP_DIR)) {
                if (!file.delete()) {
                    //删除解压中间临时文件失败
                  System.out.println("删除解压中间临时文件失败");
                }
            }
        } catch (Exception e) {
            //解压出错，当做非压缩文件处理
          System.out.println("解压出错");
            result.add(file);
        }
        if (subFileList != null) {
            for (File subFile : subFileList) {
                result.addAll(decompressFile(subFile, fileNameEncoding));
            }
        }
        return result;
    }
    /**
     * 解压文件
     * @param srcFile 原始文件
     * @param destDir 目标路径
     * @param filenameEncoding 文件名编码
     * @return 解压后文件列表
     * @throws Exception
     */
    public static List<File> decompressFile(File srcFile, String destDir, String filenameEncoding) throws Exception {
        String fileType = FileUtil.getFileTypeName(srcFile.getName()).toLowerCase();
        if (!isSupportedCompressFile(srcFile.getName())) {
            //不支持的文件类型
            throw new Exception("不资瓷的文件类型");
        }

        String decompressMethod = "un" + fileType;
        for (Method method : CompressUtil.class.getMethods()) {
            if (method.getName().equals(decompressMethod)) {
                return (List<File>) method.invoke(null, srcFile, destDir, filenameEncoding);
            }
        }
        //代码错误：不能找到解压方法
        throw new Exception("代码错误：不能找到解压方法");
    }

    /**
     * 解压缩zip文件
     */
    public static List<File> unzip(File srcFile, String destDir, String filenameEncoding) throws Exception {


        List<File> result = new ArrayList<File>();

        try {
            ZipFile zipFile = new ZipFile(srcFile, filenameEncoding);
            try {
                Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
                while (entries.hasMoreElements()) {
                    ZipArchiveEntry entry = entries.nextElement();
                    File destFile = new File(destDir + File.separator + entry.getName());
                  // System.out.println("unzip -> " + entry.getName());
                    try {
                        if (entry.isDirectory()) {
                            destFile.mkdirs();
                        } else {
                            FileUtil.writeFile(zipFile.getInputStream(entry), destFile, true);
                        }
                        result.add(destFile);
                    } catch (Exception e) {
                        //从[" + srcFile + "]中解压[" + entry.getName() + "]出错
                      e.printStackTrace();
                    }
                }
            } finally {
                closeStream(zipFile);
            }

        } catch (IOException e) {
            //解压文件[" + srcFile + "]出错
          e.printStackTrace();
        }
        return result;
    }

    /**
     * 解压TAR文件
     */
    public static List<File> untar(File srcFile, String destDir, String filenameEncoding) {

        List<File> result = new ArrayList<File>();

        InputStream in = null;
        TarArchiveInputStream tarIn = null;

        try {
            in = new FileInputStream(srcFile);
            tarIn = new TarArchiveInputStream(in, filenameEncoding);
            TarArchiveEntry entry = null;
            while ((entry = tarIn.getNextTarEntry()) != null) {
                File destFile = new File(destDir + File.separator + entry.getName());
//                LOGGER.info("untar -> " + entry.getName());
                if (entry.isDirectory()) {// 是目录
                    destFile.mkdirs();
                } else {// 是文件
                    FileUtil.writeFile(tarIn, destFile, false);
                }
                result.add(destFile);
            }
        } catch (Exception e) {
          e.printStackTrace();
        }  finally {
            /*------HW------*/
            if (tarIn != null) {
                try {
                    tarIn.close();
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
//            closeStream(tarIn, in);
        }
        return result;
    }

    /**
     * 解压GUNZip文件
     */
    public static List<File> ungzip(File srcFile, String destDir, String filenameEncoding) {
        return ungz(srcFile, destDir, filenameEncoding);
    }

    /**
     * 解压GUNZip文件
     */
    public static List<File> ungz(File srcFile, String destDir, String filenameEncoding) {

        List<File> result = new ArrayList<File>();
        InputStream in = null;
        GZIPInputStream gzipIn = null;
        try {
            in = new FileInputStream(srcFile);
            gzipIn = new GZIPInputStream(in);

            String filename = srcFile.getName().substring(0, srcFile.getName().lastIndexOf('.'));
            File destFile = new File(destDir + File.separator + filename);
            FileUtil.writeFile(gzipIn, destFile, true);
            result.add(destFile);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            /*------HW------*/
            if (gzipIn != null) {
                try {
                    gzipIn.close();
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
//            closeStream(gzipIn, in);
        }

        return result;
    }

    /**
     * 解压BZ2文件
     */
    public static List<File> unbz2(File srcFile, String destDir, String filenameEncoding) throws Exception {

        List<File> result = new ArrayList<File>();
        InputStream in = null;
        BZip2CompressorInputStream gis = null;
        try {
            in = new FileInputStream(srcFile);
            gis = new BZip2CompressorInputStream(in);
            String filename = srcFile.getName().substring(0, srcFile.getName().lastIndexOf('.'));
            File destFile = new File(destDir + File.separator + filename);
            FileUtil.writeFile(gis, destFile, true);
            result.add(destFile);
        } catch (IOException e) {
            throw new Exception("");
        } finally {
            /*------HW------*/
            if (gis != null) {
                try {
                    gis.close();
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
//            closeStream(gis, in);
        }
        return result;
    }

    /**
     * 解压RAR文件
     */
    public static List<File> unrar(File srcFile, String destDir, String filenameEncoding) {

        List<File> result = new ArrayList<File>();
        try {
            Archive archive = null;
            FileOutputStream fos = null;
            try {
                archive = new Archive(srcFile);
                FileHeader fh = null;
                while ((fh = archive.nextFileHeader()) != null) {
                    File destFile = new File(destDir + File.separator + fh.getFileNameString().trim());
                    if (fh.isDirectory()) {
                        destFile.mkdirs();
                    } else {
                        fos = new FileOutputStream(new File(destDir));
                        archive.extractFile(fh, fos);
                        fos.close();
                    }
                }
            } finally {
                closeStream(archive);
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return result;
    }


    /**
     * 关闭流操作
     * @param streams
     */
    private static void closeStream(Closeable... streams) {
        for (Closeable stream : streams) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
        }
    }
}
