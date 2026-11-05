package com.mycompany.filatiweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;

@WebServlet(
        name = "SalvarTecnicoServlet",
        urlPatterns = {"/SalvarTecnicoServlet"}
)
public class SalvarTecnicoServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            int id = Integer.parseInt(
                    request.getParameter("id")
            );

            String nome =
                    request.getParameter("nome");

            LocalTime inicioAlmoco =
                    LocalTime.parse(
                            request.getParameter(
                                    "inicioAlmoco"
                            )
                    );

            LocalTime fimAlmoco =
                    LocalTime.parse(
                            request.getParameter(
                                    "fimAlmoco"
                            )
                    );

            int ordem =
                    Integer.parseInt(
                            request.getParameter(
                                    "ordem"
                            )
                    );

            boolean ativo =
                    Boolean.parseBoolean(
                            request.getParameter(
                                    "ativo"
                            )
                    );

            Conexao.atualizarTecnico(
                    id,
                    nome,
                    inicioAlmoco,
                    fimAlmoco,
                    ativo,
                    ordem
            );

            response.sendRedirect(
                    "TecnicosServlet"
            );

        } catch (
                NumberFormatException |
                java.time.format.DateTimeParseException |
                SQLException e) {

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                    "<h2>Erro ao salvar técnico</h2>"
            );

            response.getWriter().println(
                    "<p>"
                    + e.getMessage()
                    + "</p>"
            );
        }
    }
}