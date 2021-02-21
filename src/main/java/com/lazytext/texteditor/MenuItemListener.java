package com.lazytext.texteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuItemListener implements ActionListener {
  private final LazyText window;

  public MenuItemListener(LazyText window) {
    this.window = window;
  } // End of MenuItemListener Constructor

  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
    case "New":
      new LazyText();
      break;
    case "Open File":
      MenuBarUtils.OpenFile(this.window);
      break;
    case "Save":
      MenuBarUtils.SaveFile(this.window);
      break;
    case "Save As":
      MenuBarUtils.SaveFileAs(this.window);
      break;
    case "Close File":
      MenuBarUtils.closeFile(this.window);
      break;
    case "Exit":
      this.window.getFrame().dispose();
      break;
    case "Undo":
      this.window.undoRedo("undo");
      break;
    case "Redo":
      this.window.undoRedo("redo");
      break;
    case "Cut":
      MenuBarUtils.cutText(this.window.getTextArea());
      break;
    case "Copy":
      MenuBarUtils.copyText(this.window.getTextArea());
      break;
    case "Paste":
      MenuBarUtils.pasteText(this.window.getTextArea());
      break;
    case "Find":
      new FindText(this.window);
      break;
    case "Replace":
      new FindAndReplaceText(this.window);
      break;
    case "Timestamp":
      MenuBarUtils.appendDateTime(this.window.getTextArea());
      break;
    case "Themes":
      new ThemeChooser(this.window);
      break;
    case "About":
      MenuBarUtils.aboutUs(this.window.getVersionNumber());
      break;
    }
  }
} // End of MenuItemListener Class
