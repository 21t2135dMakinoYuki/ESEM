package oplor.server.node;

import java.sql.*;

// <video>要素に関するクラス
public class HTMLVideoElement extends HTMLMediaElement {
    // ノードのプロパティ
    public String poster;
    public int videoHeight;
    public int videoWidth;
    public int width;

    // SQLiteデータベースにHTMLVideoElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLVideoElementにデータを挿入するSQL文
        String sql = "insert into HTMLVideoElement(ref, poster, videoHeight, videoWidth, width)"
                + "values(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, poster);
            ps.setInt(3, videoHeight);
            ps.setInt(4, videoWidth);
            ps.setInt(5, width);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLVideoElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
