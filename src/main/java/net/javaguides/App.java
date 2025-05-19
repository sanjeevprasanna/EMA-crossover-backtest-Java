package net.javaguides;

import net.javaguides.backtest.Backtester;
import net.javaguides.model.Summary;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        File dataDir = new File("data");
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.err.println("ERROR: data/ not found");
            System.exit(1);
        }

        File outDir = new File("output");
        if (!outDir.exists()) outDir.mkdirs();

        File[] files = dataDir.listFiles((d, n) -> n.toLowerCase().endsWith(".csv"));
        if (files == null || files.length == 0) {
            System.err.println("No CSVs in data/");
            System.exit(1);
        }

        List<Summary> list = new ArrayList<>();
        for (File f : files) {
            String name = f.getName().replaceFirst("(?i)\\.csv$", "");
            System.out.println("Processing " + name);
            try {
                Summary sum = Backtester.runSummary(f.getPath(), name);
                list.add(sum);
            } catch (Exception e) {
                System.err.println("Error on " + name + ": " + e.getMessage());
            }
        }

        // Write summary.csv
        try (PrintWriter pw = new PrintWriter(new FileWriter("output/summary.csv"))) {
            pw.println("Stock,TradingDays,TotalTrades,Profit,WinPercent");
            for (Summary s : list) {
                pw.printf("%s,%d,%d,%.2f,%.2f%n",
                    s.getStock(),
                    s.getTradingDays(),
                    s.getTotalTrades(),
                    s.getProfitPct(),
                    s.getWinPercent()
                );
            }
            System.out.println("Wrote summary to output/summary.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
