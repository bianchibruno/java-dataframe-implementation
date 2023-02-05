import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.IOException;

public class GUI extends JFrame{

    private Model model;
    private JPanel panel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel bottomPanel;
    private JButton loadButton;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JFileChooser fileChooser;
    private JTextField searchBar;
    private JButton searchButton;
    private JButton specialSearchButton;

    private JComboBox dropdownMenu;


    public GUI() { // creates GUI at welcome page.
        createEmptyGUI();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setVisible(true);
    }

    public GUI(Model model) { //using the model, displays a new GUI loading the file specified in the model

        this.model = model;
        createGUI();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setVisible(true);

    }

    public void createGUI() {
        setTitle("uwuDatabaseHandler");
        createBottomPanel();
        addSearch();
        createMainPanel();
    }

    public void createEmptyGUI(){ // welcome page
        setTitle("uwuDatabaseHandler");
        createBottomPanel();
        createWelcomeMainPanel();
    }

    public void addSearch() { //adds the search bar, search button and dropdown menu to the bottom panel
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(150,25));
        bottomPanel.add(separator);

        searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(400,25));
        bottomPanel.add(searchBar);

        searchButton = new JButton("Search");
        searchButton.addActionListener((ActionEvent e) -> search());
        bottomPanel.add(searchButton);

        JSeparator separator1 = new JSeparator();
        separator1.setPreferredSize(new Dimension(50, 25));
        bottomPanel.add(separator1);

        JLabel dropdownLabel = new JLabel("Special filters:  ");
        bottomPanel.add(dropdownLabel);

        String[] choices = {"Oldest Person"};
        dropdownMenu = new JComboBox<String>(choices);
        bottomPanel.add(dropdownMenu);

        specialSearchButton = new JButton("Search");
        specialSearchButton.addActionListener((ActionEvent e) -> specialSearch());
        bottomPanel.add(specialSearchButton);
    }

    private void specialSearch() { // used to call different types of special searches
        int index = dropdownMenu.getSelectedIndex();
        switch (index){
            case 0:
                loadData(model.getOldestPerson());
        }
    }

    public void search(){
        loadData(searchBar.getText());
    }

    private void createWelcomeMainPanel() { //simple welcome page
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome! Load a .csv file to start");

        panel.add(welcomeLabel);
        panel.add(bottomPanel);

        panel.setPreferredSize(new Dimension(500,300));
        add(panel);

    }

    private void createBottomPanel() { //bottom panel with load button
        loadButton = new JButton("Load...");
        loadButton.setSize(new Dimension(50, 20));
        loadButton.addActionListener((ActionEvent e) -> load());

        bottomPanel = new JPanel();
        bottomPanel.add(loadButton);

    }


    public void createMainPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(leftPanel = loadHeadings(), BorderLayout.LINE_START);
        panel.add(rightPanel = loadData(), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.PAGE_END);

        add(panel);
    }

    public JPanel loadHeadings() { //used to display the name of each column on the left panel once a file has been loaded
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(150, 800));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (model != null) {
            for (String s : model.getListOfHeadings()) { // creates a checkbox with a listener for each of the column names from the model.
                JCheckBox checkBox = new JCheckBox(s);
                panel.add(checkBox);
                checkBox.setSelected(true);
                checkBox.addItemListener((ItemEvent e) -> hideColumn(e));
            }
        }
        return panel;
    }

    public JPanel loadData() { //used to display each of the lines of the csv on the main right panel
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1500, 800));
        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setLayout(new ScrollPaneLayout());
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane);

        if (model != null) {
            for (String s : model.getLines()) {
                textArea.append(s + "\n");
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    scrollPane.getVerticalScrollBar().setValue(0);
                } //once we un-tick a heading from the left panel we want the scrollbar to automatically go back to the top.
            });
        }
        return panel;
    }

    public void loadData(String searched) { //used to display lines that contain a "searched" string as a substring. used to implement the search bar.
        JPanel searchedPanel = new JPanel();
        searchedPanel.setPreferredSize(new Dimension(1500, 800));
        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setLayout(new ScrollPaneLayout());
        searchedPanel.setLayout(new BorderLayout());
        searchedPanel.add(scrollPane);

        if (model != null) {
            for (String s : model.searchLines(searched)) {
                    textArea.append(s + "\n");
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    scrollPane.getVerticalScrollBar().setValue(0);
                }
            });
        }
        panel.remove(rightPanel);
        rightPanel = searchedPanel;
        panel.add(rightPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();        // if we want to load a panel from our GUI, we must revalidate and repaint it to be shown to the user.
    }


    public void load() { //ran once load button is clicked, used to browse through files.
        fileChooser = new JFileChooser("."); //start browsing from the current folder (where this project is saved)
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv"); //restricts loading to only csv files.
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {    //if we choose a file from the file browser, we dispose the current GUI and create a new GUI from using the model that will load the csv file via the dataloader.
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println(filename);
            try {
                this.dispose();
                new DataLoader();
                new GUI(new Model(DataLoader.loadFile(filename)));
            } catch (IOException ioException) {
                System.out.println("File not found.");
                this.dispose();
                new DataLoader();
                new GUI();
            }
        }

    }

    private void hideColumn(ItemEvent e) { //hides all the entries of a column once we un-tick the heading from the left panel.
        JCheckBox checkBox = (JCheckBox) e.getSource();
        model.switchShowable(checkBox.getText());
        panel.remove(rightPanel);
        rightPanel = loadData();
        panel.add(rightPanel, BorderLayout.CENTER);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
        panel.revalidate();
        panel.repaint();
    }

}
