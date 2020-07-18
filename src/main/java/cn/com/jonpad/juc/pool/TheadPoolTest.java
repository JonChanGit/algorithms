package cn.com.jonpad.juc.pool;

import lombok.Data;
import lombok.Getter;

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
@Getter
public class TheadPoolTest {

  /**
   * 测试文件
   */
  String[] testFilePaths = new String[]{
    // "00001.zip",
    // 0
    "00001.zip","00002.zip","00003.zip","00004.zip","00005.zip","00006.zip","00007.zip","00008.zip","00009.zip","00010.zip",
    "00011.zip","00012.zip","00013.zip","00014.zip","00015.zip","00016.zip","00017.zip","00018.zip","00019.zip","00020.zip",
    "00021.zip","00022.zip","00023.zip","00024.zip","00025.zip","00026.zip","00027.zip","00028.zip","00029.zip","00030.zip",
    "00031.zip","00032.zip","00033.zip","00034.zip","00035.zip","00036.zip","00037.zip","00038.zip","00039.zip","00040.zip",
    "00041.zip","00042.zip","00043.zip","00044.zip","00045.zip","00046.zip","00047.zip","00048.zip","00049.zip","00050.zip",
    "00051.zip","00052.zip","00053.zip","00054.zip","00055.zip","00056.zip","00057.zip","00058.zip","00059.zip","00060.zip",
    "00061.zip","00062.zip","00063.zip","00064.zip","00065.zip","00066.zip","00067.zip","00068.zip","00069.zip","00070.zip",
    "00071.zip","00072.zip","00073.zip","00074.zip","00075.zip","00076.zip","00077.zip","00078.zip","00079.zip","00080.zip",
    "00081.zip","00082.zip","00083.zip","00084.zip","00085.zip","00086.zip","00087.zip","00088.zip","00089.zip","00090.zip",
    "00091.zip","00092.zip","00093.zip","00094.zip","00095.zip","00096.zip","00097.zip","00098.zip","00099.zip","00100.zip",
    /*// 100
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
    // 200
*/
  };

  List<File> testFiles = new ArrayList<>(16);

  public TheadPoolTest(int corePoolSize, int maximumPoolSize ) {
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;

    for (String filePath : testFilePaths) {
      testFiles.add(new File(System.getProperty("user.dir")+"/"+ filePath));
      // testFiles.add(new File(TheadPoolTest.class.getClassLoader().getResource(filePath).getPath()));
    }

    this.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
      10L, TimeUnit.SECONDS,
      workQueue, threadFactory, handler);

  }

  int corePoolSize;
  int maximumPoolSize;
  BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1000);
  ThreadFactory threadFactory = Executors.defaultThreadFactory();
  RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
  ThreadPoolExecutor executor;


  public static void main(String[] args) throws Exception {

    byte[] b = new byte[1024];

    while (true){
      System.out.println("请输入线程数：");

      int n = System.in.read(b);
      int poolSize = Integer.parseInt(new String(b,0,n).trim());
      TheadPoolTest test = new TheadPoolTest(poolSize, poolSize);

      System.out.println("当前线程信息：");
      System.out.println("CorePoolSize："+test.getCorePoolSize()+" \t MaximumPoolSize: " + test.getMaximumPoolSize());

      for (int i = 0; i < 10; i++) {
        System.out.println("==============开始第"+(i+1)+"轮执行==============");
        test.test();
        System.out.println("ActiveCount："+test.getExecutor().getActiveCount()+
          " \t CompletedTaskCount: " + test.getExecutor().getCompletedTaskCount()+
          " \t TaskCount: " + test.getExecutor().getTaskCount());
      }
      System.out.println("__________________执行结束___________________");
      TimeUnit.SECONDS.sleep(2L);
    }

  }

  public void test() throws Exception {

    // TimeUnit.SECONDS.sleep(10L);


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
        Thread.yield();
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
