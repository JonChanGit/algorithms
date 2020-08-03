package cn.com.jonpad;

/**
 * 二进制字符串转整数
 * @author Jon Chan
 * @date 2020/8/3 08:43
 */
public class Solution04BinaryToDecimal {
  static int binary_to_integer(String binary) {

    int result = 0;

    char[] chars = binary.toCharArray();
    int length = chars.length;
    // 指数
    int pow = length;
    // 下标
    int idx = 0;
    while (pow > 0){

      boolean b = chars[idx] == '1';

      if(b){
        result += Math.pow(2, pow -1 );
      }

      idx ++;
      pow --;
    }

    return result;
  }

  public static void main(String[] args) {
    System.out.println(binary_to_integer("1000"));
  }
}
