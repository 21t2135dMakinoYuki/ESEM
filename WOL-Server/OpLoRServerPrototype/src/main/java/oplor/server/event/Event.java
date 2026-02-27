package oplor.server.event;

import java.sql.*;

// DOMで発生するイベントに関するクラス
public class Event {
    // イベントのプロパティ(内容は以下webサイト参照：https://developer.mozilla.org/ja/docs/Web/API/Event(access:2024/5/31))
    public boolean bubbles;
    public boolean cancelable;
    public boolean composed;
    public boolean defaultPrevented;
    public int eventPhase;
    public double timeStamp;
    public String type;
    public boolean isTrusted;
    public long epochMillis;

    // SQLiteデータベースにEventを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // Eventにデータを挿入するSQL文
        String sql = "insert into Event(ref, bubbles, cancelable, composed, defaultPrevented, eventPhase, timeStamp, epochMillis, type, isTrusted)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, logID);
            ps.setBoolean(2, bubbles);
            ps.setBoolean(3, cancelable);
            ps.setBoolean(4, composed);
            ps.setBoolean(5, defaultPrevented);
            ps.setInt(6, eventPhase);
            ps.setDouble(7, timeStamp);
            ps.setLong(8, epochMillis);
            ps.setString(9, type);
            ps.setBoolean(10, isTrusted);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert Event into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードのIDを返す
        return childID;
    }
}
