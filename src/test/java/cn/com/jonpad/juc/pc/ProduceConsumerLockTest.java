package cn.com.jonpad.juc.pc;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class NumericalBalanceLock {
  int num = 0;

  Lock lock = new ReentrantLock();
  Condition condition = lock.newCondition();

  public void increment() throws InterruptedException {

    lock.lock();
    try {
      while (num != 0){
        condition.await();
      }
      num++;
      System.out.println(Thread.currentThread().getName() + ": " + num);
      condition.signalAll();
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      lock.unlock();
    }
  }

  public void decrement() throws InterruptedException {

    lock.lock();
    try {

      while (num == 0){
        condition.await();
      }
      num --;

      System.out.println(Thread.currentThread().getName() + ": " + num);
      condition.signalAll();

    }finally {
      lock.unlock();
    }
  }
}

/**
 * 多线程生产者消费者测试
 *
 *  Lock 测试
 *
 * @author Jon Chan
 * @date 2020/7/9 15:29
 */
public class ProduceConsumerLockTest {
  @Test
  public void test1() {

    NumericalBalanceLock nb = new NumericalBalanceLock();
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
