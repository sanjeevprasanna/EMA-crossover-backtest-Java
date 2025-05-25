package net.javaguides.model;

import java.time.LocalDateTime;

public class Trade {
    public enum Side { LONG, SHORT }

    private Side side;
    private double entryPrice;
    private LocalDateTime entryTime;
    private boolean exited = false;
    private double exitPrice;
    private LocalDateTime exitTime;
    private double pnlPercent;
    private double currentProfit; // Current unrealized profit

    public Trade(Side side, double entryPrice, LocalDateTime entryTime) {
        this.side = side;
        this.entryPrice = entryPrice;
        this.entryTime = entryTime;
        this.currentProfit = 0.0;
    }

    public void updateCurrentProfit(double currentPrice) {
        this.currentProfit = (side == Side.LONG)
            ? (currentPrice / entryPrice - 1) * 100
            : (entryPrice / currentPrice - 1) * 100;
    }

    public void markExit(double exitPrice, LocalDateTime exitTime) {
        this.exited = true;
        this.exitPrice = exitPrice;
        this.exitTime = exitTime;
        this.pnlPercent = (side == Side.LONG)
            ? (exitPrice / entryPrice - 1) * 100
            : (entryPrice / exitPrice - 1) * 100;
    }

    // Getters
    public boolean isExited()           { return exited; }
    public Side getSide()               { return side; }
    public double getEntryPrice()       { return entryPrice; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public double getExitPrice()        { return exitPrice; }
    public LocalDateTime getExitTime()  { return exitTime; }
    public double getPnlPercent()       { return pnlPercent; }
    public double getCurrentProfit()    { return currentProfit; }
}