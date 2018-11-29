# Connect Four AI
AI program utilizing MiniMax created by Todd Nguyen to play a modified version of Connect 4. The game will end when one player gets 4 in a row, either horizontally or vertically. We will not consider any diagonals.

# Pre-Compiling Setup
Make sure `javac` and `java` is in your PATH variable.

# How to compile
Type in the following command in your terminal while you are at the `ConnectFourMiniMax` folder
```
javac -d "bin" src/algorithms/*.java src/mainBoard/*.java src/successors/*.java
```

# How to Run
1. Go into the `bin` folder
2. Type in the following command in your terminal
```
java mainBoard.MainClass
```
