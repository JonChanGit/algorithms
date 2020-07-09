package cn.com.jonpad.juc;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Print {

  AtomicInteger num = new AtomicInteger(1);
  Lock lock = new ReentrantLock();
  Condition c1 = lock.newCondition();
  Condition c2 = lock.newCondition();
  Condition c3 = lock.newCondition();


  public void print5() {
    lock.lock();
    try {
      while (num.get() != 1) {
        c1.await();
      }

      System.out.println(Thread.currentThread().getName() + ": print5");
      num.set(2);
      c2.signal();

    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public void print10() {
    lock.lock();
    try {
      while (num.get() != 2) {
        c2.await();
      }

      System.out.println(Thread.currentThread().getName() + ": print10");

      num.set(3);
      c3.signal();

    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public void print15() {
    lock.lock();
    try {
      while (num.get() != 3) {
        c3.await();
      }

      System.out.println(Thread.currentThread().getName() + ": print15");

      num.set(1);
      c1.signal();

    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }
}

/**
 * @author Jon Chan
 * @date 2020/7/9 17:12
 */
public class LockConditionTest {
  @Test
  public void test(){
    Print p = new Print();

    new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        p.print5();
      }
    }, "A").start();

    new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        p.print10();
      }
    }, "B").start();

    new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        p.print15();
      }
    }, "C").start();


    while (Thread.activeCount() > 2) {
      // 如果当前线程大于2(Main / GC)交出执行权
      Thread.yield();
    }


  }
}
