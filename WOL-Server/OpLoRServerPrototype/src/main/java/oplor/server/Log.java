package oplor.server;

import oplor.server.event.Event;
import oplor.server.node.Node;

import java.sql.*;

// ログ情報を表すクラス
public class Log {
    // ノードのプロパティ
    private Event event;
    private Node node;

    private String EventType;
    private String NodeType;

    // コンストラクタ
    Log(String eventType, String nodeType, Event vevent, Node vnode) {
        EventType = eventType;
        NodeType = nodeType;
        event = vevent;
        node = vnode;
    }

    // SQLiteにログを挿入するメソッド
    public int sqliteInsert(Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int logID = -1;

        // Logテーブルにデータを挿入するSQL文
        String sql = "insert into Log(EventType, NodeType) values(?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setString(1, EventType);
            ps.setString(2, NodeType);

            // SQL文の実行
            ps.executeUpdate();

            //生成したキーの取得
            ResultSet rs = ps.getGeneratedKeys();

            // 挿入されたレコードのキーを取得
            while (rs.next()) {
                logID = rs.getInt(1);   // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert Log into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return logID;
    }

    // イベントを取得するメソッド
    public Event getEvent() {
        return event;
    }

    // ノードを取得するメソッド
    public Node getNode() {
        return node;
    }

    // Logオブジェクトの内容を文字列で返す
    @Override
    public String toString() {
        // eventとnodeのtoString()メソッドを呼び出す
        return "Log{" +
               "EventType='" + EventType + '\'' +
               ", NodeType='" + NodeType + '\'' +
               ", event=" + event +
               ", node=" + node +
               '}';
    }
}