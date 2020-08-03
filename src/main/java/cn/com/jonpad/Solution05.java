package cn.com.jonpad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定m个有序（递增）序列， 这些序列中包含的总的元素数目为n, 请将这些数组元素合并为一个有序（递增）序列。算法复杂度O(nm)
 * @author Jon Chan
 * @date 2020/8/3 08:58
 */
public class Solution05 {
  static List<Integer> mergeUseApi(List<List<Integer>> seqs) {
    List<Integer> tmp = new ArrayList<>();
    seqs.forEach(tmp::addAll);
    tmp.sort(Integer::compareTo);
    return tmp;
  }

  /**
   * 冒泡实现
   * @param seqs
   * @return
   */
  static List<Integer> merge(List<List<Integer>> seqs) {
    List<Integer> tmpLst = new ArrayList<>();
    seqs.forEach(tmpLst::addAll);
    Integer[] array = tmpLst.toArray(new Integer[tmpLst.size()]);


    int i = 0, j = 0, tmp = 0, flag = 1;

    int size = tmpLst.size();


    for (; i < size; i++){
      for (j = size - 1; j > i; j--){
        // 从后向前遍历交换
        if(array[j -1] > array[j]){
          // 小的放前面 大的放后面
          tmp = array[j -1];
          array[j-1] = array[j];
          array[j] = tmp;
        }
      }
    }


    return new ArrayList<>(Arrays.asList(array));
  }

  public static void main(String[] args) {
    List<Integer> l1 = new ArrayList<>(Arrays.asList(1,3,6,9,11));
    List<Integer> l2 = new ArrayList<>(Arrays.asList(2,6,8,12));
    List<Integer> l3 = new ArrayList<>(Arrays.asList(4,5,7,10,15));
    List<Integer> merge = merge(new ArrayList<>(Arrays.asList(l1, l2, l3)));
    merge.forEach(System.out::println);
  }
}
