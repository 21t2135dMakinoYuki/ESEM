package oplor.server.node;

import java.sql.*;

// <form>要素に関するクラス
public class HTMLFormElement extends HTMLElement {
    // ノードのプロパティ
    public String acceptCharset;
    public String action;
    public String autocomplete;
    public String encoding;
    public String enctype;
    public int length;
    public String method;
    public String name;
    public boolean noValidate;
    public String target;

    // SQLiteデータベースにHTMLFormElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLFormElementにデータを挿入するSQL文
        String sql = "insert into HTMLFormElement(ref, acceptCharset, action, autocomplete, encoding, enctype, length, method, name, noValidate, target)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, acceptCharset);
            ps.setString(3, action);
            ps.setString(4, autocomplete);
            ps.setString(5, encoding);
            ps.setString(6, enctype);
            ps.setInt(7, length);
            ps.setString(8, method);
            ps.setString(9, name);
            ps.setBoolean(10, noValidate);
            ps.setString(11, target);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLFormElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
