package util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Login;


/*
 * CLASSE HELPER UTILIZADA PARA ARMAZENAR PARAMETRO DE PAGINA PARA PAGINA
 */
public class SessionUtil {

    public static HttpSession getSession() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpSession sessao = (HttpSession) ctx.getExternalContext().getSession(false);
        return sessao;
    }

    public static void setParam(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getParam(String key) {
        return getSession().getAttribute(key);
    }

    public static void remove(String key) {
        getSession().removeAttribute(key);
    }

    public static void invalidate() {
        getSession().invalidate();
    }
    
    
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
    	
		Login login = null;
		HttpSession sessao = ((HttpServletRequest) request).getSession();

		if (sessao != null) {
			login =  (Login) sessao.getAttribute("pessoaLogada");
		}

		if (login == null) {
			String contextPath = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(contextPath + "/index.xhtml");
		} else {
			
			//Verifica a permissao (código de permissão inventado)
			if(login.getNivel()  == 1){
				chain.doFilter(request, response);
			} else{
				String contextPath = ((HttpServletRequest) request).getContextPath();
				((HttpServletResponse) response).sendRedirect(contextPath + "/index.xhtml");
			}
		}
	}



}
