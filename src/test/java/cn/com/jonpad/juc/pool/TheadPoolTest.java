package cn.com.jonpad.juc.pool;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
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
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
    "00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip","00001.zip", "00001.zip",
  };

  List<File> testFiles = new ArrayList<>(16);

  public TheadPoolTest() {
    for (String filePath : testFilePaths) {
      testFiles.add(new File(TheadPoolTest.class.getClassLoader().getResource(filePath).getPath()));
    }
  }

  int corePoolSize = 16;
  int maximumPoolSize = 100;
  BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(1000);
  ThreadFactory threadFactory = Executors.defaultThreadFactory();
  RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

  @Test
  public void test() throws Exception {

    ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
      10L, TimeUnit.SECONDS,
      workQueue, threadFactory, handler);

    List<TaskStarter<Integer>> taskStarterList = new LinkedList<>();

    if (!testFiles.isEmpty()) {

      testFiles.forEach(file -> {
        TaskStarterImpl task = new TaskStarterImpl(file, ".*10.4.9.*.");
        taskStarterList.add(task);
      });

    }


    long startTime = System.currentTimeMillis();


    List<Future<Integer>> futures = executor.invokeAll(taskStarterList);

    while (executor.getActiveCount() > 0) {
      System.out.println("》》》》》》》》》》》》》》》当前有 " + executor.getActiveCount() + " 个线程活动中。");
      Thread.yield();
    }

    long endTime = System.currentTimeMillis();

    System.out.println("---------> 耗时： " + (endTime - startTime));


    int sum = futures.stream().mapToInt(value -> {
      while (!value.isDone()) {
      }
      try {
        return value.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
      return 0;
    }).sum();

    System.out.println(Tool.logFormatter("共匹配到 {} 个元素", sum));


  }
}
