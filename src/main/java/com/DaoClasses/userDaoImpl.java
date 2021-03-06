package com.DaoClasses;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;


//import org.springframework.stereotype.Service;
import com.EncryptionDecryption.Decryption;
import com.EncryptionDecryption.Encryption;
import com.EncryptionDecryption.SecretKeyClass;
import com.EntityClasses.Batch_Master;
import com.EntityClasses.Booking_Master;
import com.EntityClasses.Booking_Request_Master;
import com.EntityClasses.Bus_Master;
import com.EntityClasses.Location_Master;
import com.EntityClasses.Pickup_Location_Master;
import com.EntityClasses.Schedule_Master;
import com.EntityClasses.UserRole;
import com.EntityClasses.User_Info;
import com.HibernateUtil.HibernateUtil;
import com.ModelClasses.IdentifyTypeUser;
import com.ModelClasses.Mail;
import com.ModelClasses.Project_Model;
import com.ModelClasses.Reset_Password;
import com.ModelClasses.Schedule_Model;
import com.ModelClasses.UserModel;
import com.client_mail.ApplicationConfig;
//import com.ServiceClasses.usersService;
//import com.client_mail.ApplicationConfig;
//import com.client_mail.MailService;
import com.client_mail.MailService;




@Repository
public class userDaoImpl implements usersDao{
	
	Encryption encrypt= new Encryption();
	Decryption decrypt= new Decryption();
	
	
	
	//===================For SS========================================  
    
	public String Key(int mount){
		 SecureRandom random = new SecureRandom();
		    String key;
		  
		    key=  new BigInteger(mount*5, random).toString(32);
		   
		return key;
	}
	
    
    
  //=================================================================    

	
	
	
	public User_Info findByUserName(String username) {
    	Transaction trns1 = null; 
        Session session = HibernateUtil.getSessionFactory().openSession();
        //System.out.println("String type user: "+username.split("--")[1]);
        String Username[] = username.split("--");
        
		List<User_Info> users = new ArrayList<User_Info>();
		
		try {
            trns1 = session.beginTransaction();
            users = session.createQuery("from User_Info where email=?").setParameter(0, Username[0]).list();
            
           
            if (users.size() > 0) {
            	for( UserRole us: users.get(0).getUserRole()){
            		System.out.println(us.getRole());
            	}
            	System.out.println(Username.length);
            	if(Username.length>1){
            		
            		users.get(0).setType("google");
            	}
    			return users.get(0);
    		} else {
    			return null;
    		}
        } catch (RuntimeException e) {
        	e.printStackTrace();
        	
        }finally{
		    session.flush();
        	session.close();
        }
		return null;
	}
	
	public boolean createUser(UserModel user,String type) {
    	Transaction trns1 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		try {
            trns1 = session.beginTransaction();
            User_Info user_info = new User_Info();
            Encryption encode = new Encryption();
            String hashedPassword = encode.PasswordEncode(user.getPassword());
            UserRole user_role = new UserRole();
            
            user_info.setBatch_id(0);
            user_info.setEmail(user.getEmail());
            user_info.setName(user.getName());
            user_info.setUsername(user.getUsername());
            user_info.setPhone_number(user.getPhone());
            if(type.equals("google")){
            	user_info.setGooglePassword(hashedPassword);
            	System.out.println("google");
            }
            else {
            	System.out.println("system");
            	user_info.setPassword(hashedPassword);
            	user_info.setGender(user.getGender());
            	
            }
            
            user_info.setEnabled(true);
            if(user.getEmail().contains("@kit.edu.kh")){
            	
            	user_role.setRole("ROLE_STUDENT");
            	
            }else{
            	
            	user_role.setRole("ROLE_CUSTOMER");
     	
            }
            user_role.setUser_info(user_info);
            session.save(user_info);
            session.save(user_role);
          trns1.commit();
          return true;
        } catch (RuntimeException e) {
        	
        }
        finally {
		    session.flush();
		    session.close();
        }
		return false;
	}
	public boolean updateUser(User_Info user,UserModel user_model,String type){
    	Transaction trns1 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		try {
            trns1 = session.beginTransaction();
            Encryption encode = new Encryption();
            String hashedPassword = encode.PasswordEncode(user_model.getPassword());
            System.out.println(user_model.getPassword());
            if(type.equals("google")){
            	user.setGooglePassword(hashedPassword);
            }
            else {
            	System.out.println(type);
            	System.out.println(user.getEmail());
            	user.setPassword(hashedPassword);
            }
            
          session.update(user);
          trns1.commit();
          return true;
        } catch (RuntimeException e) {
        	e.printStackTrace();
        }
        finally {
		    session.flush();
		    session.close();
        }
		return false;
	}
	public int saveBus(Bus_Master bus) {
		List <Bus_Master> buses  = new ArrayList<Bus_Master>();
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            String queryString = "FROM Bus_Master where plate_number=:number and enabled=:status";
            Query query = session.createQuery(queryString);
            query.setString("number",bus.getPlate_number());
            query.setBoolean("status", true);
            buses=(List<Bus_Master>)query.list();
         
    			if(buses.size()>0)
    				return 0;
    		Timestamp created_at = new Timestamp(System.currentTimeMillis());
        	bus.setCreated_at(created_at);
        	bus.setEnabled(true);
            session.save(bus);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 5;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	
	
	public List<User_Info> getAllUsers() {
      	List<User_Info> users= new ArrayList<User_Info>();
   		Transaction trns25 = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>();
			trns25  = session.beginTransaction();
			String queryString  = "from UserRole where role=:role";
 		 	Query query = session.createQuery(queryString);
 		 	query.setString("role", "ROLE_ADMIN");
 		 	List<UserRole> roles = query.list();
 		 	for(int i=0;i<roles.size();i++)
 		 	{
 		 		User_Info user1 = new User_Info();
 		 		User_Info user = roles.get(i).getUser_info();
 		 		user1.setId(user.getId());
 		 		user1.setName(user.getName());
 		 		users.add(user1);
 		 	}
 		 	
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();			
		}
		finally{
			session.flush();
			session.close();
		}
        return users;
    } 
	
	
	public List<User_Info> getAlDrivers() {
      	List<User_Info> users= new ArrayList<User_Info>();
   		Transaction trns25 = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>();
			trns25  = session.beginTransaction();
			String queryString  = "from UserRole where role=:role";
 		 	Query query = session.createQuery(queryString);
 		 	query.setString("role", "ROLE_DRIVER");
 		 	List<UserRole> roles = query.list();
 		 	for(int i=0;i<roles.size();i++)
 		 	{
 		 		User_Info user1 = new User_Info();
 		 		User_Info user = roles.get(i).getUser_info();
 		 		user1.setId(user.getId());
 		 		user1.setName(user.getName());
 		 		user1.setPhone_number(user.getPhone_number());
 		 		user1.setEmail(user.getEmail());
 		 		users.add(user1);
 		 	}
 		 	
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();			
		}
		finally{
			session.flush();
			session.close();
		}
        return users;
    } 
	
	
	
	
	
	
	public User_Info getCustomerById(int id) {
		User_Info customer = new User_Info();
   		Transaction trns25 = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>();
			trns25  = session.beginTransaction();
			String queryString  = "from User_Info where id=:id";
 		 	Query query = session.createQuery(queryString);
 		 	query.setInteger("id", id);
 		 	customer = (User_Info) query.uniqueResult();
 		 	
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();			
		}
		finally{
			session.flush();
			session.close();
		}
        return customer;
    } 



    public User_Info getCustomerByEmail(String email) {
        User_Info customer = new User_Info();
        Transaction trns25 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>();
            trns25  = session.beginTransaction();
            String queryString  = "from User_Info where email=:email";
            Query query = session.createQuery(queryString);
            query.setString("email", email);
            customer = (User_Info) query.uniqueResult();
            
        }
        catch(RuntimeException e)
        {
            e.printStackTrace();            
        }
        finally{
            session.flush();
            session.close();
        }
        return customer;
    } 
	
	
	
	
	
	
	
	
	public List<User_Info> getAlCustomers() {
      	List<User_Info> users= new ArrayList<User_Info>();
   		Transaction trns25 = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>();
			trns25  = session.beginTransaction();
			String queryString  = "from UserRole where role=:role";
 		 	Query query = session.createQuery(queryString);
 		 	query.setString("role", "ROLE_CUSTOMER");
 		 	List<UserRole> roles = query.list();
 		 	for(int i=0;i<roles.size();i++)
 		 	{
 		 		User_Info user1 = new User_Info();
 		 		User_Info user = roles.get(i).getUser_info();
 		 		user1.setId(user.getId());
 		 		user1.setName(user.getName());
 		 		user1.setPhone_number(user.getPhone_number());
 		 		user1.setEmail(user.getEmail());
 		 		users.add(user1);
 		 	}
 		 	
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();			
		}
		finally{
			session.flush();
			session.close();
		}
        return users;
    } 
	
	
	
	
	public int updateBus(Bus_Master bus) {
		int count = 0;
		List <Bus_Master> buses  = new ArrayList<Bus_Master>();
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            buses = getAllBuses();
            for(Bus_Master b:buses)
            {
            	if(bus.getId()!=b.getId())
            			{
            				if (b.getPlate_number().equals(bus.getPlate_number()))
            					count++;
            			}
            }
            if(count>=1)
            	return 0;
            String queryString = "FROM Bus_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",bus.getId());
            Bus_Master updatedBus  = (Bus_Master) query.uniqueResult();
            Timestamp updated_at = new Timestamp(System.currentTimeMillis());
            updatedBus.setDescription(bus.getDescription());
            updatedBus.setModel(bus.getModel());
            updatedBus.setPlate_number(bus.getPlate_number());
        	updatedBus.setUpdated_at(updated_at);
        	updatedBus.setNumber_of_seat(bus.getNumber_of_seat());
            session.update(updatedBus);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 5;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
		}
	
	public List <Pickup_Location_Master> getPickUpLocationByName (String name){
		List <Pickup_Location_Master> p_locations  = new ArrayList<Pickup_Location_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Pickup_Location_Master where name=:name";
            Query query = session.createQuery(queryString);
            query.setString("name",name);
            p_locations=(List<Pickup_Location_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return p_locations;
        } finally {
            session.flush();
            session.close();
        }
        return p_locations;
		
	}
	
	
	public List <Location_Master> getLocationByName (String name){
		List <Location_Master> locations  = new ArrayList<Location_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Location_Master where name=:name";
            Query query = session.createQuery(queryString);
            query.setString("name",name);
            locations=(List<Location_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return locations;
        } finally {
            session.flush();
            session.close();
        }
        return locations;
		
	}
	
	
	
	
	
	public int updateLocation(Location_Master location) {
		int count = 0;
		List <Location_Master> locations  = new ArrayList<Location_Master>();
		List <Pickup_Location_Master> p_locations  = new ArrayList<Pickup_Location_Master>();
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            locations = getAllLocations();
            p_locations = getAllPickUpLocations();
            for(Location_Master l:locations)
            {
            	if(location.getId()!=l.getId())
            			{
            				if (l.getName().equals(location.getName()))
            					count++;
            			}
            }
            p_locations = getPickUpLocationByName(location.getName());
            if(count>=1||p_locations.size()>0)
            	return 0;
            String queryString = "FROM Location_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",location.getId());
            Location_Master updatedLocation  = (Location_Master) query.uniqueResult();
            Timestamp updated_at = new Timestamp(System.currentTimeMillis());
            updatedLocation.setName(location.getName());
            updatedLocation.setUpdated_at(updated_at);
            session.update(updatedLocation);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 5;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
		}
	
	
	
	public int updatePickUpLocation(Pickup_Location_Master p_location){
		int count = 0;
		List <Location_Master> locations  = new ArrayList<Location_Master>();
		List <Pickup_Location_Master> p_locations  = new ArrayList<Pickup_Location_Master>();
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            locations = getAllLocations();
            p_locations = getAllPickUpLocations();
            for(Pickup_Location_Master pl:p_locations)
            {
            	if(p_location.getId()!=pl.getId())
            			{
            				if (pl.getName().equals(p_location.getName()))
            					count++;
            			}
            }
            locations = getLocationByName(p_location.getName());
            if(count>=1||locations.size()>0)
            	return 0;
            String queryString = "FROM Pickup_Location_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",p_location.getId());
            Pickup_Location_Master updatedPLocation  = (Pickup_Location_Master) query.uniqueResult();
            Timestamp updated_at = new Timestamp(System.currentTimeMillis());
            updatedPLocation.setName(p_location.getName());
            updatedPLocation.setUpdated_at(updated_at);
            session.update(updatedPLocation);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 5;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
		
	}
	
	
	


	public List<Bus_Master> getAllBuses(){
		List<Bus_Master> p= new ArrayList<Bus_Master>();
        Transaction trns16 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns16 = session.beginTransaction();
            Query query = session.createQuery("from Bus_Master where enabled =:status");
            query.setBoolean("status", true);
            p = query.list();
            } catch (RuntimeException e) {
            e.printStackTrace();
            return p;
        } finally {
            session.flush();
            session.close();
        }
        return p;
		
	}
	public List<Location_Master> getAllLocations(){
		List<Location_Master> p= new ArrayList<Location_Master>();
        Transaction trns16 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns16 = session.beginTransaction();
            Query query = session.createQuery("from Location_Master where enabled =:status");
            query.setBoolean("status", true);
            p = query.list();
            } catch (RuntimeException e) {
            e.printStackTrace();
            return p;
        } finally {
            session.flush();
            session.close();
        }
        return p;
		
	}
	public List<Pickup_Location_Master> getAllPickUpLocations(){
		List<Pickup_Location_Master> p= new ArrayList<Pickup_Location_Master>();
        Transaction trns16 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns16 = session.beginTransaction();
            Query query = session.createQuery("from Pickup_Location_Master where enabled =:status");
            query.setBoolean("status", true);
            p = query.list();
            } catch (RuntimeException e) {
            e.printStackTrace();
            return p;
        } finally {
            session.flush();
            session.close();
        }
        return p;
		
	}
	public Bus_Master getBusById (int id){
		Bus_Master bus= new Bus_Master();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Bus_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            bus=(Bus_Master)query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return bus;
        } finally {
            session.flush();
            session.close();
        }
        return bus;
		
	}
	
	
	public Booking_Master getBookingById (int id){
		Booking_Master booking= new Booking_Master();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Booking_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            booking=(Booking_Master)query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return booking;
        } finally {
            session.flush();
            session.close();
        }
        return booking;
		
	}
	
	
	
	
	public Booking_Request_Master getBookingRequestById (int id){
		Booking_Request_Master request= new Booking_Request_Master();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Booking_Request_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            request=(Booking_Request_Master)query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return request;
        } finally {
            session.flush();
            session.close();
        }
        return request;
		
	}
	
	
	public List<Booking_Master> getBookingByScheduleId (int id){
		List<Booking_Master> booking= new ArrayList<Booking_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Booking_Master where schedule_id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            booking=(List<Booking_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return booking;
        } finally {
            session.flush();
            session.close();
        }
        return booking;
		
	}
	
	
	public Schedule_Master getScheduleById (int id){
		Schedule_Master schedule= new Schedule_Master();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Schedule_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            schedule=(Schedule_Master)query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return schedule;
        } finally {
            session.flush();
            session.close();
        }
        return schedule;
		
	}
	
	
	public Location_Master getLocationById (int id){
		Location_Master location= new Location_Master();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Location_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            location=(Location_Master)query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return location;
        } finally {
            session.flush();
            session.close();
        }
        return location;
		
	}
	
	
	
	
	public Pickup_Location_Master getPickUpLocationById (int id){
		Pickup_Location_Master p_location= new Pickup_Location_Master();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 = session.beginTransaction();
            String queryString = "from Pickup_Location_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            p_location=(Pickup_Location_Master)query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return p_location;
        } finally {
            session.flush();
            session.close();
        }
        return p_location;
		
	}
	
	
	
	public int deleteBus(int id){
		Transaction trns21 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Bus_Master bus = new Bus_Master();
        try {
            trns21 = session.beginTransaction();
            String queryString = "from Bus_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            bus=(Bus_Master)query.uniqueResult();
            bus.setEnabled(false);
            session.update(bus);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	public int deleteLocation(int id){
		Transaction trns21 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Location_Master location = new Location_Master();
        try {
            trns21 = session.beginTransaction();
            String queryString = "from Location_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            location=(Location_Master)query.uniqueResult();
            location.setEnabled(false);
            session.update(location);
            deletePickUpLocationByLocatinId(id);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	
	public int deleteSchedule(int id){
		Transaction trns21 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Schedule_Master schedule = new Schedule_Master();
        try {
            trns21 = session.beginTransaction();
            String queryString = "from Schedule_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            schedule=(Schedule_Master)query.uniqueResult();
            session.delete(schedule);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	
	public void deletePickUpLocationByLocatinId(int id){
		Transaction trns24 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
           	trns24 = session.beginTransaction();
            String queryString = "update Pickup_Location_Master p set p.enabled=:status where p.location_id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            query.setBoolean("status", false);
            query.executeUpdate();
            session.getTransaction().commit();
         
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
		
	}
	
	
	
	public int deletePickUpLocation(int id){
		Transaction trns21 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Pickup_Location_Master p_location = new Pickup_Location_Master();
        try {
            trns21 = session.beginTransaction();
            String queryString = "from Pickup_Location_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",id);
            p_location=(Pickup_Location_Master)query.uniqueResult();
            p_location.setEnabled(false);
            session.update(p_location);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
		
	}
	
	
	public int saveLocation(Location_Master location){
		List <Location_Master> locations  = new ArrayList<Location_Master>();
		List <Pickup_Location_Master> p_locations  = new ArrayList<Pickup_Location_Master>();
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            String queryString = "FROM Location_Master where name=:name and enabled=:status";
            String queryString2 = "FROM Pickup_Location_Master where name=:name and enabled=:status";
            Query query = session.createQuery(queryString);
            query.setString("name",location.getName());
            query.setBoolean("status",true);
            Query query2 = session.createQuery(queryString2);
            query2.setString("name",location.getName());
            query2.setBoolean("status",true);
            locations=(List<Location_Master>)query.list();
            p_locations=(List<Pickup_Location_Master>)query2.list();
            
    			if(locations.size()>0 || p_locations.size()>0)
    				return 0;
    		Timestamp created_at = new Timestamp(System.currentTimeMillis());
        	location.setCreated_at(created_at);
        	location.setEnabled(true);
            session.save(location);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 5;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	public int saveSchedule(Schedule_Model schedule) throws ParseException{
		List <Schedule_Master> schedules  = new ArrayList<Schedule_Master>();
		Schedule_Master s = new Schedule_Master();
		Date dept_date = null;
		Time dept_time = null;
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            String queryString = "FROM Schedule_Master where code=:code";
            Query query = session.createQuery(queryString);
            query.setString("code",schedule.getCode());
            schedules=(List<Schedule_Master>)query.list();
            if(schedules.size()>0)
    				return 0;
    		Timestamp created_at = new Timestamp(System.currentTimeMillis());
    		int remaining =  new userDaoImpl().getBusById(schedule.getBus_id()).getNumber_of_seat()-schedule.getNumber_booking();
    		s.setBus_id(schedule.getBus_id());
    		s.setCode(schedule.getCode());
    		s.setCreated_at(created_at);
    		s.setDept_date(new SimpleDateFormat("MM/dd/yyyy").parse(schedule.getDept_date()));
    		s.setDept_time(java.sql.Time.valueOf(schedule.getDept_time()));
    		s.setDestination_id(schedule.getDestination_id());
    		s.setDriver_id(schedule.getDriver_id());
    		s.setNumber_booking(schedule.getNumber_booking());
    		s.setNumber_customer(schedule.getNumber_customer());
    		s.setNumber_staff(schedule.getNumber_staff());
    		s.setNumber_student(schedule.getNumber_student());
    		s.setRemaining_seat(remaining);
    		s.setSource_id(schedule.getSource_id());
    		s.setFrom_id(new userDaoImpl().getPickUpLocationById(schedule.getSource_id()).getLocation_id());
    		s.setTo_id(new userDaoImpl().getPickUpLocationById(schedule.getDestination_id()).getLocation_id());
            session.save(s);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 5;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	
	
	public int saveSchedule2(Schedule_Model schedule) throws ParseException{
		List <Schedule_Master> schedules  = new ArrayList<Schedule_Master>();
		Schedule_Master s = new Schedule_Master();
		int id;
		Date dept_date = null;
		Time dept_time = null;
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            String queryString = "FROM Schedule_Master where code=:code";
            Query query = session.createQuery(queryString);
            query.setString("code",schedule.getCode());
            schedules=(List<Schedule_Master>)query.list();
            if(schedules.size()>0)
    				return 0;
    		Timestamp created_at = new Timestamp(System.currentTimeMillis());
    		int remaining =  new userDaoImpl().getBusById(schedule.getBus_id()).getNumber_of_seat()-schedule.getNumber_booking();
    		s.setBus_id(schedule.getBus_id());
    		s.setCode(schedule.getCode());
    		s.setCreated_at(created_at);
    		s.setDept_date(getScheduleById(schedule.getIdd()).getDept_date());
    		s.setDept_time(getScheduleById(schedule.getIdd()).getDept_time());
    		s.setDestination_id(schedule.getDestination_id());
    		s.setDriver_id(schedule.getDriver_id());
    		s.setNumber_booking(schedule.getNumber_booking());
    		s.setNumber_customer(schedule.getNumber_customer());
    		s.setNumber_staff(schedule.getNumber_staff());
    		s.setNumber_student(schedule.getNumber_student());
    		s.setRemaining_seat(remaining);
    		s.setSource_id(schedule.getSource_id());
    		s.setFrom_id(schedule.getFrom_id());
    		s.setTo_id(schedule.getTo_id());
            session.save(s);
            id = s.getId();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return -5;
        } finally {
            session.flush();
            session.close();
        }
		return id;
	}
	
	
	
	
	
	public int updateSchedule(Schedule_Model schedule){
		List <Schedule_Master> schedules  = new ArrayList<Schedule_Master>();
		int count =0;
		Date dept_date = null;
		Time dept_time = null;
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            schedules = getAllSchedules();
            for(Schedule_Master ss:schedules)
            {
                if(schedule.getId()!=ss.getId())
                        {
                            if (ss.getCode().equals(schedule.getCode()))
                                count++;
                        }
            }
            if(count>=1)
                return 0;
            String queryString = "FROM Schedule_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",schedule.getId());
            Schedule_Master s  = (Schedule_Master) query.uniqueResult();
    		Timestamp updated_at = new Timestamp(System.currentTimeMillis());
    		s.setBus_id(schedule.getBus_id());
    		s.setRemaining_seat(new userDaoImpl().getBusById(schedule.getBus_id()).getNumber_of_seat()-new userDaoImpl().getScheduleById(schedule.getId()).getNumber_booking());
    		s.setUpdated_at(updated_at);
    		s.setDriver_id(schedule.getDriver_id());
            session.update(s);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 5;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	
	
	
	
	public int confirmRequest(Booking_Request_Master request){
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            String queryString = "FROM Booking_Request_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",request.getId());
            Booking_Request_Master r  = (Booking_Request_Master) query.uniqueResult();
    		Timestamp updated_at = new Timestamp(System.currentTimeMillis());
    		r.setProvided_time(request.getProvided_time());
    		r.setUpdated_at(updated_at);
    		r.setStatus("Confirmed");
    		session.update(r);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 0;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	
	
	
	public int rejectRequest(Booking_Request_Master request){
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            String queryString = "FROM Booking_Request_Master where id=:id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",request.getId());
            Booking_Request_Master r  = (Booking_Request_Master) query.uniqueResult();
    		Timestamp updated_at = new Timestamp(System.currentTimeMillis());
    		r.setUpdated_at(updated_at);
    		r.setStatus("Rejected");
    		session.update(r);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 0;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
	}
	
	
	
	
	public int savePickUpLocation(Pickup_Location_Master p_location){
		List <Pickup_Location_Master> p_locations  = new ArrayList<Pickup_Location_Master>();
		List <Location_Master> locations  = new ArrayList<Location_Master>();
    	Transaction trns7 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns7 = session.beginTransaction();
            String queryString = "FROM Pickup_Location_Master where name=:name and enabled=:status";
            String queryString2 = "FROM Location_Master where name=:name and enabled=:status";
            Query query = session.createQuery(queryString);
            query.setString("name",p_location.getName());
            query.setBoolean("status",true);
            Query query2 = session.createQuery(queryString2);
            query2.setString("name",p_location.getName());
            query2.setBoolean("status",true);
            p_locations=(List<Pickup_Location_Master>)query.list();
            locations=(List<Location_Master>)query2.list();
         
    			if(locations.size()>0 || p_locations.size()>0)
    				return 0;
    		Timestamp created_at = new Timestamp(System.currentTimeMillis());
        	p_location.setCreated_at(created_at);
        	p_location.setEnabled(true);
        	p_location.setPermanent(true);
            session.save(p_location);  
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns7 != null) {
                trns7.rollback();
            }
            e.printStackTrace();
            return 5;
        } finally {
            session.flush();
            session.close();
        }
		return 1;
		
	}
	public List <Booking_Master> getAllCurrentBookings(){
		List <Booking_Master> bookings  = new ArrayList<Booking_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Booking_Master where dept_date>=:localDate";
            Query query = session.createQuery(queryString);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println(dtf.format(localDate));
            query.setDate("localDate", date);
            bookings=(List<Booking_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return bookings;
        } finally {
            session.flush();
            session.close();
        }
        return bookings;
		
	}
	
	
	
	public List <Booking_Master> getAllBookings(){
		List <Booking_Master> bookings  = new ArrayList<Booking_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Booking_Maste";
            Query query = session.createQuery(queryString);
            bookings=(List<Booking_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return bookings;
        } finally {
            session.flush();
            session.close();
        }
        return bookings;
		
	}
	
	
	
	public List <Booking_Request_Master> getAllCurrentBookingRequests(){
		List <Booking_Request_Master> requests  = new ArrayList<Booking_Request_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Booking_Request_Master where dept_date>=:localDate and status=:status";
            Query query = session.createQuery(queryString);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println(dtf.format(localDate));
            query.setDate("localDate", date);
            query.setString("status", "Pending");
            requests=(List<Booking_Request_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return requests;
        } finally {
            session.flush();
            session.close();
        }
        return requests;
		
	}
	
	
	public List <Booking_Request_Master> getAllHistoricalBookingRequests(){
		List <Booking_Request_Master> requests  = new ArrayList<Booking_Request_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Booking_Request_Master where dept_date<=:localDate or status!=:status";
            Query query = session.createQuery(queryString);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println(dtf.format(localDate));
            query.setDate("localDate", date);
            query.setString("status", "Pending");
            requests=(List<Booking_Request_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return requests;
        } finally {
            session.flush();
            session.close();
        }
        return requests;
		
	}
	
	
	
	
	public List <Schedule_Master> getAllCurrentSchedules(){
		List <Schedule_Master> schedules  = new ArrayList<Schedule_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Schedule_Master where dept_date>=:localDate";
            Query query = session.createQuery(queryString);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println(dtf.format(localDate));
            query.setDate("localDate", date);
            schedules=(List<Schedule_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return schedules;
        } finally {
            session.flush();
            session.close();
        }
        return schedules;
		
	}
	
	
	public List <Schedule_Master> getAllSchedules(){
		List <Schedule_Master> schedules  = new ArrayList<Schedule_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Schedule_Master order by id asc";
            Query query = session.createQuery(queryString);
            schedules=(List<Schedule_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return schedules;
        } finally {
            session.flush();
            session.close();
        }
        return schedules;
		
	}
	
	
	
	public List <Schedule_Master> getAllHistoricalSchedules(){
		List <Schedule_Master> schedules  = new ArrayList<Schedule_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Schedule_Master where dept_date<:localDate";
            Query query = session.createQuery(queryString);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println(dtf.format(localDate));
            query.setDate("localDate", date);
            schedules=(List<Schedule_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return schedules;
        } finally {
            session.flush();
            session.close();
        }
        return schedules;
		
	}
	
	
	
	public List <Booking_Master> getAllHistoricalBookings(){
		List <Booking_Master> bookings  = new ArrayList<Booking_Master>();
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Booking_Master where dept_date<:localDate";
            Query query = session.createQuery(queryString);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println(dtf.format(localDate));
            query.setDate("localDate", date);
            bookings=(List<Booking_Master>)query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return bookings;
        } finally {
            session.flush();
            session.close();
        }
        return bookings;
		
	}
	
	
	
	public List <Schedule_Master> getAllSchedulesByMonth(String month, String year) throws ParseException{
		List <Schedule_Master> schedules  = new ArrayList<Schedule_Master>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Schedule_Master s where s.dept_date between :date1 and :date2";
            Query query = session.createQuery(queryString);
            Date date1;
    		Date date2;
            String first = "01/"+month+"/"+year;
            date1 = formatter.parse(first);
            String last = getLastDateOfMonth(date1)+"/"+month+"/"+year;
       		date2 = formatter.parse(last);
       		System.out.println(date1+"   "+date2);
       		
      		query.setDate("date1", date1);
            query.setDate("date2", date2);
            
            schedules=(List<Schedule_Master>)query.list();        
        } catch (RuntimeException e) {
            e.printStackTrace();
            return schedules;
        } finally {
            session.flush();
            session.close();
        }
        return schedules;
		
	}
	
	
	
	public List <Schedule_Master> schedule_list(String date, String month, String year) throws ParseException{
		List <Schedule_Master> schedules  = new ArrayList<Schedule_Master>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns19 =  session.beginTransaction();
            String queryString = "from Schedule_Master s where s.dept_date=:date1";
            Query query = session.createQuery(queryString);
            Date date1;
            String first = date+"/"+month+"/"+year;
            date1 = formatter.parse(first);
      		query.setDate("date1", date1);
      		schedules=(List<Schedule_Master>)query.list();        
        } catch (RuntimeException e) {
            e.printStackTrace();
            return schedules;
        } finally {
            session.flush();
            session.close();
        }
        return schedules;
		
	}
	
	
	
	
	public String getLastDateOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return Integer.toString(cal.getTime().getDate());
    }
	
	
	public int moveSchedule(int arr[], int id)
	{
		Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        int a[]=arr;    
        int old_id;
        try {
            trns19 =  session.beginTransaction();
            old_id = new userDaoImpl().getBookingById(a[0]).getSchedule_id();
            Schedule_Master master = new userDaoImpl().getScheduleById(old_id);
            master.setNumber_booking(master.getNumber_booking()-new userDaoImpl().getScheduleById(id).getNumber_booking());
            master.setRemaining_seat(master.getRemaining_seat()+new userDaoImpl().getScheduleById(id).getNumber_booking());
            session.update(master);
            for (int i = 0; i < a.length; i++)
   		   {
              Booking_Master booking = new userDaoImpl().getBookingById(a[i]);
              booking.setSchedule_id(id);
   		      session.update(booking);
   		     
   		   }
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns19 != null) {
                trns19.rollback();
            }
            e.printStackTrace();
            return 0;
        } finally {
            session.flush();
            session.close();
        }
        return 1;
	}
	
	
	
	public int moveSimple(int arr[], int old_id, int new_id, int bookings)
	{
		Transaction trns19 = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        int a[]=arr;    
        try {
            trns19 =  session.beginTransaction();
            for (int i = 0; i < a.length; i++)
   		   {
              System.out.println(a[i]);
              Booking_Master booking = getBookingById(a[i]);
              booking.setSchedule_id(new_id);
   		      session.update(booking);
   		     
   		   }
            Schedule_Master old_schedule = getScheduleById(old_id);
            old_schedule.setNumber_booking(old_schedule.getNumber_booking()-bookings);
            old_schedule.setRemaining_seat(old_schedule.getRemaining_seat()+bookings);
            
            Schedule_Master new_schedule = getScheduleById(new_id);
            new_schedule.setNumber_booking(new_schedule.getNumber_booking()+bookings);
            new_schedule.setRemaining_seat(new_schedule.getRemaining_seat()-bookings);
            
            session.update(old_schedule);
            session.update(new_schedule);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
        	if (trns19 != null) {
                trns19.rollback();
            }
            e.printStackTrace();
            return 0;
        } finally {
            session.flush();
            session.close();
        }
        return 1;
	}
	
	
	
	public void confirmedRequest(String email) {
		Boolean ret=false;
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			trns  = session.beginTransaction();
	  	        Mail mail = new Mail();
		        mail.setMailFrom("vkirirom_shuttlebus@gmail.com.com");
		        mail.setMailTo(email);
		        mail.setMailSubject("Kirirom Shuttle Bus Booking Request");
		 
		        Map < String, Object > model = new HashMap < String, Object > ();
		        model.put("fullname", new userDaoImpl().getCustomerByEmail(email).getUsername());
		        model.put("email", email);
		        mail.setModel(model);
		        mail.setFile_name("booking_request_confirmed_email_template.txt");
		        
		 
		        AbstractApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		        MailService mailService = (MailService) context.getBean("mailService");
		        mailService.sendEmail(mail);
		        context.close();
		        ret=true;
		}
		catch (RuntimeException e){
            if (trns != null) {
                trns.rollback();
            }
     	}finally {
            session.flush();
            session.close();
          }
      
	}
	
	
	
	public void rejectedRequest(String email) {
		Boolean ret=false;
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			trns  = session.beginTransaction();
	  	        Mail mail = new Mail();
		        mail.setMailFrom("vkirirom_shuttlebus@gmail.com.com");
		        mail.setMailTo(email);
		        mail.setMailSubject("Kirirom Shuttle Bus Booking Request");
		 
		        Map < String, Object > model = new HashMap < String, Object > ();
		        model.put("fullname", new userDaoImpl().getCustomerByEmail(email).getUsername());
		        model.put("email", email);
		        mail.setModel(model);
		        mail.setFile_name("booking_request_rejected_email_template.txt");
		        
		 
		        AbstractApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		        MailService mailService = (MailService) context.getBean("mailService");
		        mailService.sendEmail(mail);
		        context.close();
		        ret=true;
		}
		catch (RuntimeException e){
            if (trns != null) {
                trns.rollback();
            }
     	}finally {
            session.flush();
            session.close();
          }
        
	}
	

	
	
}






