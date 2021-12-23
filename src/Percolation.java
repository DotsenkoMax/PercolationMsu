import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Percolation {
    public static Scanner enter = new Scanner(System.in);

    private static Double calculateMean(List<Double> arr) {
        double sum = arr.stream().reduce(0., Double::sum);
        return sum / arr.size();
    }

    private static Double calculateStd(List<Double> arr, Double mean) {
        double sum = arr.stream().reduce(0., (a, b) -> a + (b - mean) * (b - mean));
        return sum / arr.size();
    }

    public static void main(String[] args) {
        System.out.println("Сложность алгоритма O(nTrials * n^2 * alpha(n)).");
        while (true) {
            System.out.println("Введите nTrials:");
            int numberIterations = enter.nextInt();
            System.out.println("Введите n:");
            Game game = new Game(enter.nextInt(), false);
            ArrayList<Double> sample = new ArrayList<>();
            while (numberIterations-- != 0) {
                double whenHappens = game.playGame();
                sample.add(whenHappens);
            }
            Double mean = calculateMean(sample);
            Double std = calculateStd(sample, mean);
            System.out.printf("Mean: %s, Std: %s\n", mean, std);
            System.out.printf("Mean in percents: %s, Std in percents: %s\n", mean / game.n / game.n * 100., std / game.n / game.n / game.n / game.n * 100.);
        }
    }
}
