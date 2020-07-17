package cn.com.jonpad.juc.pool;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程压测
 *
 * @author Jon Chan
 * @date 2020/7/16 22:28
 */
public class TheadPoolTest {

  /**
   * 测试文件
   */
  String[] testFilePaths = new String[]{
    "00001.zip",
  };

  List<File> testFiles = new ArrayList<>(16);

  public TheadPoolTest() {
    for (String filePath : testFilePaths) {
      testFiles.add(new File(TheadPoolTest.class.getClassLoader().getResource(filePath).getPath()));
    }
  }

  int corePoolSize = 1;
  int maximumPoolSize = 1;
  BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);
  ThreadFactory threadFactory = Executors.defaultThreadFactory();
  RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

  @Test
  public void test() throws Exception {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
      10L, TimeUnit.SECONDS,
      workQueue, threadFactory, handler);

    if(!testFiles.isEmpty()){

      testFiles.forEach( file -> executor.submit(new TaskStarterImpl(file)));

    }


    TimeUnit.SECONDS.sleep(5);
    while (executor.getActiveCount() > 0) {
      Thread.yield();
    }

  }
}
