
# EMA Crossover Backtester

A simple Java/Maven project to backtest a 5-minute EMA-35/EMA-70 crossover strategy with risk controls and time restrictions.

## Features

* **EMA Periods**: Fast = 35, Slow = 70
* **Entry Window**: Trades only open on or after 10:00 AM
* **Exit Conditions**:

  * Stop Loss: 1%
  * Take Profit: 2.5%
  * Forced Exit: 3:20 PM
* **Outputs**:

  * `output/orderinfo.csv`: detailed per-trade log
  * `output/summary.csv`: aggregated results per stock

## Setup

1. **Requirements**:

   * Java 17+
   * Maven 3.8+
2. **Clone** the repo and enter the project folder.
3. **Place** your 5-minute OHLCV CSV files in `data/`.

## Run

```bash
mvn clean compile exec:java
```

This processes all `*.csv` in `data/`, applies the strategy, and writes:

* `output/orderinfo.csv`
* `output/summary.csv`  ```(Old Format)```

## Configuration

Edit constants in `Backtester.java`:

* `ENTRY_START` (entry after time)
* `EXIT_TIME` (forced exit time)
* `STOP_LOSS_PCT`, `TAKE_PROFIT_PCT`
* `EMA_FAST_PERIOD`, `EMA_SLOW_PERIOD`

