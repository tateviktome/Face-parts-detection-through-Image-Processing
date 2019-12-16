import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;
import ij.*;

public class HSVMapping implements PlugInFilter {
    public int setup(String args, ImagePlus im) {
        return DOES_RGB;
    }
    public void run(ImageProcessor ip) {
        float[] h = new float[360];
        float[] v = new float[101];
        float[] s = new float[101];

        float[] href = new float[360];
        float[] vref = new float[101];
        float[] sref = new float[101];

        setupHistogram(ip, h, v, s);
        ImageProcessor referenceImageProcessor = IJ.openImage().getProcessor();
        setupHistogram(referenceImageProcessor, href, vref, sref);

        double adj_h_value;
        float[] newIntens = new float[3];
        float[] hsv = new float[3];

        for (int i = 0; i < ip.getHeight(); i++) {
                for (int j = 0; j < ip.getWidth(); j++) {
                    Color color = new Color(ip.getPixel(j,i));
                    Color.RGBtoHSB (color.getRed(), color.getGreen(), color.getBlue(), hsv);
                    double hVal = h[(int) (hsv[0] * 360)];
                    double sVal = s[(int) (hsv[1] * 100)];
                    double vVal = v[(int) (hsv[2] * 100)];

                    double hDiff = 2;
                    double sDiff = 2;
                    double vDiff = 2;

                    for (int m = 0; m < 360; m++) {
                        if (Math.abs(href[m] - hVal) < hDiff) {
                            hDiff = Math.abs(href[m] - hVal);
                            newIntens[0] = (float) m / 360;
                        }
                    }

                    for (int m = 0; m < 101; m++) {
                        if (Math.abs(vref[m] - vVal) < vDiff) {
                            vDiff = Math.abs(vref[m] - vVal);
                            newIntens[2] = (float) m / 101;
                        }

                        if (Math.abs(sref[m] - sVal) < sDiff) {
                            sDiff = Math.abs(sref[m] - sVal);
                            newIntens[1] = (float) m / 101;
                        }
                    }
                    ip.putPixel(j, i, Color.HSBtoRGB(newIntens[0], newIntens[1], newIntens[2]));
                }
        } 
    }
    
    public void setupHistogram(ImageProcessor ip, float[] h, float[] s, float[] v) {
        float[] hsv = new float[3];
        Color color;

        for (int i = 0; i < ip.getHeight(); i++) {
            for (int j = 0; j < ip.getWidth(); j++) { 
                color = new Color(ip.getPixel(j,i));
                Color.RGBtoHSB (color.getRed(), color.getGreen(), color.getBlue(), hsv);
                h[(int) (hsv[0] * 360)] = h[(int) (hsv[0] * 360)] + 1;
                s[(int) (hsv[1] * 100)] = s[(int) (hsv[1] * 100)] + 1;
                v[(int) (hsv[2] * 100)] = v[(int) (hsv[2] * 100)] + 1;
            }
        }
        
        for (int i = 1; i < h.length; i++) {
            h[i] = h[i - 1] + h[i];
        } 
        for (int i = 1; i < s.length; i++) {
            s[i] = s[i - 1] + s[i];
            v[i] = v[i - 1] + v[i];
        }
        for (int i = 0; i < h.length; i++) {
            h[i] = h[i] / (ip.getHeight() * ip.getWidth());
        }
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i] / (ip.getHeight() * ip.getWidth());
            v[i] = v[i] / (ip.getHeight() * ip.getWidth());
        }
    }
}
