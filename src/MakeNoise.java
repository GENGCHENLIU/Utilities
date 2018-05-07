import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Make a noise picture.
 */
public class MakeNoise {
	public static void main(String[] args) throws IOException {
		if (args.length != 4) {
			System.out.println("I need the width, height, format, and output.");
			System.exit(0);
		}


		final int width = Integer.parseInt(args[0]);
		final int height = Integer.parseInt(args[1]);
		final String format = args[2];
		final String out = args[3];

		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

		final Random r = new Random();

		for (ImageIterator it = new ImageIterator(width, height);
			 it.hasNext(); it.advance()) {
			image.setRGB(it.getCurrentX(), it.getCurrentY(),
					r.nextBoolean() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
		}


		if (!ImageIO.write(image, format, new File(out))) {
			System.out.println("I couldn't write an image in that format...");
			System.out.println("Try a different format?");
		}
	}


	private static class ImageIterator {
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
}
