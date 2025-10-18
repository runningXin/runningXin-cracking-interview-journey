import java.util.*;

public class ReversePolishNotation {

    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        Set<String> operators = new HashSet<>();
        operators.addAll(Arrays.asList("+", "-", "*", "/"));
        int result = 0;

        for(int i = 0; i < tokens.length; i++){
            String current = tokens[i];
            if(!operators.contains(current)){
                stack.push(Integer.parseInt(current));
                continue;
            }
            // + , -, * , /
            int operator2 = stack.pop();
            int operator1 = stack.pop();

            int temp = 0;
            switch (current){
                case "+" : temp = operator1 + operator2; break;
                case "-" : temp = operator1 - operator2; break;
                case "*" : temp = operator1 * operator2; break;
                case "/" : temp = operator1 / operator2; break;
            }
            stack.push(temp);

        }

        return stack.pop();
    }

    public static void main (String[] args){
        //["2","1","+","3","*"]
        int result = evalRPN(args);
        System.out.println("The result of the notations are: " + result);
    }
}