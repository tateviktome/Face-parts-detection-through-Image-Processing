import ij.*;
import java.awt.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.lang.Math;
import ij.ImagePlus;
import java.awt.Color;

public class Matching implements PlugInFilter {
    ImageProcessor referenceImageProcessor;
    double[] reds = new double[256];
    double[] greens = new double[256];
    double[] blues = new double[256];
    double[] refReds = new double[256];
    double[] refGreens = new double[256];
    double[] refBlues = new double[256];

    public int setup (String args, ImagePlus im) {
        return DOES_RGB;
    }

    public void run (ImageProcessor ip) {
        referenceImageProcessor = IJ.openImage().getProcessor();
        setupHistogram(ip, reds, greens, blues);
        setupHistogram(referenceImageProcessor, refReds, refGreens, refBlues);

        double redMapping[] = new double[256];
        double greenMapping[] = new double[256];
        double blueMapping[] = new double[256];

        // calculate mappings
        for (int i = 0; i < 256; i++) {
            double calibratedRedColor = getIndex(refReds, reds[i]);
            double calibratedGreenColor = getIndex(refGreens, greens[i]);
            double calibratedBlueColor = getIndex(refBlues, blues[i]);

            redMapping[i] = calibratedRedColor;
            greenMapping[i] = calibratedGreenColor;
            blueMapping[i] = calibratedBlueColor;
        }

        // equalize
        for (int col = 0; col < ip.getHeight(); col++) {
            for (int row = 0; row < ip.getWidth(); row++) {
                Color color = new Color(ip.getPixel(row, col));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int calibrated[] = new int[3];
                calibrated[0] = (int) redMapping[r];
                calibrated[1] = (int) greenMapping[g];
                calibrated[2] = (int) blueMapping[b];
                ip.putPixel(row, col, calibrated);
            }
        }
    }

    private double getIndex(double[] referenceHistogram, double value) {
        int index = 255;
        while (referenceHistogram[index] >= value && index > 0) {
            index--;
        }
        return (double) index;
    }

    private void setupHistogram(ImageProcessor ip, double[] reds, double[] greens, double[] blues) {
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
    }
}