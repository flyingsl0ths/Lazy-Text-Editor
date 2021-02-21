package com.lazytext.texteditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ThemeChooser {
  private int x = 0;
  private int y = 0;
  private JPanel themePanel;
  private final DefaultListModel dListModel = new DefaultListModel();
  private JList availableThemes;
  private JScrollPane scrollPne;
  private JPopupMenu themeMenu;
  private final LazyText window;

  public ThemeChooser(LazyText window) {
    this.window = window;

    // Get available themes from themes/
    this.getThemes();

    // Load themes into JList
    this.loadThemes();

    // Initialize Panel to display Themes
    this.initializeThemePanel();

    // Initialize Theme Chooser Menu
    this.initializeThemeMenu();

    // Themes component based of current LazyText theme
    this.themeComponents();

    // Display Theme Chooser Menu
    this.displayThemeMenu();
  } // End of ThemeChooser Constructor

  private String extractThemeName(String themeFileName) {
    return themeFileName.substring(0, themeFileName.indexOf("."));
  } // End of extractThemeName

  private void getThemes() {
    this.window.getThemeObject().scanThemeDirectory();

    if (this.window.getThemeObject().getThemeFiles().size() != 0) {
      for (String theme : this.window.getThemeObject().getThemeFiles())
        dListModel.addElement(extractThemeName(theme));

    } else {
      dListModel.addElement("No themes available");
    }
  } // End of getThemes

  private void setJListConstraints() {
    // Define how many selections can be made
    this.availableThemes.setSelectionMode(
        ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    // Define the height of each cell
    this.availableThemes.setFixedCellHeight(40);

    // Define the width of each cell
    this.availableThemes.setFixedCellWidth(300);

    // Only display 4 items at a time
    this.availableThemes.setVisibleRowCount(5);
  } // End of setJListConstraints

  private void initializeScrollBars() {
    this.scrollPne = new JScrollPane(
        this.availableThemes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    this.scrollPne.setBorder(null);
  } // End of initializeScrollBars

  private void loadThemes() {
    this.availableThemes = new JList(dListModel);

    final ListenForClickedItem lForClickedItm = new ListenForClickedItem();
    this.availableThemes.addMouseListener(lForClickedItm);

    this.setJListConstraints();
    this.initializeScrollBars();
  } // End of loadThemes

  private void addComponentsToThemePanel() {
    this.themePanel.add(this.scrollPne);
  } // End of addComponentsToThemePanel

  private void initializeThemePanel() {
    this.themePanel = new JPanel();
    this.addComponentsToThemePanel();
  } // End of initializeThemePanel

  private void addComponentsToThemeMenu() {
    this.themeMenu.add(themePanel);
  } // End of addComponentsToThemeMenu

  private void initializeThemeMenu() {
    this.themeMenu = new JPopupMenu();
    this.themeMenu.setBorderPainted(false);
    this.themeMenu.setMaximumSize(new Dimension(100, 100));
    this.addComponentsToThemeMenu();
  } // End of initializeThemeMenu

  private void themeListScrollBarTheme(Color scrollBarThumbColor,
                                       Color bgColor) {
    Theme.themeScrollBar(this.scrollPne, scrollBarThumbColor, bgColor);
  }

  private void themeListTheme(Color fgColor, Color bgColor) {
    this.availableThemes.setForeground(fgColor);
    this.availableThemes.setBackground(bgColor);
  } // End of themeListTheme

  private void themePanelTheme(Color fgColor, Color bgColor) {
    this.themePanel.setForeground(fgColor);
    this.themePanel.setBackground(bgColor);
  } // End of themePanelTheme

  private void themeMenuTheme(Color bgColor) {
    this.themeMenu.setBackground(bgColor);
  } // End of themeMenuTheme

  private void themeComponents() {
    String[] colorValues =
        this.window.getThemeObject().getParsedThemeValues("themeChooserMenu");
    this.themeListTheme(Theme.getColorValue(colorValues[1]),
                        Theme.getColorValue(colorValues[0]));
    this.themeListScrollBarTheme(Theme.getColorValue(colorValues[1]),
                                 Theme.getColorValue(colorValues[0]));
    this.themePanelTheme(Theme.getColorValue(colorValues[1]),
                         Theme.getColorValue(colorValues[0]));
    this.themeMenuTheme(Theme.getColorValue(colorValues[0]));
  } // End of themeComponents

  private void setInitialCoordinates() {
    // Handles where the PopUpMenu should be displayed on the X/Y-axis
    double searchBoxLocationX = this.window.getFrame().getWidth();
    this.x = (int)(searchBoxLocationX / 2.0) - 150;
    this.y = this.window.getMenuBar().getHeight();

  } // End of assignXYCoordinates

  private void displayThemeMenu() {
    this.setInitialCoordinates();
    this.themeMenu.show(this.window.getFrame(), this.x, this.y);
  } // End of displayThemeMenu

  private void changeTheme(String theme) {

    if (!this.window.getCurrentThemeName().equals(theme)) {
      this.window.getThemeObject().buildTheme(theme);
      this.themeComponents();
    }
  } // End of changeTheme

  private class ListenForClickedItem extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      String selectedTheme = availableThemes.getSelectedValue() + ".xml";
      changeTheme(selectedTheme);
    }
  } // End of ListenForClickedItem

} // End of ThemeChooser