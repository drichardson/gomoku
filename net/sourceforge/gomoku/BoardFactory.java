package net.sourceforge.gomoku;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public class BoardFactory {

  public static final Board create(int rows, int cols) {
    SquareFactory.init(rows, cols);

    if (rows < 16 && cols < 16) {
      return new SmallBoard(rows, cols);
    } else {
      return new NormalBoard(rows, cols);
    }
  }
}
