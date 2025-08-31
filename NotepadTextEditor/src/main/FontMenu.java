package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class FontMenu extends JDialog {
	// will need reference to our gui to make changes to the gui from this class
	private NotepadGUI source;

	public FontMenu(NotepadGUI source) {
		this.source = source;
		setTitle("Font Settings");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // dispose used resources once closed
		setSize(425, 350);
		setLocationRelativeTo(source);
		setModal(true); // enables that the user cannot interact with the notepad until the menu is
						// closed

		// remove the layout management, giving us more control of the components
		setLayout(null);

		addMenuComponents();
	}

	private void addMenuComponents() {
		addFontChooser();
		addFontStyleChooser();
		addFontSizeChooser();
		addFontColorChooser();
		
		// action buttons
		JButton applyButton = new JButton("Apply");
	}

	private void addFontChooser() {
		JLabel fontLabel = new JLabel("Font");
		fontLabel.setBounds(10, 5, 125, 10);
		add(fontLabel);

		// font panel will display the current font and the list of fonts available
		JPanel fontPanel = new JPanel();
		fontPanel.setBounds(10, 15, 125, 160);

		// display current font
		JTextField currentFontField = new JTextField(source.getTextArea().getFont().getFontName());
		currentFontField.setPreferredSize(new Dimension(125, 25));
		currentFontField.setEditable(false); // disallow the user from editing the field
		currentFontField.setFocusable(false);
		fontPanel.add(currentFontField);

		// display list of fonts
		JPanel listOfFontsPanel = new JPanel();

		// changes our layout to only have one column to display each font properly
		listOfFontsPanel.setLayout(new BoxLayout(listOfFontsPanel, BoxLayout.Y_AXIS));

		// change the background color to white
		listOfFontsPanel.setBackground(Constants.FONT_MENU_LIST_BACKGROUND_COLOR);

		JScrollPane scrollPane = new JScrollPane(listOfFontsPanel);
		scrollPane.setPreferredSize(new Dimension(125, 125));
		scrollPane.getVerticalScrollBar().setUnitIncrement(Constants.FONT_LIST_SCROLL_BAR_UNIT_INCREMENT);

		// retrieve all of the possible fonts
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();

		// for each font name in font names, we are going to display them to our
		// listOfFontsPanel as a JLabel

		for (String fontName : fontNames) {
			JLabel fontNameLabel = new JLabel(fontName);
			Font font = new Font(fontName, Font.PLAIN, 14);
			fontNameLabel.setFont(font);

			fontNameLabel.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// when clicked set currentFontField to font menu
					currentFontField.setText(fontName);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// add highlight over font when mouse cursor is over it
					fontNameLabel.setOpaque(true);
					fontNameLabel.setBackground(Constants.MOUSE_ENTER_FONT_BACKGROUND_COLOR);
					fontNameLabel.setForeground(Constants.MOUSE_ENTER_FONT_FOREGROUND_COLOR);
					fontNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// remove the highlight once the mouse stops hovering over the font names
					fontNameLabel.setBackground(null); // reset bg color
					fontNameLabel.setForeground(null); // reset font color
					fontNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});
			// add to panel
			listOfFontsPanel.add(fontNameLabel);
		}
		fontPanel.add(scrollPane);

		add(fontPanel);
	}

	private void addFontStyleChooser() {
		JLabel fontStyleLabel = new JLabel("Font Style");
		fontStyleLabel.setBounds(145, 5, 125, 10);
		add(fontStyleLabel);

		// will display the curent font style and all available font styles
		JPanel fontStylePanel = new JPanel();
		fontStylePanel.setBounds(145, 15, 125, 160);

		// get current font style
		int currentFontStyle = source.getTextArea().getFont().getStyle();
		String currentFontStyleText;

		switch (currentFontStyle) {
			case Font.PLAIN:
				currentFontStyleText = "Plain";
				break;
			case Font.BOLD:
				currentFontStyleText = "Bold";
				break;
			case Font.ITALIC:
				currentFontStyleText = "Italic";
				break;
			default: // bold italic
				currentFontStyleText = "Bold Italic";
				break;
		}

		JTextField currentFontStyleField = new JTextField(currentFontStyleText);
		currentFontStyleField.setPreferredSize(new Dimension(125, 25));
		currentFontStyleField.setEditable(false);
		currentFontStyleField.setFocusable(false);
		fontStylePanel.add(currentFontStyleField);
		
		// display list of all font style available
		JPanel listOfFontStylesPanel = new JPanel();
		
		// make the layout have only one column (similiar to font names)
		listOfFontStylesPanel.setLayout(new BoxLayout(listOfFontStylesPanel, BoxLayout.Y_AXIS));
		listOfFontStylesPanel.setBackground(Constants.FONT_MENU_LIST_BACKGROUND_COLOR);
		
		// list of font styles
		JLabel plainStyle = new JLabel("Plain");
		plainStyle.setFont(new Font("Dialog", Font.PLAIN, 12));
		plainStyle.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				currentFontStyleField.setText(plainStyle.getText());
				currentFontStyleField.setFont(plainStyle.getFont());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				plainStyle.setOpaque(true);
				plainStyle.setBackground(Constants.MOUSE_ENTER_FONT_BACKGROUND_COLOR);
				plainStyle.setForeground(Constants.MOUSE_ENTER_FONT_FOREGROUND_COLOR);
				plainStyle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				plainStyle.setBackground(null);
				plainStyle.setForeground(null);
				plainStyle.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		listOfFontStylesPanel.add(plainStyle);
		
		JLabel boldStyle = new JLabel("Bold");
		boldStyle.setFont(new Font("Dialog", Font.BOLD, 12));
		boldStyle.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				currentFontStyleField.setText(boldStyle.getText());
				currentFontStyleField.setFont(boldStyle.getFont());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				boldStyle.setOpaque(true);
				boldStyle.setBackground(Constants.MOUSE_ENTER_FONT_BACKGROUND_COLOR);
				boldStyle.setForeground(Constants.MOUSE_ENTER_FONT_FOREGROUND_COLOR);
				boldStyle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boldStyle.setBackground(null);
				boldStyle.setForeground(null);
				boldStyle.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		listOfFontStylesPanel.add(boldStyle);
		
		JLabel italicStyle = new JLabel("Italic");
		italicStyle.setFont(new Font("Dialog", Font.ITALIC, 12));
		italicStyle.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				currentFontStyleField.setText(italicStyle.getText());
				currentFontStyleField.setFont(italicStyle.getFont());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				italicStyle.setOpaque(true);
				italicStyle.setBackground(Constants.MOUSE_ENTER_FONT_BACKGROUND_COLOR);
				italicStyle.setForeground(Constants.MOUSE_ENTER_FONT_FOREGROUND_COLOR);
				italicStyle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				italicStyle.setBackground(null);
				italicStyle.setForeground(null);
				italicStyle.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		listOfFontStylesPanel.add(italicStyle);
		
		JLabel boldItalicStyle = new JLabel("Bold Italic");
		boldItalicStyle.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		boldItalicStyle.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				currentFontStyleField.setText(boldItalicStyle.getText());
				currentFontStyleField.setFont(boldItalicStyle.getFont());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				boldItalicStyle.setOpaque(true);
				boldItalicStyle.setBackground(Constants.MOUSE_ENTER_FONT_BACKGROUND_COLOR);
				boldItalicStyle.setForeground(Constants.MOUSE_ENTER_FONT_FOREGROUND_COLOR);
				boldItalicStyle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boldItalicStyle.setBackground(null);
				boldItalicStyle.setForeground(null);
				boldItalicStyle.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));  
			}
		});
		listOfFontStylesPanel.add(boldItalicStyle);
		
		JScrollPane scrollPane = new JScrollPane(listOfFontStylesPanel);
		scrollPane.setPreferredSize(new Dimension(125, 125));
		fontStylePanel.add(scrollPane);
		
		add(fontStylePanel);
	}
	
	private void addFontSizeChooser() {
		JLabel fontSizeLabel = new JLabel("Font Size");
		fontSizeLabel.setBounds(275, 5, 125, 10);
		add(fontSizeLabel);
		
		// display the current font size and list of font size to choose from
		JPanel fontSizePanel = new JPanel();
		fontSizePanel.setBounds(275, 15, 125, 160);
		
		JTextField currentFontSizeField = new JTextField(
				Integer.toString(source.getTextArea().getFont().getSize())
				);
		currentFontSizeField.setPreferredSize(new Dimension(125, 25));
		currentFontSizeField.setEditable(false);
		currentFontSizeField.setFocusable(false);
		fontSizePanel.add(currentFontSizeField);
		
		// create a list of font sizes to choose from
		JPanel listOfFontSizesPanel = new JPanel();
		listOfFontSizesPanel.setLayout(new BoxLayout(listOfFontSizesPanel, BoxLayout.Y_AXIS));
		listOfFontSizesPanel.setBackground(Constants.FONT_MENU_LIST_BACKGROUND_COLOR);
		
		// list of available font sizes will be from 8 -> 72 with increments of 2
		for (int i = 8; i <= 72; i += 2) {
			JLabel fontSizeValueLabel = new JLabel(Integer.toString(i));
			fontSizeValueLabel.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					currentFontSizeField.setText(fontSizeValueLabel.getText());
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					fontSizeValueLabel.setOpaque(true);
					fontSizeValueLabel.setBackground(Constants.MOUSE_ENTER_FONT_BACKGROUND_COLOR);
					fontSizeValueLabel.setForeground(Constants.MOUSE_ENTER_FONT_FOREGROUND_COLOR);
					fontSizeValueLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					fontSizeValueLabel.setBackground(null);
					fontSizeValueLabel.setForeground(null);
					fontSizeValueLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				
			});
			listOfFontSizesPanel.add(fontSizeValueLabel);
		}
		
		JScrollPane scrollPane = new JScrollPane(listOfFontSizesPanel);
		scrollPane.setPreferredSize(new Dimension(125, 125));
		scrollPane.getVerticalScrollBar().setUnitIncrement(Constants.FONT_LIST_SCROLL_BAR_UNIT_INCREMENT);
		fontSizePanel.add(scrollPane);
		
		add(fontSizePanel);
	}
	
	private void addFontColorChooser() {
		// display to the user the current color of the text
		JPanel currentColorBox = new JPanel();
		currentColorBox.setBounds(175, 200, 23, 23);
		currentColorBox.setBackground(source.getTextArea().getForeground());
		currentColorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(currentColorBox);
		
		JButton chooseColorButton = new JButton("Choose Color");
		chooseColorButton.setBounds(10, 200, 150, 25);
		chooseColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(null, "Select a color", Color.BLACK);
				
				// update colorbox to selected color
				currentColorBox.setBackground(c);
			}
		});
		add(chooseColorButton);
	}
	
	
	
	
	
}
