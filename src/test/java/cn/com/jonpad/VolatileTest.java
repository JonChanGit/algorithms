package cn.com.jonpad;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class TestData {
  volatile int num = 0;

  public void addTo60() {
    num = 60;
    System.out.println(Thread.currentThread().getName() + ": num val addTo60 complete");
  }

  public void addPP() {
    num++;
  }

  AtomicInteger aNum = new AtomicInteger();

  public void aNumPP() {
    aNum.getAndIncrement();
  }
}

/**
 * @author Jon Chan
 * @date 2020/7/9 14:28
 */
public class VolatileTest {


  /**
   * Volatile 可见性测试
   * num 没有 volatile 修饰，会导致无法退出While循环
   */
  @Test
  public void testVolatileSeeOk() {

    TestData d = new TestData();

    new Thread(() -> {
      d.addTo60();
    }, "testThread").start();

    while (d.num != 60) {
      // 等待变更
    }

    System.out.println(Thread.currentThread().getName() + ": num val addTo60 complete");
  }


  /**
   * volatile 无原子性测试
   */
  @Test
  public void testVolatileNonAtomic() {

    TestData d = new TestData();

    for (int i = 0; i < 20; i++) {
      new Thread(() -> {
        // System.out.println(Thread.currentThread().getName() + " - begin :" + d.num);
        for (int i1 = 0; i1 < 1000; i1++) {
          d.addPP();
        }
        // System.out.println(Thread.currentThread().getName() + " - end :" + d.num);
      }, "subThread" + i).start();
    }
    while (Thread.activeCount() > 2) {
      // 如果当前线程大于2(Main / GC)交出执行权
      Thread.yield();
    }

    System.out.println("complete finally num is :" + d.num);
  }


  /**
   * 解决 volatile 无原子性测试
   * 1. 使用synchronization 关键字
   * 2. 使用JUC包的Atomic相关类
   */
  @Test
  public void testVolatileFixedNonAtomic(){
    TestData d = new TestData();

    for (int i = 0; i < 20; i++) {
      new Thread(() -> {
        for (int i1 = 0; i1 < 1000; i1++) {
          d.aNumPP();
        }
      }, "subThread" + i).start();
    }
    while (Thread.activeCount() > 2) {
      // 如果当前线程大于2(Main / GC)交出执行权
      Thread.yield();
    }

    System.out.println("complete finally num is :" + d.aNum);
  }
}
