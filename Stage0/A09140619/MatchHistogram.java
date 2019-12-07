import java.awt.Color;
import java.lang.*; 
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

import java.util.Arrays;

public class MatchHistogram implements PlugInFilter {
    public void run(ImageProcessor ip) {
// target image I A (to be modified)
// reference image I R
        int[] hA = ip.getHistogram(); // get histogram for I A
        int[] hR = {0, 0, 0, 0, 0, 0, 2, 6, 34, 22, 37, 39, 50, 62, 60, 58, 55, 78, 91, 111, 72, 95, 103, 81, 86, 85, 78, 102, 92, 84, 95, 115, 105, 126, 123, 132, 140, 149, 160, 178, 225, 231, 221, 210, 185, 209, 203, 229, 211, 233, 265, 241, 246, 252, 301, 336, 321, 350, 374, 413, 417, 455, 426, 439, 531, 449, 417, 361, 404, 385, 416, 522, 560, 560, 564, 495, 476, 469, 459, 502, 500, 462, 434, 389, 370, 383, 404, 393, 418, 515, 573, 630, 558, 632, 546, 519, 419, 362, 336, 298, 304, 331, 308, 307, 284, 322, 286, 273, 263, 263, 250, 225, 232, 226, 182, 163, 129, 90, 70, 48, 32, 14, 10, 8, 3, 2, 8, 5, 3, 4, 2, 2, 2, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // get histogram for I R
        int[] fhs = matchHistograms(hA, hR); // mapping function f hs (a)
        ip.applyTable(fhs);
    }

    public int setup(String args, ImagePlus im) {
        return DOES_RGB;
    }

    int[] matchHistograms(int[] hA, int[] hR) {
        // hA . . . histogram h A of the target image I A (to be modified)
        // hR . . . reference histogram h R
        // returns the mapping f hs () to be applied to image I A
        int K = hA.length;
        double[] PA = Cdf(hA);
        double[] PR = Cdf(hR);
        int[] fhs = new int[K];
        // get CDF of histogram h A
        // get CDF of histogram h R
        // mapping f hs ()
        // compute mapping function f hs () :
        for (int a = 0; a < K; a++) {
            int j = K - 1;
            do {
                fhs[a] = j;
                j--;
            } while (j >= 0 && PA[a] <= PR[j]);
        }
        return fhs;
    }

    double[] Cdf(int[] h) {
        int K = h.length;
        int n = 0;

        for (int i = 0; i < K; i++) {
            n += h[i];
        }
        double[] P = new double[K];
        int c = h[0];

        P[0] = (double) c / n;

        for (int i = 1; i < K; i++) {
            c += h[i];
            P[i] = (double) c / n;
        }
        return P;
    }

}