package cn.com.jonpad.juc.pool;

import cn.com.jonpad.util.CompressUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Jon Chan
 * @date 2020/7/17 09:04
 */
public class TaskStarterImpl implements TaskStarter<Integer> {

  File file;

  String destDir;

  String pattern;

  public TaskStarterImpl(File file, String pattern) {
    this.file = file;
    this.pattern = pattern;
    this.destDir =  System.getProperty("user.dir")  + "/" + RandomUtil.randomString(8);
  }

  @Override
  public Integer call() throws Exception {
    int count = 0;
    try {
      System.out.println(Tool.logFormatter("线程开始"));
      List<File> files = CompressUtil.decompressFile(this.file, this.destDir, "utf8");
      System.out.println(Tool.logFormatter("需要处理的文件个数： 『{}』", files.size()));

      for (File f : files) {
        count += fileProcessing(f);
      }


      System.out.println(Tool.logFormatter("线程结束"));
    }catch (Exception e){
      e.printStackTrace();
    }
    return count;
  }

  /**
   * 处理文件数据
   *
   * @param file
   * @return
   * @throws Exception
   */
  protected int fileProcessing(File file) throws Exception {
    int count = 0;
    FileReader fr = null;
    BufferedReader br = null;
    try {
      fr = new FileReader(file);
      br = new BufferedReader(fr);

      String line = null;
      while ((line = br.readLine()) != null) {
        if (StrUtil.isNotEmpty(line)) {
          if (Pattern.matches(pattern, line)) {
            count++;
          /*} else {
            for (String word : line.split(" ")) {
              if (Pattern.matches(word, line)) {
                count++;
              }
            }*/
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        br.close();
      }
      if (fr != null) {
        fr.close();
      }
    }
    return count;
  }


}
