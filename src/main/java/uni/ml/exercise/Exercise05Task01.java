/**
 * Package for Machine Learning exercises.
 * @author Julian Brummer
 * @author Alexander Petri
 * @author Hernando Amorocho
 */
package uni.ml.exercise;

import java.io.File;
import java.io.IOException;

import uni.ml.text.BayesTextClassifier;
//import uni.ml.text.TextDatasetSplit;
import uni.ml.text.TextDataset;

import static uni.ml.text.TextUtil.keepLettersOnly;
import static uni.ml.text.TextUtil.filterStopwords;


/**
 * The main class for Exercise05 Task01.
 */
public class Exercise05Task01 {
	
/*	// used for testing without test set
	public static void main(String[] args) {
		TextDataset dataset = new TextDataset("Abstracts");
		if (args.length >= 2) {
			try {
				// process dataset and split into training and test set.
				dataset.loadFromFile(new File(args[0]), keepLettersOnly().andThen(filterStopwords()));
				TextDatasetSplit split = dataset.randomSplit(2.0f/3.0f);
				
				// train
				int vocabularySize = Integer.parseInt(args[1]);	
				BayesTextClassifier c = new BayesTextClassifier(vocabularySize);
				c.learnBayesText(split.trainingSet);
				
				System.out.println("Dataset: " + dataset.name());
				System.out.println(c.vocabulary());
				System.out.println(c.targetValues());

				System.out.println("Size training set: " + split.trainingSet.numInstances());
				System.out.println("Size test set: " + split.testSet.numInstances());
				
				// test
				System.out.println("accuracy: " + c.testBayesText(split.testSet));
				
				// write out predictions
				if (args.length >= 3) {
					File outputPath = new File(args[2]);
					c.classifyBayesText(split.testSet).saveToFile(outputPath, false);
				}

			} catch (IOException e) {
				e.printStackTrace();				
			}
		}
	}
*/	
	static final int DEFAULT_VOCABULARY_SIZE = 5000;
	
	public static void main(String[] args) {
		TextDataset trainingSet = new TextDataset();
		TextDataset testSet = new TextDataset();
		if (args.length >= 3) {
			try {
				// load and process training- and test-dataset
				trainingSet.loadFromFile(new File(args[0]), keepLettersOnly().andThen(filterStopwords()));
				testSet.loadFromFile(new File(args[1]), keepLettersOnly().andThen(filterStopwords()));
				
				// train
				int vocabularySize = args.length >= 4? Integer.parseInt(args[3]) : DEFAULT_VOCABULARY_SIZE;
				BayesTextClassifier c = new BayesTextClassifier(vocabularySize);
				c.learnBayesText(trainingSet);
				
				System.out.println("TrainingSet: " + trainingSet.name());
				System.out.println("Vocabulary Size: " + c.vocabulary().size());
				System.out.println("Target Values: " + c.targetValues());
				System.out.println("TestSet: " + testSet.name());

				System.out.println("Size TrainingSet: " + trainingSet.numInstances());
				System.out.println("Size TestSet: " + testSet.numInstances());
				
				// write out predictions
				File outputPath = new File(args[2]);
				outputPath = outputPath.isDirectory()? new File(outputPath, "classification.txt") : outputPath;
				c.classifyBayesText(testSet).saveToFile(outputPath, false);
				

			} catch (IOException e) {
				System.out.println(e.getMessage());				
			}
		} else {
			System.out.println("Invalid number of arguments, run with: ");
			System.out.println("[path_to_training_file] [path_to_test_file] [path_to_output_file] [vocabulary_size (optional)]");
		}
	}
}
