package com.lazytext.texteditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Theme {
  private final ArrayList<String> themeFiles = new ArrayList<>();
  private final Map<String, String[]> themeValues = new HashMap<>();
  public static Map<String, String[]> customComponentValues = new HashMap<>();
  private final LazyText window;
  private final String themeDirectory = "src/main/resources/themes/";
  private PrintWriter writer = null;

  public Theme(LazyText window) {
    this.window = window;
  } // End of Theme Constructor

  public void addThemeToThemeFiles(File[] foundThemes) {
    // Loop through array to add contents to Map
    if (foundThemes != null) {
      for (File f : foundThemes) {
        String currentFile = f.getName();
        if (!themeFiles.contains(currentFile) & currentFile.contains("xml")) {
          themeFiles.add(currentFile);
        }
      }
    }
  } // End of addThemesToThemeFiles

  public void scanThemeDirectory() {
    // default directory where Theme class will scan for theme.xml files
    File themeDir = new File(themeDirectory);

    // Create array with files from themes folder
    if (themeDir.isDirectory()) {
      File[] foundThemes = themeDir.listFiles();

      addThemeToThemeFiles(foundThemes);
    }
  } // End of scanThemeDirectory

  public ArrayList<String> getThemeFiles() {
    return this.themeFiles;
  } // End of getThemeFiles

  public String[] getParsedThemeValues(String key) {
    return themeValues.get(key);
  } // End of getParsedThemeValues

  public static String[] getCustomComponentParsedThemeValues(String key) {
    return customComponentValues.get(key);
  } // End of getParsedThemeValues

  public static void themeTextAreaSelectedTextColor(Color bgColor,
                                                    Color fgColor) {
    UIManager.put("TextArea.selectionBackground", bgColor);
    UIManager.put("TextArea.selectionForeground", fgColor);
  } // End of themeTextAreaSelectedTextColor

  public static void themeTextArea(JTextArea textArea, Color fgColor,
                                   Color bgColor) {
    textArea.setForeground(fgColor);
    textArea.setBackground(bgColor);
  } // End of themeTextArea

  public static void themeTextAreaCaret(JTextArea textArea, Color caretColor) {
    textArea.setCaretColor(caretColor);
  } // End of themeTextAreaCaret

  public static JButton createZeroButton() {
    JButton button = new JButton();
    button.setContentAreaFilled(false);
    button.setPreferredSize(new Dimension(0, 0));
    button.setMinimumSize(new Dimension(0, 0));
    button.setMaximumSize(new Dimension(0, 0));
    return button;
  } // End of createZeroButton

  public static JButton cornerButtons(Color bgColor) {
    JButton button = new JButton();
    button.setPreferredSize(new Dimension(0, 0));
    button.setMinimumSize(new Dimension(0, 0));
    button.setMaximumSize(new Dimension(0, 0));
    button.setBackground(bgColor);
    button.setEnabled(false);
    button.setBorder(null);

    return button;
  } // End of cornerButtons

  public static void fillScrollBarCorners(JScrollPane scrollBr, Color bgColor) {
    scrollBr.setCorner(JScrollPane.LOWER_LEFT_CORNER, cornerButtons(bgColor));
    scrollBr.setCorner(JScrollPane.LOWER_RIGHT_CORNER, cornerButtons(bgColor));
  } // End of fillScrollBarCorners

  public static void themeVerticalScrollBar(JScrollPane scrollPane,
                                            final Color scrollBarThumbColor) {
    scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
      protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
      }

      protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
      }

      protected void configureScrollBarColors() {
        this.thumbColor = scrollBarThumbColor;
        this.scrollBarWidth = 10;
        this.decrGap = 0;
        this.incrGap = 0;
      }
    });
  } // End of themeVerticalScrollBar

  public static void themeHorizontalScrollBar(JScrollPane scrollPane,
                                              final Color scrollBarThumbColor) {
    scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
      protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
      }

      protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
      }

      protected void configureScrollBarColors() {
        this.thumbColor = scrollBarThumbColor;
        this.scrollBarWidth = 10;
        this.decrGap = 0;
        this.incrGap = 0;
      }
    });
  } // End of themeHorizontalScrollBar

  public static void themeScrollBar(JScrollPane scrollPane,
                                    Color scrollBarThumbColor, Color bgColor) {
    themeVerticalScrollBar(scrollPane, scrollBarThumbColor);
    themeHorizontalScrollBar(scrollPane, scrollBarThumbColor);

    scrollPane.getVerticalScrollBar().setBackground(bgColor);
    scrollPane.getVerticalScrollBar().setBorder(null);

    scrollPane.getHorizontalScrollBar().setBackground(bgColor);
    scrollPane.getHorizontalScrollBar().setBorder(null);

    fillScrollBarCorners(scrollPane, bgColor);
  } // End of themeScrollBar

  public static void themeStatusBarComponents(JToolBar statusBar, Color fgColor,
                                              Color bgColor) {
    int numberOfComponents = statusBar.getComponentCount();
    for (int i = 0; i < numberOfComponents; i++) {
      statusBar.getComponentAtIndex(i).setForeground(fgColor);
      statusBar.getComponentAtIndex(i).setBackground(bgColor);
    }
  } // End of themeStatusBarComponents

  public static void themeStatusBar(JToolBar statusBar, Color fgColor,
                                    Color bgColor) {
    statusBar.setBackground(bgColor);
    themeStatusBarComponents(statusBar, fgColor, bgColor);
  } // End of themeStatusBar

  public static void themeMenuBarItems(JMenuBar menuBar, Color fgColor,
                                       Color bgColor) {
    for (int i = 0, l = menuBar.getMenuCount(); i < l; i++) {
      JMenu menu = menuBar.getMenu(i);
      menu.setForeground(fgColor);
      menu.setBackground(bgColor);
      for (int j = 0, l2 = menu.getMenuComponentCount(); j < l2; j++) {
        java.awt.Component comp = menu.getMenuComponent(j);
        if (comp instanceof JMenuItem) {
          JMenuItem menuItem = (JMenuItem)comp;
          menuItem.setForeground(fgColor);
          menuItem.setBackground(bgColor);
        }
      }
    }
  } // End of themeMenuBarItems

  public static void themeMenuBar(JMenuBar menuBar, Color fgColor,
                                  Color bgColor) {
    menuBar.setBackground(bgColor);

    themeMenuBarItems(menuBar, fgColor, bgColor);
  } // End of themeMenuBar

  public static void changeMenuBarFont(JMenuBar menuBar, Font menuFont) {
    for (int i = 0; i < menuBar.getMenuCount(); i++) {
      JMenu menu = menuBar.getMenu(i);
      menu.setFont(menuFont);
      for (int j = 0; j < menu.getMenuComponentCount(); j++) {
        java.awt.Component comp = menu.getMenuComponent(j);
        if (comp instanceof JMenuItem) {
          JMenuItem menuItem = (JMenuItem)comp;
          menuItem.setFont(menuFont);
        }
      }
    }
  } // End of changeMenuBarFont

  public static void changeStatusBarFont(JToolBar statusBar,
                                         Font statusBarFont) {
    int numberOfComponents = statusBar.getComponentCount();
    for (int i = 0; i < numberOfComponents; i++) {
      statusBar.getComponentAtIndex(i).setFont(statusBarFont);
    }
  } // End of themeStatusBarComponents

  public void changeFont(Font menuBarFont, Font textAreaFont,
                         Font statusBarFont) {
    changeMenuBarFont(this.window.getMenuBar(), menuBarFont);

    this.window.getLinedNumberArea().setFont(textAreaFont);

    this.window.getTextArea().setFont(textAreaFont);

    changeStatusBarFont(this.window.getStatusBar(), statusBarFont);

  } // End of changeFont

  private Document createDocument(String documentName) {

    try {

      // API used to convert XML into a DOM object tree
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      // Ignore all of the comments
      factory.setIgnoringComments(true);

      // Ignore white space in elements
      factory.setIgnoringElementContentWhitespace(true);

      // Validate XML document
      factory.setValidating(true);

      // Provides access to the documents data
      DocumentBuilder builder = factory.newDocumentBuilder();

      // Takes the document
      return builder.parse(new InputSource(documentName));

    } catch (Exception ex) {

      System.out.println(ex.getMessage());
    }

    return null;
  } // End of createDocument

  private void assignValuesToMap(Node currentNode) {
    if (currentNode.getNodeName().equals("components")) {
      customComponentValues.put(currentNode.getTextContent(),
                                getAllAttributes(currentNode));
    } else {
      themeValues.put(currentNode.getNodeName(), getAllAttributes(currentNode));
    }
  } // End of assignValuesToMap

  private String[] getAllAttributes(Node i) {
    if (i.hasAttributes()) {
      NamedNodeMap attrs = i.getAttributes();
      String[] values = new String[attrs.getLength()];
      for (int j = 0, l = attrs.getLength(); j < l; j++) {
        values[j] = "" + attrs.item(j).getNodeValue();
      }

      return values;
    } else {
      return null;
    }
  } // End of getAllAttributes

  private void parseXMLValues(Node node) {

    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        assignValuesToMap(currentNode);
        parseXMLValues(currentNode);
      }
    }
  } // End of parseXMLValues

  private void parseThemeValues(String themeFile) {
    // Parses the file supplied as Document
    Document themeTemplate = createDocument(themeDirectory + themeFile);

    // Set the name of the newly loaded theme
    this.window.setCurrentThemeName(themeFile);

    assert themeTemplate != null;
    NodeList children = themeTemplate.getChildNodes();

    for (int i = 0, l = children.getLength(); i < l; i++) {
      parseXMLValues(children.item(i));
    }
  } // End of parseThemeValues

  public void getThemeValues(String themeFile) throws IOException {
    this.parseThemeValues(themeFile);
  }
  // End of getThemeValues

  public int countMissingValues(String[] values) {
    int missingValues = 0;
    for (String string : values) {
      if (string.isEmpty()) {
        missingValues++;
      }
    }
    return missingValues;
  } // End of countMissingValues

  public boolean checkForEmptyValues(String[] values) {
    int missingValues;
    if (values != null) {
      missingValues = countMissingValues(values);
      return missingValues == values.length;
    } else {
      return true;
    }
  } // Enf of checkForEmptyValues

  public void applyFontValues() {
    String[] fontValues = themeValues.get("baseFont");
    Font[] windowFonts = new Font[3];

    boolean checkValues = checkForEmptyValues(fontValues);
    if (!checkValues) {
      windowFonts[0] = new Font(fontValues[0], Font.PLAIN,
                                Integer.parseInt(fontValues[1]) - 11);
      windowFonts[1] =
          new Font(fontValues[0], Font.PLAIN, Integer.parseInt(fontValues[1]));
      windowFonts[2] = new Font(fontValues[0], Font.PLAIN,
                                Integer.parseInt(fontValues[1]) - 11);
    } else {
      windowFonts[0] = new Font("Sans-Serif", Font.PLAIN, 15);
      windowFonts[1] = new Font("Sans-Serif", Font.PLAIN, 26);
      windowFonts[2] = new Font("Sans-Serif", Font.PLAIN, 15);
    }

    this.changeFont(windowFonts[0], windowFonts[1], windowFonts[2]);
  } // End of applyFontValues

  public static Color getColorValue(String colorString) {
    return Color.decode(colorString);
  } // End of getColorValue

  public void applyMenuValues() {
    themeMenuBar(this.window.getMenuBar(),
                 getColorValue(themeValues.get("menuBar")[1]),
                 getColorValue(themeValues.get("menuBar")[0]));
  } // End of applyMenuValues

  public void applyTextAreaValues() {

    themeScrollBar(this.window.getScrollPane(),
                   getColorValue(themeValues.get("textAreaScrollBars")[1]),
                   getColorValue(themeValues.get("textAreaScrollBars")[0]));

    themeTextArea(this.window.getLinedNumberArea(),
                  getColorValue(themeValues.get("lineNumberArea")[1]),
                  getColorValue(themeValues.get("lineNumberArea")[0]));

    themeTextArea(this.window.getTextArea(),
                  getColorValue(themeValues.get("textArea")[2]),
                  getColorValue(themeValues.get("textArea")[0]));

    themeTextAreaCaret(this.window.getTextArea(),
                       getColorValue(themeValues.get("textArea")[1]));
  } // End of applyTextAreaValues

  public void applyStatusBarValues() {
    themeStatusBar(this.window.getStatusBar(),
                   getColorValue(themeValues.get("statusBar")[1]),
                   getColorValue(themeValues.get("statusBar")[0]));
  } // End of applyStatusBarValues

  private void writeLastThemeUsed(String themeName) throws IOException {
    writer = new PrintWriter(this.themeDirectory + "last_used_theme.txt");
    writer.print("");
    writer.append(themeName);
    writer.close();
  } // End of writeLastThemeUsed

  public void buildTheme(String themeFile) {
    String previousTheme = this.window.getCurrentThemeName();

    try {

      this.getThemeValues(themeFile);

      this.applyFontValues();

      this.applyMenuValues();

      this.applyTextAreaValues();

      this.applyStatusBarValues();

      this.writeLastThemeUsed(themeFile);
    } catch (IOException | NumberFormatException |
             ArrayIndexOutOfBoundsException e) {
      this.buildTheme(previousTheme);
    }
  } // End of buildTheme

} // End of Theme Class
