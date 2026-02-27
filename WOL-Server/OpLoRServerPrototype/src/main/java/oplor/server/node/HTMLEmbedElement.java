package oplor.server.node;

import java.sql.*;

// <embed>要素に関するクラス
public class HTMLEmbedElement extends HTMLElement {
    // ノードのプロパティ
    public String height;
    public String src;
    public String type;
    public String width;

    // SQLiteデータベースにHTMLEmbedElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLEmbedElementにデータを挿入するSQL文
        String sql = "insert into HTMLEmbedElement(ref, height, src, type, width)values(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, height);
            ps.setString(3, src);
            ps.setString(4, type);
            ps.setString(5, width);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLEmbedElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
