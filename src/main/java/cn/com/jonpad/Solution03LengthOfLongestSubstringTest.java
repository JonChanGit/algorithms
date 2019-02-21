package cn.com.jonpad;

import org.junit.Test;

/**
 * 【标题】：无重复字符的最长子串<br/>
 * 【题目】：给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。<br/>
 * 【示例】：<br/>
 *      01 :<br/>
 *          [输入：] "abcabcbb"<br/>
 *          [输出：] 3<br/>
 *          [原因：] 因为无重复字符的最长子串是 "abc"，所以其长度为 3。<br/>
 *      02 :<br/>
 *          [输入：] "bbbbb"<br/>
 *          [输出：] 1<br/>
 *          [原因：] 因为无重复字符的最长子串是 "b"，所以其长度为 1。<br/>
 *      02 :<br/>
 *          [输入：] "pwwkew"<br/>
 *          [输出：] 3<br/>
 *          [原因：] 因为无重复字符的最长子串是 "wke"，所以其长度为 3。<br/>
 *    请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。<br/>
 * @author Jon Chan
 * @date 2019/2/21 13:03
 */
public class Solution03LengthOfLongestSubstringTest {
    public int lengthOfLongestSubstring(String s) {
        return 0;
    }

    @Test
    public void run() {
        System.out.println(lengthOfLongestSubstring("xxx"));
    }
}
