package com.sentinelai.common.config;
import java.io.IOException; import java.util.concurrent.ConcurrentHashMap; import java.util.concurrent.atomic.AtomicInteger; import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.http.HttpStatus; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
@Component public class SafetyHeadersFilter extends OncePerRequestFilter {
 private final ConcurrentHashMap<String,Window> windows=new ConcurrentHashMap<>(); private static final int LIMIT=120;
 protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{
  response.setHeader("X-Content-Type-Options","nosniff"); response.setHeader("X-Frame-Options","DENY"); response.setHeader("Referrer-Policy","no-referrer"); response.setHeader("Content-Security-Policy","default-src 'self'; frame-ancestors 'none'");
  if(request.getContentLengthLong()>1_048_576){response.sendError(HttpStatus.PAYLOAD_TOO_LARGE.value(),"Payload exceeds 1 MiB");return;}
  String key=request.getRemoteAddr(); Window w=windows.compute(key,(k,current)->current==null||System.currentTimeMillis()-current.started>=60_000?new Window():current); if(w.count.incrementAndGet()>LIMIT){response.setHeader("Retry-After","60");response.sendError(429,"Rate limit exceeded");return;} chain.doFilter(request,response);
 }
 private static final class Window { final long started=System.currentTimeMillis(); final AtomicInteger count=new AtomicInteger(); }
}
