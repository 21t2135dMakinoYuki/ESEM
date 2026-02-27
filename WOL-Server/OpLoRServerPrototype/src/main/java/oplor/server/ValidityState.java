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
// HTMLフォーム要素の検証状態を表すクラス
public class ValidityState {
    public boolean badInput;
    public boolean customError;//ReadOnly
    public boolean patternMismatch;//ReadOnly
    public boolean rangeOverflow;//ReadOnly
    public boolean rangeUnderflow;//ReadOnly
    public boolean stepMismatch;//ReadOnly
    public boolean tooLong;
    public boolean tooShort;//ReadOnly
    public boolean typeMisMatch;//ReadOnly
    public boolean valid;//ReadOnly
    public boolean valueMissing;//ReadOnly
}
