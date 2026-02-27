package oplor.server.node;

import java.sql.*;

// <source>要素に関するクラス
public class HTMLSourceElement extends HTMLElement {
    // ノードのプロパティ
    public String keySystem;
    public String media;
    public String sizes;
    public String src;
    public String srcset;
    public String type;

    // SQLiteデータベースにHTMLSourceElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLSourceElementにデータを挿入するSQL文
        String sql = "insert into HTMLSourceElement(ref, keySystem, media, sizes, src, srcset, type)" +
                "values(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータを設定
            ps.setInt(1, parentID);
            ps.setString(2, keySystem);
            ps.setString(3, media);
            ps.setString(4, sizes);
            ps.setString(5, src);
            ps.setString(6, srcset);
            ps.setString(7, type);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLSourceElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
