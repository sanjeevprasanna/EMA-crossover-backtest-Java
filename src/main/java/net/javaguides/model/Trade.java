package net.javaguides.model;

import java.time.LocalDateTime;

public class Trade {

    // Defines the direction of the trade
    public enum Side { LONG, SHORT }

    private Side side;
    private double entryPrice;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double pnlPercent;

    // constructor to create a new trade entry
    public Trade(Side side, double entryPrice, LocalDateTime entryTime) {
        this.side = side;
        this.entryPrice = entryPrice;
        this.entryTime = entryTime;
    }

    // close the trade and calculate PnL
    public void close(double exitPrice, LocalDateTime exitTime) {
        this.exitTime = exitTime;
        if (side == Side.LONG) {
            pnlPercent = (exitPrice / entryPrice - 1.0) * 100.0;
        } else {
            pnlPercent = (entryPrice / exitPrice - 1.0) * 100.0;
        }
    }

    // getter methods
    public double getEntryPrice() {
        return entryPrice;
    }

    public Side getSide() {
        return side;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getPnlPercent() {
        return pnlPercent;
    }
}