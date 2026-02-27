package oplor.server.node;

import java.sql.*;

// HTML文書内のメディア要素に関するクラス
public class HTMLMediaElement extends HTMLElement {
    // ノードのプロパティ
    public boolean autoplay;
    public String crossOrigin;
    public String currentSrc;
    public double currentTime;
    public boolean defaultMuted;
    public double defaultPlaybackRate;
    public boolean disableRemotePlayback;
    public double duration;
    public boolean ended;
    public boolean loop;
    public String mediaGroup;
    public boolean muted;
    public int networkState;
    public boolean paused;
    public double playbackRate;
    public String preload;
    public int readyState;
    public boolean seeking;
    public String sinkId;
    public String src;
    public double volume;

    // SQLiteデータベースにHTMLMediaElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLMediaElementにデータを挿入するSQL文
        String sql = "insert into HTMLMediaElement(ref, autoplay, crossOrigin, currentSrc, currentTime, defaultTime, defaultMuted, defaultPlaybackRate," +
            " disabledRemotePlayback, duration, ended, loop, mediaGroup, muted, networkState, paused, playbackRate," +
            " preload, readyState, seeking, sinkId, src, valume)" +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, autoplay);
            ps.setString(3, crossOrigin);
            ps.setString(4, currentSrc);
            ps.setDouble(5, currentTime);
            ps.setBoolean(6, defaultMuted);
            ps.setDouble(7, defaultPlaybackRate);
            ps.setBoolean(8, disableRemotePlayback);
            ps.setDouble(9, duration);
            ps.setBoolean(10, ended);
            ps.setBoolean(11, loop);
            ps.setString(12, mediaGroup);
            ps.setBoolean(13, muted);
            ps.setInt(14, networkState);
            ps.setBoolean(15, paused);
            ps.setDouble(16, playbackRate);
            ps.setString(17, preload);
            ps.setInt(18, readyState);
            ps.setBoolean(19, seeking);
            ps.setString(20, sinkId);
            ps.setString(21, src);
            ps.setDouble(22, volume);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLMediaElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
