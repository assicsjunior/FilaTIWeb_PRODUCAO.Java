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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet(
        name = "ExportarRelatorioExcelServlet",
        urlPatterns = {"/ExportarRelatorioExcelServlet"}
)
public class ExportarRelatorioExcelServlet
        extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String dataInicial =
                request.getParameter("dataInicial");

        String dataFinal =
                request.getParameter("dataFinal");

        String tecnicoFiltro =
                request.getParameter("tecnico");

        try {

            /*
             * Busca os mesmos dados utilizados
             * no relatório.
             */
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

            /*
             * Cria o arquivo Excel.
             */
            try (Workbook workbook =
                    new XSSFWorkbook()) {

                /*
                 * =================================================
                 * ABA 1 - RESUMO
                 * =================================================
                 */

                Sheet resumo =
                        workbook.createSheet(
                                "Resumo"
                        );

                /*
                 * Fonte do cabeçalho.
                 */
                Font fonteCabecalho =
                        workbook.createFont();

                fonteCabecalho.setBold(true);

                /*
                 * Estilo do cabeçalho.
                 */
                CellStyle estiloCabecalho =
                        workbook.createCellStyle();

                estiloCabecalho.setFont(
                        fonteCabecalho
                );

                /*
                 * Título.
                 */
                Row linhaTitulo =
                        resumo.createRow(0);

                Cell celulaTitulo =
                        linhaTitulo.createCell(0);

                celulaTitulo.setCellValue(
                        "FilaTI - Resumo de Atendimentos"
                );

                celulaTitulo.setCellStyle(
                        estiloCabecalho
                );

                /*
                 * Total.
                 */
                Row linhaTotal =
                        resumo.createRow(2);

                linhaTotal
                        .createCell(0)
                        .setCellValue(
                                "Total de atendimentos"
                        );

                linhaTotal
                        .createCell(1)
                        .setCellValue(
                                atendimentos.size()
                        );

                /*
                 * Cabeçalho da tabela.
                 */
                Row linhaCabecalho =
                        resumo.createRow(4);

                Cell celulaTecnico =
                        linhaCabecalho.createCell(0);

                celulaTecnico.setCellValue(
                        "Técnico"
                );

                celulaTecnico.setCellStyle(
                        estiloCabecalho
                );

                Cell celulaQuantidade =
                        linhaCabecalho.createCell(1);

                celulaQuantidade.setCellValue(
                        "Quantidade"
                );

                celulaQuantidade.setCellStyle(
                        estiloCabecalho
                );

                /*
                 * Dados do resumo.
                 */
                int linha = 5;

                for (String[] item
                        : resumoTecnicos) {

                    Row row =
                            resumo.createRow(
                                    linha++
                            );

                    row.createCell(0)
                            .setCellValue(
                                    item[0]
                            );

                    row.createCell(1)
                            .setCellValue(
                                    Integer.parseInt(
                                            item[1]
                                    )
                            );
                }

                resumo.autoSizeColumn(0);
                resumo.autoSizeColumn(1);


                /*
                 * =================================================
                 * ABA 2 - ATENDIMENTOS
                 * =================================================
                 */

                Sheet atendimentosSheet =
                        workbook.createSheet(
                                "Atendimentos"
                        );

                /*
                 * Cabeçalho.
                 */
                Row cabecalho =
                        atendimentosSheet
                                .createRow(0);

                Cell chamado =
                        cabecalho.createCell(0);

                chamado.setCellValue(
                        "Chamado"
                );

                chamado.setCellStyle(
                        estiloCabecalho
                );

                Cell tecnico =
                        cabecalho.createCell(1);

                tecnico.setCellValue(
                        "Técnico"
                );

                tecnico.setCellStyle(
                        estiloCabecalho
                );

                Cell dataHora =
                        cabecalho.createCell(2);

                dataHora.setCellValue(
                        "Data/Hora"
                );

                dataHora.setCellStyle(
                        estiloCabecalho
                );

                /*
                 * Dados dos atendimentos.
                 */
                int linhaAtendimento = 1;

                for (Atendimento atendimento
                        : atendimentos) {

                    Row row =
                            atendimentosSheet
                                    .createRow(
                                            linhaAtendimento++
                                    );

                    row.createCell(0)
                            .setCellValue(
                                    atendimento
                                            .getChamado()
                            );

                    row.createCell(1)
                            .setCellValue(
                                    atendimento
                                            .getTecnico()
                            );

                    row.createCell(2)
                            .setCellValue(
                                    atendimento
                                            .getDataHora()
                            );
                }

                atendimentosSheet
                        .autoSizeColumn(0);

                atendimentosSheet
                        .autoSizeColumn(1);

                atendimentosSheet
                        .autoSizeColumn(2);


                /*
                 * Configura a resposta para download.
                 */
                response.setContentType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                );

                response.setHeader(
                        "Content-Disposition",
                        "attachment; filename=\"relatorio_filati.xlsx\""
                );

                /*
                 * Envia o arquivo para o navegador.
                 */
                workbook.write(
                        response.getOutputStream()
                );
            }

        } catch (SQLException e) {

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                    "<h2>Erro ao gerar relatório Excel</h2>"
            );

            response.getWriter().println(
                    "<p>"
                    + e.getMessage()
                    + "</p>"
            );
        }
    }
}