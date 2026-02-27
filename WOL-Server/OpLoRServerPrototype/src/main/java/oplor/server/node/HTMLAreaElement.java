package oplor.server.node;

import java.sql.*;

// <area>要素に関するクラス
public class HTMLAreaElement extends HTMLElement {
    // ノードのプロパティ
    public String alt;
    public String coords;
    public String download;
    public String hash;
    public String host;
    public String hostname;
    public String media;
    public String origin;
    public String password;
    public String pathname;
    public String port;
    public String protocol;
    public String refferrerPolicy;
    public String rel;
    public String search;
    public String shape;
    public String target;
    public String type;
    public String username;

    // SQLiteデータベースにHTMLAreaElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLAreaElementにデータを挿入するSQL文
        String sql = "insert into HTMLAreaElement(ref, alt, coords, download, hash, host, hostname, media, origin, password, pathname, port, protocol, refferrerPolicy, rel, search, shape, target, type, username)" +
                "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, alt);
            ps.setString(3, coords);
            ps.setString(4, download);
            ps.setString(5, hash);
            ps.setString(6, host);
            ps.setString(7, media);
            ps.setString(8, origin);
            ps.setString(9, password);
            ps.setString(10, pathname);
            ps.setString(11, port);
            ps.setString(12, protocol);
            ps.setString(13, refferrerPolicy);
            ps.setString(14, rel);
            ps.setString(15, search);
            ps.setString(16, shape);
            ps.setString(17, target);
            ps.setString(18, type);
            ps.setString(19, username);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLAreaElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
