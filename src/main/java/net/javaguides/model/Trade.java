package net.javaguides.model;

import java.time.LocalDateTime;

public class Trade {
    public enum Side { LONG, SHORT }

    private Side side;
    private double entryPrice;
    private LocalDateTime entryTime, exitTime;
    private double pnlPercent;

    public Trade(Side side, double entryPrice, LocalDateTime entryTime) {
        this.side = side;
        this.entryPrice = entryPrice;
        this.entryTime = entryTime;
    }

    public void close(double exitPrice, LocalDateTime exitTime) {
        this.exitTime = exitTime;
        if (side == Side.LONG) {
            pnlPercent = (exitPrice / entryPrice - 1.0) * 100.0;
        } else {
            pnlPercent = (entryPrice / exitPrice - 1.0) * 100.0;
        }
    }

    public double getEntryPrice() {
        return entryPrice;
    }

    public Side getSide()              { return side; }
    public LocalDateTime getEntryTime(){ return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getPnlPercent()      { return pnlPercent; }
}
