

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



df = pd.read_csv('dataset.csv')
del df['Unnamed: 0']


s = df['name']
s = list(sorted(set(s)))


def create_dataset(signal_data, look_back=1):
    dataX, dataY = [], []
    for i in range(len(signal_data) - look_back):
        dataX.append(signal_data[i:(i + look_back), 0])
        dataY.append(signal_data[i + look_back, 0])
    return np.array(dataX), np.array(dataY)

look_back = 20





data1 = df[df['name']==s[1]]


# In[14]:


data1.columns


# In[15]:


data1


# In[16]:


len(data1)


# In[17]:


data1.shape

for i in range(66):
    s.pop(0)

# In[18]:

for d in s:
    data1 = df[df['name']==d]
    name = data1.iloc[:1, 2, ]
    name = name.to_string(index=False)
    data = data1['a']
    print(name)
    data = data.values.astype('float32')
    data = data.reshape(len(data), 1)

    scaler = MinMaxScaler(feature_range=(0, 1))
    data = scaler.fit_transform(data)

    train_size = int(len(data) * 0.9)
    test_size = len(data) - train_size

    train = data[0:train_size]
    test = data[train_size:len(data)]
    x_train, y_train = create_dataset(train, look_back)
    x_test, y_test = create_dataset(test, look_back)

    x_train = np.reshape(x_train, (x_train.shape[0], x_train.shape[1], 1))
    x_test = np.reshape(x_test, (x_test.shape[0], x_test.shape[1], 1))
    # 모델 구성하기


    # In[22]:


    model = Sequential()
    model.add(LSTM(512, input_shape=(None, 1), return_sequences=True))
    model.add(Dropout(0.3))

    model.add(LSTM(512, input_shape=(None, 1)))
    model.add(Dropout(0.3))
    # model.add(LSTM(i, input_shape=(None, 1)))

    # model.add(Dense(i))
    # model.add(Dropout(0.3))
    # model.add(Dropout(0.3))
    # model.add(Dense(64))

    model.add(Dense(1))

    # 모델 학습과정 설정하기
    model.compile(loss='mean_squared_error', optimizer='rmsprop', metrics=['accuracy'])
    model.summary()
    model.fit(x_train, y_train, epochs=10, batch_size=64, verbose=2)
    # from keras.models import load_model
    # model = load_model('trythis.h5')


    # In[ ]:


    # x_test = scaler.inverse_transform(x_test)
    # y_test = scaler.inverse_transform(y_test)
    # p = model.predict(x_test)
    # np.savetxt(name+".csv", p, delimiter=",")
    # print("**************************",mean_squared_error(y_test, p,"**************************"))
    # print()
    # plt.plot(y_test)
    # plt.plot(p)
    # plt.legend(['testY', 'p'], loc='upper right')
    # plt.title(mean_squared_error(y_test, p))
    # # plt.savefig(datetime.datetime.now())
    # plt.show()


    pred_p = model.predict(x_test)
    p = scaler.inverse_transform(pred_p)
    np.savetxt(name + ".csv", p, delimiter=",")
