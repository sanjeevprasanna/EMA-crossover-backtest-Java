package net.javaguides.backtest;

import com.opencsv.CSVReader;
import net.javaguides.indicators.EMA;
import net.javaguides.model.Bar;
import net.javaguides.model.Trade;
import net.javaguides.model.Trade.Side;
import net.javaguides.model.Summary;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Backtester {

    private static final double STOP_LOSS_PCT   = 1.0;
    private static final double TAKE_PROFIT_PCT = 2.5;
    private static final LocalTime EXIT_TIME    = LocalTime.of(15, 20);

    /**
     * Runs the strategy over one CSV and returns the summary stats.
     */
    public static Summary runSummary(String csvPath, String stockName) throws Exception {
        List<Bar> bars = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] row;
            reader.readNext(); // skip header
            while ((row = reader.readNext()) != null)
                bars.add(new Bar(row));
        }

        EMA emaFast = new EMA(35), emaSlow = new EMA(70);
        Double prevFast = null, prevSlow = null;
        Trade open = null;
        List<Trade> trades = new ArrayList<>();
        Set<LocalDate> days = new HashSet<>();

        for (Bar b : bars) {
            days.add(b.getTime().toLocalDate());
            double price = b.getClose();
            Double f = emaFast.next(price), s = emaSlow.next(price);
            if (f == null || s == null) continue;

            // ENTRY
            if (prevFast != null && prevSlow != null && open == null) {
                if (prevFast <= prevSlow && f > s)
                    open = new Trade(Side.LONG, price, b.getTime());
                else if (prevFast >= prevSlow && f < s)
                    open = new Trade(Side.SHORT, price, b.getTime());
            }

            // EXIT
            if (open != null) {
                double sl = open.getEntryPrice() *
                            (open.getSide() == Side.LONG
                                ? 1 - STOP_LOSS_PCT/100
                                : 1 + STOP_LOSS_PCT/100);
                double tp = open.getEntryPrice() *
                            (open.getSide() == Side.LONG
                                ? 1 + TAKE_PROFIT_PCT/100
                                : 1 - TAKE_PROFIT_PCT/100);

                boolean shouldExit = false;
                if ((open.getSide() == Side.LONG  && b.getLow()  <= sl) ||
                    (open.getSide() == Side.SHORT && b.getHigh() >= sl)) {
                    open.close(sl, b.getTime());
                    shouldExit = true;
                }
                else if ((open.getSide() == Side.LONG  && b.getHigh() >= tp) ||
                         (open.getSide() == Side.SHORT && b.getLow()  <= tp)) {
                    open.close(tp, b.getTime());
                    shouldExit = true;
                }
                else if (b.getTime().toLocalTime().compareTo(EXIT_TIME) >= 0) {
                    open.close(price, b.getTime());
                    shouldExit = true;
                }

                if (shouldExit) {
                    trades.add(open);
                    open = null;
                }
            }

            prevFast = f; prevSlow = s;
        }

        int totalTrades = trades.size();
        double profitPct = trades.stream()
                                 .mapToDouble(Trade::getPnlPercent)
                                 .sum();
        long wins = trades.stream().filter(t -> t.getPnlPercent() > 0).count();
        double winPct = totalTrades > 0
                      ? wins * 100.0 / totalTrades
                      : 0.0;

        return new Summary(
            stockName,
            days.size(),
            totalTrades,
            profitPct,
            winPct
        );
    }

    // ... your existing run(input,output) can stay here if needed ...
}
