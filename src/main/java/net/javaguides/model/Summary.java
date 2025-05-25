package net.javaguides.model;
//This Class is needed to get the output in the old format.
public class Summary {
    private final String stock;
    private final int tradingDays;
    private final int totalTrades;
    private final double profitPct;
    private final double winPercent;

    public Summary(String stock, int tradingDays, int totalTrades, double profitPct, double winPercent) {
        this.stock       = stock;
        this.tradingDays = tradingDays;
        this.totalTrades = totalTrades;
        this.profitPct   = profitPct;
        this.winPercent  = winPercent;
    }

    public String getStock()       { return stock; }
    public int    getTradingDays() { return tradingDays; }
    public int    getTotalTrades() { return totalTrades; }
    public double getProfitPct()   { return profitPct; }
    public double getWinPercent()  { return winPercent; }
}
