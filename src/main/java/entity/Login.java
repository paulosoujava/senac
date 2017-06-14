package entity;


/*
 * Model de acesso ao sistemas com nivel;
 *  nivel = 1 ALLUNO
 *  nivel = 2 ADM
 *  
 */
public class Login {
	
	private String email;
	private String senha;
	private Integer nivel;
	private Integer id_login;
	
	
	public Login(){};
	
	public Login(String email, String senha, Integer nivel) {
		super();
		this.email = email;
		this.senha = senha;
		this.nivel = nivel;
	}
	
	
	
	public Integer getId_login() {
		return id_login;
	}

	public void setId_login(Integer id_login) {
		this.id_login = id_login;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Integer getNivel() {
		return nivel;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}
	@Override
	public String toString() {
		return "Login [email=" + email + ", senha=" + senha + ", nivel=" + nivel + "]";
	}
	
	

}
