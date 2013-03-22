/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import dao.TaskDao;
import entity.TaskEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class TaskDaoBean implements TaskDao {

    @PersistenceContext(unitName = "TaskPU")
    private EntityManager em;

    @Override
    public boolean addTask(TaskEntity task) {
        try {
            em.persist(task);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<TaskEntity> getTasksByUserId(Long userId) {
        try {
            Query q = em.createQuery("SELECT t FROM TaskEntity as t where t.uerId='" + userId + "'");
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                return q.getResultList();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TaskEntity getTaskByTaskBrokerId(long taskBrokerId) {
        try {
            Query q = em.createQuery("SELECT t FROM TaskEntity as t where t.taskBrokerID='" + taskBrokerId + "'");
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                return (TaskEntity) q.getResultList().get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
