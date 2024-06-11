
package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:mariadb://localhost:3306/chd"; // Update with the correct path to your database
        Connection conn = null;
        String user = "chd";
        String password = "1234";

        PreparedStatement pstmt = null;




        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void Connection(URLDomain urlDomain) {
        String sql = "INSERT INTO product(item_name, from_url, price, delivery, writer, want_to_buy, bought, comt, share, img_src) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, urlDomain.getFrom_url());
            pstmt.setString(2, urlDomain.getItem_name());
            pstmt.setString(3, urlDomain.getPrice());
            pstmt.setString(4, urlDomain.getDelivery());
            pstmt.setString(5, urlDomain.getWriter());
            pstmt.setString(6, urlDomain.getWant_to_buy());
            pstmt.setString(7, urlDomain.getBought());
            pstmt.setString(8, urlDomain.getComt());
            pstmt.setString(9, urlDomain.getShare());
            pstmt.setString(10, urlDomain.getImg_src());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<URLDomain> getAllData() {
        String sql = "SELECT item_name, from_url, price, delivery, writer, want_to_buy, bought, comt, share, img_src FROM product";

        List<URLDomain> dataList = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                URLDomain urlDomain = new URLDomain();
                urlDomain.setItem_name(rs.getString("item_name"));
                urlDomain.setFrom_url(rs.getString("from_url"));
                urlDomain.setPrice(rs.getString("price"));
                urlDomain.setDelivery(rs.getString("delivery"));
//                urlDomain.setWriter(rs.getString("writer"));
//                urlDomain.setWant_to_buy(rs.getString("want_to_buy"));
//                urlDomain.setBought(rs.getString("bought"));
//                urlDomain.setComt(rs.getString("comt"));
//                urlDomain.setShare(rs.getString("share"));
//                urlDomain.setImg_src(rs.getString("img_src"));

                dataList.add(urlDomain);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return dataList;
    }
}
