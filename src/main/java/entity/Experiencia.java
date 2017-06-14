package entity;

import java.util.ArrayList;
import java.util.List;

/*
 * Classe responsavel pelo minucurriculo a ser cadastrado pelo aluno
 * somente um para um
 */
public class Experiencia {
	

	private String txt_impacto;
	private String txt_livre;
	private Integer id_pessoa;
	private List<Empresa> listEmpreas = new ArrayList<>();
	
	
			
	public List<Empresa> getListEmpreas() {
		return listEmpreas;
	}
	public void setListEmpreas(List<Empresa> listEmpreas) {
		this.listEmpreas = listEmpreas;
	}
	public String getTxt_impacto() {
		return txt_impacto;
	}
	public void setTxt_impacto(String txt_impacto) {
		this.txt_impacto = txt_impacto;
	}
	public String getTxt_livre() {
		return txt_livre;
	}
	public void setTxt_livre(String txt_livre) {
		this.txt_livre = txt_livre;
	}
	public Integer getId_pessoa() {
		return id_pessoa;
	}
	public void setId_pessoa(Integer id_pessoa) {
		this.id_pessoa = id_pessoa;
	}
	
	

}
