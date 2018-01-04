/**
 * GUI for creating a new note. Provides text boxes for the title and content of the note.
 * User can choose to save their note or cancel by clicking the back button.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ArrayDeque;

public class CreateNotePanel extends JPanel {
	private static final int TITLE_MAX_LENGTH = 25;

	private SpringLayout layout;

	private JButton backToNotesMenuButton;
	private BackButtonHandler backButtonHandler;
	private JButton saveNoteButton;

	private JLabel titlePrompt;
	private JTextField title;

	private JScrollPane contentScrollPane;
	private JLabel contentPrompt;
	private JTextArea content;

	private NoteManager noteManager;

	private Font buttonFont;
	private Font labelFont;
	private Font textFieldFont;

	public CreateNotePanel (NoteManager noteManager, BackButtonHandler backButtonHandler, Runnable onNoteListUpdate) {
		this.noteManager = noteManager;
		this.backButtonHandler = backButtonHandler;

		buttonFont = new Font("Arial", Font.BOLD, 35);
		labelFont = new Font("Arial", Font.BOLD, 30);
		textFieldFont = new Font("Arial", Font.PLAIN, 30);

		layout = new SpringLayout();
		setLayout(layout);

		createTitleSection();
		createContentSection();
		createButtons(onNoteListUpdate);
	}

	private void createButtons (Runnable onNoteListUpdate) {
		backToNotesMenuButton = new JButton("<");
		backToNotesMenuButton.addActionListener(backButtonHandler);
		backToNotesMenuButton.setFont(buttonFont);
		backToNotesMenuButton.setForeground(Color.GRAY);
		add(backToNotesMenuButton);
		layout.putConstraint(SpringLayout.WEST, backToNotesMenuButton, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, backToNotesMenuButton, 10, SpringLayout.NORTH, this);


		saveNoteButton = new JButton("save note");
		saveNoteButton.addActionListener(new SaveNoteButtonHandler(onNoteListUpdate));
		saveNoteButton.setFont(buttonFont);
		saveNoteButton.setForeground(Color.GRAY);
		add(saveNoteButton);
		layout.putConstraint(SpringLayout.EAST, saveNoteButton, 0, SpringLayout.EAST, contentScrollPane);
		layout.putConstraint(SpringLayout.NORTH, saveNoteButton, 15, SpringLayout.SOUTH, contentScrollPane);
	}

	private void createTitleSection () {
		titlePrompt = new JLabel("Title:");
		titlePrompt.setFont(labelFont);
		titlePrompt.setForeground(Color.GRAY);
		add(titlePrompt);

		layout.putConstraint(SpringLayout.WEST, titlePrompt, 30, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, titlePrompt, 80, SpringLayout.NORTH, this);

		title = new JTextField("", 20);
		title.setFont(textFieldFont);
		title.setForeground(Color.GRAY);
		add(title);

		// limits the title input to TITLE_MAX_LENGTH characters
		KeyAdapter titleLimiter = new KeyAdapter() {
			public void keyTyped (KeyEvent e) {
				if(title.getText().length() >= TITLE_MAX_LENGTH) {
					e.consume();
				}
			}
		};
		title.addKeyListener(titleLimiter);

		layout.putConstraint(SpringLayout.WEST, title, 120, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, title, 76, SpringLayout.NORTH, this);
	}

	private void createContentSection () {
		contentPrompt = new JLabel("Note:");
		contentPrompt.setFont(labelFont);
		contentPrompt.setForeground(Color.GRAY);
		add(contentPrompt);
		layout.putConstraint(SpringLayout.WEST, contentPrompt, 25, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, contentPrompt, 30, SpringLayout.SOUTH, titlePrompt);

		content = new JTextArea("", 10, 20);
		content.setFont(textFieldFont);
		content.setForeground(Color.GRAY);

		contentScrollPane = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(contentScrollPane);

		layout.putConstraint(SpringLayout.WEST, contentScrollPane, 120, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, contentScrollPane, 26, SpringLayout.SOUTH, title);
	}

	public class SaveNoteButtonHandler implements ActionListener {
		private Runnable onNoteListUpdate;

		public SaveNoteButtonHandler (Runnable onNoteListUpdate) {
			this.onNoteListUpdate = onNoteListUpdate;
		}

		public void actionPerformed (ActionEvent event) {
			if (title.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(null, "Your note must have a title.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				boolean noteCreated = noteManager.addNote(title.getText(), content.getText());
				if (noteCreated) {
					onNoteListUpdate.run();
					backButtonHandler.actionPerformed(null);
					title.setText("");
					content.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "Note with that name already exists.");
				}
			}
		}
	}
}