package com.lazytext.texteditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTextPane;

public class MenuBarUtils {
    public static int errorMsg = JOptionPane.ERROR_MESSAGE;
    public static int infoMsg = JOptionPane.INFORMATION_MESSAGE;

    public static void alertUser(String message, String messageType, int jOptionPaneConst) {
        if (jOptionPaneConst == errorMsg) {
            JOptionPane.showMessageDialog(null, message, messageType, jOptionPaneConst,
                    new ImageIcon("src/main/resources/info_icon.png"));
        } else if (jOptionPaneConst == infoMsg) {
            JOptionPane.showMessageDialog(null, message, messageType, jOptionPaneConst,
                    new ImageIcon("src/main/resources/error_icon.png"));
        }

    }

    public static void readFile(File aFile, JTextArea textArea) throws IOException {

        BufferedReader getInfo = new BufferedReader(new FileReader(aFile));

        // Reads a whole line from the file and saves it in a String
        String line = getInfo.readLine();

        // readLine returns null when the end of the file is reached
        while (line != null) {
            textArea.append(line + "\n");

            line = getInfo.readLine();

        }

        getInfo.close();

    } // end of openFile()

    public static void setStatusBarComponentStatus(JToolBar statusBar, int index, String message) {
        ((JTextPane) statusBar.getComponentAtIndex(index)).setText(message);
    } // End of setStatusBarComponentStatus

    public static void reloadFile(LazyText window, File openedFile) throws IOException {
        window.getTextArea().setText(null);
        window.getFrame().setTitle(openedFile.getCanonicalPath() + " - LazyText");
        setStatusBarComponentStatus(window.getStatusBar(), 2, extractFileExtension(openedFile.getName()));
        setStatusBarComponentStatus(window.getStatusBar(), 3, extractFileEncoding(openedFile));
        readFile(openedFile, window.getTextArea());
    } // End of reloadFile

    public static void writeToFile(String outputPath, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        writer.write(content);
        writer.close();
    } // End of writeToFile

    public static String extractFileExtension(String fileName) {
        return fileName.substring((fileName.indexOf(".") + 1)).toUpperCase();
    }

    public static String extractFileEncoding(File f) {
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
            String fileEnc = reader.getEncoding();
            reader.close();
            return fileEnc;
        } catch (IOException e) {
            return "";
        }
    } // End of extractFileEncoding

    public static void closeFile(LazyText window) {
        if (!window.getFrame().getTitle().equals("LazyText")) {
            window.getFrame().setTitle("LazyText");
            window.getTextArea().setText(null);
            setStatusBarComponentStatus(window.getStatusBar(), 2, "Text");
            setStatusBarComponentStatus(window.getStatusBar(), 3, "UTF8");
        }
    } // End of closeFile

    public static void OpenFile(LazyText window) {
        JFileChooser filePicker = new JFileChooser();
        int returnValue = filePicker.showOpenDialog(window.getFrame());

        if (returnValue == JFileChooser.APPROVE_OPTION) {

            try {
                File file = filePicker.getSelectedFile();
                reloadFile(window, file);

            } catch (FileNotFoundException e) {
                alertUser("File Not Found", "Error", errorMsg);
                closeFile(window);
            } catch (IOException e) {
                alertUser("An Unknown Error Occurred", "Error", errorMsg);
                System.exit(0);
            }

        } else {
            window.getFrame().setTitle("LazyText");
        }

        window.getFrame().setVisible(true);
    } // End of OpenFile

    public static void SaveFileAs(LazyText window) {
        JFileChooser filePicker = new JFileChooser();
        int returnValue = filePicker.showSaveDialog(window.getFrame());

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File openedFile = filePicker.getSelectedFile();
                writeToFile(openedFile.getCanonicalPath(), window.getTextArea().getText());
                reloadFile(window, openedFile);

            } catch (IOException e) {
                alertUser("An Unknown Error Occurred", "Error", errorMsg);
                System.exit(0);
            }

        }

        window.getFrame().setVisible(true);
    } // End of SaveFileAs

    public static String extractFileName(String windowTitle) {
        return windowTitle.split(" ")[0];
    }

    public static void SaveFile(LazyText window) {
        try {

            if (!window.getFrame().getTitle().equals("LazyText")) {
                String openedFile = extractFileName(window.getFrame().getTitle());
                writeToFile(openedFile, window.getTextArea().getText());
            } else {
                SaveFileAs(window);
            }

        } catch (IOException ignored) {
        }

    } // End of SaveFile

    public static void cutText(JTextArea textField) {
        textField.replaceSelection("");
    } // End of cutText();

    public static void copyText(JTextArea textField) {
        textField.getSelectedText();

    } // End of copyText

    public static void pasteText(JTextArea textField) {
        textField.append(textField.getSelectedText());
    } // End of pasteText

    public static String getDateTime() {
        // A Date object contains date and time data
        Date rightNow = new Date();

        // DateFormat Type
        int format = DateFormat.DEFAULT;

        /*
         * DateFormat allows you to define dates / times using predefined styles
         * DEFAULT, SHORT, MEDIUM, LONG, or FULL
         */
        DateFormat dateFormatter = DateFormat.getDateInstance(format);

        // getTimeInstance only outputs time information
        DateFormat timeFormatter = DateFormat.getTimeInstance(format);

        String dateOutput = dateFormatter.format(rightNow);
        String timeOutput = timeFormatter.format(rightNow);

        return timeOutput + " " + dateOutput;
    }

    public static void appendDateTime(JTextArea textField) {
        String currentDateTime = getDateTime();

        textField.append(currentDateTime);
    } // End of appendDateTime

    public static void aboutUs(String versionNumber) {

        String aboutMessage = "LazyText Editor\nVersion " + versionNumber;

        alertUser(aboutMessage, "LazyText", infoMsg);
    } // End of aboutUs();

} // End of MenuBarFunctions
