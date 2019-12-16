import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class CumulativeHistogram2 implements PlugInFilter {

    double[] reds = new double[256];
    double[] greens = new double[256];
    double[] blues = new double[256];

    public int setup (String args, ImagePlus im) {
        return DOES_RGB;
    }

    public void run (ImageProcessor ip) {
        for (int i = 0; i < ip.getHeight(); i++) {
            for (int j = 0; j < ip.getWidth(); j++) {
                Color color = new Color(ip.getPixel(j, i));
                reds[color.getRed()] += 1;
                blues[color.getBlue()] += 1;
                greens[color.getGreen()] += 1;
            }
        }

        reds[0] = reds[0] / ip.getWidth() * ip.getHeight();
        greens[0] = greens[0] / ip.getWidth() * ip.getHeight();
        blues[0] = blues[0] / ip.getWidth() * ip.getHeight();

        for (int i = 1; i < reds.length; i++) {
            reds[i] = (reds[i] / (ip.getWidth() * ip.getHeight())) + reds[i - 1];
            greens[i] = (greens[i] / (ip.getWidth() * ip.getHeight())) + greens[i - 1];
            blues[i] = (blues[i] / (ip.getWidth() * ip.getHeight())) + blues[i - 1];
        }

        IJ.log("Reds");
        for (int i = 0; i < reds.length; i++) {
            IJ.log(Double.toString(reds[i]));
        }

        IJ.log("Greens");
        for (int i = 0; i < greens.length; i++) {
            IJ.log(Double.toString(greens[i]));
        }

        IJ.log("Blues");
        for (int i = 0; i < blues.length; i++) {
            IJ.log(Double.toString(blues[i]));
        }
    }
}