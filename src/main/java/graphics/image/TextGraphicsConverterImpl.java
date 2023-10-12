package graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private TextColorSchema schema = new TextColorSchemaImpl();
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        double imgRatio = (double) imgWidth / imgHeight;
        if (imgRatio > maxRatio || imgRatio < 1 / maxRatio) {
            throw new BadImageSizeException(imgRatio, maxRatio);
        }
        int[] newRatio = proportions(imgWidth, imgHeight);
        int newWidth = newRatio[0];
        int newHeight = newRatio[1];

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();

        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();

        List<String> listImg = new ArrayList<>();
        for (int h = 0; h < newHeight; h++) {
            StringBuilder imgString = new StringBuilder();
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                imgString.append(c);
                imgString.append(c);
            }
            listImg.add(imgString.toString());
        }

        // Осталось собрать все символы в один большой текст.
        // Для того, чтобы изображение не было слишком узким, рекомендую
        // каждый пиксель превращать в два повторяющихся символа, полученных
        // от схемы.
        StringBuilder strImg = new StringBuilder();
        for (String s : listImg) {
            strImg.append(s).append("\n");
        }

        return strImg.toString();// Возвращаем собранный текст.
    }

    private int[] proportions(int weight, int height) {
        int[] newProp = new int[2];
        double weightRatio = (double) weight / maxWidth;
        double heightRatio = (double) height / maxHeight;
        double ration = Math.max(weightRatio, heightRatio);
        double mainRation = Math.ceil(ration * 10.0) / 10.0;
        newProp[0] = (int) (weight / mainRation);
        newProp[1] = (int) (height / mainRation);
        return newProp;
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
