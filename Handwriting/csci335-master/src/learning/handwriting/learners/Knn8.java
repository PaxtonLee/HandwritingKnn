package learning.handwriting.learners;

import learning.classifiers.Knn;
import learning.handwriting.core.Drawing;

public class Knn8 extends Knn<Drawing,String> {
    public Knn8() {
        super(8, (d1, d2) -> (double)d1.distance(d2));
    }
}