package core;

import model.TaskDataModel;
import dao.TaskDao;
import dao.UserDao;
import entity.TaskEntity;
import entity.UserEntity;
import java.io.IOException;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.Task;
import uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerException_Exception;
import utilities.DateService;
import utilities.DueDateSort;
import utilities.Internationalization;
import utilities.PrioritySort;
import utilities.Redirect;
import utilities.UserService;

/**
 *
 * @author 109472
 */
@ManagedBean(name = "task")
@ViewScoped
public class TaskBean implements Serializable {

    @EJB
    TaskDao ejbBean;
    @EJB
    UserDao userBean;
    private String new_task = Internationalization.getString("add");
    private TaskEntity viewTask = null;
    private String allocated = "";
    private boolean uncompleted = false;
    private String username_remote = null;

    public boolean isUncompleted() {
        return uncompleted;
    }

    public void setUncompleted(boolean uncompleted) {
        this.uncompleted = uncompleted;
    }

    public TaskEntity getViewTask() {
        return viewTask;
    }

    public void setViewTask(TaskEntity viewTask) {
        this.viewTask = viewTask;
    }

    public String getNew_task() {
        return new_task;
    }

    public void setNew_task(String new_task) {
        this.new_task = new_task;
    }

    public void addTask(ActionEvent actionEvent) {
        try {
            TaskEntity t = new TaskEntity();
            t.setTitle(this.new_task);
            Long userID = UserService.UserBean().getId();
            String userName = UserService.UserBean().getUsername();
            t.setUerId(userID);


            Task task = new Task();
            Date date = DateService.getTommorrow();
            task.setDueDate(DateService.convertToXMLGregorianCalendar(date));

            long taskBrokerId = TaskBrokerRemote.allocateTask(task, userName);
            t.setTaskBrokerID(taskBrokerId);
            t.setAllocated(userName);
            t.setCompleted(false);
            t.setDueDate(date);
            t.setPriority(5);
            t.setProposer(userName);
            t.setCreateDate(new Date());
            t.setTaskBrokerID(taskBrokerId);


            ejbBean.addTask(t);
            tasks.add(t);


            if (UserService.UserBean().getSort().equals("1")) {
                PrioritySort comparatorP = new PrioritySort();
                Collections.sort(tasks, comparatorP);
            }
            if (UserService.UserBean().getSort().equals("2")) {
                DueDateSort comparatorP = new DueDateSort();
                Collections.sort(tasks, comparatorP);
            }

            this.new_task = null;
        } catch (TaskBrokerException_Exception ex) {
            Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostConstruct
    public void init() throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        username_remote = request.getRemoteUser();
        login();
        UserBean bean = UserService.UserBean();
        if (!bean.isStatus()) {
            utilities.Redirect.goLogin();
        }
        Long userID = UserService.UserBean().getId();
        if (userID != null) {
            tasks = ejbBean.getTasksByUserId(UserService.UserBean().getUsername());
            if (tasks == null) {
                tasks = new ArrayList<TaskEntity>();
            }



            tasks2 = ejbBean.getUnallocatedTasks();
            if (tasks2 == null) {
                tasks2 = new ArrayList<TaskEntity>();
            }
            this.taskDataModel2 = new TaskDataModel(tasks2);


            PrioritySort comparatorP = new PrioritySort();
            Collections.sort(tasks, comparatorP);
            this.taskDataModel = new TaskDataModel(tasks);

        }

    }

    private boolean login() {
        UserEntity ue = userBean.selectByName(username_remote);
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
    private TaskEntity selectedTask;
    private TaskDataModel taskDataModel;
    private TaskDataModel taskDataModel2;
    List<TaskEntity> tasks = null;
    List<TaskEntity> tasks2 = null;

    public TaskBean() {
    }

    public boolean isTaskEmpty() {
        if (tasks == null || tasks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTaskEmpty2() {
        if (tasks2 == null || tasks2.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public TaskEntity getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(TaskEntity selectedTask) {
        this.selectedTask = selectedTask;
    }

    public TaskDataModel getTaskDataModel() {
        return taskDataModel;
    }

    public TaskDataModel getTaskDataModel2() {
        return taskDataModel2;
    }

    public void onRowSelect(SelectEvent event) {
        TaskEntity t = (TaskEntity) event.getObject();
        this.viewTask = t;

        this.allocated = t.getAllocated();
        if (this.allocated == null) {
            this.allocated = "";
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        this.viewTask = new TaskEntity();
    }

    public void test() {
        System.out.println("save!!!!");
    }

    public void changeAllocated() {

        if (this.viewTask.getAllocated() == null) {
            this.viewTask.setAllocated(this.allocated);
        } else {

            this.allocated = this.viewTask.getAllocated();
            try {
                TaskBrokerRemote.abandonTask(this.viewTask.getTaskBrokerID());
            } catch (TaskBrokerException_Exception ex) {
                Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                TaskBrokerRemote.collectTask(this.viewTask.getTaskBrokerID(), this.viewTask.getAllocated());
            } catch (TaskBrokerException_Exception ex) {
                Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.ejbBean.update(viewTask);


        }
    }

    public void changeData() {
        this.ejbBean.update(this.viewTask);
        if (UserService.UserBean().getSort().equals("1")) {
            PrioritySort comparatorP = new PrioritySort();
            Collections.sort(tasks, comparatorP);
        }
        if (UserService.UserBean().getSort().equals("2")) {
            DueDateSort comparatorP = new DueDateSort();
            Collections.sort(tasks, comparatorP);
        }
    }

    public void sortChanged() {
        if (UserService.UserBean().getSort().equals("2")) {
            PrioritySort comparatorP = new PrioritySort();
            Collections.sort(tasks, comparatorP);
        }
        if (UserService.UserBean().getSort().equals("1")) {
            DueDateSort comparatorP = new DueDateSort();
            Collections.sort(tasks, comparatorP);
        }
        this.taskDataModel = new TaskDataModel(tasks);
    }

    public void changeComplete() {
        if (this.viewTask.isCompleted()) {
            try {
                TaskBrokerRemote.completeTask(this.viewTask.getTaskBrokerID());
                this.ejbBean.update(this.viewTask);
                uncompletedChange();


            } catch (TaskBrokerException_Exception ex) {
                Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void abandonTask() throws IOException {
        try {
            this.viewTask.setAllocated(null);

            TaskBrokerRemote.abandonTask(this.viewTask.getTaskBrokerID());
            this.ejbBean.update(viewTask);
            Redirect.goTask();
        } catch (TaskBrokerException_Exception ex) {
            Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    public void assignTask() throws IOException {
        try {
            if (this.allocated == null || this.allocated.equals("") || this.userBean.checkUsernameAvailability(this.allocated)) {
                this.allocated = "";
                return;
            }
            this.viewTask.setAllocated(this.allocated);



            TaskBrokerRemote.collectTask(this.viewTask.getTaskBrokerID(), this.allocated);
            this.ejbBean.update(viewTask);
            Redirect.goTask();
        } catch (TaskBrokerException_Exception ex) {
            Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    public void deleteTask() throws IOException {
        this.ejbBean.deleteById(this.viewTask.getId());
        Redirect.goTask();
    }

    public String getAllocated() {
        return this.allocated;
    }

    public void setAllocated(String s) {
        this.allocated = s;
    }

    public List<String> complete(String query) {
        List<String> results = userBean.searchUser(query);
        return results;
    }

    public void uncompletedChange() {
        Long userID = UserService.UserBean().getId();
        if (userID != null) {
            tasks = ejbBean.getTasksByUserId(UserService.UserBean().getUsername());
            if (tasks == null) {
                tasks = new ArrayList<TaskEntity>();
            }

            List<TaskEntity> uncompleted = new ArrayList<TaskEntity>();
            if (this.uncompleted == true) {
                for (TaskEntity t : tasks) {
                    if (t.isCompleted() == false) {
                        uncompleted.add(t);
                    }
                }
                tasks = uncompleted;
            }

            if (UserService.UserBean().getSort().equals("1")) {
                PrioritySort comparatorP = new PrioritySort();
                Collections.sort(tasks, comparatorP);
            }
            if (UserService.UserBean().getSort().equals("2")) {
                DueDateSort comparatorP = new DueDateSort();
                Collections.sort(tasks, comparatorP);
            }

            this.taskDataModel = new TaskDataModel(tasks);
        }

    }

    public void tabChanged() {

        tasks2 = ejbBean.getUnallocatedTasks();
        if (tasks2 == null) {
            tasks2 = new ArrayList<TaskEntity>();
        }
        this.taskDataModel2 = new TaskDataModel(tasks2);
    }
}