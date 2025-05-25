package net.javaguides;

import net.javaguides.backtest.Backtester;

import java.io.File;

public class App {
    public static void main(String[] args) {
        File dataDir = new File("data");
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.err.println("ERROR: data/ not found");
            System.exit(1);
        }

        // ensure output directory
        File outDir = new File("output");
        if (!outDir.exists()) outDir.mkdirs();

        File[] files = dataDir.listFiles((d, n) -> n.toLowerCase().endsWith(".csv"));
        if (files == null || files.length == 0) {
            System.err.println("No CSVs in data/");
            System.exit(1);
        }

        for (File f : files) {
            String stock = f.getName().replaceFirst("(?i)\\.csv$", "");
            System.out.println("Processing " + stock);
            try {
                Backtester.runBackTest(f.getPath(), stock);
            } catch (Exception e) {
                System.err.println("Error on " + stock + ": " + e.getMessage());
            }
        }

        // write summary.csv -OLD FORMAT
       /*  try (PrintWriter pw = new PrintWriter(new FileWriter("output/summary.csv"))) {
            pw.println("Stock,TradingDays,TotalTrades,Profit,WinPercent");
            for (Summary s : summaries) {
                pw.printf("%s,%d,%d,%.2f,%.2f%n",
                          s.getStock(),
                          s.getTradingDays(),
                          s.getTotalTrades(),
                          s.getProfitPct(),
                          s.getWinPercent());
            }
            System.out.println("Wrote summary to output/summary.csv");
        } catch (Exception e) {
            e.printStackTrace();
        } */
    }
}