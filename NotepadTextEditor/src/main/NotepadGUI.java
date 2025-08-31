package main;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class NotepadGUI extends JFrame {
	// panel that stores the components
	private JPanel panel;

	// file explorer
	private JFileChooser fileChooser;

	private JTextArea textArea;
	
	public JTextArea getTextArea() {
		return textArea;
	}

	private File currentFile;

	// Swing's built in library to manage undo and redo functionalities
	private UndoManager undoManager;

	// Swing's built in library to make key bindings
	private AbstractAction undoAction, redoAction, saveAction, openAction, newAction;
	private AbstractAction wordWrapAction;

	// Key Bindings that we are Implementing
	private KeyStroke undoKeyStroke, redoKeyStroke, saveKeyStroke, openKeyStroke, newKeyStroke;
	private KeyStroke wordWrapKeyStroke;

	public NotepadGUI() {
		super("Notepad");
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);

		// file chooser setup
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(Constants.ASSET_PATH));
		fileChooser.setFileFilter(new FileNameExtensionFilter("TXT", "txt"));

		undoManager = new UndoManager();

		// key binding setup
		undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK); // Ctrl + Z
		redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK); // Ctrl - Y
		saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK); // Ctrl - S
		openKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK); // Ctrl - O
		newKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK); // Ctrl - N
		wordWrapKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.ALT_MASK); // Alt + W

		addGuiComponents();
	}

	private void addGuiComponents() {
		addToolbar();

		// an area to type text into
		textArea = new JTextArea();
		textArea.setFont(Constants.DEFAULT_NOTEPAD_FONT);
		// everytime we text into our JTextArea, undoManager will add an EditEvent
		// to the undoManager, which will handle the undo and redos
		textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {

			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				// adds each edit tat we do in the text area (either adding or removing text)
				undoManager.addEdit(e.getEdit());

			}
		});

		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.add(scrollPane, BorderLayout.CENTER);
	}

	private void addToolbar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		// menu bar
		JMenuBar menuBar = new JMenuBar();
		toolbar.add(menuBar);

		// add menus
		menuBar.add(addFileMenu());
		menuBar.add(addEditMenu());
		menuBar.add(addFormatMenu());

		panel.add(toolbar, BorderLayout.NORTH);

	}

	private JMenu addFileMenu() {
		JMenu fileMenu = new JMenu("File");

		// new functionality - resets everything
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setToolTipText("CTRL + N");
		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// reset title header
				setTitle("Notepad");

				// reset text area
				textArea.setText("");

				// reset the current file variable
				currentFile = null;
			}
		});
		fileMenu.add(newMenuItem);

		// define the new action
		newAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newMenuItem.doClick();
			}
		};

		// associate the ctrl N keystroke with an action name in InputMap
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(newKeyStroke, "NEW");

		// associate action name with the action in the action map
		panel.getActionMap().put("NEW", newAction);

		// open functionality - open a text file
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setToolTipText("CTRL + O");
		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// open file explorer
				int result = fileChooser.showOpenDialog(NotepadGUI.this);

				try {
					// get the selected file
					File selectedFile = fileChooser.getSelectedFile();

					// continue to execute code only if the user pressed the save button and the
					// selected file is not null
					if (result != JFileChooser.APPROVE_OPTION || selectedFile == null) {
						return;
					}

					// update the currentFile variable
					currentFile = selectedFile;

					// reset notepad
					newMenuItem.doClick(); // doClick() method executes the action we implemented with the open feature

					// update title header
					setTitle(selectedFile.getName());

					// read the file
					FileReader fileReader = new FileReader(selectedFile);
					BufferedReader bufferedReader = new BufferedReader(fileReader);

					// store the text
					StringBuilder fileText = new StringBuilder();
					String readText;
					while ((readText = bufferedReader.readLine()) != null) {
						fileText.append(readText);
						fileText.append("\n");
					}

					// update text area gui
					textArea.setText(fileText.toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		fileMenu.add(openMenuItem);

		// define the new action
		openAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openMenuItem.doClick();
			}
		};

		// associate the ctrl O keystroke with an action name in InputMap
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(openKeyStroke, "OPEN");

		// associate action name with the action in the action map
		panel.getActionMap().put("OPEN", openAction);

		// save as functionality - creates a new text file and saves user text
		JMenuItem saveAsMenuItem = new JMenuItem("Save As");
		saveAsMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// open save dialog
					int result = fileChooser.showSaveDialog(NotepadGUI.this);
					File selectedFile = fileChooser.getSelectedFile();

					if (result != JFileChooser.APPROVE_OPTION || selectedFile == null) {
						return;
					}

					// we will need to append .txt to the file if it does not have the txt extension
					// yet
					String fileName = selectedFile.getName();
					if (!fileName.substring(fileName.length() - 4).equalsIgnoreCase(".txt")) {
						selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");
					}

					// create a new file
					selectedFile.createNewFile();

					// now we will write the user's text into the file we just created
					FileWriter fileWriter = new FileWriter(selectedFile);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					bufferedWriter.write(textArea.getText());
					bufferedWriter.close();
					fileWriter.close();

					// update the title header of gui to the save text file
					setTitle(fileName);

					// update the currentFile variable
					currentFile = selectedFile;

					// show display dialog
					JOptionPane.showMessageDialog(NotepadGUI.this, "Saved File!");

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		fileMenu.add(saveAsMenuItem);

		// save functionality - saves text into current text file
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setToolTipText("CTRL + S");
		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// if the current file is null, then we have to perform save as functionality
				if (currentFile == null)
					saveAsMenuItem.doClick();

				// if the user chooses to cancel saving the file, this means that the
				// current file will still be null, then we want to prevent executing the rest
				// of the code
				if (currentFile == null)
					return;

				try {
					// write to current file
					FileWriter fileWriter = new FileWriter(currentFile);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					bufferedWriter.write(textArea.getText());
					bufferedWriter.close();
					fileWriter.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		fileMenu.add(saveMenuItem);

		// define the new action
		saveAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveMenuItem.doClick();
			}
		};

		// associate the ctrl S keystroke with an action name in InputMap
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "SAVE");

		// associate action name with the action in the action map
		panel.getActionMap().put("SAVE", saveAction);

		// exit functionality - ends program process
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NotepadGUI.this.dispose();
			}
		});
		fileMenu.add(exitMenuItem);

		return fileMenu;
	}

	private JMenu addEditMenu() {
		JMenu editMenu = new JMenu("Edit");

		JMenuItem undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.setToolTipText("CTRL + Z");
		undoMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// means that if there are any edits we can undo,
				// then we undo them
				if (undoManager.canUndo()) {
					undoManager.undo();
				}

			}
		});
		editMenu.add(undoMenuItem);

		// define undo action
		undoAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undoMenuItem.doClick();
			}
		};

		// associate the ctrl z keystroke with an action name in InputMap
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "UNDO");

		// associate action name with the action in the action map
		panel.getActionMap().put("UNDO", undoAction);

		JMenuItem redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.setToolTipText("CTRL + Y");
		redoMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// means that if there is an edit we can redo, then redo it
				if (undoManager.canRedo()) {
					undoManager.redo();
				}
			}
		});
		editMenu.add(redoMenuItem);

		// define the redo action
		redoAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				redoMenuItem.doClick();
			}
		};

		// associate the ctrl y keystroke with an action name in InputMap
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "REDO");

		// associate action name with the action in the action map
		panel.getActionMap().put("REDO", redoAction);

		return editMenu;
	}

	private JMenu addFormatMenu() {
		JMenu formatMenu = new JMenu("Format");

		// wrap word functionality
		JCheckBoxMenuItem wordWrapMenuItem = new JCheckBoxMenuItem("Word Wrap");
		wordWrapMenuItem.setToolTipText("ALT + W");
		wordWrapMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isChecked = wordWrapMenuItem.getState();
				if (isChecked) {
					// wrap words
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
				} else {
					textArea.setLineWrap(false);
					textArea.setWrapStyleWord(false);
				}

			}
		});
		formatMenu.add(wordWrapMenuItem);

		wordWrapAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wordWrapMenuItem.doClick();
			}
		};
		panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(wordWrapKeyStroke, "WORD WRAP");
		panel.getActionMap().put("WORD WRAP", wordWrapAction);

		// aligning text
		JMenu alignTextMenu = new JMenu("Align Text");

		// align text to the left
		JMenuItem alignTextLeftMenuItem = new JMenuItem("Left Align");
		alignTextLeftMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			}
		});
		alignTextMenu.add(alignTextLeftMenuItem);

		// align text to the Right
		JMenuItem alignTextRightMenuItem = new JMenuItem("Right Align");
		alignTextRightMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}
		});
		alignTextMenu.add(alignTextRightMenuItem);

		formatMenu.add(alignTextMenu);

		// font format
		JMenuItem fontMenuItem = new JMenuItem("Font...");
		fontMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// launch the font menu
				new FontMenu(NotepadGUI.this).setVisible(true);

			}
		});
		formatMenu.add(fontMenuItem);

		return formatMenu;
	}
}
