package net.sourceforge.gomoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public class HumanPlayer implements Player, ActionListener {
  private final byte piece;

  private Square move = null;

  public HumanPlayer(final byte piece) {
    this.piece = piece;
  }

  public void startNewGame() {
    this.move = null;
  }

  public Square makeMove(Board board, Square opponentMove) {
    this.move = null;

    while (true) {
      synchronized (this) {
        if (this.move != null) {
          break;
        }
      }

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }

    return this.move;
  }

  public byte getPiece() {
    return this.piece;
  }

  public void actionPerformed(ActionEvent e) {
    synchronized (this) {
      this.move = ((GomokuButton) e.getSource()).getSquare();
    }
  }
}
