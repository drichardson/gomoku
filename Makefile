PACKAGEDIR=net/sourceforge/gomoku
IMAGEDIR=$(PACKAGEDIR)/images
CLASSES=$(PACKAGEDIR)/Board.class \
	$(PACKAGEDIR)/BoardFactory.class \
	$(PACKAGEDIR)/ComputerPlayer.class \
	$(PACKAGEDIR)/Game.class \
	$(PACKAGEDIR)/Gomoku.class \
	$(PACKAGEDIR)/GomokuButton.class \
	$(PACKAGEDIR)/GomokuConstants.class \
	$(PACKAGEDIR)/HumanPlayer.class \
	$(PACKAGEDIR)/MoveListener.class \
	$(PACKAGEDIR)/NormalBoard.class \
	$(PACKAGEDIR)/NormalSquare.class \
	$(PACKAGEDIR)/Player.class \
	$(PACKAGEDIR)/SmallBoard.class \
	$(PACKAGEDIR)/SmallSquare.class \
	$(PACKAGEDIR)/Square.class \
	$(PACKAGEDIR)/SquareFactory.class

IMAGES=$(IMAGEDIR)/ComputerPiece.png \
	$(IMAGEDIR)/Gomoku.png \
	$(IMAGEDIR)/NoPiece.png \
	$(IMAGEDIR)/PlayerPiece.png

gomoku.jar: $(CLASSES) $(IMAGES) mainClass
	jar cfm gomoku.jar mainClass $(PACKAGEDIR)/*.class $(IMAGES) COPYING CREDITS

%.class: %.java
	javac $<

#$(PACKAGEDIR)/Gomoku.class: $(PACKAGEDIR)/Gomoku.java
#	javac $(PACKAGEDIR)/Gomoku.java

clean:
	$(RM) $(PACKAGEDIR)/*.class gomoku.jar
