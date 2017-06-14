package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Aluno;
import entity.Cidade;
import entity.Endereco;
import entity.Estado;
import net.bootsfaces.render.E;

public class EnderecoDAO {
	
	private Connection conexao;

	public EnderecoDAO() {
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

	//CADASTRAR OU EDITAR ENDERECO
	public Integer actionEndereco( Endereco e, int idPessoa ){
		
		      // **********************************************************************************************************
				// CHECK SE É UPDATE OU INSERT
				if (this.CheckIdPessoaEndereco(idPessoa) != null ) {
					return this.editar(e, idPessoa);
				}
				// **********************************************************************************************************

				this.getConexao(); // tem que estar abixo devido o checkByCpf fechar a
									// concexao

				int idInserido = 0;
				String sql = "INSERT INTO newEgresso.endereco (en_logradouro, en_numero, en_comp, en_ref, en_bairro, id_pessoa, en_cep, id_cidade)"
						                             + " VALUES (?,            ?,         ?,         ?,     ?,          ?,      ?,      ?       );";
				try {
					PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					// seta os valores
					stmt.setString(1,(e.getLogradouro()));
					stmt.setString(2, e.getNumero());
					stmt.setString(3, e.getComp());
					stmt.setString(4, (e.getRef()));
					stmt.setString(5, (e.getRua()));
					stmt.setInt(6,  idPessoa);
					stmt.setString(7, e.getCep());
					stmt.setInt(8,  e.getCidade().getId_cidade());

					stmt.executeUpdate();

					ResultSet rs = stmt.getGeneratedKeys();

					if (rs.next()) {
						idInserido = rs.getInt(1);
					}

					if (idInserido > 0) {
						System.out.println("Endereco criado com sucesso");
					} else {
						System.out.println("Erro ao inserir endereco");
					}

					stmt.close();

					return idInserido;

				} catch (SQLException ex) {
					System.out.println("Erro ao inserir endereco: " + ex.getMessage());
					throw new RuntimeException(ex);
				} finally {
					ConnectionFactory.fecharConexao(this.conexao);
				}
	}

	/**************************************************
	 * ALTERA DADO DE ENDERECO RETURN 1 = ok 0 = not ok
	 ***************************************************/
	public Integer editar(Endereco e, int idPessoa) {

		Integer ok = 0;
		this.getConexao();
		String sql = "UPDATE endereco SET  "+
				" en_logradouro = ?, en_numero = ?, en_comp = ?, en_ref = ?, en_bairro = ?,  en_cep = ?, id_cidade = ?  WHERE  id_pessoa = ?;";
		try {

			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, ( e.getLogradouro()));
			stmt.setString(2, e.getNumero());
			stmt.setString(3, e.getComp());
			stmt.setString(4, ( e.getRef()));
			stmt.setString(5, ( e.getRua()));
			stmt.setString(6, e.getCep());
			stmt.setInt(7, e.getCidade().getId_cidade());
			stmt.setInt(8,  idPessoa);
			

			if (stmt.executeUpdate() > 0) {
				stmt.close();
				ok = 1;
			}

			if (ok > 0) {
				System.out.println("Endereco alterado com sucesso");
			} else {
				System.out.println("Erro ao alterar endereco");
			}

			stmt.close();

			return ok;

		} catch (SQLException ex) {

			System.out.println("Erro ao editar aluno: " + ex.getMessage());
			return ok;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}
	
	/**************************************************************
	 * CHECA SE O ALUNO JA NAO TEM UM ENDERECO CADASTRADO
	 ***************************************************************/
	public Endereco CheckIdPessoaEndereco(Integer id) {

		this.getConexao();
		Endereco end = null;
		String sql = "SELECT es.id_endereco,  es.en_logradouro, es.en_numero, es.en_comp, es.en_ref, es.en_bairro, es.en_cep, "+
         "c.id, c.nome, e.nome, e.sigla, e.id FROM endereco es "+
         " INNER JOIN cidades c ON c.id = es.id_cidade "+
         "  INNER JOIN estados e ON e.id - c.estados_id "+
         "  WHERE es.id_pessoa = ?"; 

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, id);

			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				
				Cidade c = new Cidade();
				Estado e = new Estado();
				end = new Endereco();
				//CIDADE
				c.setNome_cidade(rs.getString("c.nome"));
				c.setId_cidade(rs.getInt("c.id"));
				//ESTADO
				e.setId_estado(rs.getInt("e.id"));
				e.setNome(rs.getString("e.nome"));
				e.setSigla(rs.getString("e.sigla"));
				c.setEstado(e);
				//ENDERECO
				end.setCidade(c);
				end.setId_endereco( rs.getInt("es.id_endereco"));
				end.setCep(rs.getString("es.en_cep"));
				end.setComp(rs.getString("es.en_comp"));
				end.setLogradouro(rs.getString("es.en_logradouro"));
				end.setNumero(rs.getString("es.en_numero"));
				end.setRef(rs.getString("es.en_ref"));
				end.setRua(rs.getString("es.en_bairro"));
			}

			stmt.close();
			return end;

		} catch (SQLException e) {
			System.out.println("Erro ao obter ENDERECO  CheckIdPessoaEndereco: " + e.getMessage());
			e.printStackTrace();
			return end;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}
 

//	// primeira letra em maisculo
//	private String firstUpCase(String palavra) {
//		return palavra.substring(0, 1).toUpperCase().concat(palavra.substring(1));
//	}
	
}
