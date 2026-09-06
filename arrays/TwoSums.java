package arrays;

import java.util.HashMap;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> hash = new HashMap<>();
        int needed = 0;
        for (int i = 0; i < nums.length; i++) {
            needed = target - nums[i];
            if (hash.containsKey(needed)) {
                return new int[]{hash.get(needed), i};
            } else {
                hash.put(nums[i], i);
            }
            System.out.println(hash);
        }
        return new int[]{-1, -1};
    }
}

public class TwoSums {
    public static void main(String[] args) {
        Solution sol = new Solution();
        int arr[]={3,2,3};
        int result[]=sol.twoSum(arr,6);
        System.out.println(result[0]+" "+result[1]);

    }
}
