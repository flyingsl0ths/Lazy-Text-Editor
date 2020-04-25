package com.lazytext.texteditor;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

public class NewLineListener implements DocumentListener {
    private final LazyText window;

    public NewLineListener(LazyText window) {
        this.window = window;
    }

    // Code obtained from
    // https://www.tutorialspoint.com/how-can-we-display-the-line-numbers-inside-a-jtextarea-in-java
    private String getLineNumber() {
        // Number of lines in the textArea
        int caretPosition = this.window.getTextArea().getDocument().getLength();

        Element root = this.window.getTextArea().getDocument().getDefaultRootElement();

        // Line number StringBuilder
        StringBuilder lineNumbersBuilder = new StringBuilder();
        // First line is always included since it's constant
        lineNumbersBuilder.append("1" + "\n");

        // Will always loop up to the current number of lines in the text area + 2 such
        // that
        // if number of lines is 3 root.getElementIndex(caretPosition) + 2 = 5 since
        // elementIndex always starts
        // from the second line in order to account for when there is only one line
        // displayed this will always be
        // 2 lines ahead from previous number of lines in order to add a new line such
        // that: 1 2 3 4 is added because 4 < 5
        for (int elementIndex = 2; elementIndex < root.getElementIndex(caretPosition) + 2; elementIndex++)
            lineNumbersBuilder.append(elementIndex).append("\n");

        // Length of lineNumbersBuilder will always by length of added
        // string(elementIndex) + 1
        // due to new line character being appended
        return lineNumbersBuilder.toString();
    }

    private void checkIfNewLineWasInserted() {
        String numberToBeAdded = getLineNumber();
        int currentlyDisplayedNumbers = window.getLinedNumberArea().getText().length();

        // Only adds line number when new line is added and not when typing on a line
        if (numberToBeAdded.length() != currentlyDisplayedNumbers) {
            window.getLinedNumberArea().setText(numberToBeAdded);
        }
    } // End of checkLineNumberBeforeAdding

    public void changedUpdate(DocumentEvent de) {
    }

    public void insertUpdate(DocumentEvent de) {
        this.checkIfNewLineWasInserted();
    }

    public void removeUpdate(DocumentEvent de) {
        this.checkIfNewLineWasInserted();
    }
}