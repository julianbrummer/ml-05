package uni.ml.text;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import uni.ml.util.Sampling;



/**
 * A view on a subset of a dataset. 
 * A dataset can be decorated with multiple views.
 * For example a dataset can be decorated with an {@link DatasetIndexedView} to select instances, 
 * which itself can be decorated with a {@link DatasetPredicateView} to filter out instances according to some predicate.
 * 
 */
public abstract class TextDatasetView {
	
	/**
	 * Enables iterating over all instances, attributes in a for loop. 
	 */
	private interface ListIteratorBase<T> extends ListIterator<T>, Iterable<T> {

		int index = 0;

		@Override
		public default boolean hasPrevious() {
			return index-1 >= 0;
		}

		@Override
		public default int nextIndex() {
			return index;
		}

		@Override
		public default int previousIndex() {
			return index-1;
		}

		@Override
		public default void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public default void set(T e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public default void add(T e) {
			throw new UnsupportedOperationException();			
		}
		
		@Override
		public default Iterator<T> iterator() {
			return this;
		}
		
	}
	
	
	/**
	 * Enables iterating over all instances in a for loop. 
	 */
	private class InstanceIterator implements ListIteratorBase<TextInstance> {

		int index = 0;
		
		@Override
		public boolean hasNext() {
			return index < numInstances();
		}

		@Override
		public TextInstance next() {
			if (index >= numInstances())
				throw new NoSuchElementException();
			return instanceAt(index++);
		}

		@Override
		public TextInstance previous() {
			return instanceAt(--index);
		}
		
	}
	
	
	private String name = "unnamed";
	
	public TextDatasetView() {
	}
	
	public TextDatasetView(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
// access methods instances	
	public abstract int numInstances();
	public abstract TextInstance instanceAt(int index);

	
	public boolean hasInstances() {
		return numInstances() > 0;
	}

	public Iterable<TextInstance> instances() {
		return new InstanceIterator();
	}
	
//
	
	public int numWords() {
		int numWords = 0;
		for (TextInstance instance : instances()) {
			numWords += instance.numWords();
		}
		return numWords;
	}
    

	/**
	 * Splits the dataset(-view) randomly into a training- and a test set.
	 * @param ratio The ratio of the training set. Must be between 0 and 1.
	 * @return The training and test set.
	 */
	public TextDatasetSplit randomSplit(float ratio) {
		Sampling.Split split = Sampling.randomSplit(ratio, numInstances());
		return new TextDatasetSplit(new TextDatasetIndexedView(this, split.first()), new TextDatasetIndexedView(this, split.second()));
	}

	
	@Override
	public String toString() {	
		// append attribute list
		StringBuilder b = new StringBuilder();
		
		// append instance list
		if (hasInstances()) {
			for (int i=0; i< numInstances()-1; i++) {
				b.append(instanceAt(i).toString()).append("\n");
			}
			b.append(instanceAt(numInstances()-1).toString());
		}
		return b.toString();
	}
	
	
	
}
