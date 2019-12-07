import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class CumulativeHistogramHSV implements PlugInFilter {

    double[] hs = new double[256];
    double[] vs = new double[256];
    double[] ss = new double[256];

    public int setup (String args, ImagePlus im) {
        return DOES_RGB;
    }

    public void run (ImageProcessor ip) {
        for (int i = 0; i < ip.getWidth(); i++) {
            for (int j = 0; j < ip.getHeight(); j++) {
                Color color = new Color(ip.getPixel(i, j));
                int[] rgb = {(int) color.getRed(), (int) color.getGreen(), (int) color.getBlue()};
                float[] hsv = this.RGBtoHSV(rgb);
                hs[(int) Math.round(hsv[0])] += 1;
                vs[(int) Math.round(hsv[1])] += 1;
                ss[(int) Math.round(hsv[2])] += 1;
            }
        }

        hs[0] = hs[0] / ip.getWidth() * ip.getHeight();
        vs[0] = vs[0] / ip.getWidth() * ip.getHeight();
        ss[0] = ss[0] / ip.getWidth() * ip.getHeight();

        for (int i = 1; i < hs.length; i++) {
            hs[i] = (hs[i] / (ip.getWidth() * ip.getHeight())) + hs[i - 1];
            vs[i] = (vs[i] / (ip.getWidth() * ip.getHeight())) + vs[i - 1];
            ss[i] = (ss[i] / (ip.getWidth() * ip.getHeight())) + ss[i - 1];
        }

        IJ.log("H values");
        for (int i = 0; i < hs.length; i++) {
            IJ.log(Double.toString(hs[i]));
        }

        IJ.log("V values");
        for (int i = 0; i < vs.length; i++) {
            IJ.log(Double.toString(vs[i]));
        }

        IJ.log("S values");
        for (int i = 0; i < ss.length; i++) {
            IJ.log(Double.toString(ss[i]));
        }
    }

    float[] RGBtoHSV(int[] RGB) {
        int R = RGB[0], G = RGB[1], B = RGB[2]; //R,G,B∈[0,255] 
        int cHi = Math.max(R,Math.max(G,B)); // max. comp. value 
        int cLo = Math.min(R,Math.min(G,B)); // min. comp. value 
        int cRng = cHi - cLo; // component range 
        float H=0,S=0,V=0;
        float cMax = 255.0f;
        V=cHi/cMax;
        
        if(cHi>0)
            S = (float) cRng / cHi;
    
        if (cRng > 0) {
            float rr = (float)(cHi - R)/cRng; 
            float gg = (float)(cHi - R)/cRng; 
            float bb = (float)(cHi - R)/cRng;
            float hh;
            if (R == cHi) 
                hh = bb - gg;
            else if (G == cHi)
                hh = rr - bb + 2.0f;
            else
                hh = gg - rr + 4.0f;
            if (hh < 0)
                hh = hh + 6;
            H = hh / 6;
        } 
        
        return new float[] {H, S, V};
    }
}