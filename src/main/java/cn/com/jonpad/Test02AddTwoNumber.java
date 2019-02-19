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

    String getListNodeValue(ListNode node){
        if(node == null){
            throw new RuntimeException("Node is Empty");
        }
        if(node.getNext() != null){
            return getListNodeValue(node.getNext()) + node.getVal();
        }else{
            return String.valueOf(node.getVal());
        }
    }

    ListNode addTwoNumber(ListNode l1, ListNode l2){
        int val1 = Integer.parseInt(getListNodeValue(l1));
        int val2 = Integer.parseInt(getListNodeValue(l2));
        int result = val2 + val1;

        return null;
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
