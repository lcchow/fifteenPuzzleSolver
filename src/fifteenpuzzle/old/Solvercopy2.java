package fifteenpuzzle.old;

import fifteenpuzzle.Board;
import fifteenpuzzle.Vertex;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class Solvercopy2 {


    public static Board getTargetBoard(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int size = reader.read() - '0';
        reader.close();
//        System.out.println(size);
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
//        System.out.println(targetBoard.toString());
        return targetBoard;
    }

    public static <T> Vertex<T> solvePuzzle(File input) throws IOException {
        Board startBoard = new Board(input);
        Vertex<T> start = new Vertex<T>(startBoard);

        //Set up target board and target value positions
        HashMap<Integer,List<Integer>> targetMap = new HashMap<Integer, List<Integer>>();
        Board targetBoard = getTargetBoard(input);
        Vertex<T> target = new Vertex<T>(targetBoard);

        for (int i = 0; i<targetBoard.size; i++) {
            for (int j = 0; j < targetBoard.size; j++) {
                targetMap.put(targetBoard.board[i][j], new ArrayList<Integer>());
                targetMap.get(targetBoard.board[i][j]).add(i);
                targetMap.get(targetBoard.board[i][j]).add(j);
            }
        }

//        System.out.println("START BOARD: ");
//        System.out.println(start.board.toString());
//
////        System.out.println(targetMap);
//        System.out.println("TARGET BOARD: ");
//        System.out.println(targetBoard.toString());


        //Set up A* traversal
        Queue<Vertex<T>> openQueue = new PriorityQueue<>();
//        HashMap<Integer,ArrayList<Vertex<?>>> closedSet = new HashMap<Integer,ArrayList<Vertex<?>>>();
//        HashSet<Vertex<T>> closedSet = new HashSet<Vertex<T>>();
        Hashtable<Integer,Vertex<T>> closedSet = new Hashtable<Integer, Vertex<T>>();

        openQueue.add(start);

        while(!openQueue.isEmpty()) {
//            System.out.println(openQueue);
            Vertex<T> cur = openQueue.poll();

//            System.out.println("CURRENT ----------");
//            System.out.println("dist: " +cur.distance);
////            System.out.println(cur.board.toString());
//            System.out.println("HC: "+cur.hashCode);
//            System.out.println("move: "+cur.board.move);
            Set<Board> nextStates = cur.board.getNextStates();

            for(Board nextBoard: nextStates) {
                Vertex<T> neighbour = new Vertex<T>(nextBoard);
                neighbour.calcTargetDistance(targetMap);

//                System.out.println("Neighbour: ");
//                System.out.println("Distance: "+neighbour.distance);
//                System.out.println("N HC: "+neighbour.hashCode());
//                System.out.println(cur.board.equals(neighbour.board));
//                System.out.println(neighbour.board.toString());

                if (neighbour.equals(target)) {
//                    System.out.println(openQueue.toString());

                    System.out.println("FINAL: "+neighbour.board.equals(targetBoard));
                    System.out.println("Result: ");
                    System.out.println("FINAL HC: "+neighbour.hashCode());
                    System.out.println(neighbour.board.toString());
                    System.out.println("Target: ");
                    System.out.println("TARGET HC: "+target.hashCode());
                    System.out.println(target.board.toString());

                    neighbour.parent = cur;
                    return neighbour;
                } else {
//                    if(!closedSet.containsKey(neighbour.hashCode)) {
//                    System.out.println("CHECK: "+!closedSet.containsKey(neighbour.hashCode)+" "+neighbour.board.move);
                    if(!closedSet.containsKey(neighbour.hashCode()) && !openQueue.contains(neighbour)) {
                        openQueue.add(neighbour);
//                        System.out.println(openQueue.toString());
                    } else {
//                        Vertex<T> tmp = closedSet.get(neighbour.hashCode);
//                        if (neighbour.distance>tmp.distance) {
//                            openQueue.add(neighbour);
//                        }
//                        System.out.println(closedSet.containsKey(neighbour.hashCode));
//                        System.out.println("HERE");
                    }
                    neighbour.parent = cur;
                }

            }

            closedSet.put(cur.hashCode(),cur);
//             closedSet.add(cur);
//            System.out.println(closedSet.toString());
//            closedSet.put(cur.hashCode(),new ArrayList<Vertex<?>>());
//            closedSet.get(cur.hashCode()).add(cur);

//            System.out.println(closedSet.toString());
        }

        System.out.println("NULL EXIT");

        return null;
    }


    public static Board getRowSimpleTargetBoard(Board board, int row) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        int size = reader.read() - '0';
//        reader.close();

        int size = board.size;
//        int endRow = size-row;
        int[][] target = new int[size][size];
        int k = 1;
        int beg = 1;
        int end = (size) + (row*(size));


        System.out.println("SIZE: "+size+" BEG: "+beg+" END: "+end);

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

//        System.out.println("SIMPLY TARGET: ");
//        System.out.println(targetBoard.toString());

        return targetBoard;
    }

    public static Board getColSimpleTargetBoard(Board board, int col, int row) throws IOException {
        int size = board.size;
        int[][] target = new int[size][size];
        int k = 1;
        int beg = 1;
        int end = (size) + (col*(size));

        System.out.println("SIZE: "+size+" BEG: "+beg+" END: "+end);

        for (int i = 0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (k == (size*size)) {
                    target[i][j] = 0;
                } else if (j <= col || i <= row) {
                    target[i][j] = k;
                } else {
                    target[i][j] = -1;
                }
                k++;
            }
        }

        Board targetBoard = new Board(target,size);

//        System.out.println("SIMPLY COL TARGET: ");
//        System.out.println(targetBoard.toString());

        return targetBoard;
    }

    public static Board simplifyRowBoard(Board board, int row) throws IOException {
        int size = board.size;
        Board simpleBoard = new Board(board.board,size);
        int beg = 1;
        int end = (size) + (row*(size));

        for (int i = 0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (simpleBoard.board[i][j] != 0 && (simpleBoard.board[i][j] < beg || simpleBoard.board[i][j]>end)) {
                    simpleBoard.board[i][j] = -1;
                }
            }
        }
//        System.out.println("SIMPLY BOARD: ");
//        System.out.println(simpleBoard.toString());

        return simpleBoard;
    }

    public static Board simplifyColBoard(Board board, int col, int row) throws IOException {
        int size = board.size;
        Board simpleBoard = new Board(board.board,size);
        int cur = 0;
        int k = 1;

        ArrayList<Integer> colValues = new ArrayList<Integer>();
        while (cur <= row) {
            k = cur +1;
            colValues.add(k);
            for (int l = 0; l < size; l++) {
                k += size;
                colValues.add(k);
            }
            cur++;
        }

        System.out.println("COL VALU------:" + colValues.toString());

        for (int i = 0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (simpleBoard.board[i][j] != 0 && !colValues.contains(simpleBoard.board[i][j]) && i > row) {
                    simpleBoard.board[i][j] = -1;
                }
            }
        }
//        System.out.println("SIMPLY BOARD: ");
//        System.out.println(simpleBoard.toString());

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

//        System.out.println("BIG START BOARD: ");
//        System.out.println(start.board.toString());
//
////        System.out.println(targetMap);
//        System.out.println("BIG TARGET BOARD: ");
//        System.out.println(targetBoard.toString());

        int row = 0;
        int col = 0;
        LinkedList<String> result = new LinkedList<String>();

        // TO CHANGE
        while(row<2) {
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

            Vertex<T> colMoves = solveCol(start.board,col,row);
            Stack<String> colMovesStack = new Stack<String>();

            while (colMoves != null) {
                colMovesStack.push(colMoves.board.move);
                colMoves = colMoves.parent;
            }

            while (!colMovesStack.isEmpty()) {
                String move = colMovesStack.pop();
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
//            System.out.println("COL SOLVED: ");
//            System.out.println(col.board.toString());


            row++;
            col++;

            System.out.println("AFTER SIMPLE: ");
            System.out.println(start.board.toString());
//            System.out.println("row: "+row);
//            System.out.println("--------------------");
        }

//        System.out.println("SIZE DIFF: " +sizeDiff);

        System.out.println("EXIT HERE");

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
//        System.out.println("--------------------");
//        System.out.println("ROW START BOARD: ");
//        System.out.println(start.board.toString());
//
        start.board = simplifyRowBoard(start.board,row);
//
//        System.out.println("ROW SIMPLE BOARD: ");
//        System.out.println(start.board.toString());
//////
//////        System.out.println(targetMap);
//        System.out.println("ROW TARGET BOARD: ");
//        System.out.println(targetBoard.toString());

        //Set up A* traversal
        Queue<Vertex<T>> openQueue = new PriorityQueue<>();
        Hashtable<Integer,Vertex<T>> closedSet = new Hashtable<Integer, Vertex<T>>();
        ArrayList<String> moves = new ArrayList<String>();

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
//                    System.out.println("FINAL: "+neighbour.board.equals(targetBoard));
//                    System.out.println("Result: ");
//                    System.out.println("FINAL HC: "+neighbour.hashCode());
//                    System.out.println(neighbour.board.toString());
//                    System.out.println("Target: ");
//                    System.out.println("TARGET HC: "+target.hashCode());
//                    System.out.println(target.board.toString());

                    neighbour.parent = cur;
                    return neighbour;
                } else {
                    if(!closedSet.containsKey(neighbour.hashCode()) && !openQueue.contains(neighbour)) {
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

    public static <T> Vertex<T> solveCol(Board input, int col,int row) throws IOException {
        Board startBoard = new Board(input.board,input.size);
        Vertex<T> start = new Vertex<T>(startBoard);

        //Set up target board and target value positions
        HashMap<Integer,List<Integer>> targetMap = new HashMap<Integer, List<Integer>>();
        Board targetBoard = getColSimpleTargetBoard(input,col,row);
        Vertex<T> target = new Vertex<T>(targetBoard);

        for (int i = 0; i<targetBoard.size; i++) {
            for (int j = 0; j < targetBoard.size; j++) {
                if(targetBoard.board[i][j] != -1 ) {
                    targetMap.put(targetBoard.board[i][j], new ArrayList<Integer>());
                    targetMap.get(targetBoard.board[i][j]).add(i);
                    targetMap.get(targetBoard.board[i][j]).add(j);
                }
            }
        }
        System.out.println("--------------------");
        System.out.println("COL START BOARD: ");
        System.out.println(start.board.toString());

        start.board = simplifyColBoard(start.board,col,row);

        System.out.println("COL SIMPLE BOARD: ");
        System.out.println(start.board.toString());

        System.out.println("COL TARGET BOARD: ");
        System.out.println(targetBoard.toString());

        //Set up A* traversal
        Queue<Vertex<T>> openQueue = new PriorityQueue<>();
        Hashtable<Integer,Vertex<T>> closedSet = new Hashtable<Integer, Vertex<T>>();
        ArrayList<String> moves = new ArrayList<String>();

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
                    System.out.println("FINAL: "+neighbour.board.equals(targetBoard));
                    System.out.println("Result: ");
                    System.out.println("FINAL HC: "+neighbour.hashCode());
                    System.out.println(neighbour.board.toString());
                    System.out.println("Target: ");
                    System.out.println("TARGET HC: "+target.hashCode());
                    System.out.println(target.board.toString());

                    neighbour.parent = cur;
                    return neighbour;
                } else {
                    if(!closedSet.containsKey(neighbour.hashCode()) && !openQueue.contains(neighbour)) {
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
//		System.out.println("number of arguments: " + args.length);
//		for (int i = 0; i < args.length; i++) {
//			System.out.println(args[i]);
//		}

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}

		// SET UP FILE
		File input = new File(args[0]);

		// SOLVE BOARD
//		Vertex<T> result = solvePuzzle(input);
        LinkedList<String> resStack = solveBigPuzzle(input);
//        Stack<String> resStack = new Stack<String>();


        //----------- STILL NEED TO FIX PRINTING TO FILE ------------///

//        System.out.println("RESULT -----");
//		System.out.println(result.board.toString());

//		while(result.parent != null) {
//            resStack.add(result.board.move);
//            result = result.parent;
//        }

		// PRINT RESULT TO FILE
        File output = new File(args[1]);
        FileWriter fw = new FileWriter(output);

        while (resStack.size() > 0 ){
            String res = resStack.removeFirst();
//            System.out.println(res);
            fw.write(res+"\n");
        }
//        while (!resStack.isEmpty()) {
//            String res = resStack.pop();
////            System.out.println(res);
//            fw.write(res+"\n");
//        }

        fw.close();

        long finish = System.currentTimeMillis();

        long timeElapsed = finish - start;

        System.out.println("Time elapsed: "+timeElapsed);


	}
}
