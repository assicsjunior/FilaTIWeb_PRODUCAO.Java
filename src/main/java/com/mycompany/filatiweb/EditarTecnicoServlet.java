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
        name = "EditarTecnicoServlet",
        urlPatterns = {"/EditarTecnicoServlet"}
)
public class EditarTecnicoServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto =
                request.getParameter("id");

        if (idTexto == null || idTexto.isBlank()) {

            response.sendRedirect(
                    "TecnicosServlet"
            );

            return;
        }

        try {

            int id =
                    Integer.parseInt(idTexto);

            ArrayList<Tecnico> tecnicos =
                    Conexao.listarTodosTecnicos();

            Tecnico tecnicoEncontrado = null;

            for (Tecnico tecnico : tecnicos) {

                if (tecnico.getId() == id) {

                    tecnicoEncontrado = tecnico;
                    break;
                }
            }

            if (tecnicoEncontrado == null) {

                response.sendRedirect(
                        "TecnicosServlet"
                );

                return;
            }

            request.setAttribute(
                    "tecnico",
                    tecnicoEncontrado
            );

            request.getRequestDispatcher(
                    "editarTecnico.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (NumberFormatException |
                 SQLException e) {

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                    "<h2>Erro ao carregar técnico</h2>"
            );

            response.getWriter().println(
                    "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}