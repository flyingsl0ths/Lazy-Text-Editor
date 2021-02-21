package com.lazytext.texteditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class FindText {
  private Highlighter highlighter;
  private final LazyText window;
  private JPopupMenu searchBox;
  private int x = 0;
  private int y = 0;
  private int position = 0;
  private Highlighter.Highlight[] highlightedTextLocations;
  private JPanel findTextPanel;
  private JButton backButton;
  private JButton forwardButton;
  private JCheckBox regexButton;
  private KeyListener searchType;
  private JTextPane searchField;

  public FindText(LazyText window) {
    this.window = window;

    // Initializes TextArea
    this.initializeHighlighter();

    // Initialized Buttons
    this.initializeButtons();

    // Initializes searchArea
    this.initializeSearchArea();

    // Initializes searchArea Panel
    this.initializePanel();

    // Initializes PopUpMenu
    this.initializePopUpMenu();

    // Themes components in menu
    this.themeComponents();

    // Displays PopupMenu
    this.displayPopUpMenu();

  } // End of FindText Constructor

  private void initializeHighlighter() {
    this.highlighter = this.window.getTextArea().getHighlighter();
  } // End of initializeTextArea

  private JButton themedButton(String iconPath, ActionListener listener) {
    JButton themedButton = new JButton();
    try {
      BufferedImage icon = ImageIO.read(new File(iconPath));
      themedButton = new JButton(new ImageIcon(icon));
      themedButton.setPreferredSize(new Dimension(35, 35));
      themedButton.setBorder(null);
      themedButton.setContentAreaFilled(false);
    } catch (IOException ignored) {
    }

    themedButton.addActionListener(listener);

    return themedButton;
  } // End of themedButton

  private JCheckBox createCheckButton() {
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(false);
    checkBox.setBorder(null);
    checkBox.setBorderPainted(false);
    checkBox.setBorderPaintedFlat(false);

    UseRegexButtonListener lForRegex = new UseRegexButtonListener();
    checkBox.addItemListener(lForRegex);

    return checkBox;
  } // End of createCheckButton

  private void initializeButtons() {
    this.backButton = this.themedButton("src/main/resources/down_arrow.png",
                                        new ButtonListener());
    this.forwardButton = this.themedButton("src/main/resources/up_arrow.png",
                                           new ButtonListener());

    this.regexButton = this.createCheckButton();
  } // End of initializeButtons

  private void setSearchAreaSizeConstraints() {
    Dimension searchFieldWidthHeight = new Dimension(170, 20);

    this.searchField.setPreferredSize(searchFieldWidthHeight);
    this.searchField.setMaximumSize(searchFieldWidthHeight);
    this.searchField.setMinimumSize(searchFieldWidthHeight);
  } // End of setSearchAreaSizeConstraints

  private void initializeSearchArea() {
    this.searchField = new JTextPane();
    this.searchField.setBorder(null);

    this.searchField.setText(this.window.getLastSearchedValue());

    this.setSearchAreaSizeConstraints();

    this.searchType = new ListenForSearchQuery();
    this.searchField.addKeyListener(this.searchType);
  } // End of initializeSearchArea

  private void addComponents() {
    this.findTextPanel.add(this.searchField);
    this.findTextPanel.add(this.regexButton);
    this.findTextPanel.add(this.backButton);
    this.findTextPanel.add(this.forwardButton);
  } // End of addComponents

  private void initializePanel() {
    this.findTextPanel = new JPanel();
    this.findTextPanel.setBorder(null);

    this.addComponents();

  } // End of initializePanel

  private void addComponentsToMenu() {
    this.searchBox.add(this.findTextPanel);
  } // End of addComponentsToMenu

  private void setSearchBoxSizeConstraints() {
    this.searchBox.setMaximumSize(new Dimension(275, 10));
    this.searchBox.setMinimumSize(new Dimension(275, 10));
  } // End of setSizeConstraints

  private void initializePopUpMenu() {
    this.searchBox = new JPopupMenu();
    this.searchBox.setBorderPainted(false);
    this.searchBox.setBorder(null);

    this.x = this.window.getFrame().getWidth() - 320;
    this.y = this.window.getMenuBar().getHeight();
    this.searchBox.setMaximumSize(new Dimension(150, 300));

    this.setSearchBoxSizeConstraints();

    this.addComponentsToMenu();
  } // End of initializePopUpMenu

  private void themeFindTextPanel(Color bgColor) {
    this.findTextPanel.setBackground(bgColor);
  } // End of themePanel

  private void themeSearchBox(Color bgColor) {
    this.searchBox.setBackground(bgColor);
  } // End of themeSearchBox

  private void themeComponents() {
    Color bgColor = Theme.getColorValue(
        this.window.getThemeObject().getParsedThemeValues("findTextMenu")[0]);

    this.themeFindTextPanel(bgColor);
    this.themeSearchBox(bgColor);
  } // End of themeComponents

  private void displayPopUpMenu() {
    this.searchBox.show(this.window.getFrame(), this.x, this.y);
    this.searchField.requestFocus();
  }

  private void highlightText(int startIndex, int endIndex)
      throws BadLocationException {
    Color textHighlightColor = new Color(241, 196, 15);

    HighlightPainter painter =
        new DefaultHighlighter.DefaultHighlightPainter(textHighlightColor);

    this.highlighter.addHighlight(startIndex, endIndex, painter);
  } // End of highlightText

  private void searchAndHighlight(String word) {
    // Matches case and word(s)
    try {
      if (!word.isEmpty()) {
        this.highlighter.removeAllHighlights();
        String textAreaText = this.window.getTextArea().getText();

        int start = textAreaText.indexOf(word);
        int end = start + word.length();

        while (start != -1) {
          this.highlightText(start, end);

          start++;

          start = textAreaText.indexOf(word, start);

          end = start + word.length();
        }
      }
    } catch (BadLocationException ignored) {
    }
  } // End of searchAndHighlight

  private void searchAndHighlightRegex(String word) {
    try {
      if (!word.isEmpty()) {
        this.highlighter.removeAllHighlights();

        Pattern expression = Pattern.compile(word);

        String textAreaText = this.window.getTextArea().getText();

        Matcher matcher = expression.matcher(textAreaText);

        while (matcher.find()) {

          if (matcher.group().length() != 0) {
            this.highlightText(matcher.start(), matcher.end());
          }
        }
      }
    } catch (BadLocationException | PatternSyntaxException e) {
      searchField.setText("Invalid Regular Expression");
    }
  } // End of searchAndHighlight

  private int getStartOffset(Highlighter.Highlight highlight) {
    return highlight.getStartOffset();
  } // End of getStartOffset

  private void moveCaret(int pos) {
    window.getTextArea().requestFocus();
    this.window.getTextArea().setCaretPosition(
        getStartOffset(highlightedTextLocations[pos]));
  } // End of moveCaret

  private void checkPositionBeforeMovingForward(int amountOfHighlightedItems) {
    if (this.position > -1 && this.position <= amountOfHighlightedItems) {

      this.position++;
    }

    if (this.position == amountOfHighlightedItems) {
      this.position = 0;
    }
  } // End of checkPositionBeforeMovingForward

  private void
  checkPositionBeforeMovingBackwards(int amountOfHighlightedItems) {
    if (this.position > -1) {
      this.position--;
    }

    if (this.position == -1) {
      this.position = amountOfHighlightedItems - 1;
    }
  } // End of checkPositionBeforeMovingBackwards

  private class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      highlightedTextLocations = highlighter.getHighlights();
      int numberOfFoundWords = highlightedTextLocations.length;
      if (numberOfFoundWords != 0) {
        if (e.getSource() == forwardButton) {
          moveCaret(position);
          checkPositionBeforeMovingForward(numberOfFoundWords);
        } else {
          checkPositionBeforeMovingBackwards(numberOfFoundWords);
          moveCaret(position);
        }
      }
    }
  } // End of ButtonListener

  private class UseRegexButtonListener implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent e) {
      if (e.getStateChange() == ItemEvent.SELECTED) {
        searchField.removeKeyListener(searchType);
        searchType = new ListenForRegexQuery();
        searchField.addKeyListener(searchType);

      } else {
        searchField.removeKeyListener(searchType);
        searchType = new ListenForSearchQuery();
        searchField.addKeyListener(searchType);
      }
    }

  } // End of ButtonListener

  private class ListenForSearchQuery implements KeyListener {
    public void keyTyped(KeyEvent e) {
      if (!searchField.getText().isEmpty()) {
        position = 0;
        searchAndHighlight(searchField.getText() + e.getKeyChar());
      } else {
        searchAndHighlight("" + e.getKeyChar());
      }
    }

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        position = 0;
        searchAndHighlight(searchField.getText());
      }
      window.setLastSearchedValue(searchField.getText());
    }

  } // End of ListenForSearchQuery

  private class ListenForRegexQuery implements KeyListener {
    public void keyTyped(KeyEvent e) {
      if (!searchField.getText().isEmpty()) {
        position = 0;
        searchAndHighlightRegex(searchField.getText() + e.getKeyChar());
      } else {
        searchAndHighlightRegex("" + e.getKeyChar());
      }
    }

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        position = 0;
        searchAndHighlightRegex(searchField.getText());
      }
      window.setLastSearchedValue(searchField.getText());
    }
  } // End of ListenForRegexQuery

} // End of FindText
