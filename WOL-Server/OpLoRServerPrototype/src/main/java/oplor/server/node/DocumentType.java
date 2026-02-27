package oplor.server.node;

import java.sql.*;

// DOCTYPEに関するクラス
public class DocumentType extends Node {
    // ノードのプロパティ
    public String name;
    public String publicId;
    public String systemId;

    // SQLiteデータベースにDocumentTypeを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // DocumentTypeにデータを挿入するSQL文
        String sql = "insert into DocumentType(ref, name, publicId, systemId)values(?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, name);
            ps.setString(3, publicId);
            ps.setString(4, systemId);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert DocumentType into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
