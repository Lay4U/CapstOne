#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pandas as pd
import numpy as np
from keras.models import Sequential
from keras.layers import Dense, LSTM, Dropout
from keras.layers.core import Dense, Activation, Dropout
from sklearn.preprocessing import MinMaxScaler
import matplotlib.pyplot as plt
from sklearn.metrics import mean_squared_error

import winsound
import tensorflow as tf
from keras.backend import tensorflow_backend as K
config = tf.ConfigProto()
config.gpu_options.allow_growth = True
K.set_session(tf.Session(config=config))


# In[2]:


import sys
import numpy
numpy.set_printoptions(threshold=sys.maxsize)


# In[3]:


df = pd.read_csv('dataset.csv')
del df['Unnamed: 0']


# In[4]:


df


# In[5]:


df.columns


# In[6]:


df['name']


# In[7]:


s = df['name']
s


# In[8]:


s = list(set(s))
s


# In[9]:


len(s)


# In[10]:


def create_dataset(signal_data, look_back=1):
    dataX, dataY = [], []
    for i in range(len(signal_data) - look_back):
        dataX.append(signal_data[i:(i + look_back), 0])
        dataY.append(signal_data[i + look_back, 0])
    return np.array(dataX), np.array(dataY)

look_back = 20


# In[11]:


s[1]


# In[12]:


# for i in range(len(s)):
#     print(s[i] ,':', len(df[df['name']==s[i]]))


# In[13]:


20226480 - 19220360 # 71220 * 284 - rows


# In[14]:


data1 = df[df['name']=='강남']
data1


# In[15]:


name = data1.iloc[:1,2,]
name


# In[16]:


name = name.to_string(index=False)


# In[17]:


name


# In[18]:


plt.savefig(name)


# In[19]:


len(data1)


# In[20]:


data1.shape


# In[21]:


data1 = df[df['name']=='강남']
print(data1)
data = data1['a']
#     data = data.value
data = data.values.astype('float32')
data = data.reshape(len(data), 1)

scaler = MinMaxScaler(feature_range=(0, 1))
data = scaler.fit_transform(data)

train_size = int(len(data) * 0.80)
test_size = len(data) - train_size

train = data[0:train_size]
test = data[train_size:len(data)]
x_train, y_train = create_dataset(train, look_back)
x_test, y_test = create_dataset(test, look_back)

x_train = np.reshape(x_train, (x_train.shape[0], x_train.shape[1], 1))
x_test = np.reshape(x_test, (x_test.shape[0], x_test.shape[1], 1))
y_train=np.repeat(y_train.reshape(-1,1), 20, axis=1).reshape(-1,20,1)
y_test=np.repeat(y_test.reshape(-1,1), 20, axis=1).reshape(-1,20,1)
# 모델 구성하기


# In[22]:


model = Sequential()
model.add(LSTM(512,  return_sequences=True))
model.add(Dropout(0.3))

model.add(LSTM(512,  return_sequences=True))
model.add(Dropout(0.3))
# model.add(LSTM(i, input_shape=(None, 1)))

# model.add(Dense(i))
# model.add(Dropout(0.3))
# model.add(Dropout(0.3))
# model.add(Dense(64))

model.add(LSTM(1, return_sequences=True))


# 수동학습
model.compile(loss='mean_squared_error', optimizer='rmsprop', metrics=['accuracy'])
# model.summary()
model.fit(x_train, y_train, epochs=10, batch_size=64)


# In[72]:


# model = Sequential()
# model.add(LSTM(512, batch_input_shape=(1, look_back, 1), stateful=True))
# model.add(Dropout(0.3))
# model.add(Dense(1))
# model.compile(loss='mean_squared_error', optimizer='adam')


# for i in range(200):
#     model.fit(x_train, y_train, epochs=1, batch_size=1, shuffle=False)
#     model.reset_states()


# # model.add(LSTM(512, input_shape=(None, 1)))
# # model.add(Dropout(0.3))
# # model.add(LSTM(i, input_shape=(None, 1)))

# # model.add(Dense(i))
# # model.add(Dropout(0.3))
# # model.add(Dropout(0.3))
# # model.add(Dense(64))


# # name = "tt.jpg"
# # 수동학습
# # model.compile(loss='mean_squared_error', optimizer='rmsprop', metrics=['accuracy'])
# # hist = model.fit(x_train, y_train, epochs=10, batch_size=64)

# # 저장된거불러오기
# from keras.models import load_model
# # model = load_model('trythis.h5')
# # model.fit(x_train, y_train, epochs=10, batch_size=64)
# model.summary()


# In[23]:

#
#p = model.predict(x_test)
#
#plt.plot(y_test)
#plt.plot(p)
#plt.legend(['testY', 'p'], loc='upper right')
#plt.title(mean_squared_error(y_test, p))
#plt.show()
## plt.savefig(datetime.datetime.now())
## plt.savefig(name)
## plt.clf()
#print(mean_squared_error(y_test, p))


# In[25]:


predictions = model.predict(x_train)
pritn(predictions)


# In[26]:


predictions.shape


# In[30]:


future = []
currentStep = predictions[:, -20:, :]

for i in range(10):
    currentStep = model.predict(currentStep)
    future.append(currentStep)

# model.reset_states()


# In[ ]:


future





