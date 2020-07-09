package cn.com.jonpad.juc.sync_lock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;


/**
 * synchronized 实现同步的基础：Java每个对象都可以作为锁。
 * 具体表现为以下3种形式。
 * 1. 对于实例方法，锁的是当前对象。
 * 2. 对于同步方法块，锁的时synchronized括号里面的对象
 * 3. 对于静态同步方法，锁的时当前类的Class对象。
 */
class Phone {
  public synchronized void sendEMail() {
    System.out.println(Thread.currentThread().getName() + ": 邮件");
  }

  public void sendEMailSleep() throws Exception {
    synchronized (this){

      TimeUnit.SECONDS.sleep(4);
      System.out.println(Thread.currentThread().getName() + ": 邮件 sleep 4 ");
    }
  }

  public static synchronized void sendEMailStaticSleep() throws Exception {
    TimeUnit.SECONDS.sleep(4);
    System.out.println(Thread.currentThread().getName() + ": 邮件 sleep 4 ");
  }

  public synchronized void sendSMS() {
    System.out.println(Thread.currentThread().getName() + ": 短信");
  }

  public static synchronized void sendSMSStatic() {
    System.out.println(Thread.currentThread().getName() + ": 短信");
  }

  public void sayHi() {
    System.out.println(Thread.currentThread().getName() + ": Hi!");
  }

}

/**
 * @author Jon Chan
 * @date 2020/7/9 10:26
 */
public class LockTest {
  Phone p1 = new Phone();
  Phone p2 = new Phone();

  @Test
  public void test() throws InterruptedException {
    for (int i = 0; i < 100; i++) {
      test1();
      System.out.println("---------------------------------");
    }
  }

  /**
   * sendEMail()与 sendSMS()都是方法锁，进入方法锁时将锁定整个对象实例
   * <p>
   * 一个对象里面如果有多个synchronization方法，某一时可内，只要一个线程去调用其中的一个synchronization方法，其他线程都会阻塞
   * 即：
   * 某一时可有且只有一个线程访问同一实例的synchronization方法
   *
   * A、B、C三个线程是随机执行(A先执行的几率高)
   *
   * @throws InterruptedException
   */
  @Test
  public void test1() throws InterruptedException {

    new Thread(() -> {
      try {
        p1.sendEMail();
      } finally {
        System.out.println("A finally");
      }
    }, "A").start();
    new Thread(() -> {
      try {
        p1.sendSMS();
      } finally {
        System.out.println("B finally");
      }
    }, "B").start();
    new Thread(() -> {
      try {
        p1.sendEMailSleep();
      } catch (Exception e) {
      } finally {
        System.out.println("C finally");
      }
    }, "C").start();

    TimeUnit.SECONDS.sleep(5);
  }


  /**
   * sayHi() 没有synchronization锁，不受影响
   */
  @Test
  public void test2() throws InterruptedException {

    new Thread(() -> {
      try {
        p1.sendEMailSleep();
      } catch (Exception e) {
      } finally {
        System.out.println("A finally");
      }
    }, "A").start();

    // 保证上一个线程先启动
    TimeUnit.MILLISECONDS.sleep(100);

    new Thread(() -> {
      try {
        p1.sayHi();
      } finally {
        System.out.println("B finally");
      }
    }, "B").start();

    TimeUnit.SECONDS.sleep(5);
  }

  /**
   * 两个实例两把锁
   * @throws InterruptedException
   */
  @Test
  public void test3() throws InterruptedException {

    new Thread(() -> {
      try {
        p1.sendEMailSleep();
      } catch (Exception e) {
      } finally {
        System.out.println("A finally");
      }
    }, "A").start();

    // 保证上一个线程先启动
    TimeUnit.MILLISECONDS.sleep(100);

    new Thread(() -> {
      try {
        p2.sendSMS();
      } finally {
        System.out.println("B finally");
      }
    }, "B").start();

    TimeUnit.SECONDS.sleep(5);
  }

  /**
   * 静态同步锁
   *
   * static 方法是属于Class的，锁在Class上，不在实例上。
   *
   * 以下示例通过两个不同实例调用效果是一样的。
   *
   * @throws InterruptedException
   */
  @Test
  public void test4() throws InterruptedException {

    new Thread(() -> {
      try {
        // p1.sendEMailStaticSleep();
        Phone.sendEMailStaticSleep();
      } catch (Exception e) {
      } finally {
        System.out.println("A finally");
      }
    }, "A").start();

    // 保证上一个线程先启动
    TimeUnit.MILLISECONDS.sleep(100);

    new Thread(() -> {
      try {
        // p2.sendSMSStatic();
        Phone.sendSMSStatic();
      } finally {
        System.out.println("B finally");
      }
    }, "B").start();

    TimeUnit.SECONDS.sleep(5);
  }

  /**
   * 一个静态锁 一个非静态锁
   * 是两个不同的锁
   * @throws InterruptedException
   */
  @Test
  public void test5() throws InterruptedException {

    new Thread(() -> {
      try {
        Phone.sendEMailStaticSleep();
      } catch (Exception e) {
      } finally {
        System.out.println("A finally");
      }
    }, "A").start();

    // 保证上一个线程先启动
    TimeUnit.MILLISECONDS.sleep(100);

    new Thread(() -> {
      try {
        p2.sendSMS();
      } finally {
        System.out.println("B finally");
      }
    }, "B").start();

    TimeUnit.SECONDS.sleep(5);
  }




}
