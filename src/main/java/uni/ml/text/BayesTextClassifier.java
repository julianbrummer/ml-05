package uni.ml.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * A Bayes classifier for texts.
 *
 */
public class BayesTextClassifier {
	private TextDatasetView examples;
	private int vocabularySize;
	
	private List<Entry<String, Integer>> vocabulary;
	private List<String> targetValues;
	private Map<String, Double> classProbabilities = new HashMap<>();
	private Map<String, Map<String, Double>> conditionalWordProbabilities = new HashMap<>();
	
	/**
	 * Builds a vocabulary, containing the most frequently used words in examples.
	 * @param size The size (number of words) of the vocabulary
	 * @return The vocabulary: A list of pairs (word, count), sorted by count in descending order.
	 */
	private List<Entry<String, Integer>> buildVocabulary(int size) {
		Map<String, Integer> wordCount = new HashMap<>();
		for (TextInstance instance : examples.instances()) {
			for (String word : instance.words()) {
				wordCount.compute(word, (k, v) -> v == null? 1 : v+1);
			}
		}
		List<Entry<String, Integer>> wordCounts = new ArrayList<>(wordCount.entrySet());
		wordCounts.sort((w1, w2) -> w2.getValue() - w1.getValue());
		return wordCounts.subList(0, size);
	}
	
	/**
	 * Extracts all target values (labels) occurring in examples.
	 */
	private List<String> extractTargetValues() {
		Set<String> values = new HashSet<>();
		for (TextInstance instance : examples.instances()) {
			values.add(instance.label());
		}
		return new ArrayList<>(values);
	}
	
	/**
	 * Computes the probability of a class/label P(v) from examples. 
	 * @param valueExamples All examples with class value v.
	 * @return The class probability P(v) = |valueExamples|/|examples|
	 */
	private double estimateClassProbability(TextDatasetView valueExamples) {
		return (double) valueExamples.numInstances()/examples.numInstances();
	}
	
	/**
	 * Computes the conditional probability P(w|v) for each word w in the vocabulary.
	 * @param valueExamples All examples with class value v.
	 * @return The conditional propabilities P(wk|v) = (nk+1)/(n+|Vocabulary|), 
	 * where nk is the total number of times the word wk occurs in valueExamples.
	 */
	private Map<String, Double> estimateConditionalWordProbabilities(TextDatasetView valueExamples) {
		Map<String, Double> probs = new HashMap<>();
		int numWords = valueExamples.numWords();
		// for each word wk in vocabulary
		for (Entry<String,Integer> word : vocabulary) {
			String w = word.getKey();
			int numOccurrences = 0; // nk
			for (TextInstance instance : valueExamples.instances()) {
				numOccurrences += instance.countWord(w);
			}
			// P(wk|v)
			double probability = (double) (numOccurrences + 1)/(numWords+vocabularySize);
			probs.put(w, probability);
		}
		return probs;
	}
	
	/**
	 * Computes class and conditional probabilities for each target value/class label.
	 */
	private void estimateProbabilities() {
	    for (String value : targetValues) {
			TextDatasetView valueExamples = TextDatasetPredicateView.selectInstances(examples, value);
			classProbabilities.put(value, estimateClassProbability(valueExamples));
			conditionalWordProbabilities.put(value, estimateConditionalWordProbabilities(valueExamples));
	    }
	}
	
	
	public BayesTextClassifier(int vocabularySize) {
		this.vocabularySize = vocabularySize;
	}
	
	/**
	 * Trains the Bayes classifier with examples.
	 * @param examples The training set.
	 */
	public void learnBayesText(TextDatasetView examples) {
		this.examples = examples;
		vocabulary = buildVocabulary(vocabularySize);
		targetValues = extractTargetValues();
		estimateProbabilities();
	}
	
	/**
	 * Classifies a text instance with this bayes classifier.
	 * Make sure to train it first using learnBayesText().
	 * @param instance The text instance to classifiy.
	 * @return The predicted class label.
	 */
	public String classifyBayesText(TextInstance instance) {
		double maxPosterior = Double.NEGATIVE_INFINITY;
		String predictedValue = null;
		// iterate over each possible target value v, to find maximum
		// use logarithm of probabilities for numerical stability
		for (String value : targetValues) {
			// ln(P(v))
			double posterior = Math.log(classProbabilities.get(value));
			// for each word wk in vocabulary
			for (Entry<String,Integer> word : vocabulary) {
				String w = word.getKey();
				int numOccurrences = instance.countWord(w);
				if (numOccurrences > 0) { // if word is present in instance
					// sum ln(P(wk|v))
					posterior += Math.log(conditionalWordProbabilities.get(value).get(w));
				}
			}
			// find max
			if (posterior > maxPosterior) {
				maxPosterior = posterior;
				predictedValue = value;
			}
		}
		
		return predictedValue;
	}
	
	/**
	 * Classifies a full dataset.
	 * @param dataset The test set.
	 * @return A text dataset containing the instances with predicted class labels.
	 */
	public TextDataset classifyBayesText(TextDatasetView dataset) {
		TextDataset classifiedDataset = new TextDataset();
		for (TextInstance instance : dataset.instances()) {
			classifiedDataset.addInstance(new TextInstance(classifyBayesText(instance), instance.words()));
		}
		return classifiedDataset;
	}
	
	/**
	 * Test bayes classifier using a dataset with known class labels.
	 * @return The accuracy of the classifier.
	 */
	public float testBayesText(TextDatasetView testSet) {
		int correctlyClassified = 0;
		for (TextInstance instance : testSet.instances()) {
			if (classifyBayesText(instance).equals(instance.label()))
				correctlyClassified++;
		}
		return (float) correctlyClassified/testSet.numInstances();
	}
	
	public List<Entry<String, Integer>> vocabulary() {
		return vocabulary;
	}
	
	public List<String> targetValues() {
		return targetValues;
	}
	
	public Map<String, Double> classProbabilities() {
		return classProbabilities;
	}
	
	public Map<String, Map<String, Double>> conditionalWordProbabilities() {
		return conditionalWordProbabilities;
	}
}
