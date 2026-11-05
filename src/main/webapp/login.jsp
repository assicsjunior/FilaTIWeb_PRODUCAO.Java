<%-- 
    Document   : login
    Created on : 28 de out. de 2026, 17:09:40
    Author     : P0170848
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>FilaTI - Login</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;

            display: flex;
            align-items: center;
            justify-content: center;

            font-family: Arial, Helvetica, sans-serif;

            background:
                linear-gradient(
                    135deg,
                    #163a5f,
                    #0f2b46
                );
        }

        .login-container {
            width: 100%;
            max-width: 400px;

            padding: 20px;
        }

        .login-card {
            background: white;

            border-radius: 14px;

            padding: 35px;

            box-shadow:
                0 10px 30px
                rgba(0, 0, 0, 0.25);
        }

        .logo {
            text-align: center;

            margin-bottom: 30px;
        }

        .logo h1 {
            margin: 0;

            color: #163a5f;

            font-size: 36px;
        }

        .logo p {
            margin-top: 8px;

            color: #777;

            font-size: 14px;
        }

        .erro {
            background: #f8d7da;

            color: #842029;

            border: 1px solid #f5c2c7;

            border-radius: 7px;

            padding: 12px;

            margin-bottom: 20px;

            text-align: center;

            font-size: 14px;

            font-weight: bold;
        }

        .campo {
            margin-bottom: 20px;
        }

        .campo label {
            display: block;

            margin-bottom: 7px;

            font-weight: bold;

            color: #444;
        }

        .campo input {
            width: 100%;

            padding: 13px;

            border: 1px solid #ccc;

            border-radius: 7px;

            font-size: 16px;

            outline: none;
        }

        .campo input:focus {
            border-color: #163a5f;

            box-shadow:
                0 0 0 2px
                rgba(22, 58, 95, 0.1);
        }

        .botao {
            width: 100%;

            border: none;

            border-radius: 7px;

            padding: 13px;

            background: #198754;

            color: white;

            font-size: 16px;

            font-weight: bold;

            cursor: pointer;
        }

        .botao:hover {
            background: #146c43;
        }

        .rodape {
            text-align: center;

            margin-top: 25px;

            color: #777;

            font-size: 12px;
        }

    </style>

</head>

<body>

    <div class="login-container">

        <div class="login-card">

            <div class="logo">

                <h1>FilaTI</h1>

                <p>
                    Sistema Interno de Atendimento de TI
                </p>

            </div>

            <%
                String erro =
                        (String) request.getAttribute("erro");

                if (erro != null) {
            %>

                <div class="erro">
                    ⚠️ <%= erro %>
                </div>

            <%
                }
            %>

            <form
                action="LoginServlet"
                method="post">

                <div class="campo">

                    <label for="usuario">
                        Usuário
                    </label>

                    <input
                        type="text"
                        id="usuario"
                        name="usuario"
                        placeholder="Digite seu usuário"
                        required
                        autofocus>

                </div>

                <div class="campo">

                    <label for="senha">
                        Senha
                    </label>

                    <input
                        type="password"
                        id="senha"
                        name="senha"
                        placeholder="Digite sua senha"
                        required>

                </div>

                <button
                    type="submit"
                    class="botao">

                    🔐 Entrar

                </button>

            </form>

            <div class="rodape">

                FilaTI © Sistema de TI

            </div>

        </div>

    </div>

</body>

</html>