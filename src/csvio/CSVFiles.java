package csvio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for reading an writing CSV files.
 */
public final class CSVFiles {
	private CSVFiles() {}

	/**
	 * Reads the file at the specified Path as a CSV file.
	 */
	public static CSVFile readFile(Path file) throws IOException {
		final CSVFile csvFile = new CSVFile();

		Files.readAllLines(file).stream()
				.map(CSVFiles::parseRow)
				.forEach(csvFile::addRow);

		return csvFile;
	}

	/**
	 * Splits a line in a CSV file to a list of entries, parsing any quotes if needed.
	 */
	private static List<String> parseRow(String s) {
		final List<String> row = new ArrayList<>();

		try {
			final StringBuilder buffer = new StringBuilder();
			final char[] chars = s.toCharArray();
			boolean inQuotes = false;

			for (int i = 0; i < chars.length; i++) {
				final char c = chars[i];

				if (c == '\"') {	// double quote
					inQuotes = !inQuotes;
					continue;
				}

				if (c == '\\') {	// escape
					buffer.append(chars[++i]);	// add next char
					continue;
				}

				if (c == ',') {
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


	static class IllegalCSVFormatException extends RuntimeException {
		IllegalCSVFormatException(String message) { super(message); }
		IllegalCSVFormatException(Throwable cause) { super(cause); }
	}
}
