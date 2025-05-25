package net.javaguides.backtest;

import com.opencsv.CSVReader;
import net.javaguides.indicators.EMA;
import net.javaguides.model.Bar;
import net.javaguides.model.Trade;
import net.javaguides.model.Trade.Side;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Backtester {

    private static final double STOP_LOSS_PCT   = 1.0;
    private static final double TAKE_PROFIT_PCT = 2.5;
    private static final LocalTime ENTRY_START = LocalTime.of(10, 0);
    private static final LocalTime EXIT_TIME    = LocalTime.of(15, 20);
    private static final int EMA_FAST_PERIOD    = 35;
    private static final int EMA_SLOW_PERIOD    = 70;

    public static void runBackTest(String csvPath, String stockName) throws Exception {
        List<Bar> bars = new ArrayList<>();
        try (CSVReader r = new CSVReader(new FileReader(csvPath))) {
            r.readNext(); // skip header
            String[] row;
            while ((row = r.readNext()) != null)
                bars.add(new Bar(row));
        }

        EMA emaFast = new EMA(EMA_FAST_PERIOD), emaSlow = new EMA(EMA_SLOW_PERIOD);
        Double prevFast = null, prevSlow = null;

        List<Trade> allTrades  = new ArrayList<>();
        List<Trade> openTrades = new ArrayList<>();
        Set<LocalDate> days    = new HashSet<>();

        for (Bar b : bars) {
            days.add(b.getTime().toLocalDate());
            double price = b.getClose();
            Double f = emaFast.next(price), s = emaSlow.next(price);

            //  Check exit conditions FIRST
            List<Trade> finished = new ArrayList<>();
            for (Trade t : openTrades) {
                // Update current profit before checking exit conditions
                t.updateCurrentProfit(price);
                //calculate stop-loss and take -profit 
                double sl = t.getEntryPrice() * (t.getSide() == Side.LONG
                        ? 1 - STOP_LOSS_PCT/100
                        : 1 + STOP_LOSS_PCT/100);
                double tp = t.getEntryPrice() * (t.getSide() == Side.LONG
                        ? 1 + TAKE_PROFIT_PCT/100
                        : 1 - TAKE_PROFIT_PCT/100);
                //check if the base conditions are met(profit/loss and in the time limit)
                boolean slHit   = (t.getSide()==Side.LONG  && b.getLow()  <= sl) ||
                                  (t.getSide()==Side.SHORT && b.getHigh() >= sl);
                boolean tpHit   = (t.getSide()==Side.LONG  && b.getHigh() >= tp) ||
                                  (t.getSide()==Side.SHORT && b.getLow()  <= tp);
                boolean timeOut = b.getTime().toLocalTime().compareTo(EXIT_TIME) >= 0;
                
                if (slHit || tpHit || timeOut) {
                    double exitPrice = b.getClose(); // default to close price
                    
                    // If both SL and TP are hit, choose the worse outcome
                    if (slHit && tpHit) {
                        // Calculate which gives worse result
                        double slProfit = (t.getSide() == Side.LONG) 
                            ? (sl / t.getEntryPrice() - 1) * 100
                            : (t.getEntryPrice() / sl - 1) * 100;
                        double tpProfit = (t.getSide() == Side.LONG)
                            ? (tp / t.getEntryPrice() - 1) * 100  
                            : (t.getEntryPrice() / tp - 1) * 100;
                            
                        exitPrice = (slProfit < tpProfit) ? sl : tp;
                    } else if (slHit) {
                        exitPrice = sl;
                    } else if (tpHit) {
                        exitPrice = tp;
                    }
                    // timeOut uses close price (already set)
                    
                    t.markExit(exitPrice, b.getTime());
                    finished.add(t);
                }
            }
            openTrades.removeAll(finished);

            // Check entry conditions AFTER exit conditions-New Trade
            boolean timeIn =b.getTime().toLocalTime().compareTo(ENTRY_START) >= 0;
            if ((f != null && s != null && prevFast != null && prevSlow != null) && (timeIn==true) ){
            if (prevFast <= prevSlow && f > s) {
                Trade t = new Trade(Side.LONG, price, b.getTime());
                allTrades.add(t);
                openTrades.add(t);
            } else if (prevFast >= prevSlow && f < s) {
                Trade t = new Trade(Side.SHORT, price, b.getTime());
                allTrades.add(t);
                openTrades.add(t);
            }
        
    }

            prevFast = f;
            prevSlow = s;
        }

        // Write order info CSV
        try (PrintWriter w = new PrintWriter(new FileWriter("output/orderinfo.csv", true))) {
            int tradeId = 0;
            for (Trade t : allTrades) {
                if (!t.isExited()) continue;
                
                String[] entryDateTime = t.getEntryTime().toString().split("T");
                String[] exitDateTime = t.getExitTime().toString().split("T");
                String tradeType = t.getSide() == Side.LONG ? "L" : "S";
                
                w.printf("%s,%d,%s,%s,%s,%.2f,%s,%s,%.2f,%.2f,%d%n",
                    stockName, 
                    tradeId++, 
                    tradeType,
                    entryDateTime[0], 
                    entryDateTime[1], 
                    t.getEntryPrice(),
                    exitDateTime[0], 
                    exitDateTime[1], 
                    t.getExitPrice(),
                    t.getPnlPercent(),
                    EMA_FAST_PERIOD
                );
            }
        }

        // Calculate summary statistics - for Old format of output
       /*  int total = 0;
        double profit = 0;
        long wins = 0;
        for (Trade t : allTrades) {
            if (!t.isExited()) continue;
            total++;
            profit += t.getPnlPercent();
            if (t.getPnlPercent() > 0) wins++;
        }
        double winPct = total > 0 ? wins * 100.0 / total : 0;

        return new Summary(stockName, days.size(), total, profit, winPct);
    } */
    }
}