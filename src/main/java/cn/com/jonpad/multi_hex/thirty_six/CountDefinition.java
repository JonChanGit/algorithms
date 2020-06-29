package cn.com.jonpad.multi_hex.thirty_six;

import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 36进制定义
 * @author Jon Chan
 * @date 2020/6/29 10:47
 */
public class CountDefinition {
  private String value = "0";

  public CountDefinition(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    String lowerCase = value.toLowerCase();
    if(!checkLegal(lowerCase)){
      throw new IllegalArgumentException("格式不正确");
    }
    this.value = lowerCase;
  }

  /**
   * 合法性检查
   * @param value
   * @return
   */
  public static boolean checkLegal(String value){
    value = value.toLowerCase();
    Pattern pattern = Pattern.compile("^[1-z][0-z]*[0-z]$*");
    Matcher matcher = pattern.matcher(value);
    return matcher.find();
  }


}
