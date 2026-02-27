package oplor.server.node;

import java.sql.*;

// <map>要素に関するクラス
public class HTMLMapElement extends HTMLElement {
    // ノードのプロパティ
    public String name;

    // SQLiteデータベースにHTMLMapElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLMapElementにデータを挿入するSQL文
        String sql = "insert into HTMLMapElement(ref, name)values(?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, name);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLMapElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
