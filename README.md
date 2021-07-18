# ESC BOT
ESC Bot is a chess engine coded in java that has an estimated elo of around 1700.

## Features
For the first few moves, the engine follows a opening book of a few thousand grandmaster games which isn't a lot but is enough to make it past the first 5 or so moves.

ESC Bot uses a standard alpha beta negamax algorithm and quiescence search extension.

The evaluation function takes account of the following factors
* material balance
* piece mobility
* piece placement

This engine also follows UCI protocol and can be found on [lichess](https://lichess.org/@/ESC_BOT).
