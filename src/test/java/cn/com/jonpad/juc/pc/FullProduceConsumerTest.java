package cn.com.jonpad.juc.pc;


import org.junit.Test;

import java.sql.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class Resource{
  private volatile boolean FLAG = true;

  private AtomicInteger atomicInteger = new AtomicInteger(0);

  private BlockingQueue<String> queue;

  public Resource(BlockingQueue<String> queue) {
    this.queue = queue;
  }

  public void produce() throws Exception {
    System.out.println(Thread.currentThread().getName() + "\t 开始生产 >>>>>>>>>");
    while (FLAG) {
      String data = String.valueOf(atomicInteger.incrementAndGet());
      boolean offer = queue.offer(data, 2L, TimeUnit.SECONDS);
      if (offer) {
        System.out.println(Thread.currentThread().getName() + "\t insert "+data+" success");
      }else {
        System.out.println(Thread.currentThread().getName() + "\t insert "+data+" Fail");
      }
      TimeUnit.MILLISECONDS.sleep(100L);
    }
    System.out.println(Thread.currentThread().getName() + "\t 结束生产 <<<<<<<<<<");
  }

  public void consumer() throws Exception{
    System.out.println(Thread.currentThread().getName() + "\t 开始消费 ++++++++++");
    while (FLAG) {
      String data =  queue.poll(1L, TimeUnit.SECONDS);
      if(data == null || "".equals(data)){
        System.out.println(Thread.currentThread().getName() + "\t get data timeout, exit.");
        return;
      }
      System.out.println(Thread.currentThread().getName() + "\t get data "+data+" success.");
      TimeUnit.MILLISECONDS.sleep(500L);
    }
    System.out.println(Thread.currentThread().getName() + "\t 结束消费 ----------");
  }

  public void setFLAG(boolean FLAG) {
    this.FLAG = FLAG;
  }

  public int getSize(){
    return this.queue.size();
  }
}
/**
 * 完整体生产者消费者模式
 * @author Jon Chan
 * @date 2020/7/14 08:49
 */
public class FullProduceConsumerTest {
  @Test
  public void test() throws Exception{
    Resource r = new Resource(new ArrayBlockingQueue<String>(10));

    new Thread(() -> {
      try {
        r.produce();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, "produce++++++").start();

    TimeUnit.SECONDS.sleep(1L);

    new Thread(() -> {
      try {
        r.consumer();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, "consumer-----").start();


    TimeUnit.SECONDS.sleep(10L);
    r.setFLAG(false);
    System.out.println(Thread.currentThread().getName() + "\t 剩余产品数量： " + r.getSize());
  }
}
