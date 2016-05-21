package lab9.christieck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Simulator extends JFrame
{
    private DNS dnsServer;

    private JTextField domainNameField;
    private JTextField ipAddressField;

    public Simulator()
    {
        dnsServer = new DNS();

        setTitle("Domain Name System");
        setSize(400, 200);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addProgramButtons();
        addTextBoxes();
        addActionButtons();
    }

    /**
     * Adds the start, stop and update buttons to the GUI
     */
    private void addProgramButtons()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button updateButton = new Button("Update");

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

        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button exitButton = new Button("Exit");

        addButton.addActionListener(this::onAddButtonClick);
        deleteButton.addActionListener(this::onDeleteButtonClick);
        undoButton.addActionListener(this::onUndoButtonClick);
        redoButton.addActionListener(this::onRedoButtonClick);
        exitButton.addActionListener(e -> System.exit(0));

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

    }

    /**
     * The stop button action handler
     *
     * @param e The action
     */
    private void onStopButtonClick(ActionEvent e)
    {

    }

    /**
     * The update button action handler
     *
     * @param e The action
     */
    private void onUpdateButtonClick(ActionEvent e)
    {

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
            dnsServer.add(new DomainName(domainNameField.getText()));
        } catch (IllegalArgumentException ex)
        {

        }
    }

    /**
     * The delete button action handler
     *
     * @param e The action
     */
    private void onDeleteButtonClick(ActionEvent e)
    {

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

    public static void main(String[] args)
    {
        new Simulator().setVisible(true);
    }
}
