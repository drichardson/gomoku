package net.sourceforge.gomoku;


/**
 * TODO: reimplement using BitSet
 *
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public final class NormalBoard extends Board {
  private final byte[] pieces;

  private final short rows;
  private final short cols;

  NormalBoard(final int rows, final int cols) {
    this.rows = (short) rows;
    this.cols = (short) cols;
    this.pieces = new byte[rows * cols];
  }

  int getRows() {
    return this.rows;
  }

  int getCols() {
    return this.cols;
  }

  Board copy() {
    NormalBoard newBoard = new NormalBoard(getRows(), getCols());
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
