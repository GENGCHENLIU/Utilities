package csvio;

import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

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
	 *
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
	
	
	/**
	 * Encodes a row for writing to the CSV file.
	 */
	private static String encodeRow(List<String> row) {
		//TODO put entries in quotes
		final String lStr = row.toString();
		return lStr.substring(1, lStr.length()-1);
	}


	static class IllegalCSVFormatException extends RuntimeException {
		IllegalCSVFormatException(String message) { super(message); }
		IllegalCSVFormatException(Throwable cause) { super(cause); }
	}
}
