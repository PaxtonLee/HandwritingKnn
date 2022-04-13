package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;

    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }

    @Override
    public L classify(V value) {
        Histogram<Object> h = new Histogram<>();
        ArrayList<Duple<Double, L>> distances = new ArrayList<Duple<Double, L>>();
        for (int i = 0 ; i < data.size(); i++) {
            Duple<Double, L> item = new Duple<Double, L>(distance.applyAsDouble(data.get(i).getFirst(), value),
                    data.get(i).getSecond());
            distances.add(new Duple<>(item.getFirst(), item.getSecond()));
        }

        distances.sort(Comparator.comparingDouble(Duple :: getFirst));

        while(distances.size() > k) {
            distances.remove(distances.size()-1);
        }

        for (Duple<Double, L> item : distances) {
            h.bump(item.getSecond());
        }

        return (L) h.getPluralityWinner();
    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        data.addAll(training);
    }
}
