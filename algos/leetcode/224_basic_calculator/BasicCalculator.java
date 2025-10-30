

public class BasicCalculator {


    public static int calculate(String s) {

        Deque<Integer> stack = new ArrayDeque<>();
        int result = 0, num = 0, sign = 1;

        for(int i = 0; i < s.length(); i ++){

            char c = s.charAt(i);
            if(Character.isDigit(c)){
                num = num * 10 + c -'0';
            }else if ( c == '-'){
                result = num * sign;
                num = 0;
                sign = -1;
            }else if (c == '+'){
                result = num * sign;
                num = 0;
                sign = 1;
            }else if ( c == '('){
                //put the temporary results to stack
                stack.push(result);
                stack.push(sign);
            }else if( c == ')'){
                num = num * sign;
                num = num * stack.pop();
                result += num;
            }
        }

        return result;

    }

    public void main (String[] args){

        System.out.println(" (1+(4+5+2)-3)+(6+8)  == " + calculate("(1+(4+5+2)-3)+(6+8)"));
        System.out.println(" -5 - (-5-6)  == " + calculate(" - 5 - (-5-6)"));
        System.out.println("-5 - (-5 - ( -5 - 3)) == "  + calculate("-5 - (-5 - ( -5 -3))"));

    }
}