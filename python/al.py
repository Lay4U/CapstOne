import numpy as np

def P2(weight, N):
    dp = [[0 for i in range(N)] for j in range(10)]
    print(dp)
    for i in range(N):
        print(i)
        print(np.asarray(dp).shape)
        dp[i + 1][0] = max(dp[i + 1][0], max(dp[i][0], max(dp[i][1], dp[i][2])))
        dp[i + 1][1] = max(dp[i + 1][1], max(dp[i][0], dp[i][2]) + weight[0][i])
        dp[i + 1][2] = max(dp[i + 1][2], max(dp[i][0], dp[i][1]) + weight[1][i])

    #         dp[i+1][0] = max(dp[i+1][0], max(dp[i][0], max(dp[i][1], dp[i][2])));
    #         dp[i+1][1] = max(dp[i+1][1], max(dp[i][0], dp[i][2]) + value[0][i]);
    #         dp[i+1][2] = max(dp[i+1][2], max(dp[i][0], dp[i][1]) + value[1][i]);
    return max(dp[N][0], max(dp[N][1], dp[N][2]))


N = 10

weight = [[50, 10, 100, 20, 40],
          [30, 50, 70, 10, 60]]

P2(weight, N)