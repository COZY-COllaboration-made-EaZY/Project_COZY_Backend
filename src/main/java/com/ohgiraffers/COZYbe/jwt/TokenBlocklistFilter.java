//package com.ohgiraffers.COZYbe.jwt;
//
//import com.ohgiraffers.COZYbe.domain.auth.domain.service.BlocklistService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Component
//public class TokenBlocklistFilter extends OncePerRequestFilter {
//
//    BlocklistService blockListService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req,
//                                    HttpServletResponse res,
//                                    FilterChain chain)
//            throws IOException, ServletException {
//        Jwt jwt = (Jwt) req.getAttribute("jwt"); // Resource-Server가 주입
//        if (jwt != null && blockListService.exists(jwt.getId())) {
//            res.sendError(HttpStatus.UNAUTHORIZED.value(), "Logged-out token");
//            return;
//        }
//        chain.doFilter(req, res);
//    }
//
//}
