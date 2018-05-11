package csvio;

import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * Utility methods for reading an writing CSV files.
 * @version 1.1
 */
public final class CSVFiles {
	private CSVFiles() {}

	/**
	 * Reads the file at the specified Path as a CSV file.
	 * Note that this method reads the entire file into memory.
	 */
	public static CSVFile readFile(Path file) throws IOException {
		return readFileByLines(file)
				.collect(
						CSVFile::new, CSVFile::addRow,
						(f1, f2) -> f1.addRows(f2.getRows())
				);
	}


	/**
	 * Returns a stream containing parsed lines of CSV files.
	 * This method does not read the entire file at once.
	 */
	public static Stream<List<String>> readFileByLines(Path file) throws IOException {
		return Files.lines(file)
				.map(CSVFiles::parseRow);
	}
	
	
	/**
	 * Writes the specified document to the specified location.
	 */
	public static void writeFile(CSVFile document, Path file) throws IOException {
		final BufferedWriter writer = 
				Files.newBufferedWriter(file, StandardCharsets.UTF_8);
	
		for (List<String> row : document) {
			writer.write(encodeRow(row));
			writer.newLine();
		}
		
		writer.close();
	}
	
	
	/**
	 * Splits a line in a CSV file to a list of entries, parsing any quotes if needed.
	 */
	public static List<String> parseRow(String s) {
		final List<String> row = new ArrayList<>();

		try {
			final StringBuilder buffer = new StringBuilder();
			final char[] chars = s.toCharArray();
			boolean inQuotes = false;

			for (int i = 0; i < chars.length; i++) {
				final char c = chars[i];

				if (c == '"') {	// double quote
					inQuotes = !inQuotes;
					continue;
				}

				if (c == '\\') {	// escape
					buffer.append(chars[++i]);	// add next char
					continue;
				}

				if (!inQuotes && c == ',') {
					row.add(buffer.toString());
					buffer.delete(0, buffer.length());
					continue;
				}

				buffer.append(c);
			}
		}
		catch (RuntimeException e) {
			throw new IllegalCSVFormatException(e);
		}

		return row;
	}
	
	
	/**
	 * Encodes a row for writing to the CSV file.
	 */
	public static String encodeRow(List<String> row) {
		final StringBuilder line = new StringBuilder();

		if (!row.isEmpty()) {
			final Iterator<String> it = row.iterator();

			// first entry
			line.append('"').append(it.next()).append('"');

			// the rest
			while (it.hasNext())
				line.append(",\"").append(it.next()).append('"');
		}

		return line.toString();
	}


	static class IllegalCSVFormatException extends RuntimeException {
		IllegalCSVFormatException(String message) { super(message); }
		IllegalCSVFormatException(Throwable cause) { super(cause); }
	}
}
