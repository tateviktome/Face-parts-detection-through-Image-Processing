import ij.*;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;

public class CumulativeHistogramHSV2 implements PlugInFilter {
    public int setup(String args, ImagePlus im) {
        return DOES_RGB;
    }
    public void run(ImageProcessor ip) {
        Color color;
        float[] hsv  = new float[3];
        float[] h = new float[360];
        float[] s = new float[101];
        float[] v = new float[101];

        for (int k = 0; k < ip.getHeight(); k++) {
            for (int j = 0; j < ip.getWidth(); j++) { 
                color = new Color(ip.getPixel(j,k));
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

        IJ.log("H values");
        for (int i = 0; i < h.length; i++) {
            IJ.log(Double.toString(h[i]));
        }

        IJ.log("V values");
        for (int i = 0; i < v.length; i++) {
            IJ.log(Double.toString(v[i]));
        }

        IJ.log("S values");
        for (int i = 0; i < s.length; i++) {
            IJ.log(Double.toString(s[i]));
        } 
    }
}
