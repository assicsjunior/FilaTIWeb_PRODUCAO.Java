package com.mycompany.filatiweb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.time.LocalTime;

public class Conexao {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/filati";

    private static final String USUARIO = "filati_app";

    private static final String SENHA = "1234";

    public static Connection conectar() throws SQLException {

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver MariaDB não foi encontrado.",
                    e
            );
        }

        return DriverManager.getConnection(
                URL,
                USUARIO,
                SENHA
        );
    }

    public static void testarConexao() {

        try (Connection conexao = conectar()) {

            System.out.println(
                    "CONEXÃO COM O BANCO REALIZADA COM SUCESSO!"
            );

        } catch (SQLException e) {

            System.out.println(
                    "ERRO AO CONECTAR COM O BANCO:"
            );

            System.out.println(
                    e.getMessage()
            );
        }
    }

    /*
     * Verifica se o chamado já foi registrado.
     */
    public static boolean chamadoJaExiste(
            String chamado) throws SQLException {

        String sql =
                "SELECT COUNT(*) AS quantidade "
                + "FROM atendimentos "
                + "WHERE chamado = ?";

        try (Connection conexao = conectar();
             java.sql.PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(1, chamado);

            try (ResultSet resultado =
                    comando.executeQuery()) {

                if (resultado.next()) {

                    int quantidade =
                            resultado.getInt(
                                    "quantidade"
                            );

                    return quantidade > 0;
                }
            }
        }

        return false;
    }

    /*
     * Salva um novo atendimento.
     */
    public static void salvarAtendimento(
            String chamado,
            String tecnico,
            String dataHora) throws SQLException {

        String sql =
                "INSERT INTO atendimentos "
                + "(chamado, tecnico, data_hora) "
                + "VALUES (?, ?, ?)";

        try (Connection conexao = conectar();
             java.sql.PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(1, chamado);
            comando.setString(2, tecnico);
            comando.setString(3, dataHora);

            comando.executeUpdate();
        }
    }

    /*
     * Lista todos os atendimentos.
     */
    public static ArrayList<Atendimento>
            listarAtendimentos()
            throws SQLException {

        ArrayList<Atendimento> lista =
                new ArrayList<>();

        String sql =
                "SELECT chamado, tecnico, "
                + "DATE_FORMAT("
                + "data_hora, "
                + "'%d/%m/%Y %H:%i:%s'"
                + ") AS data_formatada "
                + "FROM atendimentos "
                + "ORDER BY id DESC";

        try (Connection conexao = conectar();
             java.sql.PreparedStatement comando =
                     conexao.prepareStatement(sql);
             ResultSet resultado =
                     comando.executeQuery()) {

            while (resultado.next()) {

                Atendimento atendimento =
                        new Atendimento(
                                resultado.getString(
                                        "chamado"
                                ),
                                resultado.getString(
                                        "tecnico"
                                ),
                                resultado.getString(
                                        "data_formatada"
                                )
                        );

                lista.add(atendimento);
            }
        }

        return lista;
    }

    /*
     * Lista somente os técnicos ativos.
     *
     * Usado pela fila.
     */
    public static ArrayList<Tecnico>
            listarTecnicos()
            throws SQLException {

        ArrayList<Tecnico> lista =
                new ArrayList<>();

        String sql =
                "SELECT id, nome, inicio_almoco, "
                + "fim_almoco, ativo, ordem "
                + "FROM tecnicos "
                + "WHERE ativo = TRUE "
                + "ORDER BY ordem";

        try (Connection conexao = conectar();
             java.sql.PreparedStatement comando =
                     conexao.prepareStatement(sql);
             ResultSet resultado =
                     comando.executeQuery()) {

            while (resultado.next()) {

                LocalTime inicioAlmoco =
                        resultado
                                .getTime(
                                        "inicio_almoco"
                                )
                                .toLocalTime();

                LocalTime fimAlmoco =
                        resultado
                                .getTime(
                                        "fim_almoco"
                                )
                                .toLocalTime();

                Tecnico tecnico =
                        new Tecnico(
                                resultado.getInt(
                                        "id"
                                ),
                                resultado.getString(
                                        "nome"
                                ),
                                inicioAlmoco,
                                fimAlmoco,
                                resultado.getBoolean(
                                        "ativo"
                                ),
                                resultado.getInt(
                                        "ordem"
                                )
                        );

                lista.add(tecnico);
            }
        }

        return lista;
    }

    /*
     * Lista TODOS os técnicos.
     *
     * Usado pela administração.
     */
    public static ArrayList<Tecnico>
            listarTodosTecnicos()
            throws SQLException {

        ArrayList<Tecnico> lista =
                new ArrayList<>();

        String sql =
                "SELECT id, nome, inicio_almoco, "
                + "fim_almoco, ativo, ordem "
                + "FROM tecnicos "
                + "ORDER BY ordem";

        try (Connection conexao = conectar();
             java.sql.PreparedStatement comando =
                     conexao.prepareStatement(sql);
             ResultSet resultado =
                     comando.executeQuery()) {

            while (resultado.next()) {

                LocalTime inicioAlmoco =
                        resultado
                                .getTime(
                                        "inicio_almoco"
                                )
                                .toLocalTime();

                LocalTime fimAlmoco =
                        resultado
                                .getTime(
                                        "fim_almoco"
                                )
                                .toLocalTime();

                Tecnico tecnico =
                        new Tecnico(
                                resultado.getInt(
                                        "id"
                                ),
                                resultado.getString(
                                        "nome"
                                ),
                                inicioAlmoco,
                                fimAlmoco,
                                resultado.getBoolean(
                                        "ativo"
                                ),
                                resultado.getInt(
                                        "ordem"
                                )
                        );

                lista.add(tecnico);
            }
        }

        return lista;
    }

    /*
     * Atualiza os dados de um técnico.
     */
    public static void atualizarTecnico(
            int id,
            String nome,
            LocalTime inicioAlmoco,
            LocalTime fimAlmoco,
            boolean ativo,
            int ordem) throws SQLException {

        String sql =
                "UPDATE tecnicos "
                + "SET nome = ?, "
                + "inicio_almoco = ?, "
                + "fim_almoco = ?, "
                + "ativo = ?, "
                + "ordem = ? "
                + "WHERE id = ?";

        try (Connection conexao = conectar();
             java.sql.PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(
                    1,
                    nome
            );

            comando.setTime(
                    2,
                    java.sql.Time.valueOf(
                            inicioAlmoco
                    )
            );

            comando.setTime(
                    3,
                    java.sql.Time.valueOf(
                            fimAlmoco
                    )
            );

            comando.setBoolean(
                    4,
                    ativo
            );

            comando.setInt(
                    5,
                    ordem
            );

            comando.setInt(
                    6,
                    id
            );

            comando.executeUpdate();
        }
    }

    /*
     * Lista atendimentos para o relatório,
     * permitindo filtrar por período e técnico.
     */
    public static ArrayList<Atendimento>
            listarAtendimentosRelatorio(
                    String dataInicial,
                    String dataFinal,
                    String tecnicoFiltro)
                    throws SQLException {

        ArrayList<Atendimento> lista =
                new ArrayList<>();

        StringBuilder sql =
                new StringBuilder();

        sql.append(
                "SELECT chamado, tecnico, "
                + "DATE_FORMAT(data_hora, "
                + "'%d/%m/%Y %H:%i:%s') "
                + "AS data_formatada "
                + "FROM atendimentos "
                + "WHERE 1 = 1 "
        );

        if (dataInicial != null
                && !dataInicial.isBlank()) {

            sql.append(
                    "AND data_hora >= ? "
            );
        }

        if (dataFinal != null
                && !dataFinal.isBlank()) {

            sql.append(
                    "AND data_hora < "
                    + "DATE_ADD(?, INTERVAL 1 DAY) "
            );
        }

        if (tecnicoFiltro != null
                && !tecnicoFiltro.isBlank()) {

            sql.append(
                    "AND tecnico = ? "
            );
        }

        sql.append(
                "ORDER BY data_hora DESC"
        );

        try (Connection conexao = conectar();
             java.sql.PreparedStatement comando =
                     conexao.prepareStatement(
                             sql.toString()
                     )) {

            int parametro = 1;

            if (dataInicial != null
                    && !dataInicial.isBlank()) {

                comando.setString(
                        parametro++,
                        dataInicial
                                + " 00:00:00"
                );
            }

            if (dataFinal != null
                    && !dataFinal.isBlank()) {

                comando.setString(
                        parametro++,
                        dataFinal
                );
            }

            if (tecnicoFiltro != null
                    && !tecnicoFiltro.isBlank()) {

                comando.setString(
                        parametro++,
                        tecnicoFiltro
                );
            }

            try (ResultSet resultado =
                    comando.executeQuery()) {

                while (resultado.next()) {

                    Atendimento atendimento =
                            new Atendimento(
                                    resultado.getString(
                                            "chamado"
                                    ),
                                    resultado.getString(
                                            "tecnico"
                                    ),
                                    resultado.getString(
                                            "data_formatada"
                                    )
                            );

                    lista.add(atendimento);
                }
            }
        }

        return lista;
    }

    /*
     * Retorna a quantidade de atendimentos
     * de cada técnico para o relatório.
     */
    public static ArrayList<String[]>
            listarResumoTecnicos(
                    String dataInicial,
                    String dataFinal,
                    String tecnicoFiltro)
                    throws SQLException {

        ArrayList<String[]> lista =
                new ArrayList<>();

        StringBuilder sql =
                new StringBuilder();

        sql.append(
                "SELECT tecnico, COUNT(*) AS quantidade "
                + "FROM atendimentos "
                + "WHERE 1 = 1 "
        );

        if (dataInicial != null
                && !dataInicial.isBlank()) {

            sql.append(
                    "AND data_hora >= ? "
            );
        }

        if (dataFinal != null
                && !dataFinal.isBlank()) {

            sql.append(
                    "AND data_hora < "
                    + "DATE_ADD(?, INTERVAL 1 DAY) "
            );
        }

        if (tecnicoFiltro != null
                && !tecnicoFiltro.isBlank()) {

            sql.append(
                    "AND tecnico = ? "
            );
        }

        sql.append(
                "GROUP BY tecnico "
                + "ORDER BY quantidade DESC, tecnico"
        );

        try (Connection conexao = conectar();
             java.sql.PreparedStatement comando =
                     conexao.prepareStatement(
                             sql.toString()
                     )) {

            int parametro = 1;

            if (dataInicial != null
                    && !dataInicial.isBlank()) {

                comando.setString(
                        parametro++,
                        dataInicial
                                + " 00:00:00"
                );
            }

            if (dataFinal != null
                    && !dataFinal.isBlank()) {

                comando.setString(
                        parametro++,
                        dataFinal
                );
            }

            if (tecnicoFiltro != null
                    && !tecnicoFiltro.isBlank()) {

                comando.setString(
                        parametro++,
                        tecnicoFiltro
                );
            }

            try (ResultSet resultado =
                    comando.executeQuery()) {

                while (resultado.next()) {

                    String[] resumo =
                            new String[2];

                    resumo[0] =
                            resultado.getString(
                                    "tecnico"
                            );

                    resumo[1] =
                            String.valueOf(
                                    resultado.getInt(
                                            "quantidade"
                                    )
                            );

                    lista.add(resumo);
                }
            }
        }

        return lista;
    }
}