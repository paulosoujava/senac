package util;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Login;



public class FiltroLogin implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Login login = null;
		HttpSession sessao = ((HttpServletRequest) request).getSession();

		if (sessao != null) {
			login = (Login) sessao.getAttribute("pessoaLogada");
		}

		if (login == null) {
			String contextPath = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(contextPath + "/aula10/login.xhtml");
		} else {
			
			//Verifica a permissao (código de permissão inventado)
			if(login.getNivel() == 1 || login.getNivel() == 2){
				chain.doFilter(request, response);
			} else{
				String contextPath = ((HttpServletRequest) request).getContextPath();
				((HttpServletResponse) response).sendRedirect(contextPath + "/aula10/semPermissao.xhtml");
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}
}