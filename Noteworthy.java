/**
 * Noteworthy is a note taking application that allows you to create, edit, and store
 * your notes.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayDeque;
import java.util.function.Consumer;

public class Noteworthy {
	// Identfiers used for switching between JPanels (or "cards") with CardLayout.
	private static final String LOGIN_PANEL = "card with login screen";
	private static final String REGIS_PANEL = "card with registration screen";
	private static final String MAIN_MENU_PANEL = "card with main menu";
	private static final String NOTE_MENU_PANEL = "card with list of notes";
	private static final String CREATE_NOTE_PANEL = "card that allows user to create new note";
	private static final String NOTE_PANEL = "card that allows user to view or edit existing note";
	
	private JFrame mainFrame;
	private JPanel cards;
	private CardLayout cardLayout;
	private LoginPanel loginPanel;
	private RegistrationPanel registrationPanel;
	private NoteMenuPanel noteMenuPanel;

	// ArrayDeque that keeps track of the panels visited by the user.
	private ArrayDeque<String> history;

	// Event handler for all back buttons.
	private BackButtonHandler backButtonHandler;

	// A Consumer callback that fires when a user logs in.
	Consumer<User> onUserLogin;

	// All fields associated with user management.
	private UserManager userManager = new UserManager();
	private User currentUser;

	public Noteworthy () {
		history = new ArrayDeque<String>();

		mainFrame = new JFrame("Noteworthy");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(669, 640);

		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);

		MainMenuPanel mainMenuPanel = new MainMenuPanel(cards, cardLayout, history);
		cards.add(mainMenuPanel, MAIN_MENU_PANEL);

		// Add the cards JPanel to mainFrame and show mainMenuPanel. Set mainFrame
		// to be visible. (This is done before creation of other panels to minimize
		// delay on program startup.)
		mainFrame.add(cards);
		cardLayout.show(cards, MAIN_MENU_PANEL);
		mainFrame.setVisible(true);

		backButtonHandler = new BackButtonHandler(cards, cardLayout, history);

		onUserLogin = user -> {
			currentUser = user;
			noteMenuPanel = new NoteMenuPanel(user, cards, cardLayout, history, backButtonHandler);
			history.addLast(LOGIN_PANEL);
			cards.add(noteMenuPanel, NOTE_MENU_PANEL);
			cardLayout.show(cards, NOTE_MENU_PANEL);
		};

		loginPanel = new LoginPanel(userManager, backButtonHandler, onUserLogin);
		cards.add(loginPanel, LOGIN_PANEL);

		registrationPanel = new RegistrationPanel(userManager, backButtonHandler);
		cards.add(registrationPanel, REGIS_PANEL);
	}
}