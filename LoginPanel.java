/**
 * GUI for logging users in.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {
	private static final String BANNER_IMAGE_FILE = "images/banner.png";

	private SpringLayout layout;

	private JTextField userField;
	private JLabel userFieldLabel;

	private JPasswordField passField;
	private JLabel passFieldLabel;

	private JButton loginButton;
	private JButton backToMainMenu;

	private JLabel headerImageLabel;
	private ImageIcon headerImage;

	private Font buttonFont;
	private Font labelFont;
	private Font textFieldFont;

	private UserManager userManager;

	public LoginPanel (UserManager userManager, BackButtonHandler backButtonHandler, Consumer<User> onUserLogin /* call this method when user logs in */) {
		this.userManager = userManager;

		layout = new SpringLayout();
		setLayout(layout);

		buttonFont = new Font("Arial", Font.BOLD, 35);
		labelFont = new Font("Arial", Font.BOLD, 35);
		textFieldFont = new Font("Arial", Font.PLAIN, 35);

		createHeaderImageLabel();
		createUsernameInput();
		createPasswordInput();
		createButtons(backButtonHandler, onUserLogin);
	}

	private void createHeaderImageLabel () {
		headerImageLabel = new JLabel();
		headerImage = new ImageIcon(BANNER_IMAGE_FILE);

		headerImageLabel.setSize(664, 183); // image dimensions
		headerImageLabel.setIcon(headerImage);
		add(headerImageLabel);

		layout.putConstraint(SpringLayout.NORTH, headerImageLabel, 64, SpringLayout.NORTH, this);
	}

	private void createUsernameInput () {
		userFieldLabel = new JLabel("Username: ");
		userFieldLabel.setFont(labelFont);
		userFieldLabel.setForeground(Color.GRAY);
		add(userFieldLabel);
		layout.putConstraint(SpringLayout.NORTH, userFieldLabel, 70, SpringLayout.SOUTH, headerImageLabel);
		layout.putConstraint(SpringLayout.WEST, userFieldLabel, 100, SpringLayout.WEST, this);

		userField = new JTextField ("", 10);
		userField.setFont(textFieldFont);
		userField.setForeground(Color.GRAY);
		add(userField);
		layout.putConstraint(SpringLayout.NORTH, userField, 66, SpringLayout.SOUTH, headerImageLabel);
		layout.putConstraint(SpringLayout.WEST, userField, 310, SpringLayout.WEST, this);
	}

	private void createPasswordInput () {
		passFieldLabel = new JLabel("Password: ");
		passFieldLabel.setFont(labelFont);
		passFieldLabel.setForeground(Color.GRAY);
		add(passFieldLabel);
		layout.putConstraint(SpringLayout.NORTH, passFieldLabel, 65, SpringLayout.SOUTH, userFieldLabel);
		layout.putConstraint(SpringLayout.WEST, passFieldLabel, 100, SpringLayout.WEST, this);


		passField = new JPasswordField ("", 10);
		passField.setFont(textFieldFont);
		passField.setForeground(Color.GRAY);
		add(passField);
		layout.putConstraint(SpringLayout.NORTH, passField, 61, SpringLayout.SOUTH, userFieldLabel);
		layout.putConstraint(SpringLayout.WEST, passField, 310, SpringLayout.WEST, this);
	}

	private void createButtons (BackButtonHandler backButtonHandler, Consumer<User> onUserLogin) {
		backToMainMenu = new JButton("<");
		backToMainMenu.addActionListener(backButtonHandler);
		backToMainMenu.setFont(buttonFont);
		backToMainMenu.setForeground(Color.GRAY);
		add(backToMainMenu);
		layout.putConstraint(SpringLayout.NORTH, backToMainMenu, 8, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, backToMainMenu, 8, SpringLayout.WEST, this);

		loginButton = new JButton("Log In");
		LoginHandler loginHandler = new LoginHandler(onUserLogin);
		loginButton.addActionListener(loginHandler);
		loginButton.setFont(buttonFont);
		loginButton.setForeground(Color.GRAY);
		add(loginButton);
		layout.putConstraint(SpringLayout.NORTH, loginButton, 275, SpringLayout.SOUTH, headerImageLabel);
		layout.putConstraint(SpringLayout.WEST, loginButton, 275, SpringLayout.WEST, this);
	}

	private class LoginHandler implements ActionListener {
		private Consumer<User> onUserLogin;

		public LoginHandler(Consumer<User> onUserLogin) {
			this.onUserLogin = onUserLogin;
		}

		public void actionPerformed(ActionEvent event) {
			String username = userField.getText();
			String password = new String(passField.getPassword());

			if (!username.equals("") && !password.equals("")) {
				if(userManager.checkUserAvailability(username)) {
					JOptionPane.showMessageDialog(null, "Username doesn't exist.", "Error Logging In", JOptionPane.ERROR_MESSAGE);
				} else if(userManager.checkLogin(username, password) != null) {
					User currentUser = userManager.checkLogin(username, password);
					onUserLogin.accept(currentUser);
				} else {
					JOptionPane.showMessageDialog(null, "Invalid credentials.", "Error Logging In", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please fill out all required fields.", "Error Logging In", JOptionPane.ERROR_MESSAGE);
			}
			userField.setText("");
			passField.setText("");
		}
	}
}