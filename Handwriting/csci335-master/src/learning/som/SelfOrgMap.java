package learning.som;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;

public class SelfOrgMap<V> {
    private V[][] map;
    private double[][] trainingCounts;
    private ToDoubleBiFunction<V, V> distance;
    private WeightedAverager<V> averager;

    public SelfOrgMap(int side, Supplier<V> makeDefault, ToDoubleBiFunction<V, V> distance, WeightedAverager<V> averager) {
        this.distance = distance;
        this.averager = averager;
        map = (V[][])new Object[side][side];
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                map[i][j] = makeDefault.get();
            }
        }
        trainingCounts = new double[side][side];
    }


    public SOMPoint bestFor(V example) {
		double smallest = Double.MAX_VALUE;
        SOMPoint p = new SOMPoint(0,0);

        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                double temp = distance.applyAsDouble(example, map[x][y]);
                if(temp < smallest){
                    smallest = temp;
                    p = new SOMPoint(x,y);
                }
            }
        }
        return p;
    }

    // TODO: Train this SOM with example.
    //  1. Find the best matching node.
    //  2. For every node in the map:
    //     a. Find the distance weight to the best matching node. Call computeDistanceWeight().
    //     b. Add the distance weight to its training count.
    //     c. Find the effective learning rate. Call effectiveLearningRate().
    //     d. Update the node with the average of itself and example, with example weighted by
    //        the effective learning rate.
    public void train(V example) {
        SOMPoint best = bestFor(example);
        for (int x=0; x<getMapWidth(); x++) {
            for (int y=0; y<getMapHeight(); y++) {
                SOMPoint t = new SOMPoint(x, y);
                double i = computeDistanceWeight(t, best);
                trainingCounts[x][y] += i;
                double learn = effectiveLearningRate(i, trainingCounts[x][y]);
                map[x][y] = averager.weightedAverage(example, map[x][y], learn);
            }
        }
    }


    public double computeDistanceWeight(SOMPoint sp1, SOMPoint sp2) {
        double distTo = sp1.distanceTo(sp2);
        distTo = distTo / map.length;
        distTo = 1 - distTo;
        if (distTo < 0) {
            return 0.0;
        } else {
            return distTo;
        }
    }

    // TODO: First, find the update rate. This is the reciprocal of the training
    //  count. But make sure it is no more than one, even if the training count is
    //  tiny. Then, multiply it by the distance weight.
    public static double effectiveLearningRate(double distWeight, double trainingCounts) {
        double update = 1 / trainingCounts;
        double rate = 1;
        if (update <= 1) {
            rate = update;
        }
        rate *= distWeight;
        return rate;
    }

    public V getNode(int x, int y) {
        return map[x][y];
    }

    public int getMapWidth() {
        return map.length;
    }

    public int getMapHeight() {
        return map[0].length;
    }

    public boolean inMap(SOMPoint point) {
        return point.x() >= 0 && point.x() < getMapWidth() && point.y() >= 0 && point.y() < getMapHeight();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SelfOrgMap that) {
            if (this.getMapHeight() == that.getMapHeight() && this.getMapWidth() == that.getMapWidth()) {
                for (int x = 0; x < getMapWidth(); x++) {
                    for (int y = 0; y < getMapHeight(); y++) {
                        if (!map[x][y].equals(that.map[x][y])) {
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
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                result.append(String.format("(%d, %d):\n", x, y));
                result.append(getNode(x, y));
            }
        }
        return result.toString();
    }
}
