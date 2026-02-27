package oplor.server.node;

import java.sql.*;

// <meter>要素に関するクラス
public class HTMLMeterElement extends HTMLElement {
    // ノードのプロパティ
    public double high;
    public double low;
    public double max;
    public double min;
    public double optimum;

    // SQLiteデータベースにHTMLMeterElementを挿入するSQL文
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLMeterElementにデータを挿入するSQL文
        String sql = "insert into HTMLMeterElement(ref, high, low, max, min, optimum)values(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setDouble(2, high);
            ps.setDouble(3, low);
            ps.setDouble(4, max);
            ps.setDouble(5, min);
            ps.setDouble(6, optimum);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLMeterElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
