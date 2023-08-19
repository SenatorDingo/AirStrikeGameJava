public class Ship {
    private String type;
    private int size;
    private int hits;

    public Ship(String type, int size) {
        this.type = type;
        this.size = size;
        this.hits = 0;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public int getHits() {
        return hits;
    }

    public void hit() {
        hits++;
    }

    public boolean isSunk() {
        return hits >= size;
    }
}
