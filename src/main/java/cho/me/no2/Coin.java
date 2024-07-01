package cho.me.no2;

import java.util.Arrays;

public class Coin {
    public static int countWaysToMakeChange(int sum, int[] coins) {
        int[] dp = new int[sum + 1];
        dp[0] = 1;

        for (int coin : coins) {
            for (int i = coin; i <= sum; i++) {
                dp[i] += dp[i - coin];
            }
            System.out.println(Arrays.toString(dp));
        }

        return dp[sum];
    }

}