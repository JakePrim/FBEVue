package com.algorithm.sort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {
    //树的节点
    private static class TreeNode {
        int data;
        TreeNode left;
        TreeNode right;

        public TreeNode(int data) {
            this.data = data;
        }
    }

    /**
     * 创建二叉树
     *
     * @param linkedList
     */
    public static TreeNode createBinaryTree(LinkedList<Integer> linkedList) {
        TreeNode node = null;
        if (linkedList == null || linkedList.isEmpty()) {
            return null;
        }
        Integer data = linkedList.removeFirst();
        if (data != null) {
            node = new TreeNode(data);
            node.left = createBinaryTree(linkedList);
            node.right = createBinaryTree(linkedList);
        }
        return node;
    }

    public static void preOrderTraversal(TreeNode node) {
        if (node == null) return;
        System.out.println(node.data);
        preOrderTraversal(node.left);
        preOrderTraversal(node.right);
    }

    /**
     * 使用栈来实现深度优先遍历
     *
     * @param node
     */
    public static void preOrderTraversalWithStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode root = node;
        while (root != null || !stack.isEmpty()) {
            //出栈即打印输出
            while (root != null) {
                System.out.println(root.data);
                stack.push(root);
                root = root.left;//遍历左孩子节点
            }
            //如果左孩子节点为null 弹出栈顶打印 访问右孩子
            if (!stack.isEmpty()) {
                root = stack.pop();//出栈
                //访问右孩子
                root = root.right;
            }
        }
    }

    /**
     * //              3
     * //          2        8
     * //       9   10   null 4
     * [3,2,9] -> left = null 9出栈打印 left 和 right都是空，2出栈打印 right != null treeNode = treeNode.right 10
     * [3,10] -> 10出栈打印 right != null treeNode = treeNode.right
     * [3] -> 3出栈打印 right != null treeNode = treeNode.right
     * [8] -> left == null 8出栈打印，right ！= null treeNode = treeNode.right
     * [4] -> left == null 4出栈打印
     *
     * @param node
     */
    public static void centerOrderTraversalWithStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode treeNode = node;
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            while (!stack.isEmpty()) {
                TreeNode tempeNode = stack.pop();
                System.out.println(tempeNode.data);
                if (tempeNode.right != null) {
                    treeNode = tempeNode.right;
                    break;
                }
            }
        }
    }

    public static void centerOrderTraversal(TreeNode node) {
        if (node == null) return;
        centerOrderTraversal(node.left);
        System.out.println(node.data);
        centerOrderTraversal(node.right);
    }

    public static void lastOrderTraversal(TreeNode node) {
        if (node == null) return;
        lastOrderTraversal(node.left);
        lastOrderTraversal(node.right);
        System.out.println(node.data);
    }

    /**
     * 3
     * 2        8
     * 9   10   null 4
     * 5 null
     * 9 10 2 4 8 3
     * [3,8,4,2,10,9]
     * 4 8 3
     * [3,2,9] left = null 9出栈, 2-> right != null 10入栈
     * 9
     * [3,2,10] left == null right == null 10出栈
     * 10
     * [3,2] 2 如何判断出栈呢？
     * 辅助栈：[9,10]
     *
     * @param node
     */
    public static void lastOrderTraversalWithStack(TreeNode node) {
        TreeNode treeNode = node;
        Stack<TreeNode> stack = new Stack<>();
        //辅助栈
        Stack<TreeNode> helpStack = new Stack<>();
        while (treeNode != null || !helpStack.isEmpty()) {
            if (treeNode != null) {
                stack.push(treeNode);
                helpStack.push(treeNode);
                treeNode = treeNode.right;
            } else {
                treeNode = helpStack.peek();
                helpStack.pop();
                treeNode = treeNode.left;
            }
        }
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            System.out.println(pop.data);
        }
    }

    public static void main(String[] args) {
        //             3
        //          2     8
        //       9 10   null 4
        //null null  null null
        LinkedList<Integer> linkedList = new LinkedList<>(Arrays.asList(new Integer[]{3, 2, 9, null, null, 10, null, null, 8, null, 4}));
        TreeNode treeNode = createBinaryTree(linkedList);
        System.out.println("前序遍历：");
        preOrderTraversal(treeNode);
        System.out.println("中序遍历：");
        centerOrderTraversal(treeNode);
        System.out.println("后序遍历：");
        lastOrderTraversal(treeNode);

        System.out.println("栈回溯方式的前序遍历:");
        preOrderTraversalWithStack(treeNode);
        System.out.println("栈回溯方式的中序遍历:");
        centerOrderTraversalWithStack(treeNode);
        System.out.println("栈回溯方式的后序遍历:");
        lastOrderTraversalWithStack(treeNode);

        System.out.println("深度优先遍历:");
        levelOrderTraversal(treeNode);
    }

    /**
     * 二叉树的广度优先遍历，使用队列的特点：FIFO 先进先出
     *      * //              3
     *      * //          2        8
     *      * //       9   10   null 4
     */
    public static void levelOrderTraversal(TreeNode node) {
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(node);//入队添加根节点
        while (!queue.isEmpty()) {
            TreeNode treeNode = queue.poll();//队头出队
            //打印
            System.out.println(treeNode.data);
            if (treeNode.left != null) {
                //队尾入队
                queue.offer(treeNode.left);
            }
            if (treeNode.right != null) {
                queue.offer(treeNode.right);
            }
        }
    }

}
