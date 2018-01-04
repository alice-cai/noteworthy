/**
 * Event handler for all back buttons. Displays the panel that was last accessed by the user
 * and deletes it from the user's history.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayDeque;

public class BackButtonHandler implements ActionListener {
	JPanel cards;
	CardLayout cardLayout;
	ArrayDeque<String> history;


	public BackButtonHandler(JPanel cards, CardLayout cardLayout, ArrayDeque<String> history) {
		this.cards = cards;
		this.cardLayout = cardLayout;
		this.history = history;
	}

	public void actionPerformed(ActionEvent event) {
		cardLayout.show(cards, history.getLast());
		history.removeLast();
	}
}