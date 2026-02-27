package oplor.server.node;

import java.sql.*;

// <output>要素に関するクラス
public class HTMLOutputElement extends HTMLElement {
    // ノードのプロパティ
    public String defaultValue;
    public String name;
    public String type;
    public String validationMessage;
    public String value;
    public boolean willValidate;

    // SQLiteデータベースにHTMLOutputElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLOutputElementにデータを挿入するSQL文
        String sql = "insert into HTMLOutputElement(ref, defaultValue, name, type, validationMessage, value, willValidate)" +
                "values(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, defaultValue);
            ps.setString(3, name);
            ps.setString(4, type);
            ps.setString(5, validationMessage);
            ps.setString(6, value);
            ps.setBoolean(7, willValidate);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLOutputElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
