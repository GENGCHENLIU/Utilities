package image;

/**
 * A simple iterator-like object that can be used to visit all pixels in an image.
 * For example:<br>
 * <pre>{@code
 * 	for (ImageIterator it = new ImageIterator(width, height);
 * 		it.hasNext(); it.advance()) {
 * 		// do things
 * 	}
 * }</pre>
 *
 * @version 1.0
 */
public class ImageIterator {
	private final int width, height;

	private int currentX, currentY;

	public ImageIterator(int width, int height) {
		this.width = width; this.height = height;
	}

	public void advance() {
		currentX++;
		if (currentX >= width) {
			currentY++;
			currentX = width - currentX;
		}
	}

	public boolean hasNext() {
		return currentY < height && currentX < width;
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCurrentX() { return currentX; }
	public int getCurrentY() { return currentY; }
}
