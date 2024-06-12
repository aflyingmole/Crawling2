package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import data.SeleniumTest;
import data.DBConnection;
import data.URLDomain;
import user.Operator;

public class ButtonDemo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JFrame latestWindow; // Reference to the "최신순" window
    private JFrame dataFrame; // Data window instance variable

    public ButtonDemo() {
        // Frame setup
        setTitle("중앙 HD(hot deal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the frame
        setLayout(new GridLayout(4, 4, 10, 10)); // 2x2 grid layout



        // "로그아웃" button
        JButton logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logout
                dispose(); // Close current window
                if (latestWindow != null) {
                    latestWindow.dispose(); // Close "최신순" window if it's open
                }
                new Operator(); // Open login screen
            }
        });
        add(logoutButton);

        // "최신순" button
        JButton latestButton = new JButton("최신순");
        latestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBConnection dbConnection = new DBConnection();
                dbConnection.deleteAllData();
                runSeleniumTest("https://www.algumon.com/");
            }
        });
        add(latestButton);

        // "랭킹순" button
        JButton rankingButton = new JButton("랭킹순");
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBConnection dbConnection = new DBConnection();
                dbConnection.deleteAllData();
                runSeleniumTest("https://www.algumon.com/deal/rank");
            }
        });
        add(rankingButton);

        // Show the frame
        setVisible(true);
    }

    private void showNewWindow(String title) {
        // Hide the main window
        this.setVisible(false);

        // Create a new window
        JFrame newFrame = new JFrame(title);
        newFrame.setSize(300, 200);
        newFrame.setLocationRelativeTo(null); // Center the frame
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Show the main window when the new window is closed
        newFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(true);
            }
        });

        // Add a simple label to the new window
        JLabel label = new JLabel(title + " 창", SwingConstants.CENTER);
        newFrame.add(label);

        // Show the new window
        newFrame.setVisible(true);
    }

    private void runSeleniumTest(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SeleniumTest seleniumTest = new SeleniumTest();
                seleniumTest.crawl(url);
                // Call UI update after crawling
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        displayDataWindow();
                    }
                });
            }
        }).start();
    }

    private void displayDataWindow() {
        // If dataFrame is not created or not visible, create and show it
        if (dataFrame == null || !dataFrame.isVisible()) {
            // Database connection
            DBConnection dbConnection = new DBConnection();
            // Initialize table data
            String[] columnNames = {"상품명", "출처 URL", "가격", "배송"};
            String[][] data = new String[0][10]; // Empty data

            // Get new data from the database
            List<URLDomain> dataList = dbConnection.getAllData();

            // Set table data
            if (!dataList.isEmpty()) {
                data = new String[dataList.size()][10];
                for (int i = 0; i < dataList.size(); i++) {
                    URLDomain urlDomain = dataList.get(i);
                    data[i][0] = urlDomain.getItem_name();
                    data[i][1] = urlDomain.getFrom_url();
                    data[i][2] = urlDomain.getPrice();
                    data[i][3] = urlDomain.getDelivery();
                    data[i][4] = urlDomain.getWriter();
                    data[i][5] = urlDomain.getWant_to_buy();
                    data[i][6] = urlDomain.getBought();
                    data[i][7] = urlDomain.getComt();
                    data[i][8] = urlDomain.getShare();
                    data[i][9] = urlDomain.getImg_src();
                }
            }

            // Create and display the table
            dataFrame = new JFrame("크롤링 데이터");
            dataFrame.setSize(600, 400);
            dataFrame.setLocationRelativeTo(null);
            dataFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            dataFrame.add(scrollPane);

            // Show the main window when the data window is closed
            dataFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    setVisible(true);
                }
            });

            dataFrame.setVisible(true);
            latestWindow = dataFrame; // Assigning the latest window reference
        }
    }

    public static void main(String[] args) {
        // Create UI on the event queue
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ButtonDemo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
