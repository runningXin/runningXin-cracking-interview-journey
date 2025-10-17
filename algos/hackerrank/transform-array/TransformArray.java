/*
Problem: Array Transformation

The developers of Hackerrank want to create a utility that transforms an array using an integer as a seed.
They defined a function that takes an array of integers arr of size n and an integer k, and returns the kᵗʰ largest bitwise OR sum of any subarray of arr.

Given the array arr and the integer k, implement this utility to return the kᵗʰ largest bitwise OR sum as described.

The bitwise OR sum of a subarray starting at index i and ending at index j is given by
arr[i] | arr[i + 1] | … | arr[j]
where | represents the bitwise OR operator.

A subarray is a contiguous segment of an array.
For example, the subarrays of [1, 2, 3] are [1], [1, 2], [1, 2, 3], [2], [2, 3], and [3].

Example:
Consider, n = 4, arr = [2, 4, 1, 3], and k = 6.

Subarrays and OR sums:
[1] → 2
[1, 2] → 6
[1, 2, 3] → 7
[2] → 4
[2, 3] → 7
[3] → 3

The sorted OR sums are [7, 7, 7, 6, 5, 4, 3, 3, 2, 1]
The 6th largest OR sum is 4.

Function Description:
Complete the function getKthMaximumORSum in the editor below.

The function is expected to return an integer.
The function accepts the following parameters:

INTEGER_ARRAY arr

LONG_INTEGER k

Returns:
int: the kᵗʰ largest OR sum

Constraints:
1 ≤ n ≤ 10⁵
1 ≤ arr[i] ≤ 10¹⁰
1 ≤ k ≤ n(n + 1)/2
 */
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class TransformArray {

    public static int getKthMaximumORSum (List<Integer> arr, long k){
        List<Integer> list = new ArrayList<>();

        System.out.println("The input list is：  " + arr.toString());

        List<Integer> pre = new ArrayList<>();

        for(int num : arr){
            List<Integer> current = new ArrayList<>();
            current.add(num);
            for(int pre_num : pre){
                current.add(pre_num | num);
            }

            pre = new ArrayList<>(current);
            list.addAll(new ArrayList<>(current));
        }

        Collections.sort(list, Collections.reverseOrder());

        System.out.println("The ORSUM list is：  " + list.toString());
        System.out.print("The "+ k +"th is: " );

        for(int num : list){
            k --;
            if(k == 0) {
                System.out.println(num);
                return num;
            }
        }

        return -1;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int arrCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = IntStream.range(0, arrCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine().replaceAll("\\s+$", "");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        long k = Long.parseLong(bufferedReader.readLine().trim());

        int result = getKthMaximumORSum(arr, k);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}