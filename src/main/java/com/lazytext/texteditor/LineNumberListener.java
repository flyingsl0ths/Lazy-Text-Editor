package com.lazytext.texteditor;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class LineNumberListener implements CaretListener {
    private final LazyText window;

    public LineNumberListener(LazyText window) {
        this.window = window;
    }
    // End of LineNumberListener Contructor

    private void updateCaretPaneText(int currentLine, int columnPosition) {
        ((JTextPane) this.window.getStatusBarComponent(0))
                .setText("Line: " + currentLine + "  Column: " + columnPosition);
    } // End of updateCaretPaneText

    @Override
    public void caretUpdate(CaretEvent e) {
        // Code source:
        // https://stackoverflow.com/questions/5139995/java-column-number-and-line-number-of-cursors-current-position
        // Start with some default values for the line and column.
        int lineNumber = 1;
        int columnNumber = 1;

        try {
            // First find the position of the caret. This is the number of where the caret
            // is in relation to the start of the JTextArea
            // in the upper left corner. We use this position to find offset values (eg what
            // line we are on for the given position as well as
            // what position that line starts on.
            int caretPosition = this.window.getTextArea().getCaretPosition();
            lineNumber = this.window.getTextArea().getLineOfOffset(caretPosition);

            // We subtract the offset of where our line starts from the overall caret
            // position.
            // So lets say that we are on line 5 and that line starts at caret position 100,
            // if our caret position is currently 106
            // we know that we must be on column 6 of line 5.
            columnNumber = caretPosition - this.window.getTextArea().getLineStartOffset(lineNumber);

            // We have to add one here because line/column numbers start at 0 for
            // getLineOfOffset and we want it to start at 1 for display.
            lineNumber += 1;
            columnNumber += 1;
        } catch (Exception ignored) {
        }

        updateCaretPaneText(lineNumber, columnNumber);
    } // End of caretUpdate

} // End of LineNumberListener Class
