/**
 * GUI for main menu screen. Allows user to choose between logging in and registering an account.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayDeque;

public class MainMenuPanel extends JPanel{
	private static final String LOGIN_PANEL = "card with login screen";
	private static final String REGIS_PANEL = "card with registration screen";
	private static final String MAIN_MENU_PANEL = "card with main menu";
	private static final String MAIN_IMAGE_FILE = "images/main.png";

	private SpringLayout layout;

	private JLabel headerImageLabel;
	private ImageIcon headerImage;

	private JButton loginButton;
	private JButton registrationButton;
	private Font buttonFont;

	private JPanel cards;
	private CardLayout cardLayout;
	private ArrayDeque<String> history;

	public MainMenuPanel (JPanel cards, CardLayout cardLayout, ArrayDeque<String> history) {
		this.cards = cards;
		this.cardLayout = cardLayout;
		this.history = history;

		buttonFont = new Font("Arial", Font.BOLD, 50);

		layout = new SpringLayout();
		setLayout(layout);

		createHeaderImageLabel();
		createLoginButton();
		createRegistrationButton();
	}

	private void createHeaderImageLabel () {
		headerImageLabel = new JLabel();
		headerImage = new ImageIcon(MAIN_IMAGE_FILE);
		headerImageLabel.setSize(669, 448); // image dimensions
		headerImageLabel.setIcon(headerImage);
		add(headerImageLabel);
	}

	private void createLoginButton () {
		loginButton = new JButton("Log In");
		loginButton.setFont(buttonFont);
		loginButton.setForeground(Color.GRAY);
		add(loginButton);

		ActionListener loginButtonHandler = __ -> {
			cardLayout.show(cards, LOGIN_PANEL);
			history.addLast(MAIN_MENU_PANEL);
		};
		loginButton.addActionListener(loginButtonHandler);

		layout.putConstraint(SpringLayout.NORTH, loginButton, 50, SpringLayout.SOUTH, headerImageLabel);
		layout.putConstraint(SpringLayout.WEST, loginButton, 75, SpringLayout.WEST, this);
	}

	private void createRegistrationButton () {
		registrationButton = new JButton("Register");
		registrationButton.setFont(buttonFont);
		registrationButton.setForeground(Color.GRAY);
		add(registrationButton);

		ActionListener registerButtonHandler = __ -> {
			cardLayout.show(cards, REGIS_PANEL);
			history.addLast(MAIN_MENU_PANEL);
		};
		registrationButton.addActionListener(registerButtonHandler);

		layout.putConstraint(SpringLayout.NORTH, registrationButton, 50, SpringLayout.SOUTH, headerImageLabel);
		layout.putConstraint(SpringLayout.WEST, registrationButton, 75, SpringLayout.EAST, loginButton);		
	}
}