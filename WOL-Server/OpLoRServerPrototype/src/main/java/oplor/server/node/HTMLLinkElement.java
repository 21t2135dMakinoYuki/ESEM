package oplor.server.node;

import java.sql.*;

// <link>要素に関するクラス
public class HTMLLinkElement extends HTMLElement {
    // ノードのプロパティ
    public String as;
    public String crossOrigin;
    public boolean disabled;
    public String href;
    public String hreflang;
    public String media;
    public String referrerPolicy;
    public String rel;
    public String type;

    // SQLiteデータベースにHTMLLinkElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLLinkElementにデータを挿入するSQL文
        String sql = "insert into HTMLLinkElement(ref, as, crossOrigin, disabled, href, hreflang, media, referrerPolicy, rel, type)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, as);
            ps.setString(3, crossOrigin);
            ps.setBoolean(4, disabled);
            ps.setString(5, href);
            ps.setString(6, hreflang);
            ps.setString(7, media);
            ps.setString(8, referrerPolicy);
            ps.setString(9, rel);
            ps.setString(10, type);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLLinkElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
