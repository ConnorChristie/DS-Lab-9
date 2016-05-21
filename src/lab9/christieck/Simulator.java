package lab9.christieck;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Simulator extends JFrame
{
    private DNS dnsServer;

    private JButton startButton;
    private JButton stopButton;
    private JButton updateButton;

    private JTextField domainNameField;
    private JTextField ipAddressField;

    private JButton addButton;
    private JButton deleteButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton exitButton;

    private static final String ENTRIES_FILE = "dnsentries.txt";
    private static final String UPDATE_FILE = "updates.txt";

    public Simulator()
    {
        dnsServer = new DNS(ENTRIES_FILE);

        setTitle("Domain Name System");
        setSize(400, 200);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addProgramButtons();
        addTextBoxes();
        addActionButtons();

        enableButtons(false);
    }

    /**
     * Adds the start, stop and update buttons to the GUI
     */
    private void addProgramButtons()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        updateButton = new JButton("Update");

        startButton.addActionListener(this::onStartButtonClick);
        stopButton.addActionListener(this::onStopButtonClick);
        updateButton.addActionListener(this::onUpdateButtonClick);

        panel.add(startButton);
        panel.add(stopButton);
        panel.add(updateButton);

        add(panel);
    }

    /**
     * Adds the text boxes to the GUI
     */
    private void addTextBoxes()
    {
        JPanel dnPanel = new JPanel();
        JPanel ipPanel = new JPanel();

        dnPanel.setLayout(new FlowLayout());
        ipPanel.setLayout(new FlowLayout());

        JLabel dnLabel = new JLabel("Domain Name: ");
        JLabel ipLabel = new JLabel("IP Address: ");

        domainNameField = new JTextField();
        ipAddressField = new JTextField();

        domainNameField.setPreferredSize(new Dimension(250, 20));
        ipAddressField.setPreferredSize(new Dimension(200, 20));

        domainNameField.addKeyListener(new KeyListener()
        {
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { onDomainNameFieldKeyPress(e); }
            public void keyReleased(KeyEvent e) { }
        });

        dnPanel.add(dnLabel);
        dnPanel.add(domainNameField);

        ipPanel.add(ipLabel);
        ipPanel.add(ipAddressField);

        add(dnPanel);
        add(ipPanel);
    }

    /**
     * Adds the action buttons to the GUI
     */
    private void addActionButtons()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        exitButton = new JButton("Exit");

        addButton.addActionListener(this::onAddButtonClick);
        deleteButton.addActionListener(this::onDeleteButtonClick);
        undoButton.addActionListener(this::onUndoButtonClick);
        redoButton.addActionListener(this::onRedoButtonClick);
        exitButton.addActionListener(e -> { dnsServer.stop(); System.exit(0); });

        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(undoButton);
        panel.add(redoButton);
        panel.add(exitButton);

        add(panel);
    }

    /**
     * The start button action handler
     *
     * @param e The action
     */
    private void onStartButtonClick(ActionEvent e)
    {
        boolean dnsStarted = dnsServer.start();

        if (dnsStarted)
        {
            enableButtons(true);
        } else
        {
            showError("The DNS server was not able to be started");
        }
    }

    /**
     * The stop button action handler
     *
     * @param e The action
     */
    private void onStopButtonClick(ActionEvent e)
    {
        boolean dnsStopped = dnsServer.stop();

        if (dnsStopped)
        {
            enableButtons(false);
        } else
        {
            showError("The DNS server was not able to be stopped");
        }
    }

    /**
     * The update button action handler
     *
     * @param e The action
     */
    private void onUpdateButtonClick(ActionEvent e)
    {
        try (Scanner in = new Scanner(new File(UPDATE_FILE)))
        {
            while (in.hasNextLine())
            {
                try
                {
                    dnsServer.update(in.nextLine());
                } catch (InputMismatchException | IllegalArgumentException ex)
                {
                    showError(ex.getMessage());
                }
            }
        } catch (FileNotFoundException e1)
        {
            showError("The DNS updates file could not be found");
        }
    }

    /**
     * The key press action for the domain name field
     *
     * @param e The key event
     */
    private void onDomainNameFieldKeyPress(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            try
            {
                String domain = domainNameField.getText();
                IPAddress address = dnsServer.lookup(new DomainName(domain));

                String ipAddress = address != null ? address.toString() : "Not found";

                showMessage("IP Address Lookup", "IP Address: " + ipAddress);
            } catch (IllegalArgumentException ex)
            {
                showError(ex.getMessage());
            }
        }
    }

    /**
     * The add button action handler
     *
     * @param e The action
     */
    private void onAddButtonClick(ActionEvent e)
    {
        try
        {
            Pair<DomainName, IPAddress> domainAddress = getDomainAddressEntry();

            dnsServer.add(domainAddress.getKey(), domainAddress.getValue());

            showMessage("Success", "Successfully added the DNS record");
        } catch (IllegalArgumentException ex)
        {
            showError(ex.getMessage());
        }
    }

    /**
     * The delete button action handler
     *
     * @param e The action
     */
    private void onDeleteButtonClick(ActionEvent e)
    {
        try
        {
            Pair<DomainName, IPAddress> domainAddress = getDomainAddressEntry();

            boolean success = dnsServer.delete(domainAddress.getKey(), domainAddress.getValue());

            if (success)
            {
                showMessage("Success", "Successfully deleted the DNS record");
            } else
            {
                showMessage("Failed", "Could not delete the specified DNS record");
            }
        } catch (IllegalArgumentException ex)
        {
            showError(ex.getMessage());
        }
    }

    /**
     * The undo button action handler
     *
     * @param e The action
     */
    private void onUndoButtonClick(ActionEvent e)
    {

    }

    /**
     * The redo button action handler
     *
     * @param e The action
     */
    private void onRedoButtonClick(ActionEvent e)
    {

    }

    /**
     * Enables the corresponding buttons that should be enabled when started
     *
     * @param started Whether the server has been started
     */
    private void enableButtons(boolean started)
    {
        startButton.setEnabled(!started);
        stopButton.setEnabled(started);
        updateButton.setEnabled(started);

        domainNameField.setEnabled(started);
        ipAddressField.setEnabled(started);

        addButton.setEnabled(started);
        deleteButton.setEnabled(started);
        undoButton.setEnabled(started);
        redoButton.setEnabled(started);
    }

    /**
     * Gets the entered domain name and IP address as a pair
     *
     * @return A pair of domain name and IP address
     */
    private Pair<DomainName, IPAddress> getDomainAddressEntry()
    {
        DomainName domain = new DomainName(domainNameField.getText());
        IPAddress address = new IPAddress(ipAddressField.getText());

        return new Pair<>(domain, address);
    }

    /**
     * Shows a message to the user with the specified title and message
     *
     * @param title The title of the option pane
     * @param message The message to show to the user
     */
    public static void showMessage(String title, String message)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an error message to the user
     *
     * @param message The message to display to the user
     */
    public static void showError(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args)
    {
        new Simulator().setVisible(true);
    }
}
