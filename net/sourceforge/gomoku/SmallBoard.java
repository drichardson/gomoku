package net.sourceforge.gomoku;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public final class SmallBoard extends Board {
  private final byte[] pieces;

  private final byte rows;
  private final byte cols;

  SmallBoard(final int rows, final int cols) {
    this.rows = (byte) rows;
    this.cols = (byte) cols;
    this.pieces = new byte[rows * cols];
  }

  int getRows() {
    return this.rows;
  }

  int getCols() {
    return this.cols;
  }

  Board copy() {
    SmallBoard newBoard = new SmallBoard(getRows(), getCols());
    System.arraycopy(this.pieces, 0, newBoard.pieces, 0, pieces.length);
    return newBoard;
  }

  byte getPiece(final int row, final int col) {
    try {
      return pieces[toPosition(row, col)];
    } catch (ArrayIndexOutOfBoundsException e) {
      return NO_PIECE;
    }
  }

  void setPiece(final int row, final int col, final byte piece) {
    pieces[toPosition(row, col)] = piece;
  }

  void clear() {
    for (int i = 0, max = pieces.length; i < max; ++i) {
      pieces[i] = NO_PIECE;
    }
  }
}
