package nl.zakarias.constellation.raid.configuration;

import ibis.constellation.*;
import nl.zakarias.constellation.raid.models.ModelInterface;
import nl.zakarias.constellation.raid.models.cifar10.Cifar10;
import nl.zakarias.constellation.raid.models.mnist.Mnist;
import nl.zakarias.constellation.raid.models.mnist_cnn.MnistCnn;
import nl.zakarias.constellation.raid.models.tiny_yolo.TinyYolo;
import nl.zakarias.constellation.raid.models.yolo.Yolo;

import java.io.Serializable;

/**
 * Change parameters in this file to modify the behaviour of Constellation.
 *
 * In order to turn on/off logging, see the log4j.properties in the root directory.
 */
public class Configuration {
    /** Change this to make the all sources run forever */
    public static final boolean ENDLESS = false;

    /** Whether to write the prediction results to the log file */
    public static final boolean LOG_PREDICTIONS = false;

    /** Default output file for logs, can be changed with argument -outputFile */
    public static final String DEFAULT_OUTPUT_FILE = "output.log";

    /** Cifar10 specific values */
    public static final int CIFAR_10_FILE_LENGTH = 10000;
    public static final int CIFAR_IMAGE_WIDTH = 32;
    public static final int CIFAR_IMAGE_HEIGHT = 32;

    /** How long to try to shutdown an agent gracefully, timeout results in killing that process */
    public static final int SHUTDOWN_HOOK_TIMEOUT = 60; // Seconds

    /** Number of images to send in each batch */
    public static final int BATCH_SIZE = 1;

    /** Number of of batches a source should send, if ENDLESS == true, this value is ignored */
    public static final int BATCH_COUNT = 1;

    /** Time to wait between submitting images in MS (be aware of memory limitations when using low values) */
    public static final int TIME_INTERVAL = 100;

    /** Threshold for how much available Java heap memory (KB) we need to upload new Activities */
    public static final int HEAP_MEMORY_THRESHOLD = 50000; //50 MB

    public enum NODE_ROLES {
        SOURCE("SOURCE"),
        TARGET("TARGET"),
        PREDICTOR("PREDICTOR");

        private final String name;

        NODE_ROLES(String s){
            name = s;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * All available models. If manually adding a new model to tensorflow/tensorflow_serving/models/ it must be
     * added to this enum, as well as returned from {@link #getModel(ModelName) getModel}.
     */
    public enum ModelName implements Serializable {
        MNIST("MNIST"), MNIST_CNN("MNIST_CNN"), YOLO("YOLO"),
        TINY_YOLO("TINY_YOLO"), CIFAR10("CIFAR10");

        private final String name;

        ModelName(String s){
            name = s;
        }

        @Override
        public String toString(){
            return this.name;
        }
    }

    /**
     * Return a new model instance matching the input parameter. If manually adding a new model to
     * tensorflow/tensorflow_serving/models/ it must be returned from this methods as well as added to the
     * {@link ModelName ModelName} enum.
     *
     * @param modelName Name of the model, from command line
     * @return A new model to classify with
     */
    public static ModelInterface getModel(ModelName modelName){
        switch (modelName) {
            case MNIST:
                return new Mnist();
            case MNIST_CNN:
                return new MnistCnn();
            case YOLO:
                return new Yolo();
            case CIFAR10:
                return new Cifar10();
            case TINY_YOLO:
                return new TinyYolo();
            default:
                return null;
        }
    }

    public static final Context TARGET_CONTEXT = new Context("target");

    public static String nodeRoleValues(){
        StringBuilder result = new StringBuilder();

        for (NODE_ROLES node : NODE_ROLES.values()){
            result.append(node.toString()).append(" ");
        }

        return result.toString();
    }

    public static String InferenceModelEnumToString(){
        StringBuilder result = new StringBuilder();

        for (ModelName model : ModelName.values()){
            result.append(model.toString()).append(" ");
        }

        return result.toString();
    }

}
