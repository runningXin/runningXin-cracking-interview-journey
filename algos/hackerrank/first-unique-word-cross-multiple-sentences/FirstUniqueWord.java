/** Problem: First Unique Word Across Multiple Strings (Case & Punctuation Aware)
Description:
You are given a list of strings (each representing a sentence).
Find the first word (in order of appearance across all sentences) that does not repeat anywhere.
Words should be treated case-insensitively (e.g., "The" and "the" are the same).
Punctuation marks (. , ! ? : ;) should be ignored.
If no unique word exists, return "_".

Example:
Input:
[
  "The quick brown brown fox!", //fox! and fox are same word
  "Jumped over the lazy dog.",
  "The fox was Quick."
]
Output: "brown"
Explanation:
Normalized words: the(3), quick(2), brown(1), fox(2), jumped(1), over(1), lazy(1), dog(1), was(1)
The first word with frequency 1 is "brown".

**/

public class FirstUniqueWord {

    String findFirstUniqueWord(){
        return "_";
    }

    public static void main(String[] args){
        FirstUniqueWord obj = new FirstUniqueWord();
        System.out.println(obj.findFirstUniqueWord());
    }
}

