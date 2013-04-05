package core;

import java.rmi.*;
import dao.TaskDao;
import dao.UserDao;
import entity.UserEntity;
import java.io.IOException;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.xml.ws.WebServiceRef;
import uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerException_Exception;
import utilities.EmailValidation;
import utilities.Internationalization;
import utilities.UserService;

/**
 *
 * @author 109472
 */
@ManagedBean(name = "signup")
@ViewScoped
public class SignupBean implements Serializable {

    @EJB
    UserDao ejbBean;
    private String firstname;
    private String surname;
    private String email;
    private String username;
    private String password;
    private String retype_password;
    private boolean sfirstname = false;
    private boolean ssurname = false;
    private boolean semail = false;
    private boolean susername = false;
    private boolean spassword = false;
    private boolean sretype_password = false;
    private boolean ffirstname = false;
    private boolean fsurname = false;
    private boolean femail = false;
    private boolean fusername = false;
    private boolean fpassword = false;
    private boolean fretype_password = false;
    public String getCfirstname() {

        if (!this.ffirstname) {
            this.ffirstname = true;
            return "";
        }
        if (firstname == null || firstname.isEmpty()) {
            this.sfirstname = false;
            return Internationalization.getString("firstname_not_null");
        } else {
            this.sfirstname = true;
            return Internationalization.getString("tick");
        }
    }

    public SignupBean() {

    }

    public String getCsurname() {


        if (!this.fsurname) {
            this.fsurname = true;
            return "";
        }
        if (surname == null || surname.isEmpty()) {
            this.ssurname = false;
            return Internationalization.getString("surname_not_null");
        } else {
            this.ssurname = true;
            return Internationalization.getString("tick");
        }

    }

    public String getCemail() {

        if (!this.femail) {
            this.femail = true;
            return "";
        }
        if (EmailValidation.checkEmail(this.email)) {
            this.semail = true;
            return Internationalization.getString("tick");
        } else {
            this.semail = false;
            return Internationalization.getString("email_not_available");
        }
    }

    public String getCusername() {

        if (!this.fusername) {
            this.fusername = true;
            return "";
        }

        if (this.username == null || this.username.length() == 0 || ejbBean.checkUsernameAvailability(username.toLowerCase()) == false) {
            this.susername = false;
            return Internationalization.getString("username_not_available");
        } else {
            this.susername = true;
            return Internationalization.getString("tick");
        }


    }

    public String getCpassword() {

        if (!this.fpassword) {
            this.fpassword = true;
            return "";
        }
        if (this.password == null || this.password.length() < 5) {
            this.spassword = false;
            return Internationalization.getString("password_not_available");
        } else {
            this.spassword = true;
            return Internationalization.getString("tick");
        }
    }

    public String getCretype_password() {

        if (!this.fretype_password) {
            this.fretype_password = true;
            return "";
        }
        if (this.password != null && this.retype_password == null) {
            return "";
        }
        if (this.spassword && this.retype_password != null && this.retype_password.equals(this.password)) {
            this.sretype_password = true;
            return Internationalization.getString("tick");
        } else {
            this.sretype_password = false;
            return Internationalization.getString("retype_password_not_available");
        }
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetype_password() {
        return retype_password;
    }

    public void setRetype_password(String retype_password) {
        this.retype_password = retype_password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    private boolean checkAllitems() {
        return this.semail && this.sfirstname && this.spassword && this.sretype_password && this.ssurname && this.susername;
    }

    public void submit(ActionEvent actionEvent) {
        if (!checkAllitems()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Internationalization.getString("error"), ""));
        } else {
            try {
                insertUser();
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");

            } catch (IOException ex) {
                Logger.getLogger(SignupBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void login(ActionEvent actionEvent) {


        
        if (this.username == null || this.password == null || this.username.isEmpty() || this.password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Internationalization.getString("error"), ""));
        } else {
            try {
                if (login()) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("task.xhtml");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Internationalization.getString("error"), ""));
                }


            } catch (IOException ex) {
                Logger.getLogger(SignupBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void insertUser(){
        UserEntity ue = new UserEntity();
        ue.setEmail(this.email);
        ue.setFirstname(this.firstname);
        ue.setSurname(this.surname);
        ue.setUsername(this.username.toLowerCase());
        ue.setPassword(this.password);

        List<String> userP = new ArrayList<String>();
        userP.add(this.username);
        if(ejbBean.checkUsernameAvailability(username.toLowerCase())==false)
        {
            return;
        }
        try {
            TaskBrokerRemote.registerUsers(userP);
        } catch (TaskBrokerException_Exception ex) {
            Logger.getLogger(SignupBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        ejbBean.insert(ue);

    }

    @PostConstruct
    public void init() throws IOException {
        UserBean bean = UserService.UserBean();
        if (bean.isStatus()) {
            utilities.Redirect.goHome();
        }
    }
    
    private boolean login() {
        UserEntity ue = ejbBean.selectByUsernameAndPassword(username, password);
        if (ue == null) {
            
            return false;
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            UserBean user = context.getApplication().evaluateExpressionGet(context, "#{user}", UserBean.class);
            user.setId(ue.getId());
            user.setEmail(ue.getEmail());
            user.setFirstname(ue.getFirstname());
            user.setSurname(ue.getSurname());
            user.setUsername(ue.getUsername());
            user.setStatus(true);
            return true;
        }
    }

}