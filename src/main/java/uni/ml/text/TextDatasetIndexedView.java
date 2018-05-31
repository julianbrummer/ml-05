package uni.ml.text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A view on a text dataset, that only includes instances with specific (row) indices.
 *
 */
public class TextDatasetIndexedView extends TextDatasetView {

	private TextDatasetView baseView;
	private List<Integer> indices;
	
	/**
	 * Decorates the given dataset(-view) with an index list to select instances.
	 */
	public TextDatasetIndexedView(TextDatasetView baseView, int... indices) {
		super(baseView.name());
		this.baseView = baseView;
		this.indices = Arrays.stream(indices).boxed().collect(Collectors.toList());
	}
	
	/**
	 * Decorates the given dataset(-view) with an index list to select instances.
	 */
	public TextDatasetIndexedView(TextDatasetView baseView, List<Integer> indices) {
		super(baseView.name());
		this.baseView = baseView;
		this.indices = indices;
	}
	
	
	@Override
	public int numInstances() {
		return indices.size();
	}

	
	@Override
	public TextInstance instanceAt(int index) {
		return baseView.instanceAt(indices.get(index));
	}

}
