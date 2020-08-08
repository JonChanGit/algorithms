package cn.com.jonpad.dp.observer;

/**
 * @author Jon Chan
 * @date 2020/8/7 14:43
 */
public class ObserverA implements Observer{
  /**
   *   myState需要跟目标对象的state值保持一致！
   */
  private int myState;

  public int getMyState() {
    return myState;
  }

  public void setMyState(int myState) {
    this.myState = myState;
  }

  @Override
  public void update(Subject subject) {
    myState = ((ConcreteSubject)subject).getState();
  }
}
