package csvio;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a CSV file.
 */
public final class CSVFile implements Iterable<List<String>> {

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
	public CSVFile(Collection<? extends Collection<?>> contents) { addRows(contents); }


	/**
	 * Returns an unmodifiable view of the specified row.
	 */
	public List<String> getRow(int index) {
		return Collections.unmodifiableList(contents.get(index));
	}

	/**
	 * Adds a row to the end of this document.
	 */
	public boolean addRow(Collection<?> row) {
		return contents.add(toStringList(row));
	}

	/**
	 * Inserts a row at the specified index.
	 */
	public void insertRow(int index, Collection<?> row) {
		contents.add(index, toStringList(row));
	}

	public void addRows(Collection<? extends Collection<?>> contents) {
		for (Collection<?> row : contents)
			addRow(row);
	}

	public List<String> removeRow(int index) {
		return contents.remove(index);
	}


	private static List<String> toStringList(Collection<?> c) {
		return c.stream()
				.map(Objects::toString)
				.collect(Collectors.toList());
	}

	@Override
	public Iterator<List<String>> iterator() { return contents.iterator(); }
}
