package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Aluno;
import entity.Cidade;
import entity.Curso;
import entity.Egresso;
import entity.Estado;
import entity.Login;
import entity.Pessoa;

/*
 * Tabela criada para suprir a informacao referente ao aluno e somente ao aluno
 */
public class AlunoDAO {

	private Connection conexao;

	public AlunoDAO() {
		this.getConexao();
	}

	// SINGLETON
	private void getConexao() {

		if (this.conexao != null) {
			try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.conexao = new ConnectionFactory().obterConexao();
	}

	// ********************************************************************************************************
	// RECEBE UMA ENTIDADE ALUNO
	// RETORNA INTEGER ID DA ENTIDADE CADASTRADA
	// JA FAZ A VALIDACAO DA MATRICULA, CASO EXISTA NAO CADASTRA
	// ********************************************************************************************************

	public int incluir(Aluno al) {

		// **********************************************************************************************************
		// CHECK SE JA EXISTE CPF CADASTRADO

		if (this.CheckByMatricula(al.getMatricula()) > 0) {
			return 2;
		}
		// **********************************************************************************************************

		this.getConexao(); // tem que estar abixo devido o checkByCpf fechar a
							// concexao

		int idInserido = 0;
		String sql = "INSERT INTO newEgresso.aluno (al_forma_egresso, al_matricula, al_situacao, id_pessoa, id_login)"
				+ " VALUES (?,                 ?,         ?,            ?,         ?       );";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores
			stmt.setString(1, al.getForm_egresso());
			stmt.setString(2, al.getMatricula());
			stmt.setString(3, al.getSitucao());
			stmt.setInt(4, al.getId_pessoa());
			stmt.setInt(5, al.getLogin().getId_login());

			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Aluno criado com sucesso");
			} else {
				System.out.println("Erro ao inserir aluno");
			}

			stmt.close();

			return idInserido;

		} catch (SQLException e) {
			System.out.println("Erro ao inserir aluno: " + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	}

	/**************************************************************
	 * CHECA SE O ALUNO QUE DESEJA CRIAR JA NAO EXISTE RETORNA O ID DO ALUNO
	 ***************************************************************/
	public Integer CheckByMatricula(String mat) {

		this.getConexao();
		Integer id_pessoa = 0;
		String sql = "SELECT id_aluno, al_matricula FROM aluno WHERE al_matricula = ?";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setString(1, mat);

			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				id_pessoa = (Integer.parseInt(rs.getString("id_aluno")));
			}

			stmt.close();
			return id_pessoa;

		} catch (SQLException e) {
			System.out.println("Erro ao obter ALUNO  CheckByMatricula: " + e.getMessage());
			e.printStackTrace();
			return id_pessoa;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

	/**************************************************
	 * RETURN ALUNO COM LOGIN
	 ***************************************************/
	public Aluno alunosByID(Integer id) {

		this.getConexao();
		
		Aluno a = null;
		String sql = " SELECT"
				+ " aluno.id_login,aluno.al_forma_egresso, aluno.al_matricula, aluno.al_situacao, "+
				 "  pessoa.id_pessoa, pessoa.pe_primeiro_nome, pessoa.pe_ultimo_nome, pessoa.pe_cpf,pessoa.pe_telefone,pessoa.pe_dta_nasc,pessoa.pe_sexo, "+
				 " curso.cu_nome,curso.cu_tipo_formacao,curso.cu_qts_fase, curso.cu_texto_adicional,curso.cu_sigla,curso.cu_imagem, "+
				 " cidades.nome, estados.nome, estados.sigla "+
				 " FROM aluno "+
				  " INNER JOIN pessoa ON aluno.id_pessoa = pessoa.id_pessoa "+ 
				  " INNER JOIN egresso_ids ON egresso_ids.id_pessoa = pessoa.id_pessoa "+ 
				  " INNER JOIN curso ON curso.id_curso = egresso_ids.id_curso "+
				  " INNER JOIN cidades ON cidades.id = curso.id_cidade "+
				  " INNER JOIN estados ON estados.id = cidades.estados_id "+
				  " WHERE pessoa.id_pessoa = ? ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);

			// executa
			ResultSet rs = stmt.executeQuery();
			LoginDAO lD = new LoginDAO();
			while (rs.next()) {

				// EMAIL DO ALUNO
				String email = lD.geEmailLogin(rs.getInt("aluno.id_login"));
				Login l = new Login();
				
				//curso
				Curso c = new Curso();
				Cidade cidade = new Cidade();
				Estado estado = new Estado();
				
				c.setFase_curso(rs.getInt("curso.cu_qts_fase"));
				c.setFormacao_curso(rs.getString("curso.cu_tipo_formacao"));
				c.setNome_curso(rs.getString("curso.cu_nome"));
				
				cidade.setNome_cidade(rs.getString("cidades.nome"));
				estado.setNome(rs.getString("estados.nome"));
				estado.setSigla(rs.getString("estados.sigla"));
				cidade.setEstado(estado);
				c.setCidade(cidade);
				l.setEmail(email);

				a = new Aluno();
				a.setCpf(rs.getString("pessoa.pe_cpf"));
				a.setData_nascimento(rs.getString("pessoa.pe_dta_nasc"));
				a.setId_pessoa(rs.getInt("pessoa.id_pessoa"));
				a.setPrimeiro_nome(firstUpCase(rs.getString("pessoa.pe_primeiro_nome")));
				a.setUltimo_nome(firstUpCase(rs.getString("pessoa.pe_ultimo_nome")));
				a.setSexo(rs.getString("pessoa.pe_sexo"));
				a.setTelefone(rs.getString("pessoa.pe_telefone"));
				a.setSexo(firstUpCase(rs.getString("pessoa.pe_sexo")));
				a.setForm_egresso(firstUpCase(rs.getString("aluno.al_forma_egresso")));
				a.setMatricula(rs.getString("aluno.al_matricula"));
				a.setSitucao(firstUpCase(rs.getString("aluno.al_situacao")));
				
				a.setCurso(c);
				a.setLogin(l);
			}
			
			

			stmt.close();
			return a;

		} catch (SQLException e) {
			System.out.println("Erro ao obter aluno  alunosByID: " + e.getMessage());
			e.printStackTrace();
			return a;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

	/**************************************************
	 * ALTERA DADO DE PESSOA RETURN 1 = ok 0 = not ok
	 ***************************************************/
	public Integer editar(Aluno aluno) {

		Integer ok = 0;
		this.getConexao();
		String sql = "UPDATE aluno SET  al_forma_egresso = ?  , al_situacao = ?, al_matricula = ?  WHERE  id_pessoa = ?;";
		try {

			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, aluno.getForm_egresso().toLowerCase());
			stmt.setString(2, aluno.getSitucao().toLowerCase());
			stmt.setString(3, aluno.getMatricula());
			stmt.setInt(4, aluno.getId_pessoa());

			if (stmt.executeUpdate() > 0) {
				stmt.close();
				ok = 1;
			}

			if (ok > 0) {
				System.out.println("Aluno alterado com sucesso");
			} else {
				System.out.println("Erro ao alterar aluno");
			}

			stmt.close();

			return ok;

		} catch (SQLException e) {

			System.out.println("Erro ao editar aluno: " + e.getMessage());
			return ok;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

	/**************************************************
	 * RETURN ALUNO COM LOGIN
	 ***************************************************/
	public List<Aluno> alunosTodosALunos() {

		this.getConexao();
		
		Aluno a = null;
		List<Aluno> listAluno = new ArrayList<>();
		
		String sql = " SELECT"
				+ " aluno.id_login,aluno.al_forma_egresso, aluno.al_matricula, aluno.al_situacao, "+
				 "  pessoa.id_pessoa, pessoa.pe_primeiro_nome, pessoa.pe_ultimo_nome, pessoa.pe_cpf,pessoa.pe_telefone,pessoa.pe_dta_nasc,pessoa.pe_sexo, "+
				 " curso.cu_nome,curso.cu_tipo_formacao,curso.cu_qts_fase, curso.cu_texto_adicional,curso.cu_sigla,curso.cu_imagem, "+
				 " cidades.nome, estados.nome, estados.sigla "+
				 " FROM aluno "+
				  " INNER JOIN pessoa ON aluno.id_pessoa = pessoa.id_pessoa "+ 
				  " INNER JOIN egresso_ids ON egresso_ids.id_pessoa = pessoa.id_pessoa "+ 
				  " INNER JOIN curso ON curso.id_curso = egresso_ids.id_curso "+
				  " INNER JOIN cidades ON cidades.id = curso.id_cidade "+
				  " INNER JOIN estados ON estados.id = cidades.estados_id "+
				  " WHERE pessoa.id_pessoa  > ?";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, 0);

			// executa
			ResultSet rs = stmt.executeQuery();
			LoginDAO lD = new LoginDAO();
			while (rs.next()) {

				// EMAIL DO ALUNO
				String email = lD.geEmailLogin(rs.getInt("aluno.id_login"));
				Login l = new Login();
				
				//curso
				Curso c = new Curso();
				Cidade cidade = new Cidade();
				Estado estado = new Estado();
				
				c.setFase_curso(rs.getInt("curso.cu_qts_fase"));
				c.setFormacao_curso(rs.getString("curso.cu_tipo_formacao"));
				c.setNome_curso(rs.getString("curso.cu_nome"));
				
				cidade.setNome_cidade(rs.getString("cidades.nome"));
				estado.setNome(rs.getString("estados.nome"));
				estado.setSigla(rs.getString("estados.sigla"));
				cidade.setEstado(estado);
				c.setCidade(cidade);
				l.setEmail(email);

				a = new Aluno();
				a.setCpf(rs.getString("pessoa.pe_cpf"));
				a.setData_nascimento(rs.getString("pessoa.pe_dta_nasc"));
				a.setId_pessoa(rs.getInt("pessoa.id_pessoa"));
				a.setPrimeiro_nome(firstUpCase(rs.getString("pessoa.pe_primeiro_nome")));
				a.setUltimo_nome(firstUpCase(rs.getString("pessoa.pe_ultimo_nome")));
				a.setSexo(rs.getString("pessoa.pe_sexo"));
				a.setTelefone(rs.getString("pessoa.pe_telefone"));
				a.setSexo(firstUpCase(rs.getString("pessoa.pe_sexo")));
				a.setForm_egresso(firstUpCase(rs.getString("aluno.al_forma_egresso")));
				a.setMatricula(rs.getString("aluno.al_matricula"));
				a.setSitucao(firstUpCase(rs.getString("aluno.al_situacao")));
				
				a.setCurso(c);
				a.setLogin(l);
				
				listAluno.add(a);
			}
			
			

			stmt.close();
			return listAluno;

		} catch (SQLException e) {
			System.out.println("Erro ao obter aluno  alunosByID: " + e.getMessage());
			e.printStackTrace();
			return listAluno;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

	
	
	
	
	// primeira letra em maisculo
	private String firstUpCase(String palavra) {
		return palavra.substring(0, 1).toUpperCase().concat(palavra.substring(1));
	}
}
