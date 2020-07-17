package cn.com.jonpad.juc.pool;

import cn.com.jonpad.util.CompressUtil;
import cn.hutool.core.util.RandomUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Jon Chan
 * @date 2020/7/17 09:04
 */
public class TaskStarterImpl implements TaskStarter<String>{

  File file;

  String destDir;

  public TaskStarterImpl(File file) {
    this.file = file;
    this.destDir = "/Users/jonchan/tmp/GC/"+ RandomUtil.randomString(8);
  }

  @Override
  public String call() throws Exception {
    System.out.println(Tool.logFormatter("线程开始"));
    List<File> files = CompressUtil.decompressFile(this.file, this.destDir, "utf8");
    System.out.println(Tool.logFormatter("线程结束, 文件个数： 『{}』", files.size()));
    return null;
  }
}
