/*
 * Gomoku.java
 * Copywrite 2004 Douglas Ryan Richardson
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

interface GomokuConstants
{
	// Board specification
	final static int ROWS = 19;
	final static int COLS = 19;
	final static int NUM_OF_SQUARES = ROWS * COLS;
	
	// Victory Conditions
	final static int COMPUTER_WIN = 0;
	final static int USER_WIN = 1;
	final static int NO_WIN = 2;
	final static int DRAW = 3;
	
	// Pieces
	final static int NO_PIECE = 0;
	final static int USER_PIECE = 1;
	final static int COMPUTER_PIECE= 2;
	
	// Turn
	final static int COMPUTER_TURN = 0;
	final static int USER_TURN = 1;
	
	// Player
	final static int COMPUTER_PLAYER = 0;
	final static int USER_PLAYER = 1;
}

class Square
{
	private int m_squareNumber;
	
	Square(int squareNumber)
	{
		setSquareNumber(squareNumber);
	}
	
	Square(int row, int col)
	{
		setRowCol(row, col);
	}
	
	int getRow()
	{
		return m_squareNumber / GomokuConstants.COLS;
	}
	
	int getCol()
	{
		return m_squareNumber % GomokuConstants.COLS;
	}
	
	int getSquareNumber()
	{
		return m_squareNumber;
	}
	
	void setRowCol(int row, int col)
	{
		setSquareNumber(row * GomokuConstants.COLS + col);
	}
		
	void setSquareNumber(int squareNumber)
	{
		m_squareNumber = squareNumber;
	}
}

class GomokuBoard implements GomokuConstants
{
	private int pieces[];
	
	GomokuBoard()
	{
		pieces = new int[NUM_OF_SQUARES];
	}
	
	GomokuBoard(final GomokuBoard b)
	{
		pieces = new int[b.pieces.length];
		for(int i = 0; i < b.pieces.length; ++i)
			pieces[i] = b.pieces[i];
	}
	
	int getPiece(Square square)
	{
		return pieces[square.getSquareNumber()];
	}
	
	void setPiece(Square square, int piece)
	{
		pieces[square.getSquareNumber()] = piece;
	}
	
	void clear()
	{
		for(int i = 0; i < NUM_OF_SQUARES; ++i)
			pieces[i] = NO_PIECE;
	}
}

class GomokuButton extends JButton
{
	Square m_square;
	int m_piece;

	GomokuButton(Square s)
	{
		setIcon(new ImageIcon(getClass().getResource("NoPiece.png")));
		m_square = s;
		setIconTextGap(0);
		m_piece = GomokuConstants.NO_PIECE;
	}
	
	void setPiece(int piece)
	{
		m_piece = piece;

		Class cl = getClass();
		
		if(piece == GomokuConstants.COMPUTER_PIECE)
			setIcon(new ImageIcon(cl.getResource("ComputerPiece.png")));
		else if(piece == GomokuConstants.USER_PIECE)
			setIcon(new ImageIcon(cl.getResource("PlayerPiece.png")));
		else
			setIcon(new ImageIcon(cl.getResource("NoPiece.png")));
	}
	
	protected void fireActionPerformed(ActionEvent e)
	{
		if(m_piece == GomokuConstants.NO_PIECE)
			super.fireActionPerformed(e);
	}

	Square getSquare() { return m_square; }
}

class ComputerPlayer implements GomokuConstants
{
	private GomokuBoard board;
	private boolean abEnabled; // is alpha/beta pruning enabled?
	private int depth; // Search depth.
	private int numPosEval; // Number of positions evaluated.
	
	private static final int MAXWIN = 10000;
	private static final int MINWIN = -10000;
	
	class Stats
	{
		int capped2;
		int uncapped2;
		
		int capped3;
		int uncapped3;
		
		int capped4;
		int uncapped4;
	}
	
	ComputerPlayer()
	{
		numPosEval = 0;
		depth = 2;
		abEnabled = true;
		board = new GomokuBoard();
	}
	
	private int move(int curdepth, int maxdepth, GomokuBoard board,
			Square squareOut, int turn, int alpha, int beta)
	{
		Square s = new Square(-1, -1);
		int max = MINWIN - 1;
		int min = MAXWIN + 1;
		
		Square potentialSquare = new Square(-1);
		
		Square curBestSquare = new Square(-1);
		
		if(curdepth == maxdepth)
		{
			squareOut.setSquareNumber(-1);
			++numPosEval;
			return eval(board);
		}
		
		int moveVal = 0;
		
		while(getNextPossibleMove(s, board))
		{
			GomokuBoard b = new GomokuBoard(board);
			b.setPiece(s, turn == COMPUTER_TURN ? COMPUTER_PIECE : USER_PIECE);
			
			if(turn == COMPUTER_TURN)
			{
				switch(terminalTest(b))
				{
				case USER_WIN:
					moveVal = MINWIN;
					break;
				case COMPUTER_WIN:
					moveVal = MAXWIN;
					break;
				default:
					moveVal = move(curdepth + 1, maxdepth, b, potentialSquare, USER_TURN, alpha, beta);
				}
				
				if(moveVal > max)
				{
					squareOut.setSquareNumber(s.getSquareNumber());
					max = moveVal;
				}
				
				if(abEnabled)
				{
					alpha = alpha > moveVal ? alpha : moveVal;
					if(alpha >= beta)
						return beta;
				}
			}
			else
			{
				switch(terminalTest(b))
				{
				case USER_WIN:
					moveVal = MINWIN;
					break;
				case COMPUTER_WIN:
					moveVal = MAXWIN;
					break;
				default:
					moveVal = move(curdepth + 1, maxdepth, b, potentialSquare, COMPUTER_TURN, alpha, beta);
				}
				
				if(moveVal < min)
				{
					squareOut.setSquareNumber(s.getSquareNumber());
					min = moveVal;
				}
				
				if(abEnabled)
				{
					beta = beta < moveVal ? beta : moveVal;
					if(beta <= alpha)
						return alpha;
				}
			}
		}
		
		if(abEnabled)
			return turn == COMPUTER_TURN ? alpha : beta;
		
		return turn == COMPUTER_TURN ? max : min;
	}
	
	// Moves the user piece and then responds with a move.
	int move(Square userMove, Square computerMove)
	{
		computerMove.setSquareNumber(-1);
		
		board.setPiece(userMove, USER_PIECE);
		
		// Check to see if the user won or if they took the last piece.
		int vc = terminalTest(board);
		if(vc == USER_WIN ||vc == DRAW)
			return vc;
		
		// Do a computer move.
		// alpha = MINWIN - 1
		// beta = MAXWIN + 1
		numPosEval = 0;
		move(0, depth, board, computerMove, COMPUTER_TURN, MINWIN - 1, MAXWIN + 1);
		board.setPiece(computerMove, COMPUTER_PIECE);
		
		return terminalTest(board);
	}
	
	private int terminalTest(GomokuBoard b)
	{
		int numOfEmptySquares = NUM_OF_SQUARES;
		
		for(int row = 0; row < ROWS; ++row)
		{
			for(int col = 0; col < COLS; ++col)
			{
				Square s = new Square(row, col);
				if(b.getPiece(s) != NO_PIECE)
					--numOfEmptySquares;

				switch(victory(s, b))
				{
				case USER_WIN:
					return USER_WIN;
				case COMPUTER_WIN:
					return COMPUTER_WIN;
				}
			}
		}

		return numOfEmptySquares == 0 ? DRAW : NO_WIN;

	}
	
	private int victory(Square square, GomokuBoard b)
	{
		boolean victory = false;
		
		int playerPiece = b.getPiece(square);
		
		if(playerPiece == NO_PIECE)
			return NO_WIN;
		
		if(inARowAt(square, 5, 2, b) > 0)
			return playerPiece == USER_PIECE ? USER_WIN : COMPUTER_WIN;
		
		return NO_WIN;
	}
	
	private boolean getNextPossibleMove(Square squareOut, GomokuBoard parentBoard)
	{
		int row = squareOut.getRow();
		int col = squareOut.getCol();
		
		if(row == -1 || col == -1)
		{
			row = 0;
			col = 0;
		}
		else
		{
			++col;
			if(col == ROWS)
			{
				++row;
				col = 0;
			}
		}
		
		while(row < ROWS)
		{
			while(col < COLS)
			{
				Square s = new Square(row, col);
				if(parentBoard.getPiece(s) == NO_PIECE && adjacentPieces(s, parentBoard))
				{
					squareOut.setRowCol(row, col);
					return true;
				}
				
				++col;
			}
			
			col = 0;
			++row;
		}
		
		return false;
	}
	
	private boolean adjacentPieces(Square square, GomokuBoard b)
	{
		int row = square.getRow();
		int col = square.getCol();
		
		for(int r = row - 1; r <= row + 1; ++r)
		{
			for(int c = col - 1; c <= col + 1; ++c)
			{
				if(r == row && c == col)
					continue;
				
				if(r >= 0 && r < ROWS && c >= 0 && c < COLS)
				{
					if(b.getPiece(new Square(r, c)) != NO_PIECE)
						return true;
				}
			}
		}
		
		return false;
	}
	
	private int max(int a, int b)
	{
		return a > b ? a : b;
	}
	
	private int min(int a, int b)
	{
		return a < b ? a : b;
	}
	
	private int eval(GomokuBoard b)
	{
		Stats c = new Stats(); // Computer
		Stats u = new Stats(); // User
		
		fillStatStruct(c, COMPUTER_PLAYER, b);
		fillStatStruct(u, USER_PLAYER, b);
		
		int retVal = 0;
		
		if(u.uncapped4 > 0)
			return MINWIN;
		
		if(c.uncapped4 > 0)
			return MAXWIN;
		
		retVal += c.capped2 * 5;
		retVal -= u.capped2 * 5;

		retVal += c.uncapped2 * 10;
		retVal -= u.uncapped2 * 10;

		retVal += c.capped3 * 20;
		retVal -= u.capped3 * 30;

		retVal += c.uncapped3 * 100;
		retVal -= u.uncapped3 * 120;

		retVal += c.capped4 * 500;
		retVal -= u.capped4 * 500;
		
		return max(MINWIN, min(MAXWIN, retVal));
	}
	
	private int inARowAt(Square square, int num, int maxcaps, GomokuBoard b)
	{
		int count = 0;
		int r, c;
		final int col = square.getCol();
		final int row = square.getRow();
		
		int playerPiece = b.getPiece(square);
		int opponentPiece = playerPiece == USER_PIECE ? COMPUTER_PIECE : USER_PIECE;
		
		if(playerPiece == NO_PIECE)
			return 0;
		
		// Check right.
		if(COLS - col >= num)
		{
			boolean inarow = true;
			
			for(c = col + 1; c < col + num && inarow; ++c)
			{
				if(b.getPiece(new Square(row, c)) != playerPiece)
					inarow = false;
			}
			
			if(inarow)
			{
				int capCount = 0;
				if(col == 0)
					capCount++;
				else
				{
					if(b.getPiece(new Square(row, col - 1)) == opponentPiece)
						capCount++;
				}
				
				if(col + num == COLS)
					capCount++;
				else
				{
					if(b.getPiece(new Square(row, col + num)) == opponentPiece)
							capCount++;
				}
				
				if(capCount <= maxcaps)
					count++;
			}
		}
		
		// Check down.
		if(ROWS - row >= num)
		{
			boolean inarow = true;
			
			for(r = row + 1; r < row + num && inarow; ++r)
			{
				if(b.getPiece(new Square(r, col)) != playerPiece)
					inarow = false;
			}
			
			if(inarow)
			{
				int capCount = 0;
				if(row == 0)
					capCount++;
				else
				{
					if(b.getPiece(new Square(row - 1, col)) == opponentPiece)
						capCount++;
				}
				
				if(row + num == ROWS)
					capCount++;
				{
					if(b.getPiece(new Square(row + num, col)) == opponentPiece)
						capCount++;
				}
				
				if(capCount <= maxcaps)
					count++;
			}
		}
		
		// Check down-right
		if(ROWS - row >= num && COLS - col >= num)
		{
			boolean inarow = true;

			for(r = row + 1, c = col + 1; r < row + num && c < col + num && inarow; r++, c++)
			{
				if(b.getPiece(new Square(r, c)) != playerPiece)
					inarow = false;
			}

			if(inarow)
			{
				int capCount = 0;

				// Upper-left bounds.
				if(row == 0 || col == 0)
					capCount++;
				else
				{
					if(b.getPiece(new Square(row - 1, col - 1)) == opponentPiece)
						capCount++;
				}

				// Lower-right bounds.
				if(row + num == ROWS || col + num == COLS)
					capCount++;
				else
				{
					if(b.getPiece(new Square(row + num, col + num)) == opponentPiece)
						capCount++;
				}

				if(capCount <= maxcaps)
					count++;
			}
		}
		
		// Check down-left
		if( ROWS - row >= num && col >= num - 1 )
		{
			boolean inarow = true;

			for(r = row + 1, c = col - 1; r < row + num && c >= 0  && inarow; r++, c--)
			{
				if(b.getPiece(new Square(r, c)) != playerPiece)
					inarow = false;
			}

			if(inarow)
			{
				int capCount = 0;

				// Lower-left bounds.
				if(row == ROWS - 1 || col - num == 0)
					capCount++;
				else
				{
					if(b.getPiece(new Square(row + 1, col - 1)) == opponentPiece)
						capCount++;
				}

				// Upper-right bounds.
				if( row == 0 || col == COLS - 1 )
					capCount++;
				else
				{
					if(b.getPiece(new Square(row - 1, col + 1)) == opponentPiece)
						capCount++;
				}

				if( capCount <= maxcaps )
					count++;
			}
		}
		
		return count;
	}
	
	private int inARow(int num, int maxcaps, int piece, GomokuBoard b)
	{
		int count = 0;
		
		for(int row = 0; row < ROWS; ++row)
		{
			for(int col = 0; col < COLS; ++col)
			{
				Square s = new Square(row, col);
				if(board.getPiece(s) == piece && inARowAt(s, num, maxcaps, b) != 0)
					count++;
			}
		}
		
		return count;
	}
	
	private void fillStatStruct(Stats stats, int player, GomokuBoard b)
	{
		int piece = player == COMPUTER_PLAYER ? COMPUTER_PIECE : USER_PIECE;
		
		stats.uncapped2 = inARow(2, 0, piece, b);
		stats.capped2 = inARow(2, 1, piece, b);
		
		stats.uncapped3 = inARow(3, 0, piece, b);
		stats.capped3 = inARow(3, 1, piece, b);
		
		stats.uncapped4 = inARow(4, 0, piece, b);
		stats.capped4 = inARow(4, 1, piece, b);
	}
	
	void startNewGame()
	{
		numPosEval = 0;
		board.clear();
	}
	
	void setSearchDepth(int newDepth)
	{
		depth = newDepth;
	}
	
	int getSearchDepth()
	{
		return depth;
	}
	
	void setABPruningEnabled(boolean abOn)
	{
		abEnabled = abOn;
	}
	
	int getNumPositionsEvaluated()
	{
		return numPosEval;
	}
}

public class Gomoku implements GomokuConstants, ActionListener
{
	JFrame frame;
	GomokuButton buttons[];
	boolean userMove;
	ComputerPlayer compPlayer;
	
	class CloseDialog implements ActionListener
	{
		JDialog d;
		
		CloseDialog(JDialog dialog)
		{
			d = dialog;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			d.setVisible(false);
		}
	}
	
	Gomoku()
	{
		compPlayer = new ComputerPlayer();
		userMove = true;
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("Gomoku");
		frame.setIconImage(new ImageIcon(getClass().getResource("Gomoku.png")).getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(createBoard(), BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	void newGame()
	{
		compPlayer.startNewGame();
		userMove = true;
		for(int i = 0; i < buttons.length; ++i)
			buttons[i].setPiece(NO_PIECE);
	}
	
	JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JMenuItem menuItem;
		
		// Game menu
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		menuBar.add(menu);
		
		menuItem = new JMenuItem("New Game");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				newGame();
			}
		});
		
		menuItem = new JMenuItem("Search Depth...");
		menuItem.setMnemonic(KeyEvent.VK_S);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String[] vals = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
				String sd = (String)JOptionPane.showInputDialog(frame,
						"Enter a search depth.", "Search Depth", JOptionPane.PLAIN_MESSAGE,
						null, vals, new Integer(compPlayer.getSearchDepth()).toString());
				
				if(sd != null)
				{
					compPlayer.setSearchDepth(Integer.parseInt(sd));
				}
			}
		});
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		menu.add(menuItem);
		
		
		// Help menu
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menu);
		
		menuItem = new JMenuItem("About Gomoku...");
		menuItem.setMnemonic(KeyEvent.VK_A);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				JDialog d = new JDialog(frame, "About Gomoku", true);
				Container p = d.getContentPane();
				JPanel panel = new JPanel(new FlowLayout());
				p.setLayout(new GridLayout(2, 1));
				panel.add(new JLabel(new ImageIcon("Gomoku.png")));
				panel.add(new JLabel(
						"<html>" +
						"<p>Gomoku" +
						"<p>Copywrite (C) 2004 Douglas Ryan Richardson" +
						"<p>Licensed under the terms of the General Public License" +
						"</html>"));
				p.add(panel);
				
				panel = new JPanel(new FlowLayout());
				JButton ok = new JButton("OK");
				ok.addActionListener(new CloseDialog(d));
				panel.add(ok);
				p.add(panel);
						
				d.pack();
				d.setLocationRelativeTo(frame);
				d.setVisible(true);
			}
		});
		
		return menuBar;
	}
	
	Component createBoard()
	{
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(GomokuBoard.ROWS, GomokuBoard.COLS));
		
		buttons = new GomokuButton[NUM_OF_SQUARES];
		for(int i = 0; i < NUM_OF_SQUARES; ++i)
		{	
			buttons[i] = new GomokuButton(new Square(i));
			buttons[i].addActionListener(this);
			pane.add(buttons[i]);
		}
		
		return pane;		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(userMove)
		{
			userMove = false;
			GomokuButton b = (GomokuButton) e.getSource();
			b.setPiece(USER_PIECE);
			Square compMove = new Square(-1);
			int test = compPlayer.move(b.getSquare(), compMove);
			if(test == COMPUTER_WIN || test == NO_WIN)
				buttons[compMove.getSquareNumber()].setPiece(COMPUTER_PIECE);
			
			if(test == COMPUTER_WIN)
				JOptionPane.showMessageDialog(frame, "I win, silly human!");
			else if(test == USER_PIECE)
				JOptionPane.showMessageDialog(frame, "Arg! I am vanquished.");
			else if(test == DRAW)
				JOptionPane.showMessageDialog(frame, "A draw? How unsatisfying.");
			else
				userMove = true;
		}
	}
	
	public static void main(String args[])
	{
		Gomoku gomoku = new Gomoku();
	}
}
