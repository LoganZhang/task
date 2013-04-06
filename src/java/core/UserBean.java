package core;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author 109472
 */
@Named(value = "user")
@SessionScoped
public class UserBean implements Serializable {

    private String locale = "en";
    private String email;
    private String firstname;
    private String surname;
    private String username;
    private String sort = "1";

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    private Long id;
    private boolean status = false;
    private String m = "";
    private static Map<String, Object> countries;

    static {
        countries = new LinkedHashMap<String, Object>();
        countries.put("English", Locale.ENGLISH);
        countries.put("中文", Locale.SIMPLIFIED_CHINESE);
    }

    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    public String getLocale() {
        if (locale == null) {
            locale = "en";
        }
        return locale;
    }

    public String getM() {

        if (this.status == true) {
            try {
                utilities.Redirect.goTask();
            } catch (IOException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void localeChanged(ValueChangeEvent e) {

        String newLocale = e.getNewValue().toString();

        for (Map.Entry<String, Object> entry : countries.entrySet()) {

            if (entry.getValue().toString().equals(newLocale)) {

                FacesContext.getCurrentInstance()
                        .getViewRoot().setLocale((Locale) entry.getValue());

            }
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isStatus() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String username_remote = request.getRemoteUser();
        if (username_remote != null && username_remote.isEmpty() == false) {
            this.status = true;
        }
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static Map<String, Object> getCountries() {
        return countries;
    }

    public static void setCountries(Map<String, Object> countries) {
        UserBean.countries = countries;
    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.status = false;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/ToDoList/");
        } catch (IOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
