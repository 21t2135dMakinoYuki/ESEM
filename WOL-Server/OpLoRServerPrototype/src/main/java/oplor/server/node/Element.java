package oplor.server.node;

import java.sql.*;

// HTML要素を表すクラス
public class Element extends Node {
    // ノードのプロパティ
    public String className;
    public int clientHeight;
    public int clientLeft;
    public int clientTop;
    public int clientWidth;
    public String computedName;
    public String computedRole;
    public String id;
    public String innerHTML;
    public String localName;
    public String namespaceURI;
    public String outerHTML;
    public String prefix;
    public int scrollHeight;
    public int scrollLeft;
    public int scrollTop;
    public int scrollWidth;
    public String selector;
    public String slot;
    public String tagName;

    // SQLiteデータベースにElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // Elementにデータを挿入するSQL文
        String sql = "insert into Element(ref, className, clientHeight, clientLeft, clientTop, clientWidth, computedName, computedRole, id, innerHTML, localName, namespaceURI, outerHTML, prefix, scrollHeight, scrollLeft, scrollTop, scrollWidth, selector, slot, tagName)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, className);
            ps.setInt(3, clientHeight);
            ps.setInt(4, clientLeft);
            ps.setInt(5, clientTop);
            ps.setInt(6, clientWidth);
            ps.setString(7, computedName);
            ps.setString(8, computedRole);
            ps.setString(9, id);
            ps.setString(10, innerHTML);
            ps.setString(11, localName);
            ps.setString(12, namespaceURI);
            ps.setString(13, outerHTML);
            ps.setString(14, prefix);
            ps.setInt(15, scrollHeight);
            ps.setInt(16, scrollLeft);
            ps.setInt(17, scrollTop);
            ps.setInt(18, scrollWidth);
            ps.setString(19, selector);
            ps.setString(20, slot);
            ps.setString(21, tagName);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert Element into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
