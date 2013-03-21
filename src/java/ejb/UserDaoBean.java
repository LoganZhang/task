/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import dao.UserDao;
import entity.UserEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import utilities.StringEncrypt;

@Stateless
public class UserDaoBean implements UserDao {

    @PersistenceContext(unitName = "UserPU")
    private EntityManager em;

    @Override
    public boolean insert(UserEntity user) {
        try {
            em.persist(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public UserEntity selectById(int userId) {
        UserEntity user = em.find(UserEntity.class, Integer.valueOf(userId));
        return user;
    }

    @Override
    public boolean update(UserEntity user) {
        try {
            em.merge(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteById(int userId) {
        try {
            UserEntity user = em.find(UserEntity.class, Integer.valueOf(userId));
            em.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public UserEntity selectByUsernameAndPassword(String username, String password) {

        password = StringEncrypt.Encrypt(password);
        try {
            Query q = em.createQuery("SELECT u FROM UserEntity as u where u.username='" + username + "' and u.password='" + password + "'");
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                return (UserEntity) q.getResultList().get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}