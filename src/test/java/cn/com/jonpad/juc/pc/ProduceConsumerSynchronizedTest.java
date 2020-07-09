package cn.com.jonpad.juc.pc;

import org.junit.Test;

class NumericalBalance {
  int num = 0;

  public synchronized void increment() throws InterruptedException {
    while (num != 0) { // 使用While循环 防止虚假唤醒
      this.wait();
    }
    num++;
    System.out.println(Thread.currentThread().getName() + ": " + num);
    this.notifyAll();
  }

  public synchronized void decrement() throws InterruptedException {
    while (num == 0) { // 使用While循环 防止虚假唤醒
      this.wait();
    }
    num--;
    System.out.println(Thread.currentThread().getName() + ": " + num);
    this.notifyAll();
  }
}

/**
 * 多线程生产者消费者测试
 *
 *  Synchronized 测试
 *
 * @author Jon Chan
 * @date 2020/7/9 15:29
 */
public class ProduceConsumerSynchronizedTest {
  @Test
  public void test1() {
    NumericalBalance nb = new NumericalBalance();
    new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        try {
          nb.increment();
        } catch (InterruptedException e) {
        }
      }
    }, "A").start();
    new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        try {
          nb.decrement();
        } catch (InterruptedException e) {
        }
      }
    }, "B").start();

    new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        try {
          nb.increment();
        } catch (InterruptedException e) {
        }
      }
    }, "C").start();
    new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        try {
          nb.decrement();
        } catch (InterruptedException e) {
        }
      }
    }, "D").start();

    System.out.println(Thread.activeCount());
    while (Thread.activeCount() > 2) {
      // 如果当前线程大于2(Main / GC)交出执行权
      Thread.yield();
    }

  }
}
