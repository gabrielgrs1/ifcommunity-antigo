package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConnectionFactory;
import model.Aluno;

public class AlunoDAO {

    public AlunoDAO() {
    }

    public static Aluno cadastro(String nome, String telefone, String matricula, String periodo, String login, String senha, String email) throws SQLException {
        Aluno conta = null;
        PreparedStatement pstm;
        ResultSet rs;
        int result = 0;
        Connection con = ConnectionFactory.getConnection();

        // Verifica se já não tem um usuário no banco.
        String sql = "SELECT * FROM TB_USUARIO WHERE USUARIO = ?";
        pstm = con.prepareStatement(sql);
        pstm.setString(1, login);
        rs = pstm.executeQuery();

        if (!rs.next()) {

            //Insere na tabela aluno
            sql = "INSERT INTO TB_ALUNO (NOME, PERIODO, MATRICULA, TELEFONE) VALUES ( ?, ?, ?, ?)";

            // Prepara o comando com o preparestat pstm =
            pstm = con.prepareStatement(sql);

            // Passa os parametros da consulta pra cada ? dentro da String sql
            pstm.setString(1, nome);
            pstm.setString(2, periodo);
            pstm.setString(3, matricula);
            pstm.setString(4, telefone);

            // Executa o comando retornando no result a quantidade de linhas afetadas int
            result = pstm.executeUpdate();

            sql = "SELECT * FROM TB_ALUNO WHERE NOME = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, nome);
            ResultSet idTabelaAluno = pstm.executeQuery();

            int idTabela = 0;
            if (idTabelaAluno.next()) {
                idTabela = idTabelaAluno.getInt("ID");
            }

            //Insere na tabela usuário
            sql = "INSERT INTO TB_USUARIO (ID_ALUNO, USUARIO, SENHA, EMAIL, PERMISSAO) VALUES (?,?, ?, ?, ?)";

            // Prepara o comando com o preparestat pstm =
            pstm = con.prepareStatement(sql);

            // Passa os parametros da consulta pra cada ? dentro da String sql
            pstm.setInt(1, idTabela);
            pstm.setString(2, login);
            pstm.setString(3, senha);
            pstm.setString(4, email);
            pstm.setInt(5, 0);

            // Executa o comando retornando no result a quantidade de linhas afetadas int
            result = pstm.executeUpdate();

            if (result == 1) {
                conta = new Aluno();
                conta.setNome(nome);
                conta.setId(idTabela);
                conta.setLogin(login);
                conta.setEmail(email);
                conta.setMatricula(matricula);
            }
        }

        // Fecha conexao con.close(); 
        con.close();

        return conta;
    }

    public static Aluno login(String login, String senha) throws SQLException {
        Aluno aluno = null;
        PreparedStatement pstm;
        ResultSet rs;
        Connection con = ConnectionFactory.getConnection();

        /* Comando SQL que será enviado ao banco */
        String sql = "SELECT * FROM TB_USUARIO WHERE USUARIO = ? AND SENHA = ?";

        /* Prepara a consulta e passa os parametros */
        pstm = con.prepareStatement(sql);
        pstm.setString(1, login);
        pstm.setString(2, senha);

        /* Executa a query e armazena o resultado na variavel rs */
        rs = pstm.executeQuery();

        /* Instancia um novo aluno para dar de retorno da função */
        while (rs.next()) {
            aluno = new Aluno();
            aluno.setId(rs.getInt("ID"));
            aluno.setLogin(rs.getString("USUARIO"));
            aluno.setEmail(rs.getString("EMAIL"));
        }

        /* Fecha a conexão */
        con.close();

        return aluno;
    }

    public static void main(String[] args) throws SQLException {
        /*int rs = cadastro("Administrador", "(034) 99894-8551", "01858618657-1", "2", "admin", "admin", "gabriel_guilherme2006@hotmail.com");
        System.out.println("Foram alterados: " + rs + " registros");*/

        Aluno a = login("gabrielgrs", "Gabriel@10");
        if (a.getLogin() != null) {
            System.out.println("Aluno " + a.getLogin() + " logado com sucesso!");
        } else {
            System.out.println("Erro ao realizar login!");
        }

        /*
		 * try { Aluno a = alunodao.getAlunoByMatricula("01858618657-1");
		 * System.out.println(a.getNome()); } catch (SQLException e) {
		 * 
		 * System.out.println("Erro ao buscar aluno"); }
         */
    }
}