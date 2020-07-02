package cn.com.jonpad.multi_hex.thirty_six;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 36进制定义
 *
 * @author Jon Chan
 * @date 2020/6/29 10:47
 */
public class CountDefinition {
  private char[] value = new char[]{'0'};
  private int[] i_value = new int[]{0};

  private static char[] DEF = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
          'a', 'b', 'c', 'd', 'e', 'f', 'g',
          'h', 'i', 'j', 'k', 'l', 'm', 'n',
          'o', 'p', 'q', 'r', 's', 't',
          'u', 'v', 'w', 'x', 'y', 'z'};

  public CountDefinition() {
  }

  public CountDefinition(String value) {
    setValue(value);
  }

  public String getValue() {
    return new String(this.value);
  }

  public void setValue(String value) {
    String lowerCase = value.toLowerCase();
    if (!checkLegal(lowerCase)) {
      throw new IllegalArgumentException("格式不正确");
    }
    this.value = lowerCase.toCharArray();
    this.i_value = new int[this.value.length];
    for (int i = 0; i < this.value.length; i++) {
      char item = this.value[i];
      // 将输入的字符串转为实际计算值
      for (int ci = 0; ci < DEF.length; ci++) {
        if (item == DEF[ci]) {
          i_value[i] = ci;
        }
      }
    }
  }

  public int length() {
    return this.value == null ? -1 : value.length;
  }

  /**
   * 合法性检查
   *
   * @param value
   * @return
   */
  public static boolean checkLegal(String value) {
    value = value.toLowerCase();
    Pattern pattern = Pattern.compile("^[1-z][0-z]*");
    Matcher matcher = pattern.matcher(value);
    return matcher.find();
  }

  public String add(CountDefinition addend) {
    // 始终保证addend1的长度>=addend2 方便统一处理
    int[] addend1, addend2;
    char[] result;
    if (this.length() >= addend.length()) {
      addend1 = this.i_value;
      addend2 = addend2ArrayShuffle(addend.i_value, addend1.length);
      // 结果数组比最长数组多1
      result = new char[this.i_value.length + 1];
    } else {
      addend1 = addend.i_value;
      addend2 = addend2ArrayShuffle(this.i_value, addend1.length);
      // 结果数组比最长数组多1
      result = new char[addend.i_value.length + 1];
    }

    for (int i = addend1.length - 1; i >= 0; i--) {
      result[i + 1] = (char) (result[i + 1] + addend1[i] + addend2[i]);
      // 如果相加结果达到进位要求。
      if (result[i + 1] >= DEF.length) {
        char sum = result[i + 1];
        result[i + 1] = (char) (sum % DEF.length);
        result[i] = (char) (sum / DEF.length);
      }
    }

    for (int i = 0; i < result.length; i++) {
      result[i] = DEF[result[i]];
    }

    return new String(result);
  }


  private int[] addend2ArrayShuffle(int[] addend2, int length) {
    if(length < addend2.length){
      throw new IllegalArgumentException("加数2长度过大");
    }
    int[] result = new int[length];
    int def = length - addend2.length;
    for (int i = 0; i < addend2.length; i++) {
      result[i + def] = addend2[i];
    }
    return result;
  }


  public static void main(String[] args) {
    CountDefinition a = new CountDefinition("1001");
    CountDefinition b = new CountDefinition("z");

    System.out.println(a.add(b));

  }

}
