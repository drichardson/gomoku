gomoku.jar: Gomoku.class
	jar cfm gomoku.jar mainClass *.class *.png COPYING

Gomoku.class: Gomoku.java
	javac Gomoku.java

clean:
	$(RM) *.class
