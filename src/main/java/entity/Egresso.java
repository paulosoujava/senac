package entity;

import java.util.Date;

/*
 * Egresso de alunos atraves da importacao de ma planilha
 */
public class Egresso {
	
	private String forma_egresso;
	private Curso curso;
	private String dataImporte;
	private Integer id_egresso;
	private Integer ano;
	private Integer fase;
	
	
	
	
	public Integer getId_egresso() {
		return id_egresso;
	}
	public void setId_egresso(Integer id_egresso) {
		this.id_egresso = id_egresso;
	}
	
	public String getForma_egresso() {
		return forma_egresso;
	}
	public void setForma_egresso(String forma_egresso) {
		this.forma_egresso = forma_egresso;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public String getDataImporte() {
		return dataImporte;
	}
	public void setDataImporte(String dataImporte) {
		this.dataImporte = dataImporte;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getFase() {
		return fase;
	}
	public void setFase(Integer fase) {
		this.fase = fase;
	}
	@Override
	public String toString() {
		return "Egresso [forma_egresso=" + forma_egresso + ", curso=" + curso + ", dataImporte=" + dataImporte
				+ ", id_egresso=" + id_egresso + ", ano=" + ano + ", fase=" + fase + "]";
	}
	
	
	

}
