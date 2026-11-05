<%-- 
    Document   : editarTecnico
    Created on : 28 de out. de 2026, 16:13:20
    Author     : P0170848
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.filatiweb.Tecnico"%>

<%
    Tecnico tecnico =
            (Tecnico) request.getAttribute("tecnico");

    if (tecnico == null) {
        response.sendRedirect("TecnicosServlet");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>FilaTI - Editar Técnico</title>

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
            max-width: 700px;
            margin: 30px auto;
            padding: 0 20px;
        }

        .card {
            background: white;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.08);
        }

        .card h2 {
            margin-top: 0;
            color: #163a5f;
        }

        .campo {
            margin-bottom: 20px;
        }

        .campo label {
            display: block;
            font-weight: bold;
            margin-bottom: 7px;
            color: #444;
        }

        .campo input,
        .campo select {
            width: 100%;
            padding: 11px;
            border: 1px solid #ccc;
            border-radius: 7px;
            font-size: 15px;
        }

        .campo input:focus,
        .campo select:focus {
            outline: none;
            border-color: #163a5f;
        }

        .botoes {
            display: flex;
            gap: 10px;
            margin-top: 25px;
        }

        .botao {
            border: none;
            border-radius: 7px;
            padding: 12px 20px;
            font-size: 15px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .salvar {
            background: #198754;
            color: white;
        }

        .salvar:hover {
            background: #146c43;
        }

        .cancelar {
            background: #6c757d;
            color: white;
        }

        .cancelar:hover {
            background: #565e64;
        }

        .info {
            background: #eef4fa;
            border-left: 4px solid #163a5f;
            padding: 12px;
            margin-bottom: 25px;
            border-radius: 5px;
        }

        .id {
            color: #777;
            font-size: 13px;
        }

        @media (max-width: 600px) {

            .card {
                padding: 20px;
            }

            .botoes {
                flex-direction: column;
            }

            .botao {
                text-align: center;
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

        <div class="card">

            <h2>✏️ Editar Técnico</h2>

            <div class="info">

                Editando o técnico:

                <strong>
                    <%= tecnico.getNome() %>
                </strong>

                <br>

                <span class="id">
                    ID: <%= tecnico.getId() %>
                </span>

            </div>

            <form
                action="SalvarTecnicoServlet"
                method="post">

                <input
                    type="hidden"
                    name="id"
                    value="<%= tecnico.getId() %>">

                <div class="campo">

                    <label for="nome">
                        Nome
                    </label>

                    <input
                        type="text"
                        id="nome"
                        name="nome"
                        value="<%= tecnico.getNome() %>"
                        required>

                </div>

                <div class="campo">

                    <label for="inicioAlmoco">
                        Início do almoço
                    </label>

                    <input
                        type="time"
                        id="inicioAlmoco"
                        name="inicioAlmoco"
                        value="<%= tecnico.getInicioAlmoco() %>"
                        required>

                </div>

                <div class="campo">

                    <label for="fimAlmoco">
                        Fim do almoço
                    </label>

                    <input
                        type="time"
                        id="fimAlmoco"
                        name="fimAlmoco"
                        value="<%= tecnico.getFimAlmoco() %>"
                        required>

                </div>

                <div class="campo">

                    <label for="ordem">
                        Ordem na fila
                    </label>

                    <input
                        type="number"
                        id="ordem"
                        name="ordem"
                        value="<%= tecnico.getOrdem() %>"
                        min="1"
                        required>

                </div>

                <div class="campo">

                    <label for="ativo">
                        Status
                    </label>

                    <select
                        id="ativo"
                        name="ativo">

                        <option
                            value="true"
                            <%= tecnico.isAtivo()
                                ? "selected"
                                : "" %>>
                            Ativo
                        </option>

                        <option
                            value="false"
                            <%= !tecnico.isAtivo()
                                ? "selected"
                                : "" %>>
                            Inativo
                        </option>

                    </select>

                </div>

                <div class="botoes">

                    <button
                        type="submit"
                        class="botao salvar">

                        💾 Salvar

                    </button>

                    <a
                        href="TecnicosServlet"
                        class="botao cancelar">

                        Cancelar

                    </a>

                </div>

            </form>

        </div>

    </div>

</body>

</html>