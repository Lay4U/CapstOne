import pandas as pd

df1 = pd.read_csv('1.csv')
df2 = pd.read_csv('2.csv')
df3 = pd.read_csv('3.csv')
df4 = pd.read_csv('4.csv')
df5 = pd.read_csv('5.csv')
df6 = pd.read_csv('6.csv')
df7 = pd.read_csv('7.csv')
df8 = pd.read_csv('8.csv')
df9 = pd.read_csv('9.csv')
df10 = pd.read_csv('10.csv')
df11 = pd.read_csv('11.csv')

df1 = df1.append(df2)
df1 = df1.append(df3)
df1 = df1.append(df4)
df1 = df1.append(df5)
df1 = df1.append(df6)
df1 = df1.append(df7)
df1 = df1.append(df8)
df1 = df1.append(df9)
df1 = df1.append(df10)
df1 = df1.append(df11)
print(df1)
df1.to_csv('dataset_origin.csv')
'''
D:\Anaconda3\envs\me\python.exe C:/Users/Mir/Python/capstone/python/data/generate.py
                날짜     역번호                역명  ...   22~23   23~24     24~
0       2008-01-01   150.0          서울역(150)  ...  2584.0  1059.0   264.0
1       2008-01-01   150.0          서울역(150)  ...   744.0   406.0   558.0
2       2008-01-01   151.0           시청(151)  ...   531.0   233.0   974.0
3       2008-01-01   151.0           시청(151)  ...   141.0   107.0   185.0
4       2008-01-01   152.0           종각(152)  ...  2290.0   802.0  1559.0
5       2008-01-01   152.0           종각(152)  ...   271.0   134.0   210.0
6       2008-01-01   153.0         종로3가(153)  ...  1318.0   455.0  1499.0
7       2008-01-01   153.0         종로3가(153)  ...   190.0   127.0   384.0
8       2008-01-01   154.0         종로5가(154)  ...   134.0    47.0   140.0
9       2008-01-01   154.0         종로5가(154)  ...   125.0   104.0    42.0
10      2008-01-01   155.0          동대문(155)  ...   287.0   155.0   139.0
11      2008-01-01   155.0          동대문(155)  ...   426.0   231.0   221.0
12      2008-01-01   156.0          신설동(156)  ...   143.0    49.0    19.0
13      2008-01-01   156.0          신설동(156)  ...   279.0   186.0    89.0
14      2008-01-01   157.0          제기동(157)  ...   124.0    47.0    62.0
15      2008-01-01   157.0          제기동(157)  ...   236.0   148.0    85.0
16      2008-01-01   158.0      청량리(지하)(158)  ...  1001.0   316.0    60.0
17      2008-01-01   158.0      청량리(지하)(158)  ...   900.0   546.0   403.0
18      2008-01-01   159.0          동묘앞(159)  ...    70.0    26.0    10.0
19      2008-01-01   159.0          동묘앞(159)  ...   117.0    86.0    61.0
20      2008-01-01   201.0           시청(201)  ...   287.0   135.0   570.0
21      2008-01-01   201.0           시청(201)  ...   113.0    70.0   148.0
22      2008-01-01   202.0        을지로입구(202)  ...  1216.0   329.0  1188.0
23      2008-01-01   202.0        을지로입구(202)  ...   112.0    48.0   219.0
24      2008-01-01   203.0        을지로3가(203)  ...   117.0    59.0   107.0
25      2008-01-01   203.0        을지로3가(203)  ...    48.0    30.0    34.0
26      2008-01-01   204.0        을지로4가(204)  ...    50.0     9.0    22.0
27      2008-01-01   204.0        을지로4가(204)  ...    37.0    24.0    32.0
28      2008-01-01   205.0       동대문운동장(205)  ...   407.0   247.0   151.0
29      2008-01-01   205.0       동대문운동장(205)  ...   298.0   232.0   151.0
...            ...     ...               ...  ...     ...     ...     ...
200720  2018-12-31  2813.0              강동구청  ...   222.0   100.0   183.0
200721  2018-12-31  2813.0              강동구청  ...   469.0   304.0   702.0
200722  2018-12-31  2814.0        몽촌토성(평화의문)  ...   201.0   125.0   204.0
200723  2018-12-31  2814.0        몽촌토성(평화의문)  ...   285.0   132.0   342.0
200724  2018-12-31  2815.0          잠실(송파구청)  ...  1093.0   434.0  1563.0
200725  2018-12-31  2815.0          잠실(송파구청)  ...   584.0   408.0   417.0
200726  2018-12-31  2816.0                석촌  ...   325.0   137.0   291.0
200727  2018-12-31  2816.0                석촌  ...   403.0   294.0   606.0
200728  2018-12-31  2817.0                송파  ...   121.0    62.0   102.0
200729  2018-12-31  2817.0                송파  ...   159.0   103.0   228.0
200730  2018-12-31  2818.0              가락시장  ...   341.0   164.0   132.0
200731  2018-12-31  2818.0              가락시장  ...   259.0   138.0   729.0
200732  2018-12-31  2819.0                문정  ...   352.0   186.0   177.0
200733  2018-12-31  2819.0                문정  ...   366.0   252.0   669.0
200734  2018-12-31  2820.0                장지  ...   646.0   251.0   312.0
200735  2018-12-31  2820.0                장지  ...   608.0   411.0  1089.0
200736  2018-12-31  2821.0                복정  ...   488.0   129.0   354.0
200737  2018-12-31  2821.0                복정  ...   533.0   258.0   372.0
200738  2018-12-31  2822.0                산성  ...   119.0    43.0    51.0
200739  2018-12-31  2822.0                산성  ...   358.0   193.0   459.0
200740  2018-12-31  2823.0  남한산성입구(성남법원.검찰청)  ...   220.0    94.0   123.0
200741  2018-12-31  2823.0  남한산성입구(성남법원.검찰청)  ...   677.0   419.0   846.0
200742  2018-12-31  2824.0             단대오거리  ...   239.0   145.0   153.0
200743  2018-12-31  2824.0             단대오거리  ...   553.0   370.0   816.0
200744  2018-12-31  2825.0                신흥  ...   134.0    71.0   135.0
200745  2018-12-31  2825.0                신흥  ...   219.0   179.0   396.0
200746  2018-12-31  2826.0                수진  ...   118.0    62.0   513.0
200747  2018-12-31  2826.0                수진  ...   327.0   203.0   387.0
200748  2018-12-31  2827.0                모란  ...   139.0    94.0   189.0
200749  2018-12-31  2827.0                모란  ...   185.0   113.0   828.0

[2122786 rows x 24 columns]

'''