/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import core.UserBean;

/**
 *
 * @author 109472
 */
public class Internationalization {
    public static String getString(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        UserBean languageState = context.getApplication().evaluateExpressionGet(context,"#{user}", UserBean.class);
        Locale currentLocale = new Locale(languageState.getLocale());
        ResourceBundle myResources = ResourceBundle.getBundle("language.message", currentLocale);
        if(key == null)
        {
            return "";
        }
        return myResources.getString(key);
    }
}
