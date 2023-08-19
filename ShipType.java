public enum ShipType {
    CARRIER("Carrier", 5, "C"),
    BATTLESHIP("Battleship", 4, "B"),
    CRUISER("Cruiser", 3, "R"),
    SUBMARINE("Submarine", 3, "S"),
    DESTROYER("Destroyer", 2, "D");

    private final String name;
    private final int size;
    private final String code;

    ShipType(String name, int size, String code) {
        this.name = name;
        this.size = size;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getCode() {
        return code;
    }
}
