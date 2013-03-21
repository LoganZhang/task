/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import core.UserBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Logan
 */
public class UserService {
    public static UserBean UserBean()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        UserBean b = context.getApplication().evaluateExpressionGet(context,"#{user}", UserBean.class);
        return b;
    }
}
