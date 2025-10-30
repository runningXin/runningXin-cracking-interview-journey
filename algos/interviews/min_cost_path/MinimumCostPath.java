import java.util.*;

/*
    Minimum Cost Path
    Each cell of a square grid of size NxM contains an integer cost, representing a cost to traverse through that cell.
    We need to find a path from the top-left cell to the bottom-right cell by which the total cost incurred is minimum.
    Note: It is assumed that negative cost cycles do not exist in the input matrix.
    Input
    The first line of input contains an integer N, representing the number of rows.
    The second line of input contains an integer M, representing the number ofcolumns.
    The next N lines of input contain M spaceseparated integers, representing row
    elements, each value depicting the cost of
    the respective cell from (0,0) to (N, M).
    Output
    Return an integer depicting the minimum
    cost to reach the destination.
    1<= N, M <=50
*/

public class MinimumCostPath {

        public static int minCost(int[][] grid){
            int m = grid.length, n = grid[0].length;
            int[][] directions = {{0,1},{0,-1},{1,0},{-1,0}};
            PriorityQueue<int[]> pq = new PriorityQueue<>( (a, b) -> Integer.compare(a[0], b[0]) );
            boolean [][] visited = new boolean[m][n];
            pq.offer(new int[]{grid[0][0], 0, 0});

            while(pq.size() > 0){
                int size = pq.size();

                int[] current = pq.poll();
                if(visited[current[1]][current[2]])
                    continue;
                visited[current[1]][current[2]] = true;

                for(int[] direction : directions){
                    int x = direction[0] + current[1];
                    int y = direction[1] + current[2];
                    if(x == m-1 && y == n - 1){
                        return current[0] + grid[x][y];
                    }
                    if(x >= 0 && y >= 0 && x < m && y < n && visited[x][y] == false){
                        pq.offer(new int[]{current[0] + grid[x][y], x , y});
                    }
                }

            }

            //won't be here
            return grid[m-1][n-1];

        }

    public static void main(String[] args) {
        // Example #1
        int[][] grid1 = {
                {31, 100, 65, 12, 18},
                {10, 13, 47, 157, 6},
                {100, 113, 174, 11, 33},
                {88, 124, 41, 20, 140},
                {99, 32, 111, 41, 20}
        };
        System.out.println(minCost(grid1)); // Output: 327

        // Example #2
        int[][] grid2 = {
                {42, 93},
                {7, 14}
        };
        System.out.println(minCost(grid2)); // Output: 63

        //Example #3
        int[][] grid3 = {{1, 35, 1},{1, 1, 1},{100, 1, 1}};
        System.out.println(minCost(grid3));

        //Example #4
        int[][] grid4 = {{1, 35, 1},{1, 100, 1},{100, 1, 1}};
        System.out.println(minCost(grid4));
    }
}