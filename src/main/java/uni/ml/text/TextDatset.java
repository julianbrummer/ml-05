package uni.ml.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class TextDatset extends TextDatasetView {
	private List<TextInstance> instances = new ArrayList<>();
	
	private TextInstance createTextInstance(String line, Function<String, String> filter) {
		String label = line.substring(0, 1);
		String text = line.substring(line.indexOf('"')+1, line.lastIndexOf('"'));
		List<String> words = TextUtil.words(text);
		return new TextInstance(label, TextUtil.filterWords(words, filter));
	}
	
    /**
     * Parses the given text file and adds the instances to the dataset.
     * @param filter Preprocesses each word before it is passed to the dataset.
     * A word can be altered or removed by the filter. To remove a word let the filter return an empty string.
     * @throws IOException 
     *
     */
    public void loadFromFile(File file, Function<String, String> filter) throws IOException {
		String line = null;
		BufferedReader r = new BufferedReader(new FileReader(file));
	
		while ((line = r.readLine()) != null) {
		    line = line.trim();
		    if (!line.isEmpty()) {
		    	instances.add(createTextInstance(line, filter));		    	
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
    
	@Override
	public int numInstances() {
		return instances.size();
	}

	@Override
	public TextInstance instanceAt(int index) {
		return instances.get(index);
	}
	

}
