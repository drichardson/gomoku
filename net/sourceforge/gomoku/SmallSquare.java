package net.sourceforge.gomoku;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public final class SmallSquare extends Square {
  /**
   * high nibble - row, low - column
   */
  private byte pos;

  SmallSquare(final int row, final int col) {
    setRowCol(row, col);
  }

  public int getRow() {
    int b = (this.pos >> 4) & 0x0F;
    if (b < 15) {
      return b;
    } else {
      return -1;
    }
  }

  public int getCol() {
    int b = (this.pos) & 0x0F;
    if (b < 15) {
      return b;
    } else {
      return -1;
    }
  }

  public void setRowCol(final int row, final int col) {
    this.pos = (byte) ((row << 4) | col);
  }
}
