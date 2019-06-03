//package com.example.mainmenu;
//
//public class test {
//
//        final String SIMPLE_MLP = new ClassPathResource("modelimport/keras/simple_mlp.h5").getFile().getPath();
//
//        // Keras Sequential models correspond to DL4J MultiLayerNetworks. We enforce loading the training configuration
//        // of the model as well. If you're only interested in inference, you can safely set this to 'false'.
//        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(SIMPLE_MLP, true);
//
//        // Test basic inference on the model.
//        INDArray input = Nd4j.create(256, 100);
//        INDArray output = model.output(input);
//
//        // Test basic model training.
//        model.fit(input, output);
//
//        // Sanity checks for import. First, check it optimizer is correct.
//        assert model.conf().getOptimizationAlgo().equals(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
//
//        // The first layer is a dense layer with 100 input and 64 output units, with RELU activation
//        Layer first = model.getLayer(0);
//        DenseLayer firstConf = (DenseLayer) first.conf().getLayer();
//        assert firstConf.getActivationFn().equals(Activation.RELU.getActivationFunction());
//        assert firstConf.getNIn() == 100;
//        assert firstConf.getNOut() == 64;
//
//        // The second later is a dense layer with 64 input and 10 output units, with Softmax activation.
//        Layer second = model.getLayer(1);
//        DenseLayer secondConf = (DenseLayer) second.conf().getLayer();
//        assert secondConf.getActivationFn().equals(Activation.SOFTMAX.getActivationFunction());
//        assert secondConf.getNIn() == 64;
//        assert secondConf.getNOut() == 10;
//
//        // The loss function of the Keras model gets translated into a DL4J LossLayer, which is the final
//        // layer in this MLP.
//        Layer loss = model.getLayer(2);
//        LossLayer lossConf = (LossLayer) loss.conf().getLayer();
//        assert lossConf.getLossFn().equals(LossFunctions.LossFunction.MCXENT);
//}