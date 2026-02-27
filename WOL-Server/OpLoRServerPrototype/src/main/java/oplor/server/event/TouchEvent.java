package oplor.server.event;

import java.sql.*;

// タッチ操作に関するイベント
public class TouchEvent extends UIEvent {
    // イベントのプロパティ
    public boolean altKey;
    public boolean ctrlKey;
    public boolean metaKey;
    public boolean shiftKey;

    // SQLiteデータベースにTouchEventを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親イベントのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // TouchEventにデータを挿入するSQL文
        String sql = "insert into TouchEvent(ref, altKey, ctrlKey, metaKey, shiftKey)values(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, altKey);
            ps.setBoolean(3, ctrlKey);
            ps.setBoolean(4, metaKey);
            ps.setBoolean(5, shiftKey);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert TouchEvent into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
