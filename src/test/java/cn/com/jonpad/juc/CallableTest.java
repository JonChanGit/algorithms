package cn.com.jonpad.juc;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Callable 接口
 * @author Jon Chan
 * @date 2020/7/9 17:35
 */
public class CallableTest {

  /**
   * Callable 基本用法
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Test
  public void test1() throws ExecutionException, InterruptedException {
    FutureTask task = new FutureTask(() -> 1);

    new Thread(task).start();

    System.out.println(task.get());
  }
}
