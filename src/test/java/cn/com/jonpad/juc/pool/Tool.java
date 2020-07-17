package cn.com.jonpad.juc.pool;

import cn.hutool.core.util.StrUtil;

import java.text.MessageFormat;

/**
 * @author Jon Chan
 * @date 2020/7/17 11:31
 */
public class Tool {
  public static String logFormatter(String pattern, Object ... arguments){
    return StrUtil.format(Thread.currentThread().getName()+ "\t" + pattern, arguments);
  }
}
