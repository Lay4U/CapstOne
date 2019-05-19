from keras.models import Sequential
from keras.layers.core import Dense, Dropout, Activation
from keras.optimizers import SGD
from keras import backend as K
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
from tensorflow.python.tools import freeze_graph, optimize_for_inference_lib

import numpy as np

def export_model_for_mobile(model_name, input_node_name, output_node_name):
    tf.train.write_graph(K.get_session().graph_def, 'out', \
        model_name + '_graph.pbtxt')

    tf.train.Saver().save(K.get_session(), 'out/' + model_name + '.chkp')

    freeze_graph.freeze_graph('out/' + model_name + '_graph.pbtxt', None, \
        False, 'out/' + model_name + '.chkp', output_node_name, \
        "save/restore_all", "save/Const:0", \
        'out/frozen_' + model_name + '.pb', False, "")

    input_graph_def = tf.GraphDef()
    with tf.gfile.Open('out/frozen_' + model_name + '.pb', "rb") as f:
        input_graph_def.ParseFromString(f.read())

    output_graph_def = optimize_for_inference_lib.optimize_for_inference(
            input_graph_def, [input_node_name], [output_node_name],
            tf.float32.as_datatype_enum)

    with tf.gfile.FastGFile('out/tensorflow_lite_' + model_name + '.pb', "wb") as f:
        f.write(output_graph_def.SerializeToString())


def main():


    df = pd.read_csv('dataset.csv')
    del df['Unnamed: 0']
    s = df['name']
    s = list(set(s))


    def create_dataset(signal_data, look_back=1):
        dataX, dataY = [], []
        for i in range(len(signal_data) - look_back):
            dataX.append(signal_data[i:(i + look_back), 0])
            dataY.append(signal_data[i + look_back, 0])
        return np.array(dataX), np.array(dataY)

    look_back = 7



    data1 = df[df['name'] == '강남(222)']
    data1
    name = data1.iloc[:1, 2, ]
    name = name.to_string(index=False)
    # plt.savefig(name)



    data1 = df[df['name'] == '강남(222)']
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
    X = x_train
    Y = y_train

    model = Sequential()
    model.add(LSTM(512, input_shape=(None, 1), return_sequences=True))
    model.add(Dropout(0.3))

    model.add(LSTM(512, input_shape=(None, 1)))
    model.add(Dropout(0.3))
    model.add(Dense(1))

    model.compile(loss='mean_squared_error', optimizer='rmsprop', metrics=['accuracy'])
    model.fit(X, Y, epochs=1, batch_size=64)


    export_model_for_mobile('predict_subway', "lstm_input", "dense_output")
    model.summary()

if __name__=='__main__':
    main()
