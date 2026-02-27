package oplor.server.event;

import java.sql.*;

// キーボード入力に関するクラス
public class KeyboardEvent extends UIEvent {
    // イベントのプロパティ
    public boolean altKey;
    public String code;
    public boolean ctrlKey;
    public boolean isComposing;
    public String key;
    public int location;
    public boolean metaKey;
    public boolean repeat;
    public boolean shiftKey;

    // SQLiteデータベースにKeyboardEventを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親イベントのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // KeyboardEventにデータを挿入するSQL文(SQLiteの予約語：key，MySQLの予約語：repeatより，列名をkey2, repeat2とする)
        String sql = "insert into KeyboardEvent(ref, altKey, code, ctrlKey, isComposing, key2, location, metaKey, repeat2, shiftKey)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, altKey);
            ps.setString(3, code);
            ps.setBoolean(4, ctrlKey);
            ps.setBoolean(5, isComposing);
            ps.setString(6, key);
            ps.setInt(7, location);
            ps.setBoolean(8, metaKey);
            ps.setBoolean(9, repeat);
            ps.setBoolean(10, shiftKey);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert KeyboardEvent into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
