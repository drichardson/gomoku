package net.sourceforge.gomoku;


public abstract class Board implements GomokuConstants {

  abstract byte getPiece(final int row, final int col);

  abstract void setPiece(final int row, final int col, final byte piece);

  abstract void clear();

  abstract Board copy();

  abstract int getRows();

  abstract int getCols();

  void setPiece(final Square square, final byte piece) {
    setPiece(square.getRow(), square.getCol(), piece);
  }

  int terminalTest() {
    int maxRow = getRows();
    int maxCol = getCols();
    int numOfEmptySquares = maxRow * maxCol;

    for (int row = 0; row < maxRow; ++row) {
      for (int col = 0; col < maxCol; ++col) {
        if (getPiece(row, col) != NO_PIECE) {
          --numOfEmptySquares;
          switch (victory(row, col)) {
            case USER_WIN:
              return USER_WIN;

            case COMPUTER_WIN:
              return COMPUTER_WIN;

            default:
              break;
          }
        }
      }
    }

    return numOfEmptySquares == 0 ? DRAW : NO_WIN;
  }

  int victory(final int row, final int col) {
//    final boolean victory = false;

    final int playerPiece = getPiece(row, col);

    if (playerPiece == NO_PIECE) {
      return NO_WIN;
    }

    if (inARowAt(row, col, 5, 2) > 0) {
      return playerPiece == USER_PIECE ? USER_WIN : COMPUTER_WIN;
    }

    return NO_WIN;
  }

  private int inARowAt(final int row, final int col,
      final int num, final int maxcaps) {

    int count = 0;
    int r;
    int c;

    final int playerPiece = getPiece(row, col);
    final int opponentPiece = playerPiece == USER_PIECE
        ? COMPUTER_PIECE : USER_PIECE;

    if (playerPiece == NO_PIECE) {
      return 0;
    }

    // Check right.
    if (COLS - col >= num) {
      boolean inarow = true;

      for (c = col + 1; c < col + num && inarow; ++c) {
        if (getPiece(row, c) != playerPiece) {
          inarow = false;
        }
      }

      if (inarow) {
        int capCount = 0;

        if (col == 0) {
          capCount++;
        } else {
          if (getPiece(row, col - 1) == opponentPiece) {
            capCount++;
          }
        }

        if (col + num == COLS) {
          capCount++;
        } else {
          if (getPiece(row, col + num) == opponentPiece) {
            capCount++;
          }
        }

        if (capCount <= maxcaps) {
          count++;
        }
      }
    }

    // Check down.
    if (ROWS - row >= num) {
      boolean inarow = true;

      for (r = row + 1; r < row + num && inarow; ++r) {
        if (getPiece(r, col) != playerPiece) {
          inarow = false;
        }
      }

      if (inarow) {
        int capCount = 0;
        if (row == 0) {
          capCount++;
        } else {
          if (getPiece(row - 1, col) == opponentPiece) {
            capCount++;
          }
        }

        if (row + num == ROWS) {
          capCount++;
        }

        if (getPiece(row + num, col) == opponentPiece) {
          capCount++;
        }

        if (capCount <= maxcaps) {
          count++;
        }
      }
    }

    // Check down-right
    if (ROWS - row >= num && COLS - col >= num) {
      boolean inarow = true;

      for (r = row + 1, c = (col + 1);
          r < row + num && c < col + num && inarow; r++, c++) {
        if (getPiece(r, c) != playerPiece) {
          inarow = false;
        }
      }

      if (inarow) {
        int capCount = 0;

        // Upper-left bounds.
        if (row == 0 || col == 0) {
          capCount++;
        } else {
          if (getPiece(row - 1, col - 1) == opponentPiece) {
            capCount++;
          }
        }

        // Lower-right bounds.
        if (row + num == ROWS || col + num == COLS) {
          capCount++;
        } else {
          if (getPiece(row + num, col + num) == opponentPiece) {
            capCount++;
          }
        }

        if (capCount <= maxcaps) {
          count++;
        }
      }
    }

    // Check down-left
    if (ROWS - row >= num && col >= num - 1) {
      boolean inarow = true;

      for (r = row + 1, c = col - 1;
          r < row + num && c >= 0 && inarow; r++, c--) {
        if (getPiece(r, c) != playerPiece) {
          inarow = false;
        }
      }

      if (inarow) {
        int capCount = 0;

        // Lower-left bounds.
        if (row == ROWS - 1 || col - num == 0) {
          capCount++;
        } else {
          if (getPiece(row + 1, col - 1) == opponentPiece) {
            capCount++;
          }
        }

        // Upper-right bounds.
        if (row == 0 || col == COLS - 1) {
          capCount++;
        } else {
          if (getPiece(row - 1, col + 1) == opponentPiece) {
            capCount++;
          }
        }

        if (capCount <= maxcaps) {
          count++;
        }

      }
    }

    return count;
  }

  int inARow(final int num, final int maxcaps, final int piece) {
    int count = 0;

    for (int row = 0; row < ROWS; ++row) {
      for (int col = 0; col < COLS; ++col) {
        if (getPiece(row, col) == piece && inARowAt(row, col, num, maxcaps) != 0) {
          count++;
        }
      }
    }

    return count;
  }

  boolean hasAdjacentPieces(final int row, final int col) {
    for (int r = row - 1; r <= row + 1; ++r) {
      for (int c = col - 1; c <= col + 1; ++c) {
        if (r == row && c == col) {
          continue;
        }

        if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
          if (getPiece(r, c) != NO_PIECE) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public static int toPosition(final int row, final int col) {
    return row * COLS + col;
  }
}
