package oplor.server.node;

import java.sql.*;

// <script>要素に関するクラス
public class HTMLScriptElement extends HTMLElement {
    // ノードのプロパティ
    public boolean async;
    public String charset;
    public String crossOrigin;
    public boolean defer;
    public boolean noModule;
    public String src;
    public String text;
    public String type;

    // SQLiteデータベースにHTMLScriptElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLScriptElementにデータを挿入するSQL文
        String sql = "insert into HTMLScriptElement(ref, async, charset, crossOrigin, defer, noModule, src, text, type)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, async);
            ps.setString(3, charset);
            ps.setString(4, crossOrigin);
            ps.setBoolean(5, defer);
            ps.setBoolean(6, noModule);
            ps.setString(7, src);
            ps.setString(8, text);
            ps.setString(9, type);
            

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLScriptElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
