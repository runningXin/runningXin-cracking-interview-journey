/*
Substring Calculator

Given a string, s, a substring is defined as a non-empty string that can be obtained by one of the following means:
	•	Remove zero or more characters from the left side of s.
	•	Remove zero or more characters from the right side of s.
	•	Remove zero or more characters from the left side of s and remove zero or more characters from the right side.

⸻

For example, let s = abcde. The substrings are:
	1.	abcde
	2.	abcd
	3.	bcde
	4.	abc
	5.	bcd
	6.	cde
	7.	ab
	8.	bc
	9.	cd
	10.	de
	11.	a
	12.	b
… (continues)
 */


import java.util.Scanner;

public class SubstringCalculator {
    public static int countSubstring(String s){
        //int left = 0, right = 0;
        if(s == null || s.length() == 0)
            return 0;

        int result = 0;
        for(int right = 0; right < s.length(); right++){
            int left = 0;
                result += right - left + 1;

        }

        return result;
    }

    public static void main(String[] args){
        String input;
        if(args == null || args.length == 0){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter a string: ");
            input = scanner.nextLine();
            scanner.close();
        } else {
            input = args[0];
        }

        System.out.println("Input: " + input);
        System.out.println("Total substrings: " + countSubstring(input));
    }
}