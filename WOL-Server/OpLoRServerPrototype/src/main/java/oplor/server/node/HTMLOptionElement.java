package oplor.server.node;

import java.sql.*;

// <option>要素に関するクラス
public class HTMLOptionElement extends HTMLElement {
    // ノードのプロパティ
    public boolean defaultSelected;
    public boolean disabled;
    public int index;
    public String label;
    public boolean selected;
    public String text;
    public String value;

    // SQLiteデータベースにHTMLOptionElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLOptionElementにデータを挿入するSQL文
        String sql = "insert into HTMLOptionElement(ref, defaultSelected, disabled, index, label, selected, text, value)" +
                "value(?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, defaultSelected);
            ps.setBoolean(3, disabled);
            ps.setInt(4, index);
            ps.setString(5, label);
            ps.setBoolean(6, selected);
            ps.setString(7, text);
            ps.setString(8, value);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLOptionElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
