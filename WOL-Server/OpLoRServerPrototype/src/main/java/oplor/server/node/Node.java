package oplor.server.node;

import java.sql.*;

// HTML DOM ノードを表す基底クラス
public class Node {
    // ノードのプロパティ
    public String baseURI;
    public String innerText;
    public String nodeName;
    public String nodeValue;
    public String textContent;

    // SQLiteデータベースにNodeを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // Nodeにデータを挿入するSQL文
        String sql = "insert into Node(ref, baseURI, innerText, nodeName, nodeValue)values(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, logID);
            ps.setString(2, baseURI);
            ps.setString(3, innerText);
            ps.setString(4, nodeName);
            ps.setString(5, nodeValue);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入されたレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert Node into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
