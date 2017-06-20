package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import dao.EgressoDAO;
import entity.Aluno;


@ManagedBean
@SessionScoped
public class BeanListEgresso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6891783942062071310L;
	private List<Aluno> listAluno = new ArrayList<>();
	private Integer id;
	
	
	
	public BeanListEgresso() {}
	
	//***************************************************
	//CONSTRUTOR RECEBE DO ID A EDITAR
	public BeanListEgresso( Integer id) {
		this.id = id;
	}
	
	//***************************************************
	//Lista de alunos deste egresso

	public List<Aluno> getListAluno() {
		EgressoDAO eD = new EgressoDAO();
		this.listAluno = ( eD.getAllAlunoEgresso(this.id) );
		System.out.println(listAluno.size());
		return listAluno;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	//***************************************************

	
	

}
