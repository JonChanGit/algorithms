package cn.com.jonpad;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 给出未排序数组nums和指定目标target，返回数组中两数之和$= target$的组合元素下标[index1, index2], 要求下标从1开始，而且$index1 < index2$，保证题目中有且只有1个可行解；
 * EG :
 *  给 nums = [2,7,11,15] ; target = 9
 *  因为 nums[0] + nums[2] = 9
 *  输出：[0,1]
 * @author Jon Chan
 * @date 2019/2/18 16:52
 */
public class Test01 {
    public int[] twoSum(int[] nums, int target){
        Map<Integer,Integer> map = new HashMap<>();
        int[] results = new int[2];
        for (int i = 0; i < nums.length; i++) {
            // 存储nums[i]期望的“另一半”，一旦哈希表中包含nums[i]，代表“另一半”早已存储在哈希表中，直接返回即可；
            if(map.containsKey(nums[i])){
                results[0] = map.get(nums[i]) + 1;
                results[1] = i + 1;
                break;
            }
            map.put(target - nums[i], i);
        }
        return results;
    }

    @Test
    public void run(){
        int[] nums = {2,7,11,15};
        int target = 9;
        int[] x = twoSum(nums, target);
        System.out.println(x.length);
        System.out.println(x[0]);
        System.out.println(x[1]);
    }
}
