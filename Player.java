public class Player {
    private String name;
    private Board board;
    private int highScore;

    public Player(){

    }

    public Player(String name) {
        this.name = name;
        this.board = new Board(this, 10, 10);
        this.highScore = Integer.MAX_VALUE;
    }


    // Getter and Setter for name
    public String getName() {
        return name;
    }

    // Setter for high score
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    // Method to get the high score
    public int getHighScore() {
        return highScore;
    }

    // Getter for the player's board
    public Board getBoard() {
        return board;
    }

    // Add any other methods or logic you need for the Player class
}
