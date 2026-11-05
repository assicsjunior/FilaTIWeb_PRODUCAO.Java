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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/LoginServlet"}
)
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String usuario =
                request.getParameter("usuario");

        String senha =
                request.getParameter("senha");

        String sql =
                "SELECT id, usuario, nome "
                + "FROM usuarios "
                + "WHERE usuario = ? "
                + "AND senha = ? "
                + "AND ativo = TRUE";

        try (
                Connection conexao =
                        Conexao.conectar();

                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ) {

            comando.setString(
                    1,
                    usuario
            );

            comando.setString(
                    2,
                    senha
            );

            try (
                    ResultSet resultado =
                            comando.executeQuery()
            ) {

                if (resultado.next()) {

                    /*
                     * Login correto.
                     *
                     * Criamos a sessão do usuário.
                     */
                    HttpSession sessao =
                            request.getSession();

                    sessao.setAttribute(
                            "usuarioId",
                            resultado.getInt("id")
                    );

                    sessao.setAttribute(
                            "usuario",
                            resultado.getString("usuario")
                    );

                    sessao.setAttribute(
                            "nomeUsuario",
                            resultado.getString("nome")
                    );

                    /*
                     * Entra na fila.
                     */
                    response.sendRedirect(
                            "FilaServlet"
                    );

                } else {

                    /*
                     * Usuário ou senha incorretos.
                     */
                    request.setAttribute(
                            "erro",
                            "Usuário ou senha inválidos."
                    );

                    request.getRequestDispatcher(
                            "login.jsp"
                    ).forward(
                            request,
                            response
                    );
                }
            }

        } catch (SQLException e) {

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                    "<h2>Erro ao realizar login</h2>"
            );

            response.getWriter().println(
                    "<p>"
                    + e.getMessage()
                    + "</p>"
            );
        }
    }
}