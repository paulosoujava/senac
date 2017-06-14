package entity;

import java.io.Serializable;


/*
 * Responsavel pelo cadastro dos cursos
 * 
 */
public class Curso implements Serializable{
	
	
	private Integer id_curso;
	private String sigla_curso;
	private String nome_curso;
	private String formacao_curso;  //qual o tipo de formacao que este curso fornece
	private Integer id_cidade_curso; //a qual unidade este curso esta vinculado
	private Integer fase_curso; //quantas fases tem este curso
	private String url_image;
	private String texto_livre;
	
	private Cidade cidade; //usado na listagem de edicao na consulta com INNER JOIN
	
	
	public Integer getId_curso() {
		return id_curso;
	}
	public void setId_curso(Integer id_curso) {
		this.id_curso = id_curso;
	}
	public String getSigla_curso() {
		return sigla_curso;
	}
	public void setSigla_curso(String sigla_curso) {
		this.sigla_curso = sigla_curso;
	}
	public String getNome_curso() {
		return nome_curso;
	}
	public void setNome_curso(String nome_curso) {
		this.nome_curso = nome_curso;
	}

	public String getFormacao_curso() {
		return formacao_curso;
	}
	public void setFormacao_curso(String formacao_curso) {
		this.formacao_curso = formacao_curso;
	}
	
	public Integer getId_cidade_curso() {
		return id_cidade_curso;
	}
	public void setId_cidade_curso(Integer id_cidade_curso) {
		this.id_cidade_curso = id_cidade_curso;
	}
	public Integer getFase_curso() {
		return fase_curso;
	}
	public void setFase_curso(Integer fase_curso) {
		this.fase_curso = fase_curso;
	}
	public String getUrl_image() {
		return url_image;
	}
	public void setUrl_image(String url_image) {
		this.url_image = url_image;
	}
	public String getTexto_livre() {
		return texto_livre;
	}
	public void setTexto_livre(String texto_livre) {
		this.texto_livre = texto_livre;
	}
	
	
	public Cidade getCidade() {
		return cidade;
	}
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	@Override
	public String toString() {
		return "Curso [id_curso=" + id_curso + ", sigla_curso=" + sigla_curso + ", nome_curso=" + nome_curso
				+ ", formacao_curso=" + formacao_curso + ", cidade_curso=" + id_cidade_curso + ", fase_curso=" + fase_curso
				+ ", url_image=" + url_image + ", texto_livre=" + texto_livre + "]";
	}

	
	
	
	

}
