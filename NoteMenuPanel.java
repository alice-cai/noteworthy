/**
 * GUI that displays the user's list of notes. Allows user to view an exiting note, create a new note,
 * or log out of their account.
 */

import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ArrayDeque;

public class NoteMenuPanel extends JPanel {
	private static final String NOTE_MENU_PANEL = "card with list of notes";
	private static final String CREATE_NOTE_PANEL = "card that allows user to create new note";
	private static final String NOTE_PANEL = "card that allows user to view or edit existing note";
	private static final int NOTE_LIMIT = 12;
	private static final String IMAGE_FILE = "images/no_notes.png";

	private SpringLayout layout;

	private JButton logOutButton;
	private BackButtonHandler backButtonHandler;
	private JButton newNoteButton;

	private NoteManager noteManager;
	private JPanel noteListPanel;
	private Runnable onNoteListUpdate; // callback that tells the class when to reconstruct the noteListPanel

	private JLabel imageLabel;
	private ImageIcon image;
	
	private Font buttonFont;
	private Font noteListFont;

	private JPanel cards;
	private CardLayout cardLayout;
	private ArrayDeque<String> history;

	public NoteMenuPanel (User user, JPanel cards, CardLayout cardLayout, ArrayDeque<String> history, BackButtonHandler backButtonHandler) {
		this.cards = cards;
		this.cardLayout = cardLayout;
		this.history = history;
		this.backButtonHandler = backButtonHandler;
		noteManager = new NoteManager(user);

		buttonFont = new Font("Arial", Font.BOLD, 35);
		noteListFont = new Font("Arial", Font.BOLD, 30);

		layout = new SpringLayout();
		setLayout(layout);

		onNoteListUpdate = () -> {
			createNoteMenu();
		};

		noteListPanel = new JPanel(new GridLayout(0,1));
		createButtons();
		createNoteMenu();
	}

	private void createButtons () {
		logOutButton = new JButton("<");
		logOutButton.addActionListener(backButtonHandler);
		logOutButton.setFont(buttonFont);
		logOutButton.setForeground(Color.GRAY);
		add(logOutButton);
		layout.putConstraint(SpringLayout.WEST, logOutButton, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, logOutButton, 10, SpringLayout.NORTH, this);

		newNoteButton = new JButton("+");
		newNoteButton.addActionListener(new CreateNoteButtonHandler());
		newNoteButton.setFont(buttonFont);
		newNoteButton.setForeground(Color.GRAY);
		add(newNoteButton);
		layout.putConstraint(SpringLayout.EAST, newNoteButton, 0, SpringLayout.EAST, noteListPanel);
		layout.putConstraint(SpringLayout.NORTH, newNoteButton, 10, SpringLayout.SOUTH, noteListPanel);
	}

	private void createNoteMenu () {
		ArrayList<Note> noteList = noteManager.getNoteList();
		noteListPanel.removeAll();

		if (noteList.size() == 0) {
			imageLabel = new JLabel();
			image = new ImageIcon(IMAGE_FILE);

			imageLabel.setSize(575, 409);
			imageLabel.setIcon(image);

			noteListPanel.add(imageLabel);

			add(noteListPanel);
			layout.putConstraint(SpringLayout.WEST, noteListPanel, 50, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, noteListPanel, 15, SpringLayout.NORTH, this);
			layout.putConstraint(SpringLayout.EAST, this, 50, SpringLayout.EAST, noteListPanel);
		} else {
			for(Note note : noteList) {
				JButton noteButton = new JButton(note.getTitle());
				noteButton.addActionListener(new NoteButtonHandler());
				noteButton.setFont(noteListFont);
				noteButton.setForeground(Color.GRAY);
				noteListPanel.add(noteButton);
			}
			add(noteListPanel);
			layout.putConstraint(SpringLayout.WEST, noteListPanel, 100, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, noteListPanel, 15, SpringLayout.NORTH, this);
			layout.putConstraint(SpringLayout.EAST, this, 100, SpringLayout.EAST, noteListPanel);
		}
	}

	public class NoteButtonHandler implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			String noteName = ((JButton)event.getSource()).getText();
			Note note = noteManager.findNoteByTitle(noteName);
			NotePanel notePanel = new NotePanel(note, noteManager, backButtonHandler, onNoteListUpdate);
			history.addLast(NOTE_MENU_PANEL);
			cards.add(notePanel, NOTE_PANEL);
			cardLayout.show(cards, NOTE_PANEL);
		}
	}

	public class CreateNoteButtonHandler implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			if(noteManager.getNumNotes() >= NOTE_LIMIT) {
				JOptionPane.showMessageDialog(null, "You have reached the maximum note capacity." + 
					"\nUpgrade to premium for unlimited note storage.", "Note Capacity Reached", JOptionPane.ERROR_MESSAGE);
			} else {
				CreateNotePanel createNotePanel = new CreateNotePanel(noteManager, backButtonHandler, onNoteListUpdate);
				cards.add(createNotePanel, CREATE_NOTE_PANEL);
				cardLayout.show(cards, CREATE_NOTE_PANEL);
				history.addLast(NOTE_MENU_PANEL);
			}
		}
	}
}