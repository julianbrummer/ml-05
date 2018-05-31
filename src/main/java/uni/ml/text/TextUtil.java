package uni.ml.text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import uni.ml.util.Stopwords;

public class TextUtil {

	public static List<String> words(String text) {
		String[] words = text.split(" ");
		List<String> wordList = new ArrayList<>(words.length);
		for (String word : words) {
			String w = word.trim(); 
			if (!w.isEmpty())
				wordList.add(w);
		}
		return wordList;
	}
	
	public static Function<String, String> keepLettersOnly() {
		return new Function<String, String>() {

			@Override
			public String apply(String s) {
				return s.replaceAll("[^a-zA-Z]+", "");
			}
		};
	}
	
	public static Function<String, String> filterStopwords() {
		return new Function<String, String>() {

			@Override
			public String apply(String s) {
				return Stopwords.isStemmedStopword(s)? "" : s;
			}
		};
	} 
	
	public static List<String> filterWords(List<String> words, Function<String, String> filter) {
		List<String> filteredWords = new ArrayList<>(); 
		for (String word : words) {
			String processedWord = filter.apply(word);
			if (!processedWord.isEmpty())
				filteredWords.add(processedWord);
		}
		return filteredWords;
	}
	
}
