package oplor.server.node;

import java.sql.*;

// <a>要素に関するクラス
public class HTMLAnchorElement extends HTMLElement {
    // ノードのプロパティ
    public String download;
    public String hash;
    public String host;
    public String hostname;
    public String href;
    public String hreflang;
    public String media;
    public String origin;
    public String password;
    public String pathname;
    public String port;
    public String protocol;
    public String refferrerPolicy;
    public String rel;
    public String search;
    public String target;
    public String text;
    public String type;
    public String username;

    // SQLiteデータベースにHTMLAnchorElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLAnchorElementにデータを挿入するSQL文
        String sql = "insert into HTMLAnchorElement(ref, download, hash, host, hostname, href, hreflang, media, origin, password, pathname, port, protocol, refferrerPolicy, rel, search, target, text, type, username)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, download);
            ps.setString(3, hash);
            ps.setString(4, host);
            ps.setString(5, hostname);
            ps.setString(6, href);
            ps.setString(7, hreflang);
            ps.setString(8, media);
            ps.setString(9, origin);
            ps.setString(10, password);
            ps.setString(11, pathname);
            ps.setString(12, port);
            ps.setString(13, protocol);
            ps.setString(14, refferrerPolicy);
            ps.setString(15, rel);
            ps.setString(16, search);
            ps.setString(17, target);
            ps.setString(18, text);
            ps.setString(19, type);
            ps.setString(20, username);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLAnchorElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
