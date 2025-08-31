package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NotepadGUI extends JFrame {
	// file explorer
	private JFileChooser fileChooser;

	JTextArea textArea;

	public NotepadGUI() {
		super("Notepad");
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// file chooser setup
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(Constants.ASSET_PATH));
		fileChooser.setFileFilter(new FileNameExtensionFilter("TXT", "txt"));

		addGuiComponents();
	}

	private void addGuiComponents() {
		addToolbar();

		// an area to type text into
		textArea = new JTextArea();
		add(textArea, BorderLayout.CENTER);
	}

	private void addToolbar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		// menu bar
		JMenuBar menuBar = new JMenuBar();
		toolbar.add(menuBar);

		// add menus
		menuBar.add(addFileMenu());

		add(toolbar, BorderLayout.NORTH);

	}

	private JMenu addFileMenu() {
		JMenu fileMenu = new JMenu("File");

		// new functionality - resets everything
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// reset title header
				setTitle("Notepad");

				// reset text area
				textArea.setText("");
			}
		});
		fileMenu.add(newMenuItem);

		// open functionality - open a text file
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// open file explorer
					int result = fileChooser.showOpenDialog(NotepadGUI.this);
					// get the selected file
					File selectedFile = fileChooser.getSelectedFile();
					
					// continue to execute code only if the user pressed the save button and the selected file is not null
					if (result != JFileChooser.APPROVE_OPTION || selectedFile == null) {
						return;
					}

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

					// show display dialog
					JOptionPane.showMessageDialog(NotepadGUI.this, "Saved File!");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		fileMenu.add(saveAsMenuItem);

		// save functionality - saves text into current text file
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);

		// exit functionality - ends program process
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);

		return fileMenu;
	}
}
