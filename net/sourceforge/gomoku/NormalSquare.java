package net.sourceforge.gomoku;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public final class NormalSquare extends Square {
  private byte row;
  private byte col;

  NormalSquare(final int row, final int col) {
    setRowCol(row, col);
  }

  public int getRow() {
    return this.row;
  }

  public int getCol() {
    return this.col;
  }

  public void setRowCol(final int row, final int col) {
    this.row = (byte) row;
    this.col = (byte) col;
  }
}
