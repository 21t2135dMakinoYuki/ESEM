package oplor.server.node;

import java.sql.*;

// <html>要素に関するクラス
public class HTMLElement extends Element {
    // ノードのプロパティ
    public String accessKey;
    public String accessKeyLabel;
    public String contentEditable;
    public boolean draggable;
    public boolean hidden;
    public boolean isContentEditable;
    public boolean itemScope;
    public String itemId;
    public String lang;
    public int offsetHeight;
    public int offsetLeft;
    public int offsetTop;
    public int offsetWidth;
    public boolean spellcheck;
    public int tabIndex;
    public String title;
    public boolean translate;

    // SQLiteデータベースにHTMLElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLElementにデータを挿入するSQL文
        String sql = "insert into HTMLElement(ref, accessKey, accessKeyLabel, contentEditable, draggable, hidden, isContentEditable, itemScope, itemId, lang, " +
                "offsetHeight, offsetLeft, offsetTop, offsetWidth, spellcheck, tabIndex, title, translate)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, accessKey);
            ps.setString(3, accessKeyLabel);
            ps.setString(4, contentEditable);
            ps.setBoolean(5, draggable);
            ps.setBoolean(6, hidden);
            ps.setBoolean(7, isContentEditable);
            ps.setBoolean(8, itemScope);
            ps.setString(9, itemId);
            ps.setString(10, lang);
            ps.setInt(11, offsetHeight);
            ps.setInt(12, offsetLeft);
            ps.setInt(13, offsetTop);
            ps.setInt(14, offsetWidth);
            ps.setBoolean(15, spellcheck);
            ps.setInt(16, tabIndex);
            ps.setString(17, title);
            ps.setBoolean(18, translate);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
