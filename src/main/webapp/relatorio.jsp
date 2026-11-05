<%-- 
    Document   : relatorio
    Created on : 28 de out. de 2026, 16:29:44
    Author     : P0170848
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.filatiweb.Atendimento"%>
<%@page import="com.mycompany.filatiweb.Tecnico"%>

<%
    ArrayList<Atendimento> atendimentos =
            (ArrayList<Atendimento>)
            request.getAttribute("atendimentos");

    ArrayList<String[]> resumoTecnicos =
            (ArrayList<String[]>)
            request.getAttribute("resumoTecnicos");

    ArrayList<Tecnico> tecnicos =
            (ArrayList<Tecnico>)
            request.getAttribute("tecnicos");

    String dataInicial =
            (String) request.getAttribute("dataInicial");

    String dataFinal =
            (String) request.getAttribute("dataFinal");

    String tecnicoFiltro =
            (String) request.getAttribute("tecnicoFiltro");

    int total = 0;

    if (atendimentos != null) {
        total = atendimentos.size();
    }

    int maiorQuantidade = 0;

    if (resumoTecnicos != null) {

        for (String[] resumo : resumoTecnicos) {

            int quantidade =
                    Integer.parseInt(resumo[1]);

            if (quantidade > maiorQuantidade) {
                maiorQuantidade = quantidade;
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>FilaTI - Relatório</title>

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
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }

        .acoes {
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }

        .voltar {
            display: inline-block;
            background: #163a5f;
            color: white;
            text-decoration: none;
            padding: 10px 16px;
            border-radius: 7px;
        }

        .voltar:hover {
            background: #0f2b46;
        }

        .exportar {
            background: #198754;
            color: white;
        }

        .exportar:hover {
            background: #146c43;
        }

        .card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            margin-bottom: 25px;
            box-shadow: 0 3px 12px rgba(0,0,0,0.08);
        }

        .card h2 {
            margin-top: 0;
            color: #163a5f;
        }

        .filtros {
            display: grid;
            grid-template-columns:
                1fr 1fr 1fr auto auto;
            gap: 15px;
            align-items: end;
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

        .botao {
            border: none;
            border-radius: 7px;
            padding: 11px 18px;
            font-size: 15px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .filtrar {
            background: #198754;
            color: white;
        }

        .filtrar:hover {
            background: #146c43;
        }

        .limpar {
            background: #6c757d;
            color: white;
        }

        .limpar:hover {
            background: #565e64;
        }

        .resumo {
            display: flex;
            gap: 20px;
            margin-bottom: 25px;
            flex-wrap: wrap;
        }

        .indicador {
            background: #eef4fa;
            border-left: 5px solid #163a5f;
            padding: 15px 20px;
            border-radius: 7px;
            min-width: 220px;
        }

        .indicador .numero {
            display: block;
            font-size: 30px;
            font-weight: bold;
            color: #163a5f;
        }

        .indicador .texto {
            color: #666;
            font-size: 14px;
        }

        /*
         * GRÁFICO
         */

        .grafico {
            margin-top: 20px;
        }

        .barra-linha {
            display: grid;
            grid-template-columns: 100px 1fr 50px;
            gap: 12px;
            align-items: center;
            margin-bottom: 14px;
        }

        .nome-tecnico {
            font-weight: bold;
            color: #444;
        }

        .barra-fundo {
            width: 100%;
            height: 28px;
            background: #e9ecef;
            border-radius: 6px;
            overflow: hidden;
        }

        .barra {
            height: 100%;
            background: #163a5f;
            border-radius: 6px;
            min-width: 2px;
        }

        .valor-barra {
            font-weight: bold;
            color: #163a5f;
            text-align: right;
        }

        .tabela-resumo {
            width: 100%;
            border-collapse: collapse;
            margin-top: 25px;
        }

        .tabela-resumo th {
            background: #163a5f;
            color: white;
            padding: 12px;
            text-align: left;
        }

        .tabela-resumo td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }

        .quantidade {
            font-weight: bold;
            color: #163a5f;
        }

        .tabela-atendimentos {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        .tabela-atendimentos th {
            background: #163a5f;
            color: white;
            padding: 13px;
            text-align: left;
        }

        .tabela-atendimentos td {
            padding: 13px;
            border-bottom: 1px solid #ddd;
        }

        .tabela-atendimentos tr:hover {
            background: #f7f9fb;
        }

        .sem-dados {
            text-align: center;
            color: #777;
            padding: 30px !important;
        }

        .rodape {
            text-align: center;
            color: #777;
            padding: 25px;
            font-size: 13px;
        }

        @media (max-width: 900px) {

            .filtros {
                grid-template-columns: 1fr 1fr;
            }

        }

        @media (max-width: 600px) {

            .filtros {
                grid-template-columns: 1fr;
            }

            .card {
                padding: 15px;
                overflow-x: auto;
            }

            .barra-linha {
                grid-template-columns: 80px 1fr 35px;
                gap: 8px;
                font-size: 13px;
            }

            .tabela-resumo,
            .tabela-atendimentos {
                font-size: 13px;
            }

            .tabela-resumo th,
            .tabela-resumo td,
            .tabela-atendimentos th,
            .tabela-atendimentos td {
                padding: 8px;
            }

        }

    </style>

</head>

<body>

    <div class="header">

        <h1>FilaTI</h1>

        <p>
            Relatório de atendimentos
        </p>

    </div>

    <div class="container">

        <!-- AÇÕES -->

        <div class="acoes">

            <a class="voltar"
               href="FilaServlet">

                ← Voltar para a fila

            </a>


            <!-- EXPORTAR PARA EXCEL -->

            <form
                action="ExportarRelatorioExcelServlet"
                method="get"
                style="margin: 0;">

                <input
                    type="hidden"
                    name="dataInicial"
                    value="<%= dataInicial != null
                            ? dataInicial
                            : "" %>">

                <input
                    type="hidden"
                    name="dataFinal"
                    value="<%= dataFinal != null
                            ? dataFinal
                            : "" %>">

                <input
                    type="hidden"
                    name="tecnico"
                    value="<%= tecnicoFiltro != null
                            ? tecnicoFiltro
                            : "" %>">

                <button
                    type="submit"
                    class="botao exportar">

                    📥 Exportar para Excel

                </button>

            </form>

        </div>


        <!-- FILTROS -->

        <div class="card">

            <h2>🔎 Filtros</h2>

            <form
                action="RelatorioServlet"
                method="get">

                <div class="filtros">

                    <div class="campo">

                        <label for="dataInicial">
                            Data inicial
                        </label>

                        <input
                            type="date"
                            id="dataInicial"
                            name="dataInicial"
                            value="<%= dataInicial != null
                                    ? dataInicial
                                    : "" %>">

                    </div>


                    <div class="campo">

                        <label for="dataFinal">
                            Data final
                        </label>

                        <input
                            type="date"
                            id="dataFinal"
                            name="dataFinal"
                            value="<%= dataFinal != null
                                    ? dataFinal
                                    : "" %>">

                    </div>


                    <div class="campo">

                        <label for="tecnico">
                            Técnico
                        </label>

                        <select
                            id="tecnico"
                            name="tecnico">

                            <option value="">
                                Todos os técnicos
                            </option>

                            <%
                                if (tecnicos != null) {

                                    for (Tecnico tecnico
                                            : tecnicos) {

                                        boolean selecionado =
                                                tecnicoFiltro != null
                                                && tecnicoFiltro.equals(
                                                        tecnico.getNome()
                                                );
                            %>

                            <option
                                value="<%= tecnico.getNome() %>"
                                <%= selecionado
                                        ? "selected"
                                        : "" %>>

                                <%= tecnico.getNome() %>

                            </option>

                            <%
                                    }
                                }
                            %>

                        </select>

                    </div>


                    <button
                        type="submit"
                        class="botao filtrar">

                        🔎 Filtrar

                    </button>


                    <a
                        href="RelatorioServlet"
                        class="botao limpar">

                        🔄 Limpar

                    </a>

                </div>

            </form>

        </div>


        <!-- RESUMO -->

        <div class="card">

            <h2>📊 Resumo</h2>

            <div class="resumo">

                <div class="indicador">

                    <span class="numero">
                        <%= total %>
                    </span>

                    <span class="texto">
                        Total de atendimentos
                    </span>

                </div>

            </div>


            <!-- GRÁFICO -->

            <h3>
                📈 Atendimentos por técnico
            </h3>

            <div class="grafico">

                <%
                    if (resumoTecnicos != null
                            && !resumoTecnicos.isEmpty()
                            && maiorQuantidade > 0) {

                        for (String[] resumo
                                : resumoTecnicos) {

                            int quantidade =
                                    Integer.parseInt(
                                            resumo[1]
                                    );

                            int percentual =
                                    (quantidade * 100)
                                    / maiorQuantidade;
                %>

                <div class="barra-linha">

                    <div class="nome-tecnico">
                        <%= resumo[0] %>
                    </div>

                    <div class="barra-fundo">

                        <div
                            class="barra"
                            style="width: <%= percentual %>%;">
                        </div>

                    </div>

                    <div class="valor-barra">
                        <%= quantidade %>
                    </div>

                </div>

                <%
                        }

                    } else {
                %>

                <div class="sem-dados">

                    Nenhum atendimento encontrado.

                </div>

                <%
                    }
                %>

            </div>


            <!-- TABELA RESUMO -->

            <h3>
                Atendimentos por técnico
            </h3>

            <table class="tabela-resumo">

                <thead>

                    <tr>

                        <th>
                            Técnico
                        </th>

                        <th>
                            Quantidade
                        </th>

                    </tr>

                </thead>

                <tbody>

                    <%
                        if (resumoTecnicos != null
                                && !resumoTecnicos.isEmpty()) {

                            for (String[] resumo
                                    : resumoTecnicos) {
                    %>

                    <tr>

                        <td>
                            <strong>
                                <%= resumo[0] %>
                            </strong>
                        </td>

                        <td class="quantidade">
                            <%= resumo[1] %>
                        </td>

                    </tr>

                    <%
                            }

                        } else {
                    %>

                    <tr>

                        <td
                            colspan="2"
                            class="sem-dados">

                            Nenhum atendimento encontrado.

                        </td>

                    </tr>

                    <%
                        }
                    %>

                </tbody>

            </table>

        </div>


        <!-- DETALHAMENTO -->

        <div class="card">

            <h2>📋 Detalhamento dos atendimentos</h2>

            <table class="tabela-atendimentos">

                <thead>

                    <tr>

                        <th>
                            Chamado
                        </th>

                        <th>
                            Técnico
                        </th>

                        <th>
                            Data/Hora
                        </th>

                    </tr>

                </thead>

                <tbody>

                    <%
                        if (atendimentos != null
                                && !atendimentos.isEmpty()) {

                            for (Atendimento atendimento
                                    : atendimentos) {
                    %>

                    <tr>

                        <td>
                            <strong>
                                <%= atendimento.getChamado() %>
                            </strong>
                        </td>

                        <td>
                            <%= atendimento.getTecnico() %>
                        </td>

                        <td>
                            <%= atendimento.getDataHora() %>
                        </td>

                    </tr>

                    <%
                            }

                        } else {
                    %>

                    <tr>

                        <td
                            colspan="3"
                            class="sem-dados">

                            Nenhum atendimento encontrado.

                        </td>

                    </tr>

                    <%
                        }
                    %>

                </tbody>

            </table>

        </div>

    </div>


    <div class="rodape">

        FilaTI - Sistema Interno de Atendimento de TI

    </div>

</body>

</html>