package uni.ml.text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


/**
 * A view on a text dataset, that only includes instances that comply with a given predicate.
 *
 */
public class TextDatasetPredicateView extends TextDatasetIndexedView {

	/**
	 * Computes an index list of instances that comply with the specified predicate.
	 * @param baseView The dataset(-view) to search for instances.
	 * @param predicate The predicate to test for each instance.
	 */
	private static List<Integer> validIndices(TextDatasetView baseView, Predicate<TextInstance> predicate) {
		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < baseView.numInstances(); i++) {
			if (predicate.test(baseView.instanceAt(i))) {
				indices.add(i);
			}
		}
		return indices;
	}
	
	/**
	 * Decorates the given text dataset(-view) with a predicate to filter instances.
	 */
	public TextDatasetPredicateView(TextDatasetView baseView, Predicate<TextInstance> predicate) {
		super(baseView, validIndices(baseView, predicate));
	}
	
	/**
	 * Selects all instances within the provided text dataset(-view) which have the specified label.
	 * @param dataset The text dataset(-view) to create the subset from.
	 * @return the subset view on the dataset.
	 */
	public static TextDatasetPredicateView selectInstances(TextDatasetView dataset, String label) {
		return new TextDatasetPredicateView(dataset, (instance) -> label.equals(instance.label()));
	}

}
