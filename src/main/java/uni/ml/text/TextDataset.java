package uni.ml.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class TextDataset extends TextDatasetView {
	private List<TextInstance> instances = new ArrayList<>();
	
	/**
	 * Creates a text instance from a String 'L "text"', where L is a single character label.
	 * @param line The string to create the text instance from.
	 * @param filter Apply a filter to the words contained in the text. 
	 * A word of the text can be altered or removed by the filter. To remove a word let the filter return an empty string.
	 * @return The text instance containing the label L and a list of filtered words.
	 */
	private TextInstance createTextInstance(String line, Function<String, String> filter) {
		String label = "";
		
		if (!line.startsWith("\"")) { // if labeled
			label = line.substring(0, 1);
		}
		String text = line.substring(line.indexOf('"')+1, line.lastIndexOf('"'));
		List<String> words = TextUtil.words(text);
		return new TextInstance(label, TextUtil.filterWords(words, filter));
	}
	
	public TextDataset() {}
	public TextDataset(String name) {super(name);}
	
	/**
	 * Adds a new text instance to the dataset.
	 */
	public void addInstance(TextInstance instance) {
		instances.add(instance);
	}
	
    /**
     * Parses the given text file and adds the instances to the dataset.
     * @param filter Preprocesses each word before it is passed to the dataset.
     * A word can be altered or removed by the filter. To remove a word let the filter return an empty string.
     * @throws IOException 
     *
     */
    public void loadFromFile(File file, Function<String, String> filter) throws IOException {
    	if (unnamed())
    		setName(file.getName());
    	
		String line = null;
		BufferedReader r = new BufferedReader(new FileReader(file));
	
		while ((line = r.readLine()) != null) {
		    line = line.trim();
		    if (!line.isEmpty()) {
		    	addInstance(createTextInstance(line, filter));		    	
		    }
		}
		r.close();
    }
    

    /**
     * Parses the given text file and adds the instances to the dataset.
     * @throws IOException 
     *
     */
    public void loadFromFile(File file) throws IOException {
    	loadFromFile(file, new Function<String, String>() {		
			@Override
			public String apply(String s) {
				return s;
			}
		});
    }
    
    /**
     * Saves the dataset to a text file.
     * Writes one line per instance.
     * @param file The destination file.
     * @param includeWords Specifies whether to include the list of words or just the class label.
     * @throws IOException
     */
    public void saveToFile(File file, boolean includeWords) throws IOException {
    	try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(file), "utf-8"))) {
    		for (TextInstance instance : instances) {
				writer.write(includeWords? instance.toString() : instance.label());
				writer.write("\n");
			}
		} 
    }
    
	@Override
	public int numInstances() {
		return instances.size();
	}

	@Override
	public TextInstance instanceAt(int index) {
		return instances.get(index);
	}
	

}
