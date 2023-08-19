import java.util.Scanner;

public class Game {
    private Player player;
    private AIPlayer aiPlayer;
    private boolean isPlayerTurn;
    private boolean isGameOver;

    public Game(String playerName) {
        player = new Player(playerName);
        aiPlayer = new AIPlayer("AI");
        isPlayerTurn = true;
        isGameOver = false;
    }

    public void start() {
        System.out.println("Welcome to AirStrike! The game has started.");

        // Place player's ships
        player.getBoard().placeShips();

        // Place AI's ships (you can customize the AI's ship placement logic)
        aiPlayer.placeShipsRandomly();

        while (!isGameOver) {
            if (isPlayerTurn) {
                takePlayerTurn();
            } else {
                takeAITurn();
            }

            // Check for game over conditions
            if (player.getBoard().areAllShipsSunk() || aiPlayer.getBoard().areAllShipsSunk()) {
                isGameOver = true;
                announceWinner();
            }
            // Switch turns
            isPlayerTurn = !isPlayerTurn;
        }

        System.out.println("Thanks for playing AirStrike! Goodbye!");
    }

    private void takePlayerTurn() {
        System.out.println("Your Turn!");
        boolean scouting = false;

        // Get player's attack coordinates from console input
        int attackRow, attackCol;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("Enter attack coordinate (e.g., A8) or Scout: ");
            String attackCoordinateStr = scanner.nextLine().trim().toUpperCase();

            if (attackCoordinateStr.equalsIgnoreCase("scout")) {
                // Scout plane functionality
                System.out.print("Enter scout plane position (e.g., A8): ");
                String positionStr = scanner.nextLine().trim().toUpperCase();

                try {
                    scouting = true;
                    Position position = parseCoordinate(positionStr);
                    attackRow = position.getRow();
                    attackCol = position.getCol();
                    doScoutPlane(attackRow, attackCol);
                } catch (IllegalArgumentException e) {
                    attackRow = -1; // Set invalid values to trigger retry
                    attackCol = -1;
                    System.out.println(e.getMessage());
                    System.out.println("Scout plane aborted.");
                }

            } else {
                try {
                    Position attackCoordinate = parseCoordinate(attackCoordinateStr);
                    attackRow = attackCoordinate.getRow();
                    attackCol = attackCoordinate.getCol();
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    attackRow = -1; // Set invalid values to trigger retry
                    attackCol = -1;
                }
            }
        } while (!isValidAttack(attackRow, attackCol));

        // Process player's attack
        if (attackRow != -1 && attackCol != -1 && !scouting) {
            boolean isHit = aiPlayer.getBoard().getCell(attackRow, attackCol).receiveAttack();
            if (isHit) {
                System.out.println("You hit an enemy ship!");
            } else {
                System.out.println("Missed!");
            }
        }
    }

    private void doScoutPlane(int row, int col){
            if (!aiPlayer.getBoard().getCell(row, col).isScouted()) {
                scoutAdjacentCells(row, col);

                // Scout the surrounding cells (up, down, left, right)
                scoutAdjacentCells(row - 1, col);
                scoutAdjacentCells(row + 1, col);
                scoutAdjacentCells(row, col - 1);
                scoutAdjacentCells(row, col + 1);

                System.out.println("Scout plane successfully scouted the surrounding cells!");

            } else {
                System.out.println("This cell has already been scouted. Try again.");
            }

    }

    private void scoutAdjacentCells(int row, int col) {
        Board board = aiPlayer.getBoard();
        if (board.isValidPosition(row, col)) {
            Cell cell = board.getCell(row, col);
            cell.setScouted(true);
        }
    }


    private Position parseCoordinate(String coordinateStr) {
        if (coordinateStr.length() != 2 || !Character.isLetter(coordinateStr.charAt(0)) || !Character.isDigit(coordinateStr.charAt(1))) {
            throw new IllegalArgumentException("Invalid coordinate format. Use a letter followed by a digit (e.g., A8).");
        }

        int row = coordinateStr.charAt(0) - 'A';
        int col = coordinateStr.charAt(1) - '0';

        if (row < 0 || row >= 10 || col < 0 || col >= 10) {
            throw new IllegalArgumentException("Invalid coordinate. Row and column must be within the board size.");
        }

        return new Position(row, col);
    }

    private void takeAITurn() {

        // AI's attack logic (you can customize the AI's attack strategy)
        int[] attackCoordinates;

        // If AI has hit a ship on the previous turn and there are adjacent unscouted cells,
        // focus on those neighbors to hunt for the rest of the ship.
        if (!aiPlayer.getPotentialTargets().isEmpty()) {
            attackCoordinates = aiPlayer.getNextTarget();
        } else {
            // Otherwise, generate a random attack.
            attackCoordinates = aiPlayer.generateRandomAttack();
        }

        int attackRow = attackCoordinates[0];
        int attackCol = attackCoordinates[1];

        if (player.getBoard().getCell(attackRow, attackCol).isHit()){
            takeAITurn();
            return;
        }
        System.out.println("AI's Turn!");

        // Process AI's attack
        boolean isHit = player.getBoard().getCell(attackRow, attackCol).receiveAttack();
        if (isHit) {

            player.getBoard().printBoardsHiddenShips(aiPlayer.getBoard());
            System.out.println("AI hit one of your ships!");

            // If AI hits a ship and there are adjacent unscouted cells, update the target list.
            if (player.getBoard().isValidPosition(attackRow, attackCol)) {
                //aiPlayer.setPreviousHit(attackRow, attackCol);
                aiPlayer.updateTargets(attackRow, attackCol);

            }

        } else {

            player.getBoard().printBoardsHiddenShips(aiPlayer.getBoard());
            System.out.println("AI missed!");
        }
    }


    private boolean isValidAttack(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10 && !aiPlayer.getBoard().getCell(row, col).isHit();
    }

    private void announceWinner() {
        if (player.getBoard().areAllShipsSunk()) {
            System.out.println("AI sunk all your ships. AI wins!");
        } else {
            System.out.println("Congratulations! You sunk all the enemy ships. You win!");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        Game game = new Game(playerName);
        game.start();
    }
}
