package oplor.server.node;

import java.sql.*;

// <track>要素に関するクラス
public class HTMLTrackElement extends HTMLElement {
    // ノードのプロパティ
    public String kind;
    public String label;
    public boolean m_default;   //defaultは予約語のため，m_をつける
    public String src;
    public String srclang;
    public int readyState;

    // SQLiteデータベースにHTMLTrackElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLTrackElementにデータを挿入するSQL文
        String sql = "insert into HTMLTrackElement(ref, kind, label, m_default, src, srclang, readyState)"
                + "values(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, kind);
            ps.setString(3, label);
            ps.setBoolean(4, m_default);
            ps.setString(5, src);
            ps.setString(6, srclang);
            ps.setInt(7, readyState);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLTrackElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
