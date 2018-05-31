package uni.ml.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class BayesTextClassifier {
	private TextDatasetView examples;
	private int vocabularySize;
	
	private List<Entry<String, Integer>> vocabulary;
	private List<String> targetValues;
	private Map<String, Double> classProbabilities = new HashMap<>();
	private Map<String, Map<String, Double>> conditionalWordProbabilities = new HashMap<>();
	
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
	
	private List<String> extractTargetValues() {
		Set<String> values = new HashSet<>();
		for (TextInstance instance : examples.instances()) {
			values.add(instance.label());
		}
		return new ArrayList<>(values);
	}
	
	private double estimateClassProbability(TextDatasetView valueExamples) {
		return (double) valueExamples.numInstances()/examples.numInstances();
	}
	
	private Map<String, Double> estimateConditionalWordProbabilities(TextDatasetView valueExamples) {
		Map<String, Double> probs = new HashMap<>();
		int numWords = valueExamples.numWords();
		for (Entry<String,Integer> word : vocabulary) {
			String w = word.getKey();
			int numOccurrences = 0;
			for (TextInstance instance : valueExamples.instances()) {
				numOccurrences += instance.countWord(w);
			}
			double probability = (double) (numOccurrences + 1)/(numWords+vocabularySize);
			probs.put(w, probability);
		}
		return probs;
	}
	
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
	
	public void learnBayesText(TextDatasetView examples) {
		this.examples = examples;
		vocabulary = buildVocabulary(vocabularySize);
		targetValues = extractTargetValues();
		estimateProbabilities();
	}
	
	public String classifyBayesText(TextInstance instance) {
		double maxPosterior = Double.NEGATIVE_INFINITY;
		String predictedValue = null;
		for (String value : targetValues) {
			double posterior = Math.log(classProbabilities.get(value));
			for (Entry<String,Integer> word : vocabulary) {
				String w = word.getKey();
				int numOccurrences = instance.countWord(w);
				if (numOccurrences > 0) {
					posterior += Math.log(conditionalWordProbabilities.get(value).get(w));
				}
			}
			if (posterior > maxPosterior) {
				maxPosterior = posterior;
				predictedValue = value;
			}
		}
		
		return predictedValue;
	}
	
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
