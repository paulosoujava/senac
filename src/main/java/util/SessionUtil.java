package util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


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

}
