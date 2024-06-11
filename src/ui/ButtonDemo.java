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

    public ButtonDemo() {
        // 프레임 설정
        setTitle("버튼 데모");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // 화면 가운데에 배치
        setLayout(new GridLayout(2, 2, 10, 10)); // 2x2 그리드 레이아웃

        // "마이페이지" 버튼
        JButton myPageButton = new JButton("마이페이지");
        myPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewWindow("마이페이지");
            }
        });
        add(myPageButton);

        // "로그아웃" 버튼
        JButton logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 로그아웃 처리
                dispose(); // 현재 창 닫기
                new Operator(); // 로그인 화면 열기
            }
        });
        add(logoutButton);

        // "최신순" 버튼
        JButton latestButton = new JButton("최신순");
        latestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewWindow("최신순");
                runSeleniumTest("https://www.algumon.com/");
                displayDataWindow();
            }
        });
        add(latestButton);

        // "랭킹순" 버튼
        JButton rankingButton = new JButton("랭킹순");
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewWindow("랭킹순");
                runSeleniumTest("https://www.algumon.com/deal/rank");
                displayDataWindow();
            }
        });
        add(rankingButton);

        // 프레임을 표시
        setVisible(true);
    }

    private void showNewWindow(String title) {
        // 메인 창 숨기기
        this.setVisible(false);

        // 새로운 창 생성
        JFrame newFrame = new JFrame(title);
        newFrame.setSize(300, 200);
        newFrame.setLocationRelativeTo(null); // 화면 가운데에 배치
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 새로운 창이 닫힐 때 메인 창을 다시 표시
        newFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(true);
            }
        });

        // 새로운 창에 간단한 라벨 추가
        JLabel label = new JLabel(title + " 창", SwingConstants.CENTER);
        newFrame.add(label);

        // 새로운 창을 표시
        newFrame.setVisible(true);
    }

    private void runSeleniumTest(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SeleniumTest seleniumTest = new SeleniumTest();
                seleniumTest.crawl();
            }
        }).start();
    }

    private void displayDataWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 데이터베이스 연결
                DBConnection dbConnection = new DBConnection();
                // 테이블 데이터 초기화
                String[] columnNames = {"Item Name", "From URL", "Price", "Delivery", "Writer", "Want to Buy", "Bought", "Comment", "Share", "Image URL"};
                String[][] data = new String[0][10]; // 빈 데이터

                // 데이터베이스에서 새로운 데이터 가져오기
                List<URLDomain> dataList = dbConnection.getAllData();

                // 테이블 데이터 설정
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

                // 테이블 생성 및 표시
                JFrame dataFrame = new JFrame("크롤링 데이터");
                dataFrame.setSize(600, 400);
                dataFrame.setLocationRelativeTo(null);
                dataFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JTable table = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                dataFrame.add(scrollPane);

                // 새로운 창이 닫힐 때 메인 창을 다시 표시
                dataFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        setVisible(true);
                    }
                });

                dataFrame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        // 이벤트 큐를 통해 UI를 생성
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
