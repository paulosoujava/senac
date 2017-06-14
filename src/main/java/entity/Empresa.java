package entity;

/*
 * Empresas que trabalhou 
 */
public class Empresa {

	
	private String empresa;
	private String funcao;
	private String data_saida;
	private String data_entrada;
	private String txt_livre;
	private Integer idAluno;
	private Integer idEmpresa;
	

	
	
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Integer getIdAluno() {
		return idAluno;
	}
	public void setIdAluno(Integer idAluno) {
		this.idAluno = idAluno;
	}
	public String getTxt_livre() {
		return txt_livre;
	}
	public void setTxt_livre(String txt_livre) {
		this.txt_livre = txt_livre;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getFuncao() {
		return funcao;
	}
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
	public String getData_saida() {
		return data_saida;
	}
	public void setData_saida(String data_saida) {
		this.data_saida = data_saida;
	}
	public String getData_entrada() {
		return data_entrada;
	}
	public void setData_entrada(String data_entrada) {
		this.data_entrada = data_entrada;
	}
	@Override
	public String toString() {
		return "Empresa [empresa=" + empresa + ", funcao=" + funcao + ", data_saida=" + data_saida + ", data_entrada=" + data_entrada + ", txt_livre=" + txt_livre + ", idAluno=" + idAluno + "]";
	}
	
}
