package oplor.server.node;

import java.sql.*;

// <dialog>要素に関するクラス
public class HTMLDialogElement extends HTMLElement {
    // ノードのプロパティ
    public boolean open;
    public String returnValue;

    //SQLiteデータベースにHTMLDialogElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLdialogElementにデータを挿入するSQL文
        String sql = "insert into HTMLDialogElement(ref, open, returnValue)values(?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setBoolean(2, open);
            ps.setString(3, returnValue);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLDialogElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
