package uni.ml.text;


/**
 * Represents a training and a test set of a text dataset.
 *
 */
public class TextDatasetSplit {
	public TextDatasetView trainingSet;
	public TextDatasetView testSet;
	
	public TextDatasetSplit(TextDatasetView trainingSet, TextDatasetView testSet) {
		this.trainingSet = trainingSet;
		this.testSet = testSet;
	}
}
