package net.sourceforge.gomoku;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public class SquareFactory {
  private static final byte SMALL = 0;
  private static final byte NORMAL = 1;

  private static byte mode = SMALL;

  public static final void init(int rows, int cols) {
    if (rows < 16 && cols < 16) {
      mode = SMALL;
    } else {
      mode = NORMAL;
    }
  }

  public static final Square create() {
    return create(-1, -1);
  }

  public static final Square create(int row, int col) {
    switch (mode) {
      case SMALL:
        return new SmallSquare(row, col);

      case NORMAL:
      default:
        return new NormalSquare(row, col);
    }
  }

  public static void test() {
    System.err.println("1: " + SquareFactory.create()); // default with -1, -1
    System.err.println("2: " + SquareFactory.create(0, 0));
    System.err.println("3: " + SquareFactory.create(5, 7));
    System.err.println("4: " + SquareFactory.create(7, 5));
    System.err.println("5: " + SquareFactory.create(14, 14));
  }
}
