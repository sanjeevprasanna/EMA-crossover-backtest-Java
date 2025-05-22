package net.javaguides.indicators;

public class EMA {
    private final int period;
    private final double multiplier;
    private Double lastEma = null;

    public EMA(int period) {
        this.period = period;
        this.multiplier = 2.0 / (period + 1);
    }

    //Feed in the next price; returns current EMA 
    public Double next(double price) {
        if (lastEma == null) {
            lastEma = price;  // simple init
        } else {
            lastEma = (price - lastEma) * multiplier + lastEma;//EMA FOrmula
        }
        return lastEma;
    }
}
