package oplor.server.node;

import java.sql.*;

// <iframe>要素に関するクラス
public class HTMLIFrameElement extends HTMLElement {
    // ノードのプロパティ
    public String allow;
    public boolean allowFullscreen;
    public boolean allowPaymentRequest;
    public String height;
    public String name;
    public String referrerPolicy;
    public String src;
    public String srcdoc;
    public String width;

    // SQLiteデータベースにHTMLIFrameElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLFrameElementにデータを挿入するSQL文
        String sql = "insert into HTMLIFrameElement(ref, allow, allowFullscreen, allowPaymentRequest, height, name, referrerPolicy, src, srcdoc, width)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, allow);
            ps.setBoolean(3, allowFullscreen);
            ps.setBoolean(4, allowPaymentRequest);
            ps.setString(5, height);
            ps.setString(6, name);
            ps.setString(7, referrerPolicy);
            ps.setString(8, src);
            ps.setString(9, srcdoc);
            ps.setString(10, width);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLIFrameElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
