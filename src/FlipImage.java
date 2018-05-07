import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class FlipImage {
	public static void main(String[] args) throws Exception {
		BufferedImage image = ImageIO.read(new File(args[0]));
		BufferedImage result =
				new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++)
				result.setRGB(result.getWidth()-x-1, y, image.getRGB(x, y));
		}
		ImageIO.write(result, "png", new File(args[1]));
	}
}
