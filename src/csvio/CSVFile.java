package csvio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a CSV file.
 */
public class CSVFile {
	private final List<List<String>> contents = new ArrayList<>();

	/**
	 * Creates a new empty CSV file.
	 */
	public CSVFile() {}

	/**
	 * Creates a CSV file with specified contents.
	 * @param contents a 2-dimensional collection with each sub collection representing
	 *                 a row of the CSV file.
	 */
	public CSVFile(Collection<? extends Collection<?>> contents) {
		for (Collection<?> row : contents) {
			final List<String> sRow = new ArrayList<>();

			for (Object e : row)
				sRow.add(e != null ? e.toString() : "null");

			this.contents.add(sRow);
		}
	}


	/**
	 * Returns an unmodifiable view of the specified row.
	 */
	public List<String> getRow(int row) {
		return Collections.unmodifiableList(contents.get(row));
	}
}
