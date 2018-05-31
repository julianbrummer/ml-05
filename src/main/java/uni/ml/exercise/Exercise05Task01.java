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
import uni.ml.text.TextDatasetSplit;
import uni.ml.text.TextDatset;

import static uni.ml.text.TextUtil.keepLettersOnly;
import static uni.ml.text.TextUtil.filterStopwords;


/**
 * The main class for Exercise05 Task01.
 */
public class Exercise05Task01 {
	
	public static void main(String[] args) {
		TextDatset dataset = new TextDatset();
		if (args.length > 0) {
			try {
				dataset.loadFromFile(new File(args[0]), keepLettersOnly().andThen(filterStopwords()));
				TextDatasetSplit split = dataset.randomSplit(2.0f/3.0f);
				int vocabularySize = Integer.parseInt(args[1]);
				
				System.out.println("Dataset: " + dataset.name());
				BayesTextClassifier c = new BayesTextClassifier(vocabularySize);
				c.learnBayesText(split.trainingSet);
				System.out.println(c.vocabulary());
				System.out.println(c.targetValues());
				//System.out.println(c.classProbabilities());
				//System.out.println(c.conditionalWordProbabilities());
				System.out.println("accuracy: " + c.testBayesText(split.testSet));
				
				File outputPath = args.length >= 3? new File(args[2]) : null;
				
				//System.out.println(dataset);
			} catch (IOException e) {
				e.printStackTrace();				
			}
		}
	}
}
