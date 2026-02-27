package oplor.server.node;

import java.sql.*;

// ドキュメントに関するクラス
public class Document extends Node {
    // ノードのプロパティ
    public String characterSet;
    public String compatMode;
    public String contentType;
    public String cookie;
    public String designMode;
    public String dir;
    public String documentURI;
    public String domain;
    public boolean hidden;
    public String lastModified;
    public String readyState;
    public String referrer;
    public String selectedStyleSheetSet;
    public String title;
    public String URL;
    public String visibilityState;

    // SQLiteデータベースにDocumentを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // Documentにデータを挿入するSQL文
        String sql = "insert into Document(ref, characterSet, compatMode, contentType, cookie, designMode, dir, documentURI, domain, hidden, lastModified, readyState, referrer, selectedStyleSheetSet, title, URL, visibilityState) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, characterSet);
            ps.setString(3, compatMode);
            ps.setString(4, contentType);
            ps.setString(5, cookie);
            ps.setString(6, designMode);
            ps.setString(7, dir);
            ps.setString(8, documentURI);
            ps.setString(9, domain);
            ps.setBoolean(10, hidden);
            ps.setString(11, lastModified);
            ps.setString(12, readyState);
            ps.setString(13, referrer);
            ps.setString(14, selectedStyleSheetSet);
            ps.setString(15, title);
            ps.setString(16, URL);
            ps.setString(17, visibilityState);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert Document into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
