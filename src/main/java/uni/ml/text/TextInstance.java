package uni.ml.text;

import java.util.ArrayList;
import java.util.List;

/**
 * A text instance contains a class label (optional) and a list of (possibly duplicated) words.
 *
 */
public class TextInstance {
	private String label;
	private List<String> words;
	
	public TextInstance(String label) {
		this.label = label;
		this.words = new ArrayList<>();
	}
	
	public TextInstance(String label, List<String> words) {
		this.label = label;
		this.words = words;
	}
	
	public String label() {
		return label;
	}
	
	public List<String> words() {
		return words;
	}
	
	public void addWord(String word) {
		words.add(word);
	}
	
	public int numWords() {
		return words.size();
	}

	/**
	 * Counts how often a word appears in the list of words.
	 * @param w the word to count.
	 * @return The number of times w occurs in words.
	 */
	public int countWord(String w) {
		int count = 0;
		for (String word : words) {
			if (w.equals(word))
				count++;
		}
		return count;
	}
	
	@Override
	public String toString() {
		return label + "  " + words;
	}

	
}
