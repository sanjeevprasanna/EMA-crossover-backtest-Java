📈 EMA Crossover Backtest

Backtest a 5-minute EMA-35/EMA-70 crossover trading strategy on multiple stock CSV files.
Includes logic for entry, stop-loss, take-profit, and time-based exits — with results summarized into a single CSV report.

🔧 Features

Uses Exponential Moving Averages (EMA-35 and EMA-70)


Strategy:

Buy when EMA-35 crosses above EMA-70

Sell when EMA-35 crosses below EMA-70

Exit at:

+2.5% profit (TP)

−1% loss (SL)

or 3:20 PM if neither triggered

Accepts multiple CSV files (one per stock) from data/

Outputs one summary CSV in output/summary.csv

Built in Java 17 + Maven

📥 Input Format
Each file in data/ must be a CSV with this format:

timestamp,open,high,low,close,volume
13-07-17 09:15,264.8,266.5,264.15,266.0,703982
Timestamp format: dd-MM-yy HH:mm

5-minute OHLCV data per stock

📤 Output Summary
Generates a file: output/summary.csv with columns:

Stock,TradingDays,TotalTrades,Profit,WinPercent
AAPL,223,1021,25012,66.66
TSLA,643,1211,18401,72.13

🚀 Run the Backtest

mvn clean compile exec:java
🔁 This will process all CSVs in data/ and generate output/summary.csv.

📦 Build Requirements
Java 17+

Maven 3.8+

✏️ Customize Parameters
You can change:

Parameter	Location
EMA lengths	Backtester.java line ~31
SL/TP Percent	Backtester.java constants
Exit time	Backtester.java line ~32

📄 License
MIT License (or add your preferred license)

