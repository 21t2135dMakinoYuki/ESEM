package oplor.server.event;

import java.sql.*;

// 入力操作に関するクラス
public class InputEvent extends UIEvent {
    // イベントのプロパティ
    public String data;
    public String inputType;
    public boolean isComposing;

    // SQLiteデータベースにInputEventを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親イベントのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // InputEventにデータを挿入するSQL文
        String sql = "insert into InputEvent(ref, data, inputType, isComposing)values(?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, data);
            ps.setString(3, inputType);
            ps.setBoolean(4, isComposing);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert InputEvent into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
