package oplor.server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;

// 未実装
// 日付情報を表すクラス
public class Date {
    public int year;
    public int monthIndex;
    public int day;
    public int hours;
    public int minutes;
    public int seconds;
    public int milliseconds;
}
