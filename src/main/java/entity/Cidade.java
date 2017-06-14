package entity;


import java.util.Set;
import java.util.TreeSet;


/*
 * Model cidade
 * Implementa o Compareble para uma possivel oredencao
 */
public class Cidade  implements Comparable<Cidade>{

	
	
	private String nome_cidade;
	private Integer id_cidade;
	private Estado  estado;
	
	
	public Cidade(){}
	
	public Cidade(String nome_cidade, Integer id_cidade) {
		super();
		this.nome_cidade = nome_cidade;
		this.id_cidade = id_cidade;
	}
	public Cidade(String nome_cidade, Integer id_cidade, Estado estado) {
		super();
		this.nome_cidade = nome_cidade;
		this.id_cidade = id_cidade;
		this.estado = estado;
	}
	
	
	
	public String getNome_cidade() {
		return nome_cidade;
	}
	public void setNome_cidade(String nome_cidade) {
		this.nome_cidade = nome_cidade;
	}
	public Integer getId_cidade() {
		return id_cidade;
	}
	public void setId_cidade(Integer id_cidade) {
		this.id_cidade = id_cidade;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	
	



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_cidade == null) ? 0 : id_cidade.hashCode());
		result = prime * result + ((nome_cidade == null) ? 0 : nome_cidade.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidade other = (Cidade) obj;
		if (id_cidade == null) {
			if (other.id_cidade != null)
				return false;
		} else if (!id_cidade.equals(other.id_cidade))
			return false;
		if (nome_cidade == null) {
			if (other.nome_cidade != null)
				return false;
		} else if (!nome_cidade.equals(other.nome_cidade))
			return false;
		return true;
	}
	@Override
	public int compareTo(Cidade o) {
		return this.nome_cidade.compareTo(o.nome_cidade);
	}



	
	
	
}
