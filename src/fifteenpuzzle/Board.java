package fifteenpuzzle;

import java.io.*;
import java.util.*;

public class Board {

    int[][] board;
    int size;
    int emptyRow;
    int emptyCol;
    int tile;
    String move;

    Board(int[][] input, int size) {
        this.size = size;

        this.board = new int[size][size];

        for (int i = 0; i<size;i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = input[i][j];
            }
        }

        this.findEmpty();
    }

    Board(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        size = reader.read() - '0';
        String firstLine = reader.readLine();
        this.board = new int[size][size];
        int c1, c2, sp;

        //Create starting board
        for (int i = 0; i<size;i++) {
            for (int j=0; j<size;j++) {
                c1 = reader.read();
                c2 = reader.read();
                sp = reader.read();

                if(c1 == ' ') {
                    c1 = '0';
                }
                if (c2 == ' ') {
                    c2 = '0';
                    emptyRow = i;
                    emptyCol = j;
                }

                board[i][j] = 10*(c1-'0') + (c2-'0');
            }
        }
        reader.close();
    }

    public Board moveLeft() {
        Board copy = new Board(this.board,size);
        copy.tile = copy.board[emptyRow][emptyCol+1];
        copy.move = "L";

        copy.board[emptyRow][emptyCol] = copy.board[emptyRow][emptyCol+1];
        copy.board[emptyRow][emptyCol+1] = 0;
        copy.emptyCol++;

        return copy;
    }

    public Board moveRight() {
        Board copy = new Board(this.board,size);

        copy.tile = copy.board[emptyRow][emptyCol-1];
        copy.move = "R";

        copy.board[emptyRow][emptyCol] = copy.board[emptyRow][emptyCol-1];
        copy.board[emptyRow][emptyCol-1] = 0;
        copy.emptyCol--;

        return copy;
    }

    public Board moveDown() {
        Board copy = new Board(this.board,size);

        copy.tile = copy.board[emptyRow-1][emptyCol];
        copy.move = "D";

        copy.board[emptyRow][emptyCol] = copy.board[emptyRow-1][emptyCol];
        copy.board[emptyRow-1][emptyCol] = 0;
        copy.emptyRow--;

        return copy;
    }

    public Board moveUp() {
        Board copy = new Board(this.board,size);

        copy.tile = copy.board[emptyRow+1][emptyCol];
        copy.move = "U";
        copy.board[emptyRow][emptyCol] = copy.board[emptyRow+1][emptyCol];
        copy.board[emptyRow+1][emptyCol] = 0;
        copy.emptyRow++;

        return copy;
    }

    public Set<Board> getNextStates() throws IOException {

        Set<Board> nextStates = new HashSet<Board>();
        Board tmp;

        //in corner - 2 moves
        if (emptyRow == 0) {
            tmp = moveUp();
            nextStates.add(tmp);

            if (emptyCol == 0) {
                //TOP LEFT CORNER
                tmp = moveLeft();
                nextStates.add(tmp);
            } else if (emptyCol == (size-1)) {
                //TOP RIGHT CORNER
                tmp = moveRight();
                nextStates.add(tmp);
            }
            else {
                //TOP ROW
                tmp = moveLeft();
                nextStates.add(tmp);

                tmp = moveRight();
                nextStates.add(tmp);
            }

        } else if (emptyRow == (size-1)) {
            tmp = moveDown();
            nextStates.add(tmp);

            if (emptyCol == 0) {
                //BOTTOM LEFT CORNER
                tmp = moveLeft();
                nextStates.add(tmp);
            } else if (emptyCol == (size-1)) {
                //BOTTOM RIGHT CORNER
                tmp = moveRight();
                nextStates.add(tmp);
            } else {
                //BOTTOM ROW
                tmp = moveLeft();
                nextStates.add(tmp);

                tmp = moveRight();
                nextStates.add(tmp);
            }
        } else if (emptyCol == 0) {
            // LEFT COLUMN
            tmp = moveLeft();
            nextStates.add(tmp);

            tmp = moveUp();
            nextStates.add(tmp);

            tmp = moveDown();
            nextStates.add(tmp);
        } else if (emptyCol == (size-1)) {
            // RIGHT COLUMN
            tmp = moveRight();
            nextStates.add(tmp);

            tmp = moveUp();
            nextStates.add(tmp);

            tmp = moveDown();
            nextStates.add(tmp);
        } else {
            //in middle - 4 moves
            tmp = moveRight();
            nextStates.add(tmp);

            tmp = moveLeft();
            nextStates.add(tmp);

            tmp = moveUp();
            nextStates.add(tmp);

            tmp = moveDown();
            nextStates.add(tmp);
        }

        return nextStates;
    }

    public void findEmpty() {
        for (int i = 0; i<size;i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    public String numToString(int i) {
        if (i==0)
            return "  ";
        else if (i<10)
            return " " + Integer.toString(i);
        else
            return Integer.toString(i);
    }

    public String toString() {
        String res = "";
        for (int i=0; i<size; i++) {
            res += numToString(board[i][0]);
            for (int j=1; j<size; j++) {
                res += " " + numToString(board[i][j]);
            }
            res += "\n";
        }
        return res;
    }


    public boolean equals(Object other) {
        if (!(other instanceof Board)) {
            return false;
        }

        if (this.emptyRow != ((Board) other).emptyRow || this.emptyCol != ((Board) other).emptyCol) {
            return false;
        } else {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] != ((Board) other).board[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
