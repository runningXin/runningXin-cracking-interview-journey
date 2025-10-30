/*
Given the root of a binary tree, write a function that creates and returns a deep copy (clone) of the tree.
Example:
             A /|\ / | \ B | E / \ | \ / \| \ C D-----F

                            |
                            |
                            |

             A’ /|\ / | \ B' | E' / \ | \ / \| \ C' D'----F'

*/
import java.util.*;

class Node {
    int val;
    Node left;
    Node right;

    Node( int val ){
        this.val = val;
    }
}

public class TreeDeepCopy {

//    public Node deepCopy (Node root ){
//
//        if(root == null)
//            return null;
//
//        Node new_root = new Node(root.val);
//
//        Node new_left = deepCopy(root.left);
//        Node new_right = deepCopy(root.right);
//        new_root.left = new_left;
//        new_root.right = new_right;
//
//        return new_root;


    public Node deepCopy(Node root) {
        if(root == null)
            return null;

        Stack<Node> stack = new Stack<>();
        Map<Node, Node> map = new HashMap<>();

        stack.push(root);
        map.put(root, new Node(root.val));

        while( stack.size() > 0) {

            Node current = stack.pop();
            Node currentCopy = map.get(current);

            //left child
            if(current.left != null){
                if(!map.containsKey(current.left)){
                    map.put(current.left, new Node(current.left.val));
                    stack.push(current.left);
                }
                currentCopy.left = map.get(current.left);
            }
            //right child
            if(current.right != null){
                if(!map.containsKey(current.right)){
                    map.put(current.right, new Node(current.right.val));
                    stack.push(current.right);
                }
                currentCopy.right = map.get(current.right);
            }
        }

        return map.get(root);

    }

    public static void main (String[] args){

        //provide the input tree
        Node root = new Node(1);
        Node child1 = new Node(2);
        Node child2 = new Node(3);
        root.right = child1;
        child1.left = child2;

        TreeDeepCopy obj = new TreeDeepCopy();
        obj.print_tree( root);
        System.out.println("_____________________________________________");
        //print the deep copied new tree
        obj.print_tree( obj.deepCopy(root));

    }

    private void print_tree(Node node){
        if(node == null)
            return;

        System.out.println(node.val);

        if(node.left != null)
            print_tree(node.left);
        else
            System.out.println("null");

        if(node.right != null)
            print_tree(node.right);
        else
            System.out.println("null");
    }

    private boolean same_tree(Node node1, Node node2){

        return true;
    }
}