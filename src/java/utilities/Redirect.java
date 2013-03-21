/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.IOException;
import javax.faces.context.FacesContext;

/**
 *
 * @author Logan
 */
public class Redirect {

    public static void goHome() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/ToDoList/");
        
    }
    public static void goLogin() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        
    }
}
