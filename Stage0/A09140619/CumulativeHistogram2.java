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

        for (int k = 1; k < reds.length; k++) {
            reds[k] = reds[k] + reds[k - 1];
            greens[k] = greens[k] + greens[k - 1];
            blues[k] = blues[k]  + blues[k - 1];
        }

        for (int k = 0; k < reds.length; k++) {
            reds[k] = reds[k] / (ip.getWidth() * ip.getHeight());
            greens[k] = greens[k] / (ip.getWidth() * ip.getHeight());
            blues[k] = blues[k] / (ip.getWidth() * ip.getHeight());
        }

        IJ.log("Reds");
        for (int k = 0; k < reds.length; k++) {
            IJ.log(Double.toString(reds[k]));
        }

        IJ.log("Greens");
        for (int k = 0; k < greens.length; k++) {
            IJ.log(Double.toString(greens[k]));
        }

        IJ.log("Blues");
        for (int k = 0; k < blues.length; k++) {
            IJ.log(Double.toString(blues[k]));
        }
    }
}
