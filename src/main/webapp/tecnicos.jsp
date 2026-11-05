<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.filatiweb.Tecnico"%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>FilaTI - Técnicos</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f6f9;
            color: #333;
        }

        .header {
            background: #163a5f;
            color: white;
            padding: 25px 30px;
        }

        .header h1 {
            margin: 0;
            font-size: 28px;
        }

        .header p {
            margin: 6px 0 0;
            opacity: 0.85;
        }

        .container {
            max-width: 1100px;
            margin: 30px auto;
            padding: 0 20px;
        }

        .card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.08);
        }

        .card h2 {
            margin-top: 0;
            color: #163a5f;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th {
            background: #163a5f;
            color: white;
            padding: 13px;
            text-align: left;
        }

        td {
            padding: 13px;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background: #f7f9fb;
        }

        .ativo {
            display: inline-block;
            background: #dff5e5;
            color: #18733c;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
        }

        .inativo {
            display: inline-block;
            background: #fce0e0;
            color: #a52a2a;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
        }

        .voltar {
            display: inline-block;
            margin-bottom: 20px;
            background: #163a5f;
            color: white;
            text-decoration: none;
            padding: 10px 16px;
            border-radius: 7px;
        }

        .voltar:hover {
            background: #0f2b46;
        }

        .editar {
            display: inline-block;
            background: #f0ad4e;
            color: white;
            text-decoration: none;
            padding: 7px 12px;
            border-radius: 6px;
            font-size: 13px;
            font-weight: bold;
        }

        .editar:hover {
            background: #ec971f;
        }

        .footer {
            text-align: center;
            color: #777;
            padding: 25px;
            font-size: 13px;
        }

        @media (max-width: 700px) {

            table {
                font-size: 13px;
            }

            th,
            td {
                padding: 8px;
            }

            .card {
                padding: 15px;
                overflow-x: auto;
            }
        }

    </style>

</head>

<body>

    <div class="header">

        <h1>FilaTI</h1>

        <p>
            Administração dos técnicos
        </p>

    </div>

    <div class="container">

        <a class="voltar"
           href="FilaServlet">

            ← Voltar para a fila

        </a>

        <div class="card">

            <h2>👨‍💻 Técnicos cadastrados</h2>

            <p>
                Nesta tela você pode consultar e editar
                os técnicos da fila.
            </p>

            <table>

                <thead>

                    <tr>

                        <th>ID</th>

                        <th>Ordem</th>

                        <th>Nome</th>

                        <th>Início almoço</th>

                        <th>Fim almoço</th>

                        <th>Status</th>

                        <th>Ação</th>

                    </tr>

                </thead>

                <tbody>

                    <%
                        ArrayList<Tecnico> tecnicos =
                                (ArrayList<Tecnico>)
                                request.getAttribute("tecnicos");

                        if (tecnicos != null) {

                            for (Tecnico tecnico : tecnicos) {
                    %>

                    <tr>

                        <td>
                            <%= tecnico.getId() %>
                        </td>

                        <td>
                            <%= tecnico.getOrdem() %>
                        </td>

                        <td>
                            <strong>
                                <%= tecnico.getNome() %>
                            </strong>
                        </td>

                        <td>
                            <%= tecnico.getInicioAlmoco() %>
                        </td>

                        <td>
                            <%= tecnico.getFimAlmoco() %>
                        </td>

                        <td>

                            <% if (tecnico.isAtivo()) { %>

                                <span class="ativo">
                                    ATIVO
                                </span>

                            <% } else { %>

                                <span class="inativo">
                                    INATIVO
                                </span>

                            <% } %>

                        </td>

                        <td>

                            <a
                                class="editar"
                                href="EditarTecnicoServlet?id=<%= tecnico.getId() %>">

                                ✏️ Editar

                            </a>

                        </td>

                    </tr>

                    <%
                            }

                        }
                    %>

                </tbody>

            </table>

        </div>

    </div>

    <div class="footer">

        FilaTI - Sistema Interno de Atendimento de TI

    </div>

</body>

</html>