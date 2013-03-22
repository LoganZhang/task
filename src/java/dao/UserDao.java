package dao;

import entity.UserEntity;
import javax.ejb.Local;

@Local
public interface UserDao {
	
	public boolean insert(UserEntity user);
	
	public UserEntity selectById(int userId);
        
        public boolean checkUsernameAvailability(String username);
        
        public UserEntity selectByUsernameAndPassword(String username,String password);
	
	public boolean update(UserEntity user);
	
	public boolean deleteById(int userId);

}