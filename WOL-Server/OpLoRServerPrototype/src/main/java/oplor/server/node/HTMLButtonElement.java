package oplor.server.node;

import java.sql.*;

// <button>要素に関するクラス
public class HTMLButtonElement extends HTMLElement {
    // ノードのプロパティ
    public boolean autofocus;
    public boolean disabled;
    public String formAction;
    public String formEnctype;
    public String formMethod;
    public boolean formNoValidate;
    public String formTarget;
    public String name;
    public String type;
    public String validationMessage;
    public String value;
    public boolean willValidate;

    // SQLiteデータベースにHTMLButtonElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLButtonElementにデータを挿入するSQL文
        String sql = "insert into HTMLButtonElement(ref, autofocus, disabled, formAcion, formEnctype, formMethod, formNoValidate, formTarget, name, type, validationMessage, value, willValidate)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, autofocus);
            ps.setBoolean(3, disabled);
            ps.setString(4, formAction);
            ps.setString(5, formEnctype);
            ps.setString(6, formMethod);
            ps.setBoolean(7, formNoValidate);
            ps.setString(8, formTarget);
            ps.setString(9, name);
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
            System.err.println("Failed to insert HTMLButtonElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
