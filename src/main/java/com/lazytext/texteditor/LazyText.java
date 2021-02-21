package com.lazytext.texteditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class LazyText {
  private JFrame mainWindow;
  private final MenuItemListener menuItemListener =
      new MenuItemListener(LazyText.this);
  private final Theme lazyTextTheme = new Theme(LazyText.this);
  private JMenuBar menuBar;
  private final String[] commands = {
      "New",  "Open File", "Save",      "Save As", "Close File", "Themes",
      "Exit", "Undo",      "Redo",      "Cut",     "Copy",       "Paste",
      "Find", "Replace",   "Timestamp", "About"};
  private final int[] keyCodes = {
      KeyEvent.VK_N, KeyEvent.VK_O, KeyEvent.VK_S, KeyEvent.VK_L,
      KeyEvent.VK_P, KeyEvent.VK_K, KeyEvent.VK_T, KeyEvent.VK_Z,
      KeyEvent.VK_Y, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V,
      KeyEvent.VK_F, KeyEvent.VK_R, KeyEvent.VK_E, KeyEvent.VK_K};
  private final Map<String, Integer> keyMappings = new HashMap<>();
  private final UndoManager manager = new UndoManager();
  private String currentTheme = "Light.xml";
  private String lastSearchedValue = "";
  private JTextArea textAreaLineNumbers;
  private JTextArea textArea;
  private JScrollPane scrollableTextArea;
  private JToolBar statusBar;
  private String versionNumber = "1.0";
  private BufferedReader themeReader = null;

  public LazyText() {
    // Initializes JFrame
    this.initializeFrame();

    // Sets the KeyMappings
    this.setKeyMappings();

    // Initializes TextArea
    this.initializeTextArea();

    // Initializes StatusBar
    this.initializeStatusBar();

    // Initializes MenuBar
    this.initializeMenu();

    // Loads last theme used
    this.loadLastUsedTheme();

    // Displays Main Window
    this.mainWindow.setVisible(true);
  } // end of LazyText Constructor

  private void loadLastUsedTheme() {
    try {
      this.themeReader = new BufferedReader(
          new FileReader("src/main/resources/themes/last_used_theme.txt"));
      String lastThemeUsed = this.themeReader.readLine();
      if (!lastThemeUsed.isEmpty()) {
        this.currentTheme = lastThemeUsed;
      }
      this.themeReader.close();
    } catch (IOException e) {
      System.exit(1);
    }

    // Set the default theme
    this.lazyTextTheme.buildTheme(this.currentTheme);
  } // End of loadLastUsedTheme

  private void initializeFrame() {
    // Initializes Main Window
    this.mainWindow = new JFrame("LazyText");
    ImageIcon windowIcon =
        new ImageIcon("src/main/resources/lazy_text_logo.png");
    this.mainWindow.setIconImage(windowIcon.getImage());
    this.mainWindow.setSize(850, 600);
    this.mainWindow.setLocationRelativeTo(null);
    this.mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  } // End of initializeFrame

  private void initializeLinedNumberArea() {
    this.textAreaLineNumbers = new JTextArea("1", 1, 0);
    this.textAreaLineNumbers.setEditable(false);
    this.textAreaLineNumbers.setFocusable(false);
    this.textAreaLineNumbers.setMargin(new Insets(4, 6, 6, 4));
  }

  private void addTextAreaListeners() {
    LineNumberListener lForCurrentLine = new LineNumberListener(LazyText.this);
    this.textArea.addCaretListener(lForCurrentLine);

    // Initializes DocumentListener
    DocumentListener lForNewLine = new NewLineListener(LazyText.this);

    this.textArea.getDocument().addDocumentListener(lForNewLine);
    this.textArea.getDocument().addUndoableEditListener(manager);
  }

  private void initializeTextArea() {
    Color selectionBgColor = new Color(72, 71, 61);
    Theme.themeTextAreaSelectedTextColor(selectionBgColor, Color.WHITE);

    // TextArea
    this.textArea = new JTextArea();

    this.initializeLinedNumberArea();

    this.addTextAreaListeners();

    this.textArea.setMargin(new Insets(4, 15, 6, 4));

    this.textArea.setLineWrap(false);
    this.textArea.setWrapStyleWord(true);

    this.scrollableTextArea =
        new JScrollPane(this.textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    this.scrollableTextArea.setRowHeaderView(this.textAreaLineNumbers);

    this.scrollableTextArea.setBorder(null);

    // Add textArea to the frame
    this.mainWindow.add(scrollableTextArea);
  } // End of initializeTextArea

  private JTextPane createStatusBarTextPane(String initialValue, int width,
                                            int height) {
    /* Displays the current caret position */
    Dimension widthHeight = new Dimension(width, height);
    JTextPane caretPane = new JTextPane();
    caretPane.setPreferredSize(widthHeight);
    caretPane.setMaximumSize(widthHeight);
    caretPane.setMinimumSize(widthHeight);
    caretPane.setFocusable(false);
    caretPane.setEditable(false);
    caretPane.setText(initialValue);

    return caretPane;
  } // End of lineNumberPane

  private void initializeStatusBarComponents() {
    this.statusBar.add(
        this.createStatusBarTextPane("Line: 1  Column: 1", 200, 20));
    this.statusBar.add(Box.createGlue());
    this.statusBar.add(this.createStatusBarTextPane("TXT", 65, 20));
    this.statusBar.add(this.createStatusBarTextPane("UTF8", 60, 20));
  } // End of initializeStatusBarComponents

  private void initializeStatusBar() {
    this.statusBar = new JToolBar();
    this.statusBar.setFloatable(false);
    this.statusBar.setRollover(true);
    this.statusBar.setBorderPainted(false);
    this.initializeStatusBarComponents();
    this.mainWindow.add(this.statusBar, BorderLayout.SOUTH);
  } // End of initializeStatusBar

  public void undoRedo(String undoOrdRedo) {
    try {
      switch (undoOrdRedo) {
      case "undo":
        LazyText.this.manager.undo();
        break;
      case "redo":
        LazyText.this.manager.redo();
      }
    } catch (CannotUndoException | CannotRedoException ignored) {
    }
  } // End of undoRedo

  private void setKeyMappings() {
    for (int i = 0; i < commands.length; i++) {
      this.keyMappings.put(this.commands[i], this.keyCodes[i]);
    }
  } // End of setKeyMappings

  private void setAccelerator(int keyCode, JMenuItem newMenuItem) {
    newMenuItem.setAccelerator(
        KeyStroke.getKeyStroke(keyCode, ActionEvent.CTRL_MASK));

  } // End of setAccelerator

  private void setShorcuts(String command, JMenuItem newMenuItem) {
    switch (command) {
    case "New":
    case "Open File":
    case "Save":
    case "Close File":
    case "Save As":
    case "Exit":
    case "Undo":
    case "Redo":
    case "Cut":
    case "Copy":
    case "Paste":
    case "Find":
    case "Replace":
    case "Timestamp":
    case "Themes":
      setAccelerator(this.keyMappings.get(command), newMenuItem);
      break;
    }
  } // End of setShortcuts

  private JMenuItem createMenuItem(String menuName) {

    // Creates menu item
    JMenuItem newMenuItem = new JMenuItem(menuName);

    this.setShorcuts(menuName, newMenuItem);

    newMenuItem.setBorderPainted(false);

    newMenuItem.setOpaque(true);

    newMenuItem.setActionCommand(menuName);

    newMenuItem.addActionListener(this.menuItemListener);

    return newMenuItem;

  } // End of createMenuItem

  private JMenu createMenu(String menuName, int start, int length) {
    JMenu newMenu = new JMenu(menuName);
    newMenu.getPopupMenu().setBorderPainted(false);
    newMenu.getPopupMenu().setOpaque(true);
    newMenu.getPopupMenu().setBorder(null);
    newMenu.setBorderPainted(false);
    newMenu.setOpaque(true);

    for (int i = start; i < length; i++) {
      newMenu.add(this.createMenuItem(this.commands[i]));
    }

    return newMenu;
  } // End of createMenu

  private void initializeMenu() {
    // Create a menu bar
    this.menuBar = new JMenuBar();

    this.menuBar.setOpaque(true);

    this.menuBar.setBorderPainted(false);

    this.menuBar.setBorder(null);

    // Add menu to menubar
    menuBar.add(createMenu("File", 0, 7));

    menuBar.add(createMenu("Edit", 7, 15));

    menuBar.add(createMenu("Help", 15, 16));

    // Add menubar to the frame
    this.mainWindow.setJMenuBar(menuBar);
  } // End of initializeMenu

  // Getters
  public String getVersionNumber() { return this.versionNumber; }

  public String getCurrentThemeName() { return this.currentTheme; }

  public JFrame getFrame() { return this.mainWindow; }

  public Theme getThemeObject() { return this.lazyTextTheme; }

  public JMenuBar getMenuBar() { return this.menuBar; }

  public String getLastSearchedValue() { return this.lastSearchedValue; }

  public JScrollPane getScrollPane() { return this.scrollableTextArea; }

  public JTextArea getLinedNumberArea() { return this.textAreaLineNumbers; }

  public JTextArea getTextArea() { return this.textArea; }

  public JToolBar getStatusBar() { return this.statusBar; }

  public Component getStatusBarComponent(int index) {
    if (index >= 0 || index <= this.statusBar.getComponentCount()) {
      return this.statusBar.getComponentAtIndex(index);
    }

    return null;
  }
  // End of Getters

  // Setters
  public void setLastSearchedValue(String word) {
    this.lastSearchedValue = word;
  }

  public void setCurrentThemeName(String currentTheme) {
    this.currentTheme = currentTheme;
  }
  // End of Setters

} // End of LazyText
