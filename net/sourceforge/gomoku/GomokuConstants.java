package net.sourceforge.gomoku;


public interface GomokuConstants {
  // Board specification
  byte ROWS = 15;
  byte COLS = 15;

  // Victory Conditions
  byte COMPUTER_WIN = 0;
  byte USER_WIN = 1;
  byte NO_WIN = 2;
  byte DRAW = 3;
  byte CANCELLED = 4;

  // Pieces
  byte NO_PIECE = 0;
  byte USER_PIECE = 1;
  byte COMPUTER_PIECE = 2;
}
