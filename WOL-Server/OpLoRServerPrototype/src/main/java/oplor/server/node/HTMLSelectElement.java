package oplor.server.node;

import java.sql.*;

// <select>要素に関するクラス
public class HTMLSelectElement extends HTMLElement {
    // ノードのプロパティ
    public boolean autofocus;
    public boolean disabled;
    public int length;
    public boolean multiple;
    public String name;
    public boolean required;
    public int selectedIndex;
    public int size;
    public String type;
    public String validationMessage;
    public String value;
    public boolean willValidate;

    // SQLiteデータベースにHTMLSelectElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLSelectElementにデータを挿入するSQL文
        String sql = "insert into HTMLSelectElement(ref, autofocus, disabled, length, multiple, name, required, selectedIndex, size, type, validationMessage, value, willValidate)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, autofocus);
            ps.setBoolean(3, disabled);
            ps.setInt(4, length);
            ps.setBoolean(5, multiple);
            ps.setString(6, name);
            ps.setBoolean(7, required);
            ps.setInt(8, selectedIndex);
            ps.setInt(9, size);
            ps.setString(10, type);
            ps.setString(11, validationMessage);
            ps.setString(12, value);
            ps.setBoolean(13, willValidate);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLSelectElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
