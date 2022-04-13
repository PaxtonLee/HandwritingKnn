package learning.handwriting.core;

public class FloatDrawing {
    private double[][] pixels;

    public FloatDrawing(int width, int height) {
        pixels = new double[width][height];
    }

    public FloatDrawing(double[][] pixels) {
        this.pixels = pixels;
    }

    public FloatDrawing(Drawing src) {
        pixels = new double[src.getWidth()][src.getHeight()];
        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                pixels[x][y] = src.isSet(x, y) ? 1.0 : 0.0;
            }
        }
    }

    public double get(int x, int y) {
        return pixels[x][y];
    }

    public void set(int x, int y, double value) {
        pixels[x][y] = value;
    }

    public static FloatDrawing weightedAverageOf(FloatDrawing d1, FloatDrawing d2, double d1weight) {
        FloatDrawing floatDrawing = new FloatDrawing(d1.getWidth(), d1.getHeight());
        for (int i = 0; i <d1.getWidth(); i++) {
            for (int j = 0; j< d1.getHeight(); j++) {
                floatDrawing.set(i,j,(d1weight * d1.get(i, j) + (1.0 - d1weight) * d2.get(i,j)));
            }
        }
        return floatDrawing;
    }

    public int getWidth() {
        return pixels.length;
    }

    public int getHeight() {
        return pixels[0].length;
    }


    public double euclideanDistance(FloatDrawing other) {
        double d = 0.0;
        for (int i=0; i<other.getWidth(); i++) {
            for (int j=0; j<other.getHeight(); j++) {
                d += Math.pow(this.get(i,j) - other.get(i,j), 2);
            }
        }
        return d;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FloatDrawing that) {
            if (this.getHeight() == that.getHeight() && this.getWidth() == that.getWidth()) {
                for (int x = 0; x < getWidth(); x++) {
                    for (int y = 0; y < getHeight(); y++) {
                        if (this.pixels[x][y] != that.pixels[x][y]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                result.append(pixels[x][y]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
