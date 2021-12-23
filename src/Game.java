import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Complexity: O(n^2 * \alpha(n))
 */
public class Game {
    int[][] parentMap;
    int[] dx = new int[]{-1, 1};
    int[] dy = new int[]{-1, 1};
    boolean[][] touched;
    int[] rank;
    int n;
    boolean debugMode;

    public Game(int n, boolean debugMode) {
        this.debugMode = debugMode;
        parentMap = new int[n + 2][n];
        touched = new boolean[n + 2][n];
        rank = new int[n * (n + 2)];
        this.n = n;
    }
    protected int getN(){
        return n;
    }

    protected Double playGame() {
        fillInitialMatrix(parentMap);
        ArrayList<Integer> idxArr = shuffleArrayIdx();
        int actions = 0;
        while (notCompleted()) {
            int idx = idxArr.get(actions);
            int i = splitBackIdx(idx).left, j = splitBackIdx(idx).right;
            assert !touched[i][j];
            touched[i][j] = true;
            for (int item : dx) {
                for (int value : dy) {
                    if (ok(i + item, j + value)) {
                        unite(idx, getIdx(i + item, j + value));
                    }
                }
            }

            if (debugMode) {
                for (boolean[] ints : touched) {
                    System.out.println(Arrays.toString(ints));
                }
                System.out.println("--------------------------------------------------------");
                for (int[] ints : parentMap) {
                    System.out.println(Arrays.toString(ints));
                }
                System.out.println("--------------------------------------------------------");
            }
            actions++;
        }
        return (double) actions;
    }

    private boolean ok(int lhsIdx, int rhsIdx) {
        if (0 <= lhsIdx && lhsIdx < n + 2 && 0 <= rhsIdx && rhsIdx < n) {
            return touched[lhsIdx][rhsIdx];
        }
        return false;
    }

    private ArrayList<Integer> shuffleArrayIdx() {
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = n; i < n * (n + 2) - n; i++) {
            arr.add(i);
        }
        Collections.shuffle(arr);
        return arr;
    }

    private boolean notCompleted() {
        return findParent(0, 0) != findParent(n + 1, 0);
    }

    private int getIdx(int i, int j) {
        return i * n + j;
    }

    private Pair splitBackIdx(int idx) {
        return new Pair(idx / n, idx % n);
    }

    private void uniteConcrete(int leftParent, int rightParent) {
        rank[rightParent] += rank[leftParent];
        Pair leftSplitted = splitBackIdx(leftParent);
        parentMap[leftSplitted.left][leftSplitted.right] = rightParent;
    }

    private void unite(int leftNode, int rightNode) {
        int leftParent = findParent(leftNode);
        int rightParent = findParent(rightNode);
        if (leftParent == rightParent) return;
        if (rank[leftParent] < rank[rightParent]) {
            uniteConcrete(leftParent, rightParent);
        } else {
            uniteConcrete(rightParent, leftParent);
        }
    }

    private int findParent(int i, int j) {
        Pair parent = splitBackIdx(parentMap[i][j]);
        if (parent.left == i && parent.right == j) {
            return parentMap[i][j];
        }
        return parentMap[i][j] = findParent(parent.left, parent.right);
    }

    private int findParent(int Node) {
        int i = splitBackIdx(Node).left, j = splitBackIdx(Node).right;
        return findParent(i, j);
    }

    private void fillInitialMatrix(int[][] matrix) {
        Arrays.fill(matrix[0], getIdx(0, 0));
        Arrays.fill(touched[0], true);
        rank[matrix[0][0]] = n;
        for (int i = 1; i < n + 1; i++) {
            Arrays.fill(touched[i], false);
            for (int j = 0; j < n; j++) {
                matrix[i][j] = getIdx(i, j);
                rank[matrix[i][j]] = 1;
            }
        }
        Arrays.fill(matrix[n + 1], getIdx(n + 1, 0));
        Arrays.fill(touched[n + 1], true);
        rank[getIdx(n + 1, 0)] = n;
    }

    private static class Pair {
        int left, right;

        Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }
}
