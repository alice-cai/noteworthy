/**
 * GUI for registering users.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RegistrationPanel extends JPanel {
	private static final String BANNER_IMAGE_FILE = "images/banner.png";

	private SpringLayout layout;

	private JLabel userFieldLabel;
	private JTextField userField;

	private JLabel passFieldLabel;
	private JTextField passField;

	private JLabel confirmPassFieldLabel;
	private JTextField confirmPassField;

	private JButton registerButton;
	private JButton backToMainMenu;
	private UserManager userManager;

	private JLabel headerImageLabel;
	private ImageIcon headerImage;

	private Font buttonFont;
	private Font labelFont;
	private Font textFieldFont;

	public RegistrationPanel (UserManager userManager, BackButtonHandler backButtonHandler) {
		this.userManager = userManager;

		layout = new SpringLayout();
		setLayout(layout);

		buttonFont = new Font("Arial", Font.BOLD, 35);
		labelFont = new Font("Arial", Font.BOLD, 30);
		textFieldFont = new Font("Arial", Font.PLAIN, 30);

		createHeaderImageLabel();
		createUsernameInput();
		createPasswordInput();
		createButtons(backButtonHandler);
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
		layout.putConstraint(SpringLayout.NORTH, userFieldLabel, 40, SpringLayout.SOUTH, headerImageLabel);
		layout.putConstraint(SpringLayout.WEST, userFieldLabel, 174, SpringLayout.WEST, this);

		userField = new JTextField ("", 10);
		userField.setFont(textFieldFont);
		userField.setForeground(Color.GRAY);
		add(userField);
		layout.putConstraint(SpringLayout.NORTH, userField, 36, SpringLayout.SOUTH, headerImageLabel);
		layout.putConstraint(SpringLayout.WEST, userField, 340, SpringLayout.WEST, this);
	}

	private void createPasswordInput () {
		passFieldLabel = new JLabel("Password: ");
		passFieldLabel.setFont(labelFont);
		passFieldLabel.setForeground(Color.GRAY);
		add(passFieldLabel);
		layout.putConstraint(SpringLayout.NORTH, passFieldLabel, 47, SpringLayout.SOUTH, userFieldLabel);
		layout.putConstraint(SpringLayout.WEST, passFieldLabel, 178, SpringLayout.WEST, this);

		passField = new JPasswordField ("", 10);
		passField.setFont(textFieldFont);
		passField.setForeground(Color.GRAY);
		add(passField);
		layout.putConstraint(SpringLayout.NORTH, passField, 44, SpringLayout.SOUTH, userFieldLabel);
		layout.putConstraint(SpringLayout.WEST, passField, 340, SpringLayout.WEST, this);


		confirmPassFieldLabel = new JLabel("Confirm Password: ");
		confirmPassFieldLabel.setFont(labelFont);
		confirmPassFieldLabel.setForeground(Color.GRAY);
		add(confirmPassFieldLabel);
		layout.putConstraint(SpringLayout.NORTH, confirmPassFieldLabel, 47, SpringLayout.SOUTH, passFieldLabel);
		layout.putConstraint(SpringLayout.WEST, confirmPassFieldLabel, 55, SpringLayout.WEST, this);

		confirmPassField = new JPasswordField ("", 10);
		confirmPassField.setFont(textFieldFont);
		confirmPassField.setForeground(Color.GRAY);
		add(confirmPassField);
		layout.putConstraint(SpringLayout.NORTH, confirmPassField, 44, SpringLayout.SOUTH, passFieldLabel);
		layout.putConstraint(SpringLayout.WEST, confirmPassField, 340, SpringLayout.WEST, this);
	}

	private void createButtons (BackButtonHandler backButtonHandler) {
		backToMainMenu = new JButton("<");
		backToMainMenu.addActionListener(backButtonHandler);
		backToMainMenu.setFont(buttonFont);
		backToMainMenu.setForeground(Color.GRAY);
		add(backToMainMenu);
		layout.putConstraint(SpringLayout.NORTH, backToMainMenu, 8, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, backToMainMenu, 8, SpringLayout.WEST, this);

		registerButton = new JButton("Register");
		registerButton.addActionListener(new RegistrationHandler());
		registerButton.setFont(buttonFont);
		registerButton.setForeground(Color.GRAY);
		add(registerButton);
		layout.putConstraint(SpringLayout.NORTH, registerButton, 290, SpringLayout.SOUTH, headerImageLabel);
		layout.putConstraint(SpringLayout.WEST, registerButton, 255, SpringLayout.WEST, this);
	}

	private class RegistrationHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String user = userField.getText();
			String pass = passField.getText();
			String confirm = confirmPassField.getText();

			if(user.equals("") || pass.equals("") || confirm.equals("")) {
				JOptionPane.showMessageDialog(null, "Please fill out all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (user.trim().contains(" ")) {
				JOptionPane.showMessageDialog(null, "Usernames cannot contain spaces.", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (!userManager.checkUserAvailability(user)) {
				JOptionPane.showMessageDialog(null, "Username unavailable.", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (!pass.equals(confirm)) {
				JOptionPane.showMessageDialog(null, "Passwords don't match.", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (pass.length() < 6) {
				JOptionPane.showMessageDialog(null, "Password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				User newUser = new User(user, pass);
				userManager.addUser(newUser);

				JOptionPane.showMessageDialog(null, "Account created successfully.", "", JOptionPane.INFORMATION_MESSAGE);

				userField.setText("");
				passField.setText("");
				confirmPassField.setText("");
			}
		}
	}
}