package oplor.server;

import oplor.server.event.MouseEvent;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import oplor.server.event.*;
import oplor.server.node.*;

public class LogWriter extends Thread {
    private final LinkedBlockingQueue<Log> logs;   
    private final Properties properties;        

    String dbPath = System.getenv("DB_PATH");  
    public LogWriter(LinkedBlockingQueue<Log> logs) {
        this.logs = logs;
        SQLiteConfig sqLiteConfig = new SQLiteConfig();
        sqLiteConfig.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);   
        sqLiteConfig.setJournalMode(SQLiteConfig.JournalMode.WAL); 
        this.properties = sqLiteConfig.toProperties();
    }

    public void run() {
        while (true) {
            int logID = -1;
            try {
                Class.forName("org.sqlite.JDBC");

                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath, this.properties)){
                    connection.setAutoCommit(false);

                    while (logs.size() != 0) {
                        Log log = logs.poll();  
                        logID = log.sqliteInsert(connection);

                        Event event = log.getEvent();
                        Node node = log.getNode();

                        System.out.println("--LogWriter--");
                        System.out.println(log);

                        if (event instanceof MouseEvent) {
                            MouseEvent mouseEvent = (MouseEvent) event;
                            System.out.println("logID: " + logID + " pageX: " + mouseEvent.getPageX() + ", pageY: " + mouseEvent.getPageY());
                        }

                        System.out.println("SQLite size:" + logs.size());
                        System.out.println("--Fin(LogWriter)--");
                        
                        event.sqliteInsert(logID, connection);
                        node.sqliteInsert(logID, connection);
                    }
                    connection.commit();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();    
            } catch (SQLException e) {
                e.printStackTrace();    
            }
        }
    }
}
