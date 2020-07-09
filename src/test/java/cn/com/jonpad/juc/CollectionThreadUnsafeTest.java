package cn.com.jonpad.juc;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 线程不安全测试
 *
 * 原因：多个线程对同一个资源读写同时进行导致 java.util.ConcurrentModificationException
 *
 *
 * @author Jon Chan
 * @date 2020/7/9 8:49
 */
public class CollectionThreadUnsafeTest {

  @Test
  public void testArrayListThreadUnsafe() throws InterruptedException {
    // List<String> list = new ArrayList<>();// 使用内置线程安全类
    // List<String> list = new Vector<>();// 使用内置线程安全类
    // List<String> list = Collections.synchronizedList(new ArrayList<>());
    List<String> list = new CopyOnWriteArrayList<>();

    for (int i = 0; i < 30; i++) {
      new Thread(()->{
        list.add(UUID.randomUUID().toString());
        System.out.println(list);
      },"thread"+i).start();
    }
    Thread.sleep(2000);
  }


  @Test
  public void testHashSetThreadUnsafe() throws InterruptedException {
    Set<String> set = new HashSet<>();

    for (int i = 0; i < 30; i++) {
      new Thread(()->{
        set.add(UUID.randomUUID().toString());
        System.out.println(set);
      },"thread"+i).start();
    }
    Thread.sleep(2000);
  }

  @Test
  public void testHashMapThreadUnsafe() throws InterruptedException {
    Map<String, Object> map = new HashMap();

    for (int i = 0; i < 30; i++) {
      new Thread(()->{
        map.put(UUID.randomUUID().toString(), null);
        System.out.println(map);
      },"thread"+i).start();
    }
    Thread.sleep(2000);
  }
}
