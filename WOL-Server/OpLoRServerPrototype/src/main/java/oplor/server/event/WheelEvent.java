package oplor.server.event;

import java.sql.*;

// ホイール操作に関するイベント
public class WheelEvent extends MouseEvent {
    // イベントのプロパティ
    public double deltaX;
    public double deltaY;
    public double deltaZ;
    public int deltaMode;

    // SQLiteデータベースにWheelEventを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親イベントのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // WheelEventにデータを挿入するSQL文
        String sql = "insert into WheelEvent(ref, deltaX, deltaY, deltaZ, dataMode)"
                + "values(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setDouble(2, deltaX);
            ps.setDouble(3, deltaY);
            ps.setDouble(4, deltaZ);
            ps.setInt(5, deltaMode);

            // SQL文の実行
            ps.executeUpdate();

            // 生成したキーの取得
            ResultSet rs = ps.getGeneratedKeys();

            // 挿入したレコードのキーを取得
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert WheelEvent into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
