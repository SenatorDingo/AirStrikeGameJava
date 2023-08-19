public class Cell {
    private boolean hasShip;
    private boolean hit;
    private String shipTypeCode;
    private boolean scouted;

    public Cell() {
        hasShip = false;
        hit = false;
        shipTypeCode = "";
        scouted = false;
    }

    public boolean hasShip() {
        return hasShip;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean receiveAttack() {
        if (hit) {
            return false; // Cell is already hit
        }

        hit = true;
        scouted = true;

        if (hasShip) {
            return true; // Attack hit a ship
        }

        return false; // Attack missed
    }

    public void placeShip(String shipTypeCode) {
        hasShip = true;
        this.shipTypeCode = shipTypeCode;
    }

    public String getCellDisplay() {
        if (hit) {
            return hasShip ? "X" : "M";
        } else {
            return hasShip ? shipTypeCode : "~";
        }
    }


    public boolean isScouted() {
        return scouted;
    }

    public void setScouted(boolean scouted) {
        this.scouted = scouted;
    }
}
