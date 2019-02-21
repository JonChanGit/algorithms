package cn.com.jonpad;

import org.junit.Test;

/**
 * 【标题】：两数相加
 * 【题目】：给定两个非空链表来标识两个非负整数。位数按照逆序方式存储，它们的每个节点只保存单个数字。将两数相加返回新链表
 * 【示例】：
 *      [输入：] (2->4->3) + (5->6->4)
 *      [输出：] 7->0->8
 *      [原因：] 342->465->807
 * @author Jon Chan
 * @date 2019/2/19 12:42
 */
public class Test02AddTwoNumber {

    /**
     * 思路：按小学加法计算思路，按位相加
     * @param l1
     * @param l2
     * @return
     */
    ListNode addTwoNumber(ListNode l1, ListNode l2){
        ListNode node1 = l1;
        ListNode node2 = l2;

        ListNode sentinel = new ListNode(0);
        ListNode sentinelPoint = sentinel;

        int sum = 0;

        while (node1 != null || node2 != null){
            // 取上一位进位值
            sum /= 10;
            if(node1 != null){
                sum += node1.getVal();
                node1 = node1.getNext();
            }
            if(node2 != null){
                sum += node2.getVal();
                node2 = node2.getNext();
            }

            sentinelPoint.setNext(new ListNode(sum % 10));
            sentinelPoint = sentinelPoint.getNext();
        }
        if(sum / 10 == 1){
            // 保留最终进位值
            sentinelPoint.setNext(new ListNode(1));
        }

        return sentinel.getNext();
    }

    @Test
    public void run(){
        ListNode l1 = new ListNode(2)
                .setNext(new ListNode(4)
                    .setNext(new ListNode(3)));
        ListNode l2 = new ListNode(5)
                .setNext(new ListNode(6)
                    .setNext(new ListNode(4)));
        addTwoNumber(l1,l2);
    }
}

class ListNode{
    int val;
    ListNode next;
    public ListNode(int val) {
        this.val = val;
    }

    public ListNode getNext() {
        return next;
    }

    public ListNode setNext(ListNode next) {
        this.next = next;
        return this;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
