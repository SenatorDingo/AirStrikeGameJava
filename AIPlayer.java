import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class AIPlayer extends Player{
    private String name;
    private Board board;
    private Position prev;
    private List<Position> potentialTargets;

    public AIPlayer(String name) {
        super(name);
        potentialTargets = new ArrayList<>();
        this.board = new Board(this, 10, 10);
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public void placeShipsRandomly() {
        Random random = new Random();

        for (ShipType shipType : ShipType.values()) {
            boolean isHorizontal = random.nextBoolean();
            int shipSize = shipType.getSize();

            boolean isPlacementSuccessful = false;

            while (!isPlacementSuccessful) {
                int startRow = random.nextInt(10);
                int startCol = random.nextInt(10);

                isPlacementSuccessful = tryPlaceShip(shipType, shipType.getCode(), startRow, startCol, isHorizontal);
            }
        }
    }

    private boolean tryPlaceShip(ShipType shipType, String shipTypeCode, int startRow, int startCol, boolean isHorizontal) {
        int shipSize = shipType.getSize();
        int endRow = startRow + (isHorizontal ? 0 : shipSize - 1);
        int endCol = startCol + (isHorizontal ? shipSize - 1 : 0);

        if (endRow >= 10 || endCol >= 10) {
            return false;
        }

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (board.getCell(row, col).hasShip()) {
                    return false;
                }
            }
        }

        for (int i = 0; i < shipSize; i++) {
            int row = startRow + (isHorizontal ? 0 : i);
            int col = startCol + (isHorizontal ? i : 0);
            board.getCell(row, col).placeShip(shipTypeCode);
        }

        return true;
    }


    private boolean isValidPlacement(int startRow, int startCol, int shipSize, boolean isHorizontal) {
        for (int i = 0; i < shipSize; i++) {
            int row = startRow;
            int col = startCol;

            if (isHorizontal) {
                col += i;
            } else {
                row += i;
            }

            if (!board.isValidPosition(row, col) || board.getCell(row, col).hasShip()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPreviousHit() {
        return prev != null;
    }

    public boolean hasAdjacentUnscoutedCells() {
        if (prev == null) {
            return false;
        }

        int row = prev.getRow();
        int col = prev.getCol();
        return (board.isValidPosition(row - 1, col) && !board.getCell(row - 1, col).isScouted()) ||   // North
                (board.isValidPosition(row + 1, col) && !board.getCell(row + 1, col).isScouted()) ||   // South
                (board.isValidPosition(row, col - 1) && !board.getCell(row, col - 1).isScouted()) ||   // West
                (board.isValidPosition(row, col + 1) && !board.getCell(row, col + 1).isScouted());     // East
    }

    public int[] getNextTarget() {
        if (potentialTargets.isEmpty()) {
            return generateRandomAttack(); // If no potential targets, fallback to random attack.
        }

        // Choose a random potential target from the list.
        Random random = new Random();
        int index = random.nextInt(potentialTargets.size());
        Position target = potentialTargets.get(potentialTargets.size()-1);

        // Remove the chosen target from the list to avoid attacking the same target repeatedly.
        potentialTargets.remove(potentialTargets.size()-1);

        return new int[]{target.getRow(), target.getCol()};
    }

    public void setPreviousHit(int row, int col) {

        // When a new hit occurs, reset the list of potential targets.
        potentialTargets.clear();
    }

    public void setPrev(Position prev){
        this.prev = prev;
    }

    public void updateTargets(int row, int col) {
        // If the hit position is valid and there are unscouted adjacent cells, add them to the list of potential targets.
        if (board.isValidPosition(row - 1, col) && !board.getCell(row - 1, col).isHit() && !potentialTargets.contains(new Position(row - 1, col))) {
            potentialTargets.add(new Position(row - 1, col)); // North
        }
        if (board.isValidPosition(row + 1, col) && !board.getCell(row + 1, col).isHit() && !potentialTargets.contains(new Position(row + 1, col))) {
            potentialTargets.add(new Position(row + 1, col)); // South
        }
        if (board.isValidPosition(row, col - 1) && !board.getCell(row, col - 1).isHit() && !potentialTargets.contains(new Position(row, col - 1))) {
            potentialTargets.add(new Position(row, col - 1)); // West
        }
        if (board.isValidPosition(row, col + 1) && !board.getCell(row, col + 1).isHit() && !potentialTargets.contains(new Position(row, col + 1))) {
            potentialTargets.add(new Position(row, col + 1)); // East
        }
    }




    public int[] generateRandomAttack() {
        Random random = new Random();
        int attackRow = random.nextInt(10);
        int attackCol = random.nextInt(10);
        return new int[]{attackRow, attackCol};
    }

    public List<Position> getPotentialTargets() {
        return potentialTargets;
    }
}
