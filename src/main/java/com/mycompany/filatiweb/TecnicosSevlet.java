/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.filatiweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(
        name = "TecnicosServlet",
        urlPatterns = {"/TecnicosServlet"}
)
public class TecnicosSevlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            ArrayList<Tecnico> tecnicos =
                    Conexao.listarTodosTecnicos();

            request.setAttribute(
                    "tecnicos",
                    tecnicos
            );

            request.getRequestDispatcher(
                    "tecnicos.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                    "<h2>Erro ao carregar técnicos</h2>"
            );

            response.getWriter().println(
                    "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}