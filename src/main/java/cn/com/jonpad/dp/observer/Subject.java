package cn.com.jonpad.dp.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 主题
 * @author Jon Chan
 * @date 2020/8/7 14:36
 */
public interface Subject<T> {

  /**
   * 注册订阅者
   */
  void registerObserver(T obs);

  /**
   * 移除订阅者
   */
  void removeObserver(T obs);

  /**
   * 通知所有的观察者更新状态
   */
  void notifyAllObservers();
}
