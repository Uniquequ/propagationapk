package com.example.propagationapk;

import java.util.Arrays;

public class PropagationLossCalculator {
    private double d2D;
    private double hBS;
    private double hUT;
    public double hE;
    private double frequency;
    public double W;
    public double h;
    private final double c = 3e8;
    private static final double PI = Math.PI;

    public PropagationLossCalculator(double d2D, double hBS, double hUT, double hE, double frequency, double W, double h) {
        this.d2D = d2D;
        this.hBS = hBS;
        this.hUT = hUT;
        this.hE = hE;
        this.frequency = frequency;
        this.W = W;
        this.h = h;
    }

    public double calculateRMaLoS1(double d3D) {
        double term1 = 20 * Math.log10((40 * PI * d3D * frequency) / 3);
        double term2 = Math.min(3 * Math.pow(h, 1.72) / 100, 10.0) * Math.log10(d3D);
        double term3 = Math.min(44 * Math.pow(h, 1.72) / 1000, 14.77);
        double term4 = (2 * Math.log10(h) * d3D) / 1000;
        return term1 + term2 + term3 + term4;
    }

    public double calculateRMaLoS2(double d3D, double dBP) {
        double term1 = 20 * Math.log10((40 * PI * d3D * frequency) / 3);
        double term2 = Math.min(3 * Math.pow(h, 1.72) / 100, 10.0) * Math.log10(d3D);
        double term3 = Math.min(44 * Math.pow(h, 1.72) / 1000, 14.77);
        double term4 = (2 * Math.log10(h) * d3D) / 1000;
        double term5 = 40 * Math.log10(d3D / dBP);
        return term1 + term2 + term3 + term4 + term5;
    }

    public double calculateRMaNLoS(double d3D) {
        double term1 = 161.04 - 7.11 * Math.log10(W) + 7.5 * Math.log10(h);
        double term2 = -1 * (24.37 - 3.7 * Math.pow((h / hBS), 2)) * Math.log10(hBS);
        double term3 = (43.42 - 3.1 * Math.log10(hBS)) * (Math.log10(d3D) - 3) + 20 * Math.log10(frequency);
        double term4 = -1 * (3.2 * Math.pow(Math.log10(11.75 * hUT), 2) - 4.97);
        return term1 + term2 + term3 + term4;
    }

    public double calculateUMaLoS1(double d3D) {
        return 28 + 22 * Math.log10(d3D) + 20 * Math.log10(frequency);
    }

    public double calculateUMaLoS2(double d3D, double dBP_prim) {
        return 28 + 40 * Math.log10(d3D) + 20 * Math.log10(frequency) - 9 * Math.log10(Math.pow(dBP_prim, 2) + Math.pow((hBS - hUT), 2));
    }

    public double calculateUMaNLoS(double d3D) {
        return 13.54 + 39.08 * Math.log10(d3D) + 20 * Math.log10(frequency) - 0.6 * (hUT - 1.5);
    }

    public double calculateUMiLoS1(double d3D) {
        return 32.4 + 21 * Math.log10(d3D) + 20 * Math.log10(frequency);
    }

    public double calculateUMiLoS2(double d3D, double dBP_prim) {
        return 32.4 + 40 * Math.log10(d3D) + 20 * Math.log10(frequency) - 9.5 * Math.log10(Math.pow(dBP_prim, 2) + Math.pow((hBS - hUT), 2));
    }

    public double calculateUMiNLoS(double d3D) {
        return 22.4 + 35.3 * Math.log10(d3D) + 21.3 * Math.log10(frequency) - 0.3 * (hUT - 1.5);
    }

    public double calculateInHLoS(double d3D) {
        return 32.4 + 17.3 * Math.log10(d3D) + 20 * Math.log10(frequency);
    }

    public double calculateInHNLoS(double d3D) {
        return 17.3 + 38.3 * Math.log10(d3D) + 24.9 * Math.log10(frequency);
    }

    public double calculateInFLoS(double d3D) {
        return 31.84 + 21.5 * Math.log10(d3D) + 19 * Math.log10(frequency);
    }

    public double calculateInFSL(double d3D) {
        return 33 + 25.5 * Math.log10(d3D) + 20 * Math.log10(frequency);
    }

    public double calculateInFDL(double d3D) {
        return 18.6 + 35.7 * Math.log10(d3D) + 20 * Math.log10(frequency);
    }

    public double calculateInFSH(double d3D) {
        return 32.4 + 23 * Math.log10(d3D) + 20 * Math.log10(frequency);
    }

    public double calculateInFDH(double d3D) {
        return 33.63 + 21.9 * Math.log10(d3D) + 20 * Math.log10(frequency);
    }

    private double calculateD3D() {
        return Math.sqrt(Math.pow(d2D, 2) + Math.pow((hBS - hUT), 2));
    }

    private double calculateDBP() {
        return (2 * PI * hBS * hUT * frequency * 1e9) / c;
    }

    private double calculateDBPPrim() {
        double hBS_prim = hBS - hE;
        double hUT_prim = hUT - hE;
        return (4 * hBS_prim * hUT_prim * frequency * 1e9) / c;
    }

    public String calculateRMa(int choice) {
        double d3D = calculateD3D();
        double dBP = calculateDBP();
        double result;
        if (hBS < 10 || hBS > 150){
            return "Wysokość hBs poza zakresem!";
        } else if (d2D < 10 || d2D > 10000) {
            return "Odległość d2D poza zakresem";
        } else if (hUT < 1 || hUT > 10) {
            return "Wysokość hUT poza zakresem";
        } else if (h < 5 || h > 50) {
            return "Wysokość budynków h poza zakresem";
        } else {
            if (choice == 1) {
                if (d2D >= 10 && d2D <= dBP) {
                    result = calculateRMaLoS1(d3D);
                } else if (d2D > dBP && d2D <= 10000) {
                    result = calculateRMaLoS2(d3D, dBP);
                } else {
                    return "Odległość d2D poza zakresem!";
                }
            } else if (choice == 2) {
                if (W < 5 || W > 50){
                    return "Szerokość ulic W poza zakresem";
                } else if (d2D >= 10 && d2D <= 5000) {
                    result = Math.max(Math.max(calculateRMaLoS1(d3D), calculateRMaLoS2(d3D, dBP)), calculateRMaNLoS(d3D));
                } else {
                    return "Odległość d2D poza zakresem!";
                }
            } else {
                return "Nieprawidłowy wybór!";
            }
            return "Tłumienie wynosi: " + String.format("%.2f", result) + " dB";
        }
    }

    public String calculateUMa(int choice) {
        double d3D = calculateD3D();
        double dBP_prim = calculateDBPPrim();
        double result;

        if (hUT < 1.5 || hUT > 22.5) {
            return "Wysokość hUT poza zakresem";
        } else {
            if (choice == 1) {
                if (d2D >= 10 && d2D <= dBP_prim) {
                    result = calculateUMaLoS1(d3D);
                } else if (d2D > dBP_prim && d2D <= 5000) {
                    result = calculateUMaLoS2(d3D, dBP_prim);
                } else {
                    return "Odległość d2D poza zakresem!";
                }
            } else if (choice == 2) {
                result = Math.max(Math.max(calculateUMaLoS1(d3D), calculateUMaLoS2(d3D, dBP_prim)), calculateUMaNLoS(d3D));
            } else {
                return "Nieprawidłowy wybór!";
            }
            return "Tłumienie wynosi: " + String.format("%.2f", result) + " dB";
        }
    }

    public String calculateUMi(int choice) {
        double d3D = calculateD3D();
        double dBP_prim = calculateDBPPrim();
        double result;

        if(hUT < 1.5 || hUT > 22.5) {
            return "Wysokość hUT poza zakresem";
        } else {
            if (choice == 1) {
                if (d2D >= 10 && d2D <= dBP_prim) {
                    result = calculateUMiLoS1(d3D);
                } else if (d2D > dBP_prim && d2D <= 5000) {
                    result = calculateUMiLoS2(d3D, dBP_prim);
                } else {
                    return "Odległość d2D poza zakresem!";
                }
            } else if (choice == 2) {
                result = Math.max(Math.max(calculateUMiLoS1(d3D), calculateUMiLoS2(d3D, dBP_prim)), calculateUMiNLoS(d3D));
            } else {
                return "Nieprawidłowy wybór!";
            }
            return "Tłumienie wynosi: " + String.format("%.2f", result) + " dB";
        }
    }

    public String calculateInH(int choice) {
        double d3D = calculateD3D();
        double result;

        if (d3D < 1 || d3D > 150) {
            return "Odległość d3D poza zakresem [1, 150]!";
        }

        if (choice == 1) {
            result = calculateInHLoS(d3D);
        } else if (choice == 2) {
            result = Math.max(calculateInHLoS(d3D), calculateInHNLoS(d3D));
        } else {
            return "Nieprawidłowy wybór!";
        }
        return "Tłumienie wynosi: " + String.format("%.2f", result) + " dB";
    }

    public String calculateInF(int choice, int nlosOption) {
        double d3D = calculateD3D();
        double result;

        if (d3D < 1 || d3D > 600) {
            return "Odległość d3D poza zakresem [1, 600]!";
        }

        if (choice == 1) {
            result = calculateInFLoS(d3D);
        } else if (choice == 2) {
            switch (nlosOption) {
                case 1:
                    result = Math.max(calculateInFSL(d3D), calculateInFLoS(d3D));
                    break;
                case 2:
                    result = Math.max(Math.max(calculateInFDL(d3D), calculateInFSL(d3D)), calculateInFLoS(d3D));
                    break;
                case 3:
                    result = Math.max(calculateInFSH(d3D), calculateInFLoS(d3D));
                    break;
                case 4:
                    result = Math.max(calculateInFDH(d3D), calculateInFLoS(d3D));
                    break;
                default:
                    return "Nieprawidłowy wybór NLoS!";
            }
        } else {
            return "Nieprawidłowy wybór!";
        }
        return "Tłumienie wynosi: " + String.format("%.2f", result) + " dB";
    }
}