package cn.com.jonpad;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Jon Chan
 * @date 2020/7/28 21:39
 */
public class StaticTest {
  public static final Object o = new Object();
  //public static final int x = 9;

  public static void main(String[] args) {
    new StaticTest().test01();
    System.out.println(x());
    final String s1 = "a";
    final String s2 = "b";
    String s3 = "ab";
    String s4 = s1 + s2;
    System.out.println(s3 == s4);
    String t1 = "a";
    String t2 = "b";
    //String t3 = "ab";
    String t4 = t1 + t2;
    System.out.println(s3 == t4);

  }

  Byte x;
  byte x1;

  public final static int x(){
    String x = new String("str");
    return 1;
  }

  @Test
  public void test01(){
    ThreadLocal<Integer> l = new ThreadLocal<Integer>();
    new Thread(()->{
      l.set(1);
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + '\t' + l.get());
    }, "T1").start();
    new Thread(()->{
      l.set(2);
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + '\t' + l.get());
    }, "T2").start();
    new Thread(()->{
      l.set(3);
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + '\t' + l.get());
    }, "T3").start();


  }


  @Override
  protected void finalize() throws Throwable {
    super.finalize();
  }
}
