import javafx.embed.swing.SwingFXUtils;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.events.LearningEventType;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;


import java.awt.image.BufferedImage;


public class MultiLayerNeuralNetwork implements LearningEventListener {

    private static final int INPUT_SIZE = 784;
    private static final int OUTPUT_SIZE = 10;
    private static final double LEARNING_RATE = 0.05;
    private static final double MAX_ERROR = 0.01;
    private static final int MAX_ITERATIONS = 5000;
    public static final String TRAINED_FILE_NAME = "trainedNetwork.nnet";

    public void run() {
        createMultiLayerPerceptron();
    }

    private void createMultiLayerPerceptron() {
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 784, 128, 64, 10);//625 30 15 10
        myMlPerceptron.randomizeWeights(-1,1);
        myMlPerceptron.setLearningRule(getBackPropagation());
        myMlPerceptron.learn(getDataSet());
        myMlPerceptron.getLearningRule().addListener(this);
        myMlPerceptron.save(TRAINED_FILE_NAME);
        System.out.println("trained");
    }

    private DataSet getDataSet() {
        DataSet trainingSet = new DataSet(INPUT_SIZE, OUTPUT_SIZE);
        String path;
        for (int i = 0; i < OUTPUT_SIZE; i++) {
            double[] output = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (String name : DirectoryUtils.getFileNamesFromPath(DirectoryUtils.PATH + i + "/")) {
                output[i] = 1;
                path = DirectoryUtils.PATH + i + "/" + name;
                BufferedImage bufferedImage = ImagesTransformation.getBinarizedImage(SwingFXUtils.fromFXImage(ImagesTransformation.scaleImage(SwingFXUtils.toFXImage(DirectoryUtils.getImageByPath(path), null), Utils.IMG_WIDTH, Utils.IMG_HEIGHT), null));
                trainingSet.addRow(new DataSetRow(ImagesTransformation.getPixelsValuesFromBinaryImage(bufferedImage), output));
            }
        }
        return trainingSet;
    }

    private BackPropagation getBackPropagation() {
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setLearningRate(LEARNING_RATE); //0.005
        backPropagation.setMaxError(MAX_ERROR);//0.02


        backPropagation.setMaxIterations(MAX_ITERATIONS);//50000
        return backPropagation;
    }


        @Override
        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation)event.getSource();
            if (event.getEventType() != LearningEventType.LEARNING_STOPPED)
                System.out.println(bp.getCurrentIteration() + ". iteration : "+ bp.getTotalNetworkError());
        }
}
