import java.util.Random;
import java.util.Scanner;

public class Board {
    private Player player;
    private Cell[][] grid;
    private int numRows;
    private int numCols;

    public Board(Player player, int numRows, int numCols) {
        this.player = player;
        this.numRows = numRows;
        this.numCols = numCols;
        grid = new Cell[numRows][numCols];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                grid[row][col] = new Cell();
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            throw new IllegalArgumentException("Invalid cell coordinates.");
        }
        return grid[row][col];
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    public void placeShips() {
        System.out.println("Player " + player.getName() + ", it's time to place your ships!");

        for (ShipType shipType : ShipType.values()) {
            boolean isPlacementSuccessful = false;
            Ship ship = new Ship(shipType.getName(), shipType.getSize());

            while (!isPlacementSuccessful) {
                System.out.println("Your current board:");
                printBoard();

                Scanner scanner = new Scanner(System.in);
                System.out.println("Placing " + shipType.getName() + " (" + shipType.getSize() + " cells)");

                System.out.print("Enter start position (e.g., A6): ");
                String startPositionStr = scanner.nextLine().trim().toUpperCase();

                System.out.print("Enter end position (e.g., A3): ");
                String endPositionStr = scanner.nextLine().trim().toUpperCase();

                try {
                    Position startPosition = parsePosition(startPositionStr);
                    Position endPosition = parsePosition(endPositionStr);

                    isPlacementSuccessful = tryPlaceShip(ship, shipType.getCode(), startPosition, endPosition);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("Your board after placing ships:");
        printBoard();
    }

    private boolean tryPlaceShip(Ship ship, String shipCode, Position startPosition, Position endPosition) {
        // Check if the ship placement is valid (not overlapping with other ships)
        int shipSize = ship.getSize();
        boolean isHorizontal = startPosition.getRow() == endPosition.getRow();

        int minRow = Math.min(startPosition.getRow(), endPosition.getRow());
        int maxRow = Math.max(startPosition.getRow(), endPosition.getRow());
        int minCol = Math.min(startPosition.getCol(), endPosition.getCol());
        int maxCol = Math.max(startPosition.getCol(), endPosition.getCol());

        if ((isHorizontal && maxCol - minCol + 1 != shipSize) || (!isHorizontal && maxRow - minRow + 1 != shipSize)) {
            System.out.println("Invalid ship placement. The ship must have exactly " + shipSize + " cells.");
            return false;
        }

        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                if (!isValidPosition(row, col) || getCell(row, col).hasShip()) {
                    System.out.println("Invalid ship placement. Ships cannot overlap.");
                    return false;
                }
            }
        }

        // Place the ship on the board
        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                getCell(row, col).placeShip(shipCode);
            }
        }

        return true;
    }


    private Position parsePosition(String positionStr) {
        if (positionStr.length() != 2 || !Character.isLetter(positionStr.charAt(0)) || !Character.isDigit(positionStr.charAt(1))) {
            throw new IllegalArgumentException("Invalid position format. Use a letter followed by a digit (e.g., A6).");
        }

        int row = positionStr.charAt(0) - 'A';
        int col = positionStr.charAt(1) - '0';

        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            throw new IllegalArgumentException("Invalid position. Row and column must be within the board size.");
        }

        return new Position(row, col);
    }


    public void printBoard() {
        System.out.print("   ");
        for (int col = 0; col < numCols; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < numRows; row++) {
            char rowLabel = (char) ('A' + row);
            System.out.print(rowLabel + "  ");

            for (int col = 0; col < numCols; col++) {
                System.out.print(getCell(row, col).getCellDisplay() + " ");
            }

            System.out.println();
        }
    }

    public void printBoardsHiddenShips(Board aiBoard) {
        System.out.println("Your board:");
        printBoard();

        if (aiBoard != null) {
            System.out.println("AI's board:");
            aiBoard.printBoardHiddenShips();
        }
    }

    public void printBoardHiddenShips() {
        System.out.print("   ");
        for (int col = 0; col < numCols; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < numRows; row++) {
            char rowLabel = (char) ('A' + row);
            System.out.print(rowLabel + "  ");

            for (int col = 0; col < numCols; col++) {
                Cell cell = getCell(row, col);

                if (cell.isHit()) {
                    System.out.print(cell.hasShip() ? "X " : "M ");
                } else if (cell.isScouted()) {
                    System.out.print(cell.hasShip() ? "S " : "M ");
                } else {
                    System.out.print("~ ");
                }
            }

            System.out.println();
        }
    }


    public boolean areAllShipsSunk() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Cell cell = getCell(row, col);
                if (cell.hasShip() && !cell.isHit()) {
                    return false;
                }
            }
        }
        return true;
    }



}
