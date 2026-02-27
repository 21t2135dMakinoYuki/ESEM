package oplor.server.event;

import java.sql.*;

// マウス操作に関するイベント
public class MouseEvent extends UIEvent {
    // イベントのプロパティ
    public boolean altKey;
    public int button;
    public int buttons;
    public int clientX;
    public int clientY;
    public boolean ctrlKey;
    public boolean metaKey;
    public int movementX;
    public int movementY;
    public int offsetX;
    public int offsetY;
    public int pageX;
    public int pageY;
    public int screenX;
    public int screenY;
    public boolean shiftKey;
    public int x;
    public int y;

    // SQLiteデータベースにMouseEventを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親イベントのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // MouseEventにデータを挿入するSQL文
        String sql = "insert into MouseEvent(ref, altKey, button, buttons, client_X, client_Y, ctrlKey, metaKey, movementX, movementY, offsetX, offsetY, pageX, pageY, screenX, screenY, shiftKey, x, y)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, altKey);
            ps.setInt(3, button);
            ps.setInt(4, buttons);
            ps.setInt(5, clientX);
            ps.setInt(6, clientY);
            ps.setBoolean(7, ctrlKey);
            ps.setBoolean(8, metaKey);
            ps.setInt(9, movementX);
            ps.setInt(10, movementY);
            ps.setInt(11, offsetX);
            ps.setInt(12, offsetY);
            ps.setInt(13, pageX);
            ps.setInt(14, pageY);
            ps.setInt(15, screenX);
            ps.setInt(16, screenY);
            ps.setBoolean(17, shiftKey);
            ps.setInt(18, x);
            ps.setInt(19, y);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert MouseEvent into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }

    // pageXを返す(デバック用)
    public int getPageX() {
        return pageX;
    }

    // pageYを返す(デバック用)
    public int getPageY() {
        return pageY;
    }
}
