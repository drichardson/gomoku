package net.sourceforge.gomoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public final class GomokuButton extends JButton {
  private final Square square;

  private static final ImageIcon computerIcon
      = new ImageIcon(GomokuButton.class.getResource("images/ComputerPiece.png"));
  private static final ImageIcon playerIcon
      = new ImageIcon(GomokuButton.class.getResource("images/PlayerPiece.png"));
  private static final ImageIcon noIcon
      = new ImageIcon(GomokuButton.class.getResource("images/NoPiece.png"));

  private static final Insets noMargin = new Insets(0, 0, 0, 0);

  public GomokuButton(final Square square) {
    this.square = square;
    setIcon(noIcon);

    setIconTextGap(0);
    setMargin(noMargin);
    setAlignmentX(Component.CENTER_ALIGNMENT);
    setAlignmentY(Component.CENTER_ALIGNMENT);
  }

  public void setPiece(final byte piece) {
    switch (piece) {
      case GomokuConstants.COMPUTER_PIECE:
        setIcon(computerIcon);
        break;
      case GomokuConstants.USER_PIECE:
        setIcon(playerIcon);
        break;
      default:
        setIcon(noIcon);
        break;
    }
  }

  protected void fireActionPerformed(final ActionEvent e) {
    if (getIcon().equals(noIcon)) {
      super.fireActionPerformed(e);
    }
  }

  public Square getSquare() {
    return square;
  }

  public Dimension getPreferredSize() {
    return new Dimension(getIcon().getIconWidth() + 8, getIcon().getIconHeight() + 8);
  }
}
