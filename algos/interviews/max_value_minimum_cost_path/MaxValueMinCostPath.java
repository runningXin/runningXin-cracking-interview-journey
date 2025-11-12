/*
You are given two arrays, A and B, each made of N integers. They represent a grid with N columns and 2 rows,
where A is the upper row and B is the lower row.
Your task is to go from the upper-left cell (represented by A[0]) to the bottom-right cell (represented by B[N−1])
moving only right and down, so that the maximum value over which you pass is as small as possible.
Write a function:
int solution(int A[], int B[], int N);
that, given two arrays of integers, A and B, of length N, returns the maximum value on the optimal path.
Examples:
1. Given A = [3, 4, 6], B = [6, 5, 4], the function should return 5. The optimal path is 3 → 4 → 5 → 4.

 3, 4, 7
 6, 5, 2
 */

/*
        3, 7, 13
        9, 12, 16
 */
import java.util.*;

public class MaxValueMinCostPath {

    public static int max_value(int[] A, int[] B) {
        int[][] directions = {{0, 1}, { 1, 0} };
        int N = A.length;
        //corner case only 1 column
        if (N == 1)
            return Math.max(A[0], B[0]);


        //Dijkstra + BFS
        PriorityQueue<int[]> pq = new PriorityQueue<>( (a, b) -> Integer.compare(a[0], b[0]));
        pq.offer(new int[]{A[0], 0, 0});

        int[][] grid = {A, B};
        boolean[][] visited = new boolean[2][N];
        int max = A[0];

        while(pq.size() > 0){
            //the cell selected on the optimal path
            int[] current = pq.poll();
            int x = current[1];
            int y = current[2];
            if( visited[x][y])
                continue;

            visited[x][y] = true;
            max = Math.max(max, grid[x][y]);
            if(x == 1 && y == N - 1)
                return max;

            for(int[] direction : directions){
                int newX = x + direction[0];
                int newY = y + direction[1];
                if(newX >= 0 && newX <= 1 && newY >= 0 && newY < N )
                    pq.offer( new int[] {grid[newX][newY] , newX, newY});
            }
        }

        //the program won't reach here
        return max;
    }

    public static void main(String[] args) {

        /*A = [1, 2, 1, 1, 1, 4], B = [1, 1, 1, 3, 1, 1], the function should return 2. */
        int[] A = {1, 2, 1, 1, 1, 4};
        int[] B = {1, 1, 1, 3, 1, 1};
        System.out.println("the max value  " + max_value(A, B));
        /*
            Given A = [−5, −1, −3], B = [−5, 5, −2], the function should return −1.
        */

        int[] C = {-5, -1, -3};
        int[] D = {-5, 5, -2};
        System.out.println("the max value  " + max_value(C, D));
    }
}




