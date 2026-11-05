
package com.mycompany.filatiweb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "FilaServlet", urlPatterns = {"/FilaServlet"})
public class FilaServlet extends HttpServlet {

    /*
     * Posição atual da fila.
     *
     * IMPORTANTE:
     * Não é salva no banco.
     * Ao reiniciar a aplicação/Tomcat,
     * a fila volta para o início.
     */
    private int posicaoAtual = 0;

    private String ultimoChamado = "";
    private String ultimoTecnico = "";
    private String ultimaDataHora = "";

    private ArrayList<Atendimento> historico =
            new ArrayList<>();

    /*
     * =========================================================
     * TELA PRINCIPAL
     * =========================================================
     */
    protected void processRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        /*
         * =====================================================
         * RECUPERA MENSAGEM DA SESSÃO
         * =====================================================
         */
        HttpSession session =
                request.getSession(false);

        String mensagem =
                (String) session.getAttribute(
                        "mensagem"
                );

        String tipoMensagem =
                (String) session.getAttribute(
                        "tipoMensagem"
                );

        /*
         * Remove imediatamente para que a mensagem
         * apareça somente uma vez.
         */
        session.removeAttribute(
                "mensagem"
        );

        session.removeAttribute(
                "tipoMensagem"
        );

        /*
         * =====================================================
         * CARREGA TÉCNICOS
         * =====================================================
         */
        ArrayList<Tecnico> tecnicos;

        try {

            tecnicos =
                    Conexao.listarTecnicos();

        } catch (SQLException e) {

            throw new ServletException(
                    "ERRO AO CARREGAR TÉCNICOS: "
                    + e.getMessage(),
                    e
            );
        }

        /*
         * =====================================================
         * CARREGA HISTÓRICO
         * =====================================================
         */
        try {

            historico =
                    Conexao.listarAtendimentos();

        } catch (SQLException e) {

            throw new ServletException(
                    "ERRO AO CARREGAR HISTÓRICO: "
                    + e.getMessage(),
                    e
            );
        }

        /*
         * Proteção caso a quantidade de técnicos
         * seja alterada.
         */
        if (!tecnicos.isEmpty()
                && posicaoAtual >= tecnicos.size()) {

            posicaoAtual = 0;
        }

        LocalTime agora =
                LocalTime.now();

        String proximo =
                "Nenhum técnico disponível";

        /*
         * =====================================================
         * DESCOBRE O PRÓXIMO TÉCNICO
         * =====================================================
         */
        if (!tecnicos.isEmpty()) {

            for (int tentativa = 0;
                    tentativa < tecnicos.size();
                    tentativa++) {

                int indice =
                        (posicaoAtual + tentativa)
                        % tecnicos.size();

                Tecnico tecnico =
                        tecnicos.get(indice);

                if (!estaNoAlmoco(
                        tecnico,
                        agora)) {

                    proximo =
                            tecnico.getNome();

                    break;
                }
            }
        }

        try (PrintWriter out =
                response.getWriter()) {

            /*
             * =================================================
             * HTML
             * =================================================
             */

            out.println("<!DOCTYPE html>");
            out.println("<html lang='pt-BR'>");

            out.println("<head>");

            out.println(
                    "<meta charset='UTF-8'>"
            );

            out.println(
                    "<meta name='viewport' "
                    + "content='width=device-width, "
                    + "initial-scale=1.0'>"
            );

            out.println(
                    "<title>FilaTI - Atendimento</title>"
            );

            /*
             * =================================================
             * CSS
             * =================================================
             */
            out.println("<style>");

            out.println(
                    "* {"
                    + "box-sizing: border-box;"
                    + "}"
            );

            out.println(
                    "body {"
                    + "margin: 0;"
                    + "font-family: Arial, Helvetica, sans-serif;"
                    + "background: #f4f6f9;"
                    + "color: #263238;"
                    + "}"
            );

            out.println(
                    ".topo {"
                    + "background: #163a5f;"
                    + "color: white;"
                    + "padding: 20px 30px;"
                    + "display: flex;"
                    + "justify-content: space-between;"
                    + "align-items: center;"
                    + "gap: 20px;"
                    + "flex-wrap: wrap;"
                    + "}"
            );

            out.println(
                    ".titulo {"
                    + "font-size: 28px;"
                    + "font-weight: bold;"
                    + "margin: 0;"
                    + "}"
            );

            out.println(
                    ".subtitulo {"
                    + "font-size: 14px;"
                    + "margin-top: 5px;"
                    + "opacity: 0.85;"
                    + "}"
            );

            out.println(
                    ".status-banco {"
                    + "background: #e8f5e9;"
                    + "color: #1b5e20;"
                    + "padding: 10px 16px;"
                    + "border-radius: 20px;"
                    + "font-size: 14px;"
                    + "font-weight: bold;"
                    + "}"
            );

            out.println(
                    ".status-erro {"
                    + "background: #ffebee;"
                    + "color: #b71c1c;"
                    + "padding: 10px 16px;"
                    + "border-radius: 20px;"
                    + "font-size: 14px;"
                    + "font-weight: bold;"
                    + "}"
            );

            out.println(
                    ".container {"
                    + "max-width: 1200px;"
                    + "margin: 30px auto;"
                    + "padding: 0 20px;"
                    + "}"
            );

            out.println(
                    ".card {"
                    + "background: white;"
                    + "border-radius: 12px;"
                    + "padding: 24px;"
                    + "margin-bottom: 24px;"
                    + "box-shadow: 0 2px 10px "
                    + "rgba(0,0,0,0.08);"
                    + "}"
            );

            out.println(
                    ".proximo-card {"
                    + "text-align: center;"
                    + "padding: 32px 20px;"
                    + "}"
            );

            out.println(
                    ".proximo-label {"
                    + "font-size: 14px;"
                    + "font-weight: bold;"
                    + "color: #607d8b;"
                    + "letter-spacing: 1px;"
                    + "}"
            );

            out.println(
                    ".proximo-nome {"
                    + "font-size: 42px;"
                    + "font-weight: bold;"
                    + "color: #163a5f;"
                    + "margin-top: 10px;"
                    + "}"
            );

            out.println(
                    ".info-tecnicos {"
                    + "margin-top: 10px;"
                    + "font-size: 13px;"
                    + "color: #78909c;"
                    + "}"
            );

            out.println(
                    ".card h2 {"
                    + "margin-top: 0;"
                    + "color: #163a5f;"
                    + "}"
            );

            out.println(
                    ".mensagem {"
                    + "padding: 15px 18px;"
                    + "border-radius: 8px;"
                    + "margin-bottom: 24px;"
                    + "font-weight: bold;"
                    + "}"
            );

            out.println(
                    ".mensagem-erro {"
                    + "background: #ffebee;"
                    + "color: #b71c1c;"
                    + "border-left: 5px solid #c62828;"
                    + "}"
            );

            out.println(
                    ".mensagem-sucesso {"
                    + "background: #e8f5e9;"
                    + "color: #1b5e20;"
                    + "border-left: 5px solid #2e7d32;"
                    + "}"
            );

            out.println(
                    ".form-atendimento {"
                    + "display: flex;"
                    + "gap: 12px;"
                    + "align-items: end;"
                    + "flex-wrap: wrap;"
                    + "}"
            );

            out.println(
                    ".campo {"
                    + "flex: 1;"
                    + "min-width: 240px;"
                    + "}"
            );

            out.println(
                    ".campo label {"
                    + "display: block;"
                    + "font-weight: bold;"
                    + "margin-bottom: 8px;"
                    + "}"
            );

            out.println(
                    ".campo input {"
                    + "width: 100%;"
                    + "padding: 12px;"
                    + "border: 1px solid #cfd8dc;"
                    + "border-radius: 6px;"
                    + "font-size: 16px;"
                    + "outline: none;"
                    + "}"
            );

            out.println(
                    ".campo input:focus {"
                    + "border-color: #1976d2;"
                    + "}"
            );

            out.println(
                    ".botao {"
                    + "border: none;"
                    + "background: #1976d2;"
                    + "color: white;"
                    + "padding: 12px 22px;"
                    + "border-radius: 6px;"
                    + "font-size: 15px;"
                    + "font-weight: bold;"
                    + "cursor: pointer;"
                    + "}"
            );

            out.println(
                    ".botao:hover {"
                    + "background: #125ca8;"
                    + "}"
            );

            out.println(
                    ".ultimo {"
                    + "margin-top: 20px;"
                    + "padding: 16px;"
                    + "background: #f5f7fa;"
                    + "border-left: 4px solid #1976d2;"
                    + "border-radius: 6px;"
                    + "}"
            );

            out.println(
                    ".ultimo strong {"
                    + "color: #163a5f;"
                    + "}"
            );

            out.println(
                    ".tabela-container {"
                    + "overflow-x: auto;"
                    + "}"
            );

            out.println(
                    "table {"
                    + "width: 100%;"
                    + "border-collapse: collapse;"
                    + "margin-top: 15px;"
                    + "}"
            );

            out.println(
                    "th {"
                    + "background: #163a5f;"
                    + "color: white;"
                    + "padding: 12px;"
                    + "text-align: left;"
                    + "}"
            );

            out.println(
                    "td {"
                    + "padding: 12px;"
                    + "border-bottom: 1px solid #e0e0e0;"
                    + "}"
            );

            out.println(
                    "tr:hover td {"
                    + "background: #f7f9fb;"
                    + "}"
            );

            out.println(
                    ".badge {"
                    + "display: inline-block;"
                    + "padding: 6px 10px;"
                    + "border-radius: 15px;"
                    + "font-size: 12px;"
                    + "font-weight: bold;"
                    + "}"
            );

            out.println(
                    ".badge-proximo {"
                    + "background: #e3f2fd;"
                    + "color: #0d47a1;"
                    + "}"
            );

            out.println(
                    ".badge-almoco {"
                    + "background: #fff3e0;"
                    + "color: #e65100;"
                    + "}"
            );

            out.println(
                    ".badge-disponivel {"
                    + "background: #e8f5e9;"
                    + "color: #1b5e20;"
                    + "}"
            );

            out.println(
                    ".vazio {"
                    + "color: #78909c;"
                    + "font-style: italic;"
                    + "}"
            );

            out.println(
                    ".rodape {"
                    + "text-align: center;"
                    + "padding: 20px;"
                    + "color: #78909c;"
                    + "font-size: 12px;"
                    + "}"
            );

            out.println(
                    "@media (max-width: 600px) {"
                    + ".proximo-nome {"
                    + "font-size: 32px;"
                    + "}"
                    + ".topo {"
                    + "padding: 18px;"
                    + "}"
                    + ".container {"
                    + "margin-top: 20px;"
                    + "}"
                    + ".botao {"
                    + "width: 100%;"
                    + "}"
                    + "}"
            );

            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            /*
             * =================================================
             * CABEÇALHO
             * =================================================
             */
            out.println("<div class='topo'>");

            out.println("<div>");

            out.println(
                    "<div class='titulo'>FilaTI</div>"
            );

            out.println(
                    "<div class='subtitulo'>"
                    + "Sistema de Rodízio "
                    + "de Atendimento de TI"
                    + "</div>"
            );

            out.println("</div>");

            /*
             * Status do banco.
             */
            try (Connection conexao =
                    Conexao.conectar()) {

                out.println(
                        "<div class='status-banco'>"
                        + "● Banco conectado"
                        + "</div>"
                );

            } catch (SQLException e) {

                out.println(
                        "<div class='status-erro'>"
                        + "● Erro na conexão"
                        + "</div>"
                );
            }

            out.println("</div>");

            out.println("<div class='container'>");

            /*
             * =================================================
             * MENSAGEM
             * =================================================
             */
            if (mensagem != null
                    && !mensagem.isEmpty()) {

                String classeMensagem;

                if ("erro".equals(tipoMensagem)) {

                    classeMensagem =
                            "mensagem mensagem-erro";

                } else {

                    classeMensagem =
                            "mensagem mensagem-sucesso";
                }

                out.println(
                        "<div class='"
                        + classeMensagem
                        + "'>"
                        + mensagem
                        + "</div>"
                );
            }

            /*
             * =================================================
             * PRÓXIMO TÉCNICO
             * =================================================
             */
            out.println(
                    "<div class='card proximo-card'>"
            );

            out.println(
                    "<div class='proximo-label'>"
                    + "PRÓXIMO ATENDIMENTO"
                    + "</div>"
            );

            out.println(
                    "<div class='proximo-nome'>"
                    + proximo
                    + "</div>"
            );

            out.println(
                    "<div class='info-tecnicos'>"
                    + tecnicos.size()
                    + " técnico(s) ativo(s)"
                    + "</div>"
            );

            out.println("</div>");

            /*
             * =================================================
             * REGISTRAR ATENDIMENTO
             * =================================================
             */
            out.println("<div class='card'>");

            out.println(
                    "<h2>Registrar Atendimento</h2>"
            );

            out.println(
                    "<form method='post' "
                    + "action='FilaServlet' "
                    + "class='form-atendimento'>"
            );

            out.println("<div class='campo'>");

            out.println(
                    "<label for='chamado'>"
                    + "Número do chamado"
                    + "</label>"
            );

            out.println(
                    "<input type='text' "
                    + "id='chamado' "
                    + "name='chamado' "
                    + "placeholder='Ex.: INC123456' "
                    + "autocomplete='off' "
                    + "required>"
            );

            out.println("</div>");

            out.println(
                    "<button type='submit' "
                    + "class='botao'>"
                    + "Registrar Atendimento"
                    + "</button>"
            );

            out.println("</form>");

            /*
             * Último atendimento.
             */
            if (!ultimoChamado.isEmpty()
                    && !ultimoTecnico.isEmpty()) {

                out.println(
                        "<div class='ultimo'>"
                );

                out.println(
                        "<strong>Último atendimento</strong>"
                );

                out.println(
                        "<br><br>Chamado: <strong>"
                        + ultimoChamado
                        + "</strong>"
                );

                out.println(
                        " &nbsp; | &nbsp; Técnico: <strong>"
                        + ultimoTecnico
                        + "</strong>"
                );

                out.println(
                        " &nbsp; | &nbsp; Data/Hora: <strong>"
                        + ultimaDataHora
                        + "</strong>"
                );

                out.println("</div>");
            }

            out.println("</div>");

            /*
             * =================================================
             * TÉCNICOS
             * =================================================
             */
            out.println("<div class='card'>");

            out.println("<h2>Técnicos</h2>");

            if (tecnicos.isEmpty()) {

                out.println(
                        "<p class='vazio'>"
                        + "Nenhum técnico ativo cadastrado."
                        + "</p>"
                );

            } else {

                out.println(
                        "<div class='tabela-container'>"
                );

                out.println("<table>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>Ordem</th>");
                out.println("<th>Técnico</th>");
                out.println("<th>Situação</th>");
                out.println("<th>Almoço</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");

                for (Tecnico tecnico : tecnicos) {

                    boolean noAlmoco =
                            estaNoAlmoco(
                                    tecnico,
                                    agora
                            );

                    String situacao;
                    String classeBadge;

                    if (tecnico.getNome()
                            .equals(proximo)) {

                        situacao =
                                "PRÓXIMO";

                        classeBadge =
                                "badge-proximo";

                    } else if (noAlmoco) {

                        situacao =
                                "ALMOÇO";

                        classeBadge =
                                "badge-almoco";

                    } else {

                        situacao =
                                "DISPONÍVEL";

                        classeBadge =
                                "badge-disponivel";
                    }

                    out.println("<tr>");

                    out.println(
                            "<td>"
                            + tecnico.getOrdem()
                            + "</td>"
                    );

                    out.println(
                            "<td><strong>"
                            + tecnico.getNome()
                            + "</strong></td>"
                    );

                    out.println(
                            "<td>"
                            + "<span class='badge "
                            + classeBadge
                            + "'>"
                            + situacao
                            + "</span>"
                            + "</td>"
                    );

                    out.println(
                            "<td>"
                            + formatarHora(
                                    tecnico.getInicioAlmoco()
                            )
                            + " - "
                            + formatarHora(
                                    tecnico.getFimAlmoco()
                            )
                            + "</td>"
                    );

                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println("</div>");
            }

            out.println("</div>");

            /*
             * =================================================
             * HISTÓRICO
             * =================================================
             */
            out.println("<div class='card'>");

            out.println(
                    "<h2>Histórico de Atendimentos</h2>"
            );

            if (historico.isEmpty()) {

                out.println(
                        "<p class='vazio'>"
                        + "Nenhum atendimento registrado."
                        + "</p>"
                );

            } else {

                out.println(
                        "<div class='tabela-container'>"
                );

                out.println("<table>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>Chamado</th>");
                out.println("<th>Técnico</th>");
                out.println("<th>Data/Hora</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");

                for (Atendimento atendimento
                        : historico) {

                    out.println("<tr>");

                    out.println(
                            "<td><strong>"
                            + atendimento.getChamado()
                            + "</strong></td>"
                    );

                    out.println(
                            "<td>"
                            + atendimento.getTecnico()
                            + "</td>"
                    );

                    out.println(
                            "<td>"
                            + atendimento.getDataHora()
                            + "</td>"
                    );

                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println("</div>");
            }

            out.println("</div>");
            out.println("</div>");

            out.println(
                    "<div class='rodape'>"
                    + "FilaTI - Sistema Interno "
                    + "de Atendimento de TI"
                    + "</div>"
            );

            out.println("</body>");
            out.println("</html>");
        }
    }

    /*
     * =========================================================
     * GET
     * =========================================================
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Conexao.testarConexao();

        processRequest(
                request,
                response
        );
    }

    /*
     * =========================================================
     * POST
     * =========================================================
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        String chamado =
                request.getParameter("chamado");

        /*
         * Remove espaços no início e no final.
         */
        if (chamado != null) {

            chamado =
                    chamado.trim();
        }

        /*
         * =====================================================
         * VALIDAÇÃO DE CAMPO VAZIO
         * =====================================================
         */
        if (chamado == null
                || chamado.isEmpty()) {

            session.setAttribute(
                    "mensagem",
                    "Informe o número do chamado."
            );

            session.setAttribute(
                    "tipoMensagem",
                    "erro"
            );

            response.sendRedirect(
                    "FilaServlet"
            );

            return;
        }

        /*
         * =====================================================
         * VERIFICA CHAMADO DUPLICADO
         * =====================================================
         */
        try {

            if (Conexao.chamadoJaExiste(
                    chamado)) {

                session.setAttribute(
                        "mensagem",
                        "⚠ O chamado "
                        + chamado
                        + " já foi registrado anteriormente."
                );

                session.setAttribute(
                        "tipoMensagem",
                        "erro"
                );

                /*
                 * IMPORTANTE:
                 *
                 * Retorna antes de escolher técnico,
                 * salvar atendimento ou avançar a fila.
                 */
                response.sendRedirect(
                        "FilaServlet"
                );

                return;
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "ERRO AO VERIFICAR "
                    + "CHAMADO DUPLICADO: "
                    + e.getMessage(),
                    e
            );
        }

        /*
         * =====================================================
         * CARREGA TÉCNICOS
         * =====================================================
         */
        ArrayList<Tecnico> tecnicos;

        try {

            tecnicos =
                    Conexao.listarTecnicos();

        } catch (SQLException e) {

            throw new ServletException(
                    "ERRO AO CARREGAR TÉCNICOS: "
                    + e.getMessage(),
                    e
            );
        }

        if (tecnicos.isEmpty()) {

            session.setAttribute(
                    "mensagem",
                    "Nenhum técnico ativo disponível."
            );

            session.setAttribute(
                    "tipoMensagem",
                    "erro"
            );

            response.sendRedirect(
                    "FilaServlet"
            );

            return;
        }

        if (posicaoAtual >= tecnicos.size()) {

            posicaoAtual = 0;
        }

        LocalTime agora =
                LocalTime.now();

        int tecnicoEscolhido = -1;

        /*
         * =====================================================
         * PROCURA TÉCNICO DISPONÍVEL
         * =====================================================
         */
        for (int tentativa = 0;
                tentativa < tecnicos.size();
                tentativa++) {

            int indice =
                    (posicaoAtual + tentativa)
                    % tecnicos.size();

            Tecnico tecnico =
                    tecnicos.get(indice);

            if (!estaNoAlmoco(
                    tecnico,
                    agora)) {

                tecnicoEscolhido =
                        indice;

                break;
            }
        }

        /*
         * Caso todos estejam indisponíveis.
         */
        if (tecnicoEscolhido == -1) {

            session.setAttribute(
                    "mensagem",
                    "Nenhum técnico disponível "
                    + "neste momento."
            );

            session.setAttribute(
                    "tipoMensagem",
                    "erro"
            );

            response.sendRedirect(
                    "FilaServlet"
            );

            return;
        }

        /*
         * =====================================================
         * REGISTRA ATENDIMENTO
         * =====================================================
         */
        Tecnico tecnico =
                tecnicos.get(
                        tecnicoEscolhido
                );

        ultimoChamado =
                chamado;

        ultimoTecnico =
                tecnico.getNome();

        LocalDateTime agoraDataHora =
                LocalDateTime.now();

        DateTimeFormatter formatoBanco =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm:ss"
                );

        DateTimeFormatter formatoTela =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm:ss"
                );

        String dataHoraBanco =
                agoraDataHora.format(
                        formatoBanco
                );

        String dataHoraTela =
                agoraDataHora.format(
                        formatoTela
                );

        ultimaDataHora =
                dataHoraTela;

        /*
         * Salva no MariaDB.
         */
        try {

            Conexao.salvarAtendimento(
                    chamado,
                    ultimoTecnico,
                    dataHoraBanco
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "ERRO AO SALVAR "
                    + "ATENDIMENTO NO BANCO: "
                    + e.getMessage(),
                    e
            );
        }

        /*
         * Somente depois de salvar com sucesso
         * avançamos a fila.
         */
        posicaoAtual =
                (tecnicoEscolhido + 1)
                % tecnicos.size();

        /*
         * Mensagem de sucesso.
         */
        session.setAttribute(
                "mensagem",
                "✓ Chamado "
                + chamado
                + " registrado para "
                + ultimoTecnico
                + "."
        );

        session.setAttribute(
                "tipoMensagem",
                "sucesso"
        );

        response.sendRedirect(
                "FilaServlet"
        );
    }

    /*
     * =========================================================
     * HORÁRIO DE ALMOÇO
     * =========================================================
     */
    private boolean estaNoAlmoco(
            Tecnico tecnico,
            LocalTime agora) {

        LocalTime inicioAlmoco =
                tecnico.getInicioAlmoco();

        LocalTime fimAlmoco =
                tecnico.getFimAlmoco();

        return !agora.isBefore(
                inicioAlmoco
        )
                && agora.isBefore(
                        fimAlmoco
                );
    }

    /*
     * Formata:
     *
     * 12:00:00 -> 12:00
     */
    private String formatarHora(
            LocalTime hora) {

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "HH:mm"
                );

        return hora.format(
                formato
        );
    }

    @Override
    public String getServletInfo() {

        return "Sistema de Rodízio "
                + "de Atendimento de TI";
    }
}
