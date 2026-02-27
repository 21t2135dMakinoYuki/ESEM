package oplor.server.node;

import java.sql.*;

// <textarea>要素に関するクラス
public class HTMLTextAreaElement extends HTMLElement {
    // ノードのプロパティ
    public String autocapitalize;
    public String autocomplete;
    public boolean autofocus;
    public int cols;
    public String defaultValue;
    public boolean disabled;
    public String inputMode;
    public int maxLength;
    public String name;
    public String placeholder;
    public boolean readOnly;
    public boolean required;
    public int rows;
    public String selectionDirection;
    public int selectionEnd;
    public int selectionStart;
    public int textLength;
    public String type;
    public String validationMessage;
    public String value;
    public boolean willValidate;
    public String wrap;

    // SQLiteデータベースにHTMLTextAreaElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLTextAreaElementにデータを挿入するSQL文
        String sql = "insert into HTMLTextAreaElement(ref, autocapitalize, autocomplete, autofocus, cols, defaultValue, disabled, inputMode, maxLength, name, placeholder, readOnly, required, rows, selectionDirection, selectionEnd, selectionStart, textLength, type, validationMessage, value, willValidate, wrap)"
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, autocapitalize);
            ps.setString(3, autocomplete);
            ps.setBoolean(4, autofocus);
            ps.setInt(5, cols);
            ps.setString(6, defaultValue);
            ps.setBoolean(7, disabled);
            ps.setString(8, inputMode);
            ps.setInt(9, maxLength);
            ps.setString(10, name);
            ps.setString(11, placeholder);
            ps.setBoolean(12, readOnly);
            ps.setBoolean(13, required);
            ps.setInt(14, rows);
            ps.setString(15, selectionDirection);
            ps.setInt(16, selectionEnd);
            ps.setInt(17, selectionStart);
            ps.setInt(18, textLength);
            ps.setString(19, type);
            ps.setString(20, validationMessage);
            ps.setString(21, value);
            ps.setBoolean(22, willValidate);
            ps.setString(23, wrap);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLTextAreaElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
