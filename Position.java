import java.util.Objects;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Position other = (Position) obj;
        return row == other.row && col == other.col;
    }

    // You should also override the hashCode() method whenever you override equals()
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

