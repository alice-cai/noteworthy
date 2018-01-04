/**
 * GUI for displaying the contents of a single Note. Allows user to edit the note, delete
 * the note, or go back to the note menu.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ArrayDeque;

public class NotePanel extends JPanel {
	private static final int TITLE_MAX_LENGTH = 25;

	private SpringLayout layout;

	private JButton backToNotesMenuButton;
	private BackButtonHandler backButtonHandler;
	private JButton editNoteButton;
	private JButton saveNoteButton;
	private JButton deleteNoteButton;

	private JLabel titlePrompt;
	private JTextField title;
	private JLabel contentPrompt;
	private JTextArea content;
	private JScrollPane contentScrollPane;

	private Note note;
	private NoteManager noteManager;
	private Runnable onNoteListUpdate;

	private Font buttonFont;
	private Font labelFont;
	private Font textFieldFont;

	public NotePanel (Note note, NoteManager noteManager, BackButtonHandler backButtonHandler, Runnable onNoteListUpdate) {
		this.note = note;
		this.noteManager = noteManager;
		this.backButtonHandler = backButtonHandler;
		this.onNoteListUpdate = onNoteListUpdate;

		layout = new SpringLayout();
		setLayout(layout);

		buttonFont = new Font("Arial", Font.BOLD, 35);
		labelFont = new Font("Arial", Font.BOLD, 30);
		textFieldFont = new Font("Arial", Font.PLAIN, 30);

		createTitleSection();
		createContentSection();
		createButtons();
	}

	private void createTitleSection () {
		titlePrompt = new JLabel("Title:");
		titlePrompt.setFont(labelFont);
		titlePrompt.setForeground(Color.GRAY);
		add(titlePrompt);

		layout.putConstraint(SpringLayout.WEST, titlePrompt, 30, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, titlePrompt, 80, SpringLayout.NORTH, this);

		title = new JTextField(note.getTitle(), 20);
		title.setEditable(false);
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

		content = new JTextArea(note.getContent(), 10, 20);
		content.setEditable(false);
		content.setFont(textFieldFont);
		content.setForeground(Color.GRAY);

		contentScrollPane = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(contentScrollPane);

		layout.putConstraint(SpringLayout.WEST, contentScrollPane, 120, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, contentScrollPane, 26, SpringLayout.SOUTH, title);
	}

	private void createButtons () {
		backToNotesMenuButton = new JButton("<");
		backToNotesMenuButton.addActionListener(backButtonHandler);
		backToNotesMenuButton.setFont(buttonFont);
		backToNotesMenuButton.setForeground(Color.GRAY);
		add(backToNotesMenuButton);
		layout.putConstraint(SpringLayout.WEST, backToNotesMenuButton, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, backToNotesMenuButton, 10, SpringLayout.NORTH, this);

		editNoteButton = new JButton("edit");
		editNoteButton.addActionListener(new EditNoteButtonHandler());
		editNoteButton.setFont(buttonFont);
		editNoteButton.setForeground(Color.GRAY);
		add(editNoteButton);
		layout.putConstraint(SpringLayout.EAST, editNoteButton, 0, SpringLayout.EAST, contentScrollPane);
		layout.putConstraint(SpringLayout.NORTH, editNoteButton, 15, SpringLayout.SOUTH, contentScrollPane);

		saveNoteButton = new JButton("save");
		saveNoteButton.addActionListener(new SaveNoteButtonHandler(onNoteListUpdate));
		saveNoteButton.setFont(buttonFont);
		saveNoteButton.setForeground(Color.GRAY);
		add(saveNoteButton);
		layout.putConstraint(SpringLayout.EAST, saveNoteButton, 0, SpringLayout.EAST, contentScrollPane);
		layout.putConstraint(SpringLayout.NORTH, saveNoteButton, 15, SpringLayout.SOUTH, contentScrollPane);
		saveNoteButton.setVisible(false);

		deleteNoteButton = new JButton("delete");
		deleteNoteButton.addActionListener(new DeleteNoteButtonHandler(onNoteListUpdate));
		deleteNoteButton.setFont(buttonFont);
		deleteNoteButton.setForeground(Color.GRAY);
		add(deleteNoteButton);
		layout.putConstraint(SpringLayout.WEST, deleteNoteButton, 365, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, deleteNoteButton, 15, SpringLayout.SOUTH, contentScrollPane);
	}

	public class DeleteNoteButtonHandler implements ActionListener {
		private Runnable onNoteListUpdate;

		public DeleteNoteButtonHandler (Runnable onNoteListUpdate) {
			this.onNoteListUpdate = onNoteListUpdate;
		}

		public void actionPerformed (ActionEvent event) {
			int userChoice = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete your note?" +
				"\nThis action cannot be undone.", "Delete Note", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if(userChoice == JOptionPane.YES_OPTION) {
				noteManager.deleteNote(note.getTitle());
				onNoteListUpdate.run();
				backButtonHandler.actionPerformed(null);				
			}
		}
	}

	public class EditNoteButtonHandler implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			editNoteButton.setVisible(false);
			saveNoteButton.setVisible(true);

			title.setEditable(true);
			content.setEditable(true);
		}
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
				boolean noteSaved = noteManager.updateNote(note.getTitle(), title.getText(), content.getText());
				if (noteSaved) {
					saveNoteButton.setVisible(false);
					editNoteButton.setVisible(true);

					title.setEditable(false);
					content.setEditable(false);

					onNoteListUpdate.run();
				} else {
					JOptionPane.showMessageDialog(null, "Note with that name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}