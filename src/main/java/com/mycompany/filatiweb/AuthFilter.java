/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.filatiweb;

/**
 *
 * @author P0170848
 */



import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        String uri =
                httpRequest.getRequestURI();

        String contexto =
                httpRequest.getContextPath();

        String pagina =
                uri.substring(
                        contexto.length()
                );

        boolean logado =
                httpRequest.getSession()
                        .getAttribute("usuarioId")
                        != null;

        boolean paginaPublica =
                pagina.equals("/login.jsp")
                || pagina.equals("/LoginServlet");

        if (logado || paginaPublica) {

            chain.doFilter(
                    request,
                    response
            );

        } else {

            httpResponse.sendRedirect(
                    contexto + "/login.jsp"
            );
        }
    }
}