package net.sourceforge.gomoku;


/**
 * @author <a href="mailto:anton.safonov@gmail.com">Anton Safonov</a>
 */
public interface Player {

  void startNewGame();

  Square makeMove(final Board board, final Square opponentMove);

  byte getPiece();
}
