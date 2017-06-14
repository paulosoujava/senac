package entity;


/*
 * Classe endereco contendo dados do endereco, cidade, estado referente a uma pessoa ou  unidade
 */
public class Endereco {

	private String logradouro;
	private String rua;
	private String cep;
	private String numero;
	private String comp;
	private String ref;
	private Cidade cidade;
	private Integer id_endereco;
	
	
	
	
	
	public Integer getId_endereco() {
		return id_endereco;
	}
	public void setId_endereco(Integer id_endereco) {
		this.id_endereco = id_endereco;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComp() {
		return comp;
	}
	public void setComp(String comp) {
		this.comp = comp;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public Cidade getCidade() {
		return cidade;
	}
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	@Override
	public String toString() {
		return "Endereco [logradouro=" + logradouro + ", rua=" + rua + ", cep=" + cep + ", numero=" + numero + ", comp=" + comp + ", ref=" + ref + ", cidade=" + cidade + ", id_endereco=" + id_endereco + "]";
	}
	
	
	
	
}
