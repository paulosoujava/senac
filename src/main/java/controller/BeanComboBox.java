package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

import dao.CidadeDAO;
import dao.EstadoDAO;
import entity.Cidade;
import entity.Curso;
import entity.Estado;

@ManagedBean (name="beanEdicaoCurso")
public class BeanComboBox {
	
	private String id_estado = new String();
	private String id_cidade = new String();
	private List<Cidade> listCidades = new ArrayList<>();
	private Curso curso = new Curso();

	 public BeanComboBox() {}

	 
	
	
	 //edicao em escopo sem redirecionamento
	 // paramentros sao os que estao no combo ou seja o que ele quer editar
	 public String redirectEdicao( Curso c ){
		
		 this.curso = c;
			this.id_cidade = String.valueOf( curso.getCidade());
			this.id_estado = String.valueOf( curso.getCidade().getEstado().getId_estado());
			
		 
			//pegando a cidade do qual esta cadastrado o curso para a edicao
			CidadeDAO cD = new CidadeDAO();
			Estado e = new Estado();
			 e.setNome(curso.getCidade().getEstado().getNome());
			 e.setId_estado(Integer.parseInt(this.id_estado ));
			 
		
			 //tirando a cidade da lista para nao haver duplicidade
			 this.listCidades = cD.readCidadeByEstado(e);
			 
			  if( listCidades.contains(curso.getCidade()) ){
				   listCidades.remove(curso.getCidade());
			  }
			  //seto para primeiro para aparecer no arrayList do combo
			 listCidades.add(0, curso.getCidade());
			 
			
			
			return "/admin/teste.xhtml";
	}	 

	 
		// ***************************************************************************
		// BANCO DE DADOS LISTAS ESTADOS E CIDADES
		// Chamada para popular o combo de cidades referente ao id passao
		// ****************************************************************************
		public void ShowCidades(ValueChangeEvent event) {
			
			this.id_estado = event.getNewValue().toString();
			  System.out.println("CHENGE VALUR PARA ID "+ id_estado );
		 	CidadeDAO cD = new CidadeDAO();
				Estado e = new Estado();
				e.setId_estado(Integer.parseInt(this.id_estado));
				this.listCidades = cD.readCidadeByEstado(e);

			

		} 
		public List<Estado> getEstados() {
			EstadoDAO eD = new EstadoDAO();
			
			
			return eD.readEstado();
		}
	 
/***********************************************************
*   GET AND SETTER`S
*  
/***********************************************************/
	public String getId_estado() {
		return id_estado;
	}

	public void setId_estado(String id_estado) {
		this.id_estado = id_estado;
	}

	public String getId_cidade() {
		return id_cidade;
	}

	public void setId_cidade(String id_cidade) {
		this.id_cidade = id_cidade;
	}

	public List<Cidade> getListCidades() {
		return listCidades;
	}

	public void setListCidades(List<Cidade> listCidades) {
		this.listCidades = listCidades;
	}



	public Curso getCurso() {
		return curso;
	}


	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	 
	 
	
}
