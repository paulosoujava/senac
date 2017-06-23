package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;

import dao.AlunoDAO;
import dao.LoginDAO;
import entity.Aluno;
import entity.Login;
import entity.Pessoa;

@ManagedBean
public class BeanListarPesquisALuno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Aluno> alunos = new ArrayList<>();
	
	public String pesSrt;
	public String[] pesquisarpor;
	
	
	private Pessoa pessoa = new Pessoa();
	private Aluno aluno = new Aluno();
	private Login login = new Login();
	private boolean isTem = false;
	private boolean isPesq = false;
	
	public BeanListarPesquisALuno() {
		// TODO Auto-generated constructor stub
	}
	

	
	
	public String pesquisar(){
		
	   AlunoDAO aD = new AlunoDAO();
	 String str = "";
	   for (String st :  pesquisarpor) {
			str += st + " '%" + pesSrt + "%' AND, ";
		}
	   
		this.alunos =  aD.pesquisarALuno( str );
		System.out.println( this.alunos.size());
		if( this.alunos.size() > 0 ){
			this.isTem = true;
		}
		
		this.isPesq = true;
		
		return "";
	}
	public String limpar(){
		this.isPesq = false;
		this.isTem = false;
		return null;
	}
	
	public String inativar( Integer id, Integer nivel  ){
		Boolean ok = false;
		
		if( nivel == 1 ){
			nivel = -1;
		}else{
			ok = true;
		}
		
		if( ok ){
			nivel = 1;
		}
		
		LoginDAO lG = new LoginDAO();
		lG.inativarAtivarLogin(id,nivel );
		return null;
	}
	
	public String getPesqInString() {
		
		
		return Arrays.toString(pesquisarpor);
	}
	

	public List<Aluno> getAlunos() {
		return alunos;
	}
	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}



	public Pessoa getPessoa() {
		return pessoa;
	}



	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}



	public Aluno getAluno() {
		return aluno;
	}



	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}



	public Login getLogin() {
		return login;
	}



	public void setLogin(Login login) {
		this.login = login;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}



	public String getPesSrt() {
		return pesSrt;
	}



	public void setPesSrt(String pesSrt) {
		this.pesSrt = pesSrt;
	}


	public String[] getFavNumber1() {
		return pesquisarpor;
	}


	public void setFavNumber1(String[] favNumber1) {
		this.pesquisarpor = favNumber1;
	}



	public boolean isTem() {
		return isTem;
	}



	public void setTem(boolean isTem) {
		this.isTem = isTem;
	}




	public boolean isPesq() {
		return isPesq;
	}




	public void setPesq(boolean isPesq) {
		this.isPesq = isPesq;
	}


	
	

	
}
