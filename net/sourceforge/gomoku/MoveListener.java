package net.sourceforge.gomoku;

/**
 * Created by IntelliJ IDEA.
 * User: Master
 * Date: 26.12.2004
 * Time: 21:50:37
 * To change this template use File | Settings | File Templates.
 */
public interface MoveListener {
  void moveMade(Square move, byte piece);
}
