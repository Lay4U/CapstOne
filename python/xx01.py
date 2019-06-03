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
print(x_train.shape)
print("y_train :", y_train.shape)
print(y_test.shape)
x_train = np.reshape(x_train, (x_train.shape[0], x_train.shape[1], 1))
x_test = np.reshape(x_test, (x_test.shape[0], x_test.shape[1], 1))

y_train=np.repeat(y_train.reshape(-1,1), 20, axis=1).reshape(-1,20,1)
y_test=np.repeat(y_test.reshape(-1,1), 20, axis=1).reshape(-1,20,1)
# y_train = y_train[len(y_train)%look_back:]
# y_test = y_test[len(y_test)%look_back:]
# y_train = np.reshape(y_train, (-1, look_back, 1))
# y_test = np.reshape(y_test, (-1, look_back, 1))
# print(x_train.shape)
print(y_train.shape)
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


from keras.models import load_model
model.save('xx.h5')
# 수동학습
# model.compile(loss='mean_squared_error', optimizer='rmsprop', metrics=['accuracy'])
# hist = model.fit(x_train, y_train, epochs=10, batch_size=64)

# 저장된거불러오기
# from keras.models import load_model
# model = load_model('trythis.h5')
# model.summary()


# In[23]:


p = model.predict(x_test)

plt.plot(y_test)
plt.plot(p)
plt.legend(['testY', 'p'], loc='upper right')
plt.title(mean_squared_error(y_test, p))
plt.show()
# plt.savefig(datetime.datetime.now())
# plt.savefig(name)
# plt.clf()
print(mean_squared_error(y_test, p))


# In[69]:


predictions = model.predict(x_train)


# In[70]:


# predictions = predictions.shape(62796,1,1)
# b = a[:, :, newaxis]
predictions = predictions[:, :, np.newaxis]
predictions.shape


# In[75]:


future = []
currentStep = predictions[:, -1:, :]


# In[76]:


currentStep = currentStep[16:]


# In[77]:


currentStep.shape


# In[74]:


# currentStep = currentStep.reshape(3139, look_back, 1)
# x_train = np.reshape(x_train, (x_train.shape[0], x_train.shape[1], 1))
# currentStep = np.reshape(currentStep, (-1, ))


# In[78]:


currentStep.shape


# In[80]:


future=[]

# for i in range(100):
#     print(i)
#     currentStep = model.predict(currentStep)
#     currentStep = currentStep[:, :, np.newaxis]
#     print(currentStep.shape)
#     future.append(currentStep)


# In[ ]:


print(future)


# In[65]:


a = np.asarray(future)


# In[66]:


a.shape


# In[68]:


a.reshape(1, -1)


# In[ ]:





# In[ ]:





# In[ ]:





# In[117]:


predictions = np.zeros((look_ahead, 1))


# In[118]:


for i in range(look_ahead):
    prediction = model.predict(np.array([xhat]), batch_size=1)
    predictions[i] = prediction
    xhat = np.vstack([xhat[1:], prediction])


# In[119]:


xhat


# In[120]:


plt.plot(predictions)


# In[113]:


predictions


# In[98]:


p[-10:]


# In[96]:


y_test[-10:]


# In[97]:


x_test[-10:]


# In[ ]:


x_train.shape


# In[ ]:


# x_train = np.reshape(x_train, (len(x_train), 1, 1))


# In[ ]:


predictions = model.predict(x_train)


# In[ ]:


predictions.shape


# In[ ]:


future = []
currentStep = predictions[:, -1: :]


# In[ ]:


for i in range(50):
    currentStep = model.predict(currentStep)
    future.append(currentStep)


# In[ ]:


# # Plot training & validation loss values
# plt.plot(hist.history['loss'])
# plt.title('Model loss')
# plt.ylabel('Loss')
# plt.xlabel('Epoch')
# plt.legend(['Train', 'Test'], loc='upper left')
# plt.show()


# In[ ]:


plt.plot(y_test)
plt.plot(p)
plt.legend(['testY', 'prediction'], loc='best')
plt.title('LSTM(512)+LSTM(1024)')
plt.ylabel('embarked personnel')
plt.xlabel('Time')


# In[ ]:


trainPredict = model.predict(x_train)
testPredict = model.predict(x_test)
# invert predictions
trainPredict = scaler.inverse_transform(trainPredict)
trainY = scaler.inverse_transform([y_train])
testPredict = scaler.inverse_transform(testPredict)
testY = scaler.inverse_transform([y_test])

# shift train predictions for plotting
trainPredictPlot = np.empty_like(data)
trainPredictPlot[:, :] = np.nan
trainPredictPlot[look_back:len(trainPredict)+look_back, :] = trainPredict
# shift test predictions for plotting
testPredictPlot = np.empty_like(data)
testPredictPlot[:, :] = np.nan
testPredictPlot[len(trainPredict)+(look_back*2):len(data)+2, :] = testPredict
# plot baseline and predictions
plt.plot(scaler.inverse_transform(data))
plt.plot(trainPredictPlot)
plt.plot(testPredictPlot)
plt.legend(['data', 'train','predict'], loc='best')
plt.show()


# In[ ]:


print(y_train.shape)
print(y_test.shape)


# In[ ]:


yhat = y_test
yhat = yhat.reshape(len(yhat), 1, 1)


# In[ ]:


y = model.predict(yhat)
y


# In[ ]:


data = y

data = data.reshape(len(data), 1)

scaler = MinMaxScaler(feature_range=(0, 1))
data = scaler.fit_transform(data)

train_size = int(len(data) * 0.80)
test_size = len(data) - train_size

train = data[0:train_size]
test = data[train_size:len(data)]
x_train, y_train = create_dataset(train, look_back)
x_test, y_test = create_dataset(test, look_back)

from xgboost import XGBRegressor
XGBModel = XGBRegressor()
XGBModel.fit(x_train,y_train, verbose=False)

p = XGBModel.predict(x_test)
plt.plot(y_test)
plt.plot(p)
plt.legend(['testY', 'p'], loc='upper right')
plt.title(mean_squared_error(y_test, p))

plt.show()
print(mean_squared_error(y_test, p))


# In[ ]:


plt.plot(y_test)
plt.xlim(0, 100)


# In[ ]:


plt.plot(p)
plt.xlim(0, 100)


# In[ ]:


print(y_test[:100])


# In[ ]:


print(p[:100])


# In[ ]:


plt.scatter(len(y_test), y_test)


# In[ ]:


data = y
#     data = data.value
data = data.values.astype('float32')
data = data.reshape(len(data), 1)

scaler = MinMaxScaler(feature_range=(0, 1))
data = scaler.fit_transform(data)

train_size = int(len(data) * 0.80)
test_size = len(data) - train_size

train = data[0:train_size]
test = data[train_size:len(data)]
# x_train, y_train = create_dataset(train, look_back)
# x_test, y_test = create_dataset(test, look_back)

# x_train = np.reshape(x_train, (x_train.shape[0], x_train.shape[1], 1))
# x_test = np.reshape(x_test, (x_test.shape[0], x_test.shape[1], 1))


# In[ ]:


# x = range(0,len(y))
x = np.arange(0, len(y))
x=x.reshape(len(x),1)


# In[ ]:


print(len(x))
print(len(y))
print(x.shape)
print(y.shape)
print(type(y))
print(type(x))


# In[ ]:


x=x.reshape(len(x),1)
x.shape


# In[ ]:


data = y

from sklearn.model_selection import train_test_split
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=42)

XGBModel = XGBRegressor()
XGBModel.fit(x_train,y_train, verbose=False)

p = XGBModel.predict(x_test)
plt.plot(y_test)
plt.plot(p)
plt.legend(['testY', 'p'], loc='upper right')
plt.title(mean_squared_error(y_test, p))

plt.show()
print(mean_squared_error(y_test, p))


# In[ ]:


plt.plot(x_train)
plt.plot(y_train)
plt.show()


# In[ ]:


plt.plot(x_train)


# In[ ]:


plt.plot(y_train)


# In[ ]:


plt.plot(p)


# In[ ]:


from xgboost import XGBRegressor
# import xgboost as xgb
# from xgboost.sklearn import XGBRegressor
XGBModel = XGBRegressor()
XGBModel.fit(x,y, verbose=False)

# Get the mean absolute error on the validation data :
# XGBpredictions = XGBModel.predict(val_X)
# MAE = mean_absolute_error(val_y , XGBpredictions)
# print('XGBoost validation MAE = ',MAE)


# In[ ]:


from xgboost import plot_tree
plot_tree(XGBModel)


# In[ ]:


from sklearn.ensemble import RandomForestRegressor
model = RandomForestRegressor()
model.fit(x,y)

# Get the mean absolute error on the validation data
predicted_prices = model.predict(val_X)
MAE = mean_absolute_error(val_y , predicted_prices)
print('Random forest validation MAE = ', MAE)


# In[ ]:


# 저장
model_json = model.to_json()
with open("s[0].json", "w") as json_file :
    json_file.write(model_json)
model.save_weights("s[0].h5")
print("Saved model to disk")


# In[ ]:


# # 모델 불러오기
# from keras.models import model_from_json
# json_file = open("model1.json", "r")
# model_json = json_file.read()
# json_file.close()
# model = model_from_json(loaded_model_json)
# model.load_weights("model1.h5")
# model.compile(loss="mean_squared_error", optimizer="rmsprop", metrics=['accuracy'])


# In[ ]:


from keras.utils import plot_model
plot_model(loaded_model, to_file='model.png')
from IPython.display import SVG
from keras.utils.vis_utils import model_to_dot

SVG(model_to_dot(loaded_model).create(prog='dot', format='svg'))
plot_model(loaded_model, to_file='model_plot.png')


# In[ ]:


SVG(model_to_dot(loaded_model).create(prog='dot', format='svg'))


# In[ ]:


plt.plot(y_test)
plt.plot(p)
plt.legend(['testY', 'p'], loc='upper right')
plt.title(mean_squared_error(y_test, p))
# plt.savefig(datetime.datetime.now())
plt.show()
print(mean_squared_error(y_test, p))


# In[ ]:


p = model.predict(x_test)

plt.plot(y_test)
plt.plot(p)
plt.legend(['testY', 'p'], loc='upper right')
plt.title(mean_squared_error(y_test, p))
# plt.savefig(datetime.datetime.now())
plt.savefig(name)
plt.show()
print(mean_squared_error(y_test, p))


# In[ ]:


data = y # p
scaler = MinMaxScaler(feature_range=(0, 1))
data = scaler.fit_transform(data)
x = np.arange(0, len(y))
x=x.reshape(len(x),1)
x = scaler.fit_transform(x)

train_size = int(len(data) * 0.80)
test_size = len(data) - train_size

y_train = data[0:train_size]
y_test = data[train_size:len(data)]
x_train = x[0:train_size]
x_test = x[train_size:len(x)]
# from sklearn.model_selection import train_test_split
# x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.8, random_state=42)

XGBModel = XGBRegressor()
XGBModel.fit(x_train,y_train, verbose=False)

p = XGBModel.predict(x_test)
plt.plot(y_test)
plt.plot(p)
plt.legend(['y_test', 'p'], loc='upper right')
plt.title(mean_squared_error(y_test, p))

plt.show()
print(mean_squared_error(y_test, p))


# In[ ]:


plt.plot(p)


# In[ ]:


plt.plot(y_test)


# In[ ]:




