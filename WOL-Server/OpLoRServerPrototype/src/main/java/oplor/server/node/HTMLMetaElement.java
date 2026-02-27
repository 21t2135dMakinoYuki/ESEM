package oplor.server.node;

import java.sql.*;

// HTML文書内のメタデータを表現するクラス
public class HTMLMetaElement extends HTMLElement {
    // ノードのプロパティ
    public String content;
    public String httpEquiv;
    public String name;

    // SQLiteデータベースにHTMLMetaElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLMetaElementにデータを挿入するSQL文
        String sql = "insert into HTMLMetaElement(ref, content, httpEquiv, name)values(?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, content);
            ps.setString(3, httpEquiv);
            ps.setString(4, name);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLMetaElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
