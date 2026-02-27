package oplor.server.node;

import java.sql.*;

// <td>要素に関するクラス
public class HTMLTableCellElement extends HTMLElement {
    // ノードのプロパティ
    public String abbr;
    public int cellIndex;
    public int colSpan;
    public int rowSpan;
    public String scope;

    // SQLiteデータベースにHTMLTableCellElementを挿入
    public int sqliteInsert(int logID, Connection connection) throws SQLException {
        // 挿入したレコードのIDを格納する変数
        int childID = -1;

        // 親ノードのIDを取得
        int parentID = super.sqliteInsert(logID, connection);

        // HTMLTableCellElementにデータを挿入するSQL文
        String sql = "insert into HTMLTableCellElement(ref, abbr, cellIndex, colSpan, rowSpan, scope)" +
                "values(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // パラメータの設定
            ps.setInt(1, parentID);
            ps.setString(2, abbr);
            ps.setInt(3, cellIndex);
            ps.setInt(4, colSpan);
            ps.setInt(5, rowSpan);
            ps.setString(6, scope);

            // SQL文の実行
            ps.executeUpdate();

            // 挿入したレコードのキーを取得
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                childID = rs.getInt(1); // 挿入したレコードのIDを取得
            }
        }catch (SQLException e) {
            // エラー内容をログに出力
            System.err.println("Failed to insert HTMLTableCellElement into the database.");
            e.printStackTrace();
        }

        // 挿入したレコードIDを返す
        return childID;
    }
}
