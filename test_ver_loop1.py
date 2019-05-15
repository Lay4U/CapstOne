
import pandas as pd
import numpy as np
import datetime
from keras.models import Sequential
from keras.layers import Dense, LSTM, Dropout
from keras.layers.core import Dense, Activation, Dropout
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_squared_error
import matplotlib.pyplot as plt
import winsound
import tensorflow as tf
config = tf.ConfigProto()
config.gpu_options.allow_growth=True
sess = tf.Session(config=config)



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
# data1 = df[df['name']==s[1]]
for d in s:
    data1 = df[df['name']==d]
    # data1 = df[df['name']=='서울역(150)']
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
    # 모델 구성하기

    model = Sequential()
    model.add(LSTM(128, input_shape=(None, 1), return_sequences=True))
    model.add(Dropout(0.3))

    model.add(LSTM(128, input_shape=(None, 1)))
    model.add(Dropout(0.3))


    model.add(Dense(1))

    # 모델 학습과정 설정하기
    model.compile(loss='mean_squared_error', optimizer='rmsprop', metrics=['accuracy'])
    model.summary()
    hist = model.fit(x_train, y_train, epochs=10, batch_size=16, verbose=2)
    p = model.predict(x_test)


    plt.plot(y_test)
    plt.plot(p)
    plt.legend(['testY', 'p'], loc='upper right')
    plt.title(mean_squared_error(y_test, p))
    # plt.savefig(datetime.datetime.now())
    # plt.savefig(name)
    print(mean_squared_error(y_test, p))
    # frequency = 2500  # Set Frequency To 2500 Hertz
    # duration = 1000  # Set Duration To 1000 ms == 1 second
    # winsound.Beep(frequency, duration)
    plt.savefig(d+'jpg')

# ## 저장
# model_json = model.to_json()
# with open("model1.json", "w") as json_file :
#     json_file.write(model_json)
# model.save_weights("model1.h5")
# print("Saved model to disk")

## 불러오기
# from keras.models import model_from_json
# json_file = open("model.json", "r")
# loaded_model_json = json_file.read()
# json_file.close()
# model = model_from_json(loaded_model_json)
# model.load_weights("model.h5")
# print("Loaded model from disk")

# 불러오기 후 컴파일
# model.compile(loss="binary_crossentropy", optimizer="rmsprop", metrics=['accuracy'])

# model evaluation
# score = model.evaluate(X,Y,verbose=0)


