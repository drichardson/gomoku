package net.sourceforge.gomoku;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public abstract class Square {

  public abstract int getRow();

  public abstract int getCol();

  public abstract void setRowCol(int row, int col);

  public final void copyPosition(final Square from) {
    setRowCol(from.getRow(), from.getCol());
  }

  public final void init() {
    setRowCol(-1, -1);
  }

  public String toString() {
    return "[" + getRow() + ":" + getCol() + "]";
  }
}
