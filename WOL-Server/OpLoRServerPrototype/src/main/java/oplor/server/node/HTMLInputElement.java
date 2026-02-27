package oplor.server.node;

import java.sql.*;

// <input>要素に関するクラス
public class HTMLInputElement extends HTMLElement {
    // ノードのプロパティ
    public String accept;
    public String alt;
    public String autocapitalize;
    public String autocomplete;
    public boolean autofocus;
    public boolean checked;
    public boolean defaultChecked;
    public String defaultValue;
    public String dirName;
    public boolean disabled;
    public String formAction;
    public String formEncType;
    public String formMethod;
    public String formNoValidate;
    public String formTarget;
    public int height;
    public boolean indeterminate;
    public String max;
    public int maxLength;
    public String min;
    public boolean multiple;
    public String name;
    public String pattern;
    public String placeholder;
    public boolean readyOnly;
    public boolean required;
    public String selectionDirection;
    public int selectionEnd;
    public int selectionStart;
    public int size;
    public String src;
    public String step;
    public String type;
    public String validationMessage;
    public String value;
    public double valueAsNumber;
    public int width;
    public boolean willValidate;


    // SQLiteデータベースにHTMLInputElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLInputElementにデータを挿入するSQL文
        String sql = "insert into HTMLInputElement(ref, accept, alt, autocapitalize, autocomplete, autofocus, checked," +
                " defaultChecked, defaultValue, dirName, disabled, formAction, formEnctype, formMethod, formNoValidate," +
                " formTarget, height, indeterminate, max, maxLength, min, multiple, name, pattern, placeholder, readyOnly," +
                " required, selectionDirection, selectionEnd, selectionStart, size, src, step, type, validationMessage," +
                " value, valueAsNumber, width, willValidate)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, accept);
            ps.setString(3, alt);
            ps.setString(4, autocapitalize);
            ps.setString(5, autocomplete);
            ps.setBoolean(6, autofocus);
            ps.setBoolean(7, checked);
            ps.setBoolean(8, defaultChecked);
            ps.setString(9, defaultValue);
            ps.setString(10, dirName);
            ps.setBoolean(11, disabled);
            ps.setString(12, formAction);
            ps.setString(13, formEncType);
            ps.setString(14, formMethod);
            ps.setString(15, formNoValidate);
            ps.setString(16, formTarget);
            ps.setInt(17, height);
            ps.setBoolean(18, indeterminate);
            ps.setString(19, max);
            ps.setInt(20, maxLength);
            ps.setString(21, min);
            ps.setBoolean(22, multiple);
            ps.setString(23, name);
            ps.setString(24, pattern);
            ps.setString(25, placeholder);
            ps.setBoolean(26, readyOnly);
            ps.setBoolean(27, required);
            ps.setString(28, selectionDirection);
            ps.setInt(29, selectionEnd);
            ps.setInt(30, selectionStart);
            ps.setInt(31, size);
            ps.setString(32, src);
            ps.setString(33, step);
            ps.setString(34, type);
            ps.setString(35, validationMessage);
            ps.setString(36, value);
            ps.setDouble(37, valueAsNumber);
            ps.setInt(38, width);
            ps.setBoolean(39, willValidate);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLInputElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
