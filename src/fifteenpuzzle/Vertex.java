package fifteenpuzzle;

import java.util.*;

public class Vertex<T> implements Comparable<Vertex<T>>{
    Board board;
    Vertex<T> parent;
    int distance;
    int hashCode;

    public Vertex(Board board) {
        this.board = board;
        this.hashCode = hashCode();
        this.parent = null;
    }

    public void setParent(Vertex<T> parent) {
        this.parent = parent;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Vertex<?>)) {
            return false;
        }

        if (this.hashCode != ((Vertex) other).hashCode) {
            return false;
        }

        return board.equals(((Vertex) other).board);
    }

    public int hashCode() {
        int hash = 0;
        int k = 25;

        for (int i = 0; i<board.size;i++) {
            for (int j = 0; j < board.size; j++) {
                if (board.board[i][j] == 0) {
                    continue;
                } else {
                    hash += board.board[i][j]*(k*2)*(i+7)*(j+7);
                }
                k++;
            }
        }
        return hash;
    }

    public int compareTo(Vertex<T> other) {
        if (this.distance > other.distance) {
            return 1;
        } else if (this.distance < other.distance) {
            return -1;
        } else {
            return 0;
        }
    }

    public void calcTargetDistance(HashMap<Integer, List<Integer>> targetMap) {
        for (int i = 0; i<board.size; i++) {
            for (int j = 0; j <board.size; j++) {
                int val = board.board[i][j];

                if (val > 0) {
                    int targetRow = targetMap.get(val).get(0);
                    int targetCol = targetMap.get(val).get(1);

                    int valDist = Math.abs(i - targetRow) + Math.abs(j - targetCol);
                    this.distance += valDist;
                }
            }
        }

        this.distance += linearConflicts(targetMap);
    }

    public int linearConflicts(HashMap<Integer, List<Integer>> targetMap) {
        int rowCount = 0;
        int colCount = 0;

        ArrayList<Integer> rowValues = new ArrayList<>();
        int k = 1;
        int beg = 1;
        int end = board.size;

        for (int i = 0; i<board.size; i++) {
            while (k<=end) {
                rowValues.add(k);
                k++;
            }

            boolean rowConflict = false;

            //ROW CONFLICTS
            for (int j = 0; j <board.size; j++) {
                int val = board.board[i][j];
                if (rowValues.contains(val)) {
                    int r = targetMap.get(val).get(0);
                    int c = targetMap.get(val).get(1);

                    if (i == r && c == j) {
                        continue;
                    } else {
                        if (!rowConflict) {
                            rowConflict = true;
                        } else {
                            rowCount++;
                        }
                    }
                }

            }
            rowValues.clear();
            end += board.size;
        }

        int l = 1;
        int colEnd;

        ArrayList<Integer> colValues = new ArrayList<>();

        //COLUMN CONFLICTS
        for (int i = 0; i<board.size; i++) {
            colEnd = 0;
            l = i+1;
            while (colEnd < board.size) {
                colValues.add(l);
                colEnd++;
                l+=board.size;
            }

            boolean colConflict = false;

            for (int j = 0; j<board.size; j++) {
                int colVal = board.board[j][i];

                if (colValues.contains(colVal)) {
                    int r2 = targetMap.get(colVal).get(0);
                    int c2 = targetMap.get(colVal).get(1);

                    if (j == r2 && c2 == i) {
                        continue;
                    } else {
                        if (!colConflict) {
                            colConflict = true;
                        } else {
                            colCount++;
                        }
                    }
                }
            }
            colValues.clear();
        }

        return 2*(rowCount+colCount);
    }
}
