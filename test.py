import pandas as pd
import numpy as np

df = pd.read_csv('dataset_ori.csv')
del df['Unnamed: 0']
df

df1 = df.iloc[:,0:4]
df1 = df1.append([df1]*(len(df.iloc[:,4].T)-1))
df1.insert(df1.shape[1],'a',list(df.iloc[:,4:].values[0]))
df1
