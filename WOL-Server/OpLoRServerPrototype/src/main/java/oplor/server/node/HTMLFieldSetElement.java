package oplor.server.node;

import java.sql.*;

// <fieldset>要素に関するクラス
public class HTMLFieldSetElement extends HTMLElement {
    // ノードのプロパティ
    public boolean disabled;
    public String name;
    public String type;
    public String validationMessage;
    public boolean willValidate;

    // SQLiteデータベースにHTMLFieldSetElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLFieldSetElementにデータを挿入するSQL文
        String sql = "insert into HTMLFieldSetElement(ref, disabled, name, type, validationMessage, willValidate)" +
                "values(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, disabled);
            ps.setString(3, name);
            ps.setString(4, type);
            ps.setString(5, validationMessage);
            ps.setBoolean(6, willValidate);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLFieldSetElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
