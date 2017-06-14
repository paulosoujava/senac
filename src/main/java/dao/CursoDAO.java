package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Cidade;
import entity.Curso;
import entity.Estado;
import entity.Login;

/*
 * 
 * Classe responsavel pelo CRUDE do curso
 * Tabela Curso
 * dados: id_curso, cu_nome, cu_tipo_formacao, cu_qts_fase, cu_texto_adicional, cu_sigla, id_cidade
 */
public class CursoDAO {
	
	private Connection conexao;

	public CursoDAO() {
		this.getConexao();
	}

	//SINGLETON
	private void getConexao() {
		if( this.conexao != null ){
			try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.conexao = new ConnectionFactory().obterConexao();
	}
	//********************************************************************************************************
		 // RECEBE UM CURSO
		//	RETORNA INTEGER 1 ok cadastra 0 erro sql -1 ja tem 
		//********************************************************************************************************
		
	public Integer editar(Curso curso ){
		
	
		Integer ok = 0;
		
		//**************************************************************************		
		//CHECK SE JA EXISTE mais de um CURSO CADASTRADO COM ESTE NOME E CIDADE

		System.out.println("IDS "+ this.readCursoByIdCursoAndIdCidade(curso).size() );
		
		if( this.readCursoByIdCursoAndIdCidade(curso).size() >= 2 ){
			return -1;
		}
		//**************************************************************************	
		
		System.out.println("Cuso que vou add "+ curso );
		this.getConexao();
		String sql = "UPDATE curso SET  "+
										 "cu_nome = ?  , "+
										  "cu_tipo_formacao = ?,"+
										  "cu_qts_fase = ?, "+
										  "cu_texto_adicional = ?, "+
										  "cu_sigla = ?, "+
										  "cu_imagem = ?, "+
										  "id_cidade = ? "+
										 	 "WHERE  id_curso = ?;";
		try {
			


			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, curso.getNome_curso().toLowerCase());
			stmt.setString(2, curso.getFormacao_curso());
			stmt.setInt(3,    curso.getFase_curso());
			stmt.setString(4, curso.getTexto_livre().toLowerCase());
			stmt.setString(5, curso.getSigla_curso().toLowerCase());
			stmt.setString(6, curso.getUrl_image());
			stmt.setInt(7,    curso.getId_cidade_curso());
			stmt.setInt(8,    curso.getId_curso() );
			
		
			if( stmt.executeUpdate() > 0){
				stmt.close();
				ok = 1;
			}

			if (ok > 0) {
				System.out.println("Curso alterado com sucesso");
			} else {
				System.out.println("Erro ao alterar curso");
			}

			stmt.close();

			return ok;
			
			
		} catch (SQLException e) {
			
			System.out.println("Erro ao editar curso: " + e.getMessage());
			return ok;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	
	}
	
	
	
	//********************************************************************************************************
	 // RECEBE UM CURSO
	//	RETORNA INTEGER id do curso cadastrado 
	//********************************************************************************************************
	
	public int incluir(Curso curso) {
	
		
		//*********		
		//CHECK SE JA EXISTE CURSO CADASTRADO COM ESTE NOME E CIDADE
	
		if( this.CheckCourseByIdAndByCidade(curso) > 0 ){
			
			return -1;
		}
		//*********	
		

		this.getConexao();
		
		int idInserido = 0;
		System.out.println( curso );
		String sql = "INSERT INTO newEgresso.curso (cu_nome, cu_tipo_formacao, cu_qts_fase, cu_texto_adicional, cu_sigla, cu_imagem,  id_cidade)"
								        + " VALUES ( ?,     ?,               ?,          ?,              ?,       ?,         ?);";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores
			stmt.setString(1, curso.getNome_curso().toLowerCase());
			stmt.setString(2, curso.getFormacao_curso().toLowerCase());
			stmt.setInt(3,    curso.getFase_curso());
			stmt.setString(4, curso.getTexto_livre().toLowerCase());
			stmt.setString(5, curso.getSigla_curso().toLowerCase());
			stmt.setString(6, curso.getUrl_image());
			stmt.setInt(7,    curso.getId_cidade_curso());
			// executa
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Curso inserido com sucesso");
			} else {
				System.out.println("Erro ao inserir curso");
			}

			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir curso: " + e.getSQLState() + e.getErrorCode() + e.getMessage() + e.getCause());
			throw new RuntimeException(e);
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	}
	
	

	//********************************************************************************************************
	 // RECEBE UM CURSO
	//	RETORNA UMA LISTA E IDS CASA HAJA CURSOS CADASTRADO COM ESTE NOME E CIDADE
	//********************************************************************************************************
	public List<Curso> readCursoByIdCursoAndIdCidade( Curso curso ){
		
		this.getConexao();
		List<Curso> cursoList = new ArrayList<Curso>();
		String sql = "SELECT * FROM curso WHERE cu_nome = ? AND id_cidade = ?";
		try {

			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, curso.getNome_curso());
			stmt.setInt(2, curso.getId_cidade_curso());
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Curso  c = new Curso();
				c.setFase_curso(rs.getInt("cu_qts_fase"));
				c.setFormacao_curso(rs.getString("cu_tipo_formacao"));
				c.setId_curso(rs.getInt("id_curso"));
				c.setNome_curso(rs.getString("cu_nome"));
				c.setSigla_curso(rs.getString("cu_sigla"));
				c.setTexto_livre(rs.getString("cu_texto_adicional"));
				c.setUrl_image(rs.getString("cu_imagem"));
				cursoList.add(c);
			}

			stmt.close();
			return cursoList;
		} catch (SQLException e) {
			System.out.println("Erro ao listar pessoas: " + e.getMessage());
			return cursoList;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************
	
	
}

	//********************************************************************************************************
	 // RECEBE O ID DO CURSO E RETORNA
	//********************************************************************************************************
	public Curso readAllDataCursoById( Integer id ){
		
		this.getConexao();
		Curso c = null;
		
		String sql =" SELECT   curso.id_curso, curso.cu_nome, curso.cu_tipo_formacao, curso.cu_qts_fase, curso.cu_texto_adicional, curso.cu_sigla, curso.cu_imagem, curso.id_cidade, "
							+ "cidades.nome, estados.nome,estados.sigla, estados.id FROM newEgresso.curso  "
							+ "INNER JOIN cidades ON curso.id_cidade = cidades.id "
							+ "INNER JOIN estados ON estados.id = cidades.estados_id WHERE id_curso = ?" ;
				
		
	
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, id);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				c = new Curso();
				Cidade cid = new Cidade();
				Estado es =new Estado();
				
				
				//CURSO
				c.setFase_curso(rs.getInt("curso.cu_qts_fase"));
				c.setFormacao_curso(  myUpcase( rs.getString("curso.cu_tipo_formacao") ) );
				c.setId_curso(rs.getInt("curso.id_curso"));
				c.setNome_curso( myUpcase( rs.getString( "curso.cu_nome")));
				c.setSigla_curso(rs.getString("curso.cu_sigla"));
				c.setTexto_livre(rs.getString("curso.cu_texto_adicional"));
				c.setUrl_image(rs.getString("curso.cu_imagem"));
				//CIDADE
				cid.setId_cidade(rs.getInt("curso.id_cidade"));
				cid.setNome_cidade(rs.getString("cidades.nome"));
				
				//ESTADO
				es.setId_estado(rs.getInt("estados.id"));
				es.setNome(rs.getString("estados.nome"));
				es.setSigla(rs.getString("estados.sigla"));
				//associando cidade ao estado
				cid.setEstado(es);
				//associando  cidade ao cusro
				c.setCidade(cid);
				
				
				
			}

			stmt.close();
			return c;

		} catch (SQLException e) {
			System.out.println("Erro ao obter lista readCursoListById: " + e.getMessage());
			e.printStackTrace();
			return c;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	//*************************************************************************************
	
	
}

	//********************************************************************************************************
	//	RETORNA TODOS OS CURSO 
	//********************************************************************************************************
	public List<Curso> readAllCurso(  ){
		

		this.getConexao();
		List<Curso> listC = new ArrayList<>();
		
		String sql =" SELECT   curso.id_curso, curso.cu_nome, curso.cu_tipo_formacao, curso.cu_qts_fase, curso.cu_texto_adicional, curso.cu_sigla, curso.cu_imagem, curso.id_cidade, "
							+ "cidades.nome, estados.nome, estados.id FROM newEgresso.curso  "
							+ "INNER JOIN cidades ON curso.id_cidade = cidades.id "
							+ "INNER JOIN estados ON estados.id = cidades.estados_id;" ;
				
		
	
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Curso c = new Curso();
				Cidade cid = new Cidade();
				Estado es =new Estado();
				//CURSO
				c.setFase_curso(rs.getInt("curso.cu_qts_fase"));
				c.setFormacao_curso(rs.getString("curso.cu_tipo_formacao"));
				c.setId_curso(rs.getInt("curso.id_curso"));
				c.setNome_curso(rs.getString("curso.cu_nome"));
				c.setSigla_curso(rs.getString("curso.cu_sigla"));
				c.setTexto_livre(rs.getString("curso.cu_texto_adicional"));
				c.setUrl_image(rs.getString("curso.cu_imagem"));
				//CIDADE
				cid.setId_cidade(rs.getInt("curso.id_cidade"));
				cid.setNome_cidade(rs.getString("cidades.nome"));
				//ESTADO
				es.setId_estado(rs.getInt("estados.id"));
				es.setNome(rs.getString("estados.nome"));
				
				//associando cidade ao estado
				cid.setEstado(es);
				//associando  cidade ao cusro
				c.setCidade(cid);
				
				
				listC.add(c);
			}

			stmt.close();
			return listC;

		} catch (SQLException e) {
			System.out.println("Erro ao obter lista curso: " + e.getMessage());
			e.printStackTrace();
			return listC;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************
	
	
}

	//********************************************************************************************************
	 // RECEBE UMA STRING
	//	RETORNA UMA LISTA DE CURSO CUJO O LIKE ENCONTROU
	//********************************************************************************************************
	public List<Curso> readCursoByName( String str ){
		

		this.getConexao();
		List<Curso> listC = new ArrayList<>();
		
		System.out.println("PESQUISAR ");
		String sql =" SELECT   curso.id_curso, curso.cu_nome, curso.cu_tipo_formacao, curso.cu_qts_fase, curso.cu_texto_adicional, curso.cu_sigla, curso.cu_imagem, curso.id_cidade, "
				+ "cidades.nome, estados.nome, estados.id FROM newEgresso.curso  "
				+ "INNER JOIN cidades ON curso.id_cidade = cidades.id "
				+ "INNER JOIN estados ON estados.id = cidades.estados_id WHERE curso.cu_nome LIKE ?;" ;

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			
			stmt.setString(1, '%' + str + '%');
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Curso c = new Curso();
				Cidade cid = new Cidade();
				Estado es =new Estado();
				//CURSO
				c.setFase_curso(rs.getInt("curso.cu_qts_fase"));
				c.setFormacao_curso(rs.getString("curso.cu_tipo_formacao"));
				c.setId_curso(rs.getInt("curso.id_curso"));
				c.setNome_curso(rs.getString("curso.cu_nome"));
				c.setSigla_curso(rs.getString("curso.cu_sigla"));
				c.setTexto_livre(rs.getString("curso.cu_texto_adicional"));
				c.setUrl_image(rs.getString("curso.cu_imagem"));
				//CIDADE
				cid.setId_cidade(rs.getInt("curso.id_cidade"));
				cid.setNome_cidade(rs.getString("cidades.nome"));
				//ESTADO
				es.setId_estado(rs.getInt("estados.id"));
				es.setNome(rs.getString("estados.nome"));
				
				//associando cidade ao estado
				cid.setEstado(es);
				//associando  cidade ao cusro
				c.setCidade(cid);
				
				
				listC.add(c);
			}

			stmt.close();
			
			
			return listC;

		} catch (SQLException e) {
			System.out.println("Erro ao obter curso: " + e.getMessage());
			e.printStackTrace();
			return listC;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************
	
	
}
	
	//********************************************************************************************************
	 // DELETE
	//	EXCLUSAO DO OBJETO CURSO, COM OU SEM VINCULOS COM ALUNO
	//********************************************************************************************************
		public Boolean deleteCurso( Curso cu ){
			
			this.getConexao();
			String sql = "DELETE  FROM curso WHERE id_curso = ?";

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql);
				stmt.setInt(1, cu.getId_curso());
				
				// executa
				   
				  if( stmt.executeUpdate() >= 1 ){
					  System.out.println("Deletado com sucesso o curso ");
					  stmt.close();
						return true;
				  }

			} catch (SQLException e) {
				System.out.println("Erro ao deletar curso : " + e.getMessage());
				e.printStackTrace();
				return false;
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
			
			return false;
		}
		
	
	
	
//*************************************************************************************
// BEGIN PRIVATE METHODS
//*************************************************************************************	
	
	/*
	 * CHECA SE O CURSO JA FOI CADASTRADO CONSIDERASSE UM CURSO CADASTRADO
	 * CASO O NOME E A CIDADE JA EXISTAM EX: ADS FLORIANOPOLIS
	 * RETURN ID_DO_CURSO
	 */
	private Integer CheckCourseByIdAndByCidade(Curso curso ){
		
		this.getConexao();
		Integer id_curso = 0;
		String sql = "SELECT  id_curso, cu_nome, cu_tipo_formacao, cu_qts_fase, cu_texto_adicional, cu_sigla, id_cidade  FROM curso WHERE cu_nome = ? AND id_cidade = ? ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setString(1, curso.getNome_curso().toLowerCase());
			stmt.setInt(2, curso.getId_cidade_curso());
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				id_curso =  (Integer.parseInt( rs.getString("id_curso")));
			}

			stmt.close();
			return id_curso;

		} catch (SQLException e) {
			System.out.println("Erro ao obter curso byId: " + e.getMessage());
			e.printStackTrace();
			return id_curso;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
		
	}
	
	//DEIXANDO A PRIMEIRA LETRA EM MAISCULO
	private String myUpcase(String str){
		return str.substring(0,1).toUpperCase().concat(str.substring(1));
	}
//*************************************************************************************
// END PRIVATE METHODS
//*************************************************************************************	



}
