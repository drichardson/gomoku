package net.sourceforge.gomoku;

import java.util.ArrayList;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public class Game implements GomokuConstants {

  private final Player firstPlayer;
  private final Player secondPlayer;

  private final Board board;

  private boolean cancelled = false;

  private ArrayList moveListeners = null;

  public Game(Player firstPlayer, Player secondPlayer, byte rows, byte cols) {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
    this.board = BoardFactory.create(rows, cols);
  }

  public Board getBoard() {
    return this.board;
  }

  public void addMoveListener(MoveListener listener) {
    if (this.moveListeners == null) {
      this.moveListeners = new ArrayList(1);
    }
    this.moveListeners.add(listener);
  }

  public byte play() {
    Square lastMove = SquareFactory.create();

    this.cancelled = false;

    byte test;

    do {
      lastMove = this.firstPlayer.makeMove(this.board, lastMove);
      if (this.cancelled) {
        test = CANCELLED;
        break;
      }
      markMove(lastMove, this.firstPlayer.getPiece());

      test = (byte) board.terminalTest();
      if (test != NO_WIN) {
        break;
      }

      lastMove = this.secondPlayer.makeMove(this.board, lastMove);
      if (this.cancelled) {
        test = CANCELLED;
        break;
      }
      markMove(lastMove, this.secondPlayer.getPiece());

      test = (byte) board.terminalTest();
      if (test != NO_WIN) {
        break;
      }
    } while (true);

    return test;
  }

  public void cancel() {
    this.cancelled = true;
  }

  public void markMove(final Square move, byte piece) {
    board.setPiece(move, piece);

    if (this.moveListeners != null) {
      for (int i = 0, max = this.moveListeners.size(); i < max; i++) {
        ((MoveListener) this.moveListeners.get(i)).moveMade(move, piece);
      }
    }
  }
}
