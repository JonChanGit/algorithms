package cn.com.jonpad.dp.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jon Chan
 * @date 2020/8/7 14:39
 */
public class ConcreteSubject implements Subject<Observer>{

  /**
   * 存储订阅者
   */
  List<Observer> list = new ArrayList<>();

  private int state;

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
    //主题对象(目标对象)值发生了变化，请通知所有的观察者
    this.notifyAllObservers();
  }



  @Override
  public void registerObserver(Observer obs) {
    list.add(obs);
  }

  @Override
  public void removeObserver(Observer obs) {
    list.remove(obs);
  }

  @Override
  public void notifyAllObservers() {
    list.forEach(observer -> observer.update(this));
  }
}
