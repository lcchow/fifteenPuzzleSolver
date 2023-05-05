package fifteenpuzzle;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class Solver {


    public static Board getTargetBoard(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int size = reader.read() - '0';
        reader.close();

        int[][] target = new int[size][size];
        int k = 1;

        for (int i = 0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (k == (size*size)) {
                    target[i][j] = 0;
                } else {
                    target[i][j] = k;
                    k++;
                }
            }
        }
        Board targetBoard = new Board(target,size);

        return targetBoard;
    }

    /*
     * Returns a simplified target board for the row # inputted
     */

    public static Board getRowSimpleTargetBoard(Board board, int row) throws IOException {
        int size = board.size;
        int[][] target = new int[size][size];
        int k = 1;
        int beg = 1;
        int end = (size) + (row*(size));

        for (int i = 0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (k == (size*size)) {
                    target[i][j] = 0;
                } else if (k < beg || k > end) {
                    target[i][j] = -1;
                } else {
                    target[i][j] = k;

                }
                k++;
            }
        }

        Board targetBoard = new Board(target,size);

        return targetBoard;
    }

    /*
     * Returns a simplified board based on the board inputted to only include values for the row # inputted.
     * All other values are set as -1.
     */

    public static Board simplifyRowBoard(Board board, int row) throws IOException {
        int size = board.size;
        Board simpleBoard = new Board(board.board,size);
        int beg = 1;
        int end = (size) + (row*(size));

        for (int i = 0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (simpleBoard.board[i][j] == 0) {
                    simpleBoard.board[i][j] =0;
                }
                else if (simpleBoard.board[i][j] != 0 && (simpleBoard.board[i][j] < beg || simpleBoard.board[i][j]>end)) {
                    simpleBoard.board[i][j] = -1;
                }
            }
        }

        return simpleBoard;
    }


    public static <T> LinkedList<String> solveBigPuzzle(File input) throws IOException {
        Board startBoard = new Board(input);
        Vertex<T> start = new Vertex<T>(startBoard);

        //Set up target board and target value positions
        HashMap<Integer,List<Integer>> targetMap = new HashMap<Integer, List<Integer>>();
        Board targetBoard = getTargetBoard(input);
        Vertex<T> target = new Vertex<T>(targetBoard);
        int sizeDiff = targetBoard.size - 5;

        for (int i = 0; i<targetBoard.size; i++) {
            for (int j = 0; j < targetBoard.size; j++) {
                targetMap.put(targetBoard.board[i][j], new ArrayList<Integer>());
                targetMap.get(targetBoard.board[i][j]).add(i);
                targetMap.get(targetBoard.board[i][j]).add(j);
            }
        }

        //Solve each row individually until we reach target board
        int row = 0;
        LinkedList<String> result = new LinkedList<String>();

        while(row<start.board.size) {
            Vertex<T> rowMoves = solveRow(start.board, row);
            Stack<String> rowMovesStack = new Stack<String>();

            while (rowMoves != null) {
                rowMovesStack.push(rowMoves.board.move);
                rowMoves = rowMoves.parent;
            }

            while (!rowMovesStack.isEmpty()) {
                String move = rowMovesStack.pop();
                if (move != null) {
                    if (move == "U") {
                        start.board = start.board.moveUp();
                    } else if (move == "D") {
                        start.board = start.board.moveDown();
                    } else if (move == "L") {
                        start.board = start.board.moveLeft();
                    } else {
                        start.board = start.board.moveRight();
                    }
                    String moveStr = start.board.tile + " " + start.board.move;
                    result.add(moveStr);
                }
            }

            if (start.equals(target)) {
                System.out.println("FOUND FINAL RESULT");
                System.out.println(start.board.toString());
                return result;
            }
            row++;
        }

        return result;
    }

    public static <T> Vertex<T> solveRow(Board input, int row) throws IOException {
        Board startBoard = new Board(input.board,input.size);
        Vertex<T> start = new Vertex<T>(startBoard);

        //Set up target board and target value positions
        HashMap<Integer,List<Integer>> targetMap = new HashMap<Integer, List<Integer>>();
        Board targetBoard = getRowSimpleTargetBoard(input,row);
        Vertex<T> target = new Vertex<T>(targetBoard);

        int k = 1;
        for (int i = 0; i<targetBoard.size; i++) {
            for (int j = 0; j < targetBoard.size; j++) {
                if(targetBoard.board[i][j] != -1 ) {
                    targetMap.put(targetBoard.board[i][j], new ArrayList<Integer>());
                    targetMap.get(targetBoard.board[i][j]).add(i);
                    targetMap.get(targetBoard.board[i][j]).add(j);
                }
            }
        }

        //Simplify board to only consider values of row we are solving for
        start.board = simplifyRowBoard(start.board,row);

        //Set up A* traversal with Greedy Algorithm
        Queue<Vertex<T>> openQueue = new PriorityQueue<>();
        Hashtable<Integer,Vertex<T>> closedSet = new Hashtable<Integer, Vertex<T>>();

        if (start.equals(target)) {
            return start;
        }

        openQueue.add(start);
        while(!openQueue.isEmpty()) {
            Vertex<T> cur = openQueue.poll();
            Set<Board> nextStates = cur.board.getNextStates();

            for(Board nextBoard: nextStates) {
                Vertex<T> neighbour = new Vertex<T>(nextBoard);
                neighbour.calcTargetDistance(targetMap);

                if (neighbour.equals(target)) {
                    neighbour.parent = cur;
                    return neighbour;
                } else {
                    if(!closedSet.containsKey(neighbour.hashCode) && !openQueue.contains(neighbour)) {
                        openQueue.add(neighbour);
                    } else {
                        continue;
                    }
                    neighbour.parent = cur;
                }
            }
            closedSet.put(cur.hashCode(),cur);
        }
        return null;
    }


    public static <T> void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        if (args.length < 2) {
            System.out.println("File names are not specified");
            System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
            return;
        }

        // SET UP FILE
        File input = new File(args[0]);

        // SOLVE BOARD
        LinkedList<String> resList = solveBigPuzzle(input);

        // PRINT FINAL BOARD MOVES TO FILE
        File output = new File(args[1]);
        FileWriter fw = new FileWriter(output);

        while (resList.size() > 0 ){
            String res = resList.removeFirst();
            fw.write(res+"\n");
        }

        fw.close();

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Time elapsed: "+timeElapsed);
    }
}
