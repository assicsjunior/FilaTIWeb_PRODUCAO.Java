
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
        name = "RelatorioServlet",
        urlPatterns = {"/RelatorioServlet"}
)
public class RelatorioServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica se o usuário está logado
        if (request.getSession().getAttribute("usuarioLogado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String dataInicial =
                request.getParameter("dataInicial");

        String dataFinal =
                request.getParameter("dataFinal");

        String tecnicoFiltro =
                request.getParameter("tecnico");

        try {

            ArrayList<Atendimento> atendimentos =
                    Conexao.listarAtendimentosRelatorio(
                            dataInicial,
                            dataFinal,
                            tecnicoFiltro
                    );

            ArrayList<String[]> resumoTecnicos =
                    Conexao.listarResumoTecnicos(
                            dataInicial,
                            dataFinal,
                            tecnicoFiltro
                    );

            ArrayList<Tecnico> tecnicos =
                    Conexao.listarTodosTecnicos();

            request.setAttribute(
                    "atendimentos",
                    atendimentos
            );

            request.setAttribute(
                    "resumoTecnicos",
                    resumoTecnicos
            );

            request.setAttribute(
                    "tecnicos",
                    tecnicos
            );

            request.setAttribute(
                    "dataInicial",
                    dataInicial
            );

            request.setAttribute(
                    "dataFinal",
                    dataFinal
            );

            request.setAttribute(
                    "tecnicoFiltro",
                    tecnicoFiltro
            );

            request.getRequestDispatcher(
                    "relatorio.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                    "<h2>Erro ao carregar relatório</h2>"
            );

            response.getWriter().println(
                    "<p>"
                    + e.getMessage()
                    + "</p>"
            );
        }
    }
}
