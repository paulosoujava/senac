
package entity;


/*
 * Classe aluno contendo dados referente a aluno, extende classe pessoa
 * Todo aluno tem um lgin sendo ele a senha inicia os 3 primeiros numeros do cpf
 */
public class Aluno extends Pessoa {

	private String matricula;
	private Login login;
	private String situcao;
	private String form_egresso;
	private Curso curso;
	private Egresso egresso;
	
	
	
	
	
	public Egresso getEgresso() {
		return egresso;
	}
	public void setEgresso(Egresso egresso) {
		this.egresso = egresso;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public String getSitucao() {
		return situcao;
	}
	public void setSitucao(String situcao) {
		this.situcao = situcao;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}
	
	public String getForm_egresso() {
		return form_egresso;
	}
	public void setForm_egresso(String form_egresso) {
		this.form_egresso = form_egresso;
	}
	@Override
	public String toString() {
		return "Aluno [matricula=" + matricula + ", login=" + login + ", situcao=" + situcao + ", form_egresso=" + form_egresso + ", curso=" + curso + ", egresso=" + egresso + ", getLogin()=" + getLogin() + "]";
	}
	
	
}
