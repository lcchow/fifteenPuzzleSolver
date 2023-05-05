package fifteenpuzzle.old;

import fifteenpuzzle.Board;
import fifteenpuzzle.Vertex;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class Solvercopy {


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
        HashSet<Vertex<T>> closedSet = new HashSet<Vertex<T>>();

        openQueue.add(start);

        while(!openQueue.isEmpty()) {
//            System.out.println(openQueue);
            Vertex<T> cur = openQueue.poll();
//            System.out.println("CURRENT ----------");
//            System.out.println("dist: " +cur.distance);
////            System.out.println(cur.board.toString());
//            System.out.println("HC: "+cur.hashCode());
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
                    if(!closedSet.contains(neighbour)) {
                        neighbour.parent = cur;
                        openQueue.add(neighbour);
                    } else {
//                        System.out.println(closedSet.contains(neighbour));
                    }
                }

            }
            closedSet.add(cur);
//            System.out.println(closedSet.toString());
        }

        System.out.println("NULL EXIT");

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
		Vertex<T> result = solvePuzzle(input);

        System.out.println("RESULT -----");
		System.out.println(result.board.toString());

        Stack<String> resStack = new Stack<String>();

		while(result.parent != null) {
//		    System.out.println(result.board.move);
//            System.out.println(result.board.toString());
            resStack.add(result.board.move);
            result = result.parent;
        }

		// PRINT RESULT TO FILE
        File output = new File(args[1]);
        FileWriter fw = new FileWriter(output);
        while (!resStack.isEmpty()) {
            String res = resStack.pop();
//            System.out.println(res);
            fw.write(res+"\n");
        }

        fw.close();

        long finish = System.currentTimeMillis();

        long timeElapsed = finish - start;

        System.out.println("Time elapsed: "+timeElapsed);


	}
}
