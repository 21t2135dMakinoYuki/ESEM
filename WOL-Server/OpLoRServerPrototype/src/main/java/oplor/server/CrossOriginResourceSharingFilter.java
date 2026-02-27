package oplor.server;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CrossOriginResourceSharingFilter", urlPatterns = {"/*"})
public class CrossOriginResourceSharingFilter implements Filter {
    private static final String ALLOWED_ORIGIN = "http://localhost:5000";   
    private static final String ALLOWED_METHODS = "GET, HEAD, OPTIONS, POST, PUT";  
    private static final String ALLOWED_HEADERS = "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers";  
    private static final String ALLOW_CREDENTIALS = "true"; 
    private static final String MAX_AGE = "-1"; 

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            setCORSHeaders(httpResponse);  
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        response.setHeader("Access-Control-Allow-Credentials", ALLOW_CREDENTIALS);
        response.setHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
        response.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        response.setHeader("Access-Control-Max-Age", MAX_AGE);
    }
}
