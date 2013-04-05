/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.Task;
import uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerException_Exception;
import uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.Username;

/**
 *
 * @author Logan
 */
public class TaskBrokerRemote {



    public static void registerUsers(java.util.List<java.lang.String> users) throws TaskBrokerException_Exception {
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service service = new uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service();
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService port = service.getTaskBrokerWebServicePort();
        port.registerUsers(users);
    }

    public static java.util.List<uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.Task> listTasks() throws TaskBrokerException_Exception {
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service service = new uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service();
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService port = service.getTaskBrokerWebServicePort();
        return port.listTasks();
    }

    public static Username getUsername(java.lang.String user) {
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service service = new uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service();
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService port = service.getTaskBrokerWebServicePort();
        return port.getUsername(user);
    }

    public static Task getTask(long taskId) throws TaskBrokerException_Exception {
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service service = new uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service();
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService port = service.getTaskBrokerWebServicePort();
        return port.getTask(taskId);
    }

    public static void completeTask(long taskId) throws TaskBrokerException_Exception {
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service service = new uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service();
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService port = service.getTaskBrokerWebServicePort();
        port.completeTask(taskId);
    }

    public static Task collectTask(long taskId, java.lang.String user) throws TaskBrokerException_Exception {
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service service = new uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service();
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService port = service.getTaskBrokerWebServicePort();
        return port.collectTask(taskId, user);
    }

    public static long allocateTask(uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.Task task, java.lang.String user) throws TaskBrokerException_Exception {
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service service = new uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service();
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService port = service.getTaskBrokerWebServicePort();
        return port.allocateTask(task, user);
    }

    public static void abandonTask(long taskId) throws TaskBrokerException_Exception {
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service service = new uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService_Service();
        uk.ac.susx.inf.ianw.webapps.taskbroker.ejb.TaskBrokerWebService port = service.getTaskBrokerWebServicePort();
        port.abandonTask(taskId);
    }
    
}
