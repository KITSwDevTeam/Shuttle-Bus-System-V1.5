package com.MainController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.EntityClasses.*;
import com.ModelClasses.UserModel;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.DaoClasses.userDaoImpl;
import com.ModelClasses.B_Model;
import com.ModelClasses.Schedule_Model;
import com.ServiceClasses.usersService;

@Controller
public class AdminController {
	@Autowired
	usersService usersService1;
	
	@RequestMapping(value="/customer_home")
	public ModelAndView customer_home() {
		return new ModelAndView("cusomer_home");
	}
//=========================To sign up an account for customer================================
	@RequestMapping(value="/isexist",method=RequestMethod.POST)
	@ResponseBody public Map<String,Object> isExistUser(@RequestBody UserModel userModel) {
		User_Info user = usersService1.findByUserName(userModel.getEmail());
		boolean status = false;
		Map<String,Object> map = new HashMap<String,Object>();
		String role = "";
		if(user != null){

			if(user.getPassword()!=null){
				status = true;
			}
			for(UserRole Role : user.getUserRole()){
				role = Role.getRole();
			}
		}
		map.put("status",status);
		map.put("role",role);
		return map;
	}
//=========================Returns bus management view================================
	@RequestMapping(value="/bus_management", method=RequestMethod.GET)
	public ModelAndView viewBusMng() {
		return new ModelAndView("bus_management");
	}
	@RequestMapping(value="/student_home", method=RequestMethod.GET)
	public ModelAndView Student_Home() {
		return new ModelAndView("student_home");
	}
//=========================Returns report view================================
	@RequestMapping(value="/report", method=RequestMethod.GET)
	public ModelAndView viewReport() {
		return new ModelAndView("report");
	}
//=========================Returns admin booking view================================
	@RequestMapping(value="/admin_booking", method=RequestMethod.GET)
	public ModelAndView admin_booking() {
		return new ModelAndView("admin_booking");
	}
//=========================Returns historical_booking_requestt view================================
	@RequestMapping(value="/historical_booking_request", method=RequestMethod.GET)
	public ModelAndView historical_booking_request() {
		return new ModelAndView("historical_booking_request");
	}
//=========================Returns admin booking request view================================
	@RequestMapping(value="/admin_booking_request", method=RequestMethod.GET)
	public ModelAndView admin_booking_request() {
		return new ModelAndView("admin_booking_request");
	}
//=========================Returns location management view================================
	@RequestMapping(value="/location_management", method=RequestMethod.GET)
	public ModelAndView viewLocationMng() {
		return new ModelAndView("location_management");
	}
//=========================Returns current schedule view================================
	@RequestMapping(value="/current_schedule", method=RequestMethod.GET)
	public ModelAndView current_schedule() {
		return new ModelAndView("current_schedule");
	}
//=========================Returns schedule management view================================
	@RequestMapping(value="/schedule", method=RequestMethod.GET)
	public ModelAndView schedule() {
		return new ModelAndView("schedule");
	}
//=========================Returns historical booking admin view================================
	@RequestMapping(value="/historical_booking", method=RequestMethod.GET)
	public ModelAndView historical_booking() {
		return new ModelAndView("historical_booking");
	}
//=========================Returns historical schedule view================================
	@RequestMapping(value="/historical_schedule", method=RequestMethod.GET)
	public ModelAndView historical_schedule() {
		return new ModelAndView("historical_schedule");
	}
//=========================Returns bus update view================================
	@RequestMapping(value="/bus_update", method=RequestMethod.GET)
	public ModelAndView bus_update(@RequestParam(value = "id", required=true, defaultValue = "0") Integer id) {
		Bus_Master bus = usersService1.getBusById(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bus", bus);
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("bus_update","data",json);
	}

	
//=========================Returns booking detail view================================
	@RequestMapping(value="/booking_detail", method=RequestMethod.GET)
	public ModelAndView booking_detail(@RequestParam(value = "id", required=true, defaultValue = "0") Integer id) {
		Booking_Master booking = usersService1.getBookingById(id);
		B_Model model = new B_Model();
		model.setN(usersService1.getCustomerById(booking.getUser_id()).getName());
		model.setCode(booking.getCode());
		model.setCreated_at(booking.getCreated_at().toString());
		model.setDept_date(booking.getDept_date().toString());
		model.setDept_time(booking.getDept_time());
		model.setDescription(booking.getDescription());
		model.setId(booking.getId());
		model.setDestination_id(booking.getDestination_id());
		model.setNotification(booking.getNotification());
		model.setNumber_booking(booking.getNumber_booking());
		model.setQr(booking.getQr());
		model.setSchedule_id(booking.getSchedule_id());
		model.setSource_id(booking.getSource_id());
		model.setUpdated_at(booking.getUpdated_at().toString());
		model.setUser_id(booking.getUser_id());
		model.setFrom_id(booking.getFrom_id());
		model.setTo_id(booking.getTo_id());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("booking", model);
		map.put("locations", usersService1.getAllLocations());
		map.put("p_locations", usersService1.getAllPickUpLocations());
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("booking_detail","data",json);
	}
	
	
	
	
//=========================Returns booking request detail view================================
	@RequestMapping(value="/request_detail", method=RequestMethod.GET)
	public ModelAndView request_detail(@RequestParam(value = "id", required=true, defaultValue = "0") Integer id) {
		Booking_Request_Master request = usersService1.getBookingRequestById(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("request", request);
		map.put("p_locations", usersService1.getAllPickUpLocations());
		map.put("locations", usersService1.getAllLocations());
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("request_detail","data",json);
	}
	
	
	
//=========================Returns historical booking request detail view================================
	@RequestMapping(value="/historical_request_detail", method=RequestMethod.GET)
	public ModelAndView historical_request_detail(@RequestParam(value = "id", required=true, defaultValue = "0") Integer id) {
		Booking_Request_Master request = usersService1.getBookingRequestById(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("request", request);
		map.put("locations", usersService1.getAllLocations());
		map.put("p_locations", usersService1.getAllPickUpLocations());
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("historical_request_detail","data",json);
	}
	
	
//=========================Returns schedule detail view================================
	@RequestMapping(value="/schedule_detail", method=RequestMethod.GET)
	public ModelAndView schedule_detail(@RequestParam(value = "id", required=true, defaultValue = "0") Integer id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("schedule", usersService1.getScheduleById(id));
		map.put("schedules", usersService1.getAllSchedules());
		map.put("locations", usersService1.getAllLocations());
		map.put("p_locations", usersService1.getAllPickUpLocations());
		map.put("buses", usersService1.getAllBuses());
		map.put("bookings", usersService1.getBookingByScheduleId(id));
		map.put("drivers", usersService1.getAlDrivers());
		map.put("customers", usersService1.getAlCustomers());
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("schedule_detail","data",json);
	}
//=========================Returns create schedule view================================
	@RequestMapping(value="/create_schedule", method=RequestMethod.GET)
	public ModelAndView create_schedule() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", getScheduleSequence());
		map.put("main_locations", usersService1.getAllLocations());
		map.put("locations", usersService1.getAllPickUpLocations());
		map.put("buses", usersService1.getAllBuses());
		map.put("drivers", usersService1.getAlDrivers());
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("create_schedule","data",json);
	}
	
//=========================Returns create user view================================
	@RequestMapping(value="/create_user", method=RequestMethod.GET)
	public ModelAndView create_user() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", getScheduleSequence());
		map.put("main_locations", usersService1.getAllLocations());
		map.put("locations", usersService1.getAllPickUpLocations());
		map.put("buses", usersService1.getAllBuses());
		map.put("drivers", usersService1.getAlDrivers());
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("create_user","data",json);
	}


	
//====================To save bus============================
	@RequestMapping(value="/createBus", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toSaveProject(Bus_Master bus) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		int check = usersService1.saveBus(bus);
		if(check==0)
		{
			map.put("status","0");
			map.put("message","Plate number already existed!");
		}
		else if(check==1)
		{
			map.put("status","1");
			map.put("message","Bus has just been created successfully");
		}
		else
		{
			map.put("status","5");
			map.put("message","Technical problem occurs");
		}
		return map;
		}
//====================To save schedule by admin============================
	@RequestMapping(value="/createSchedule", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toSaveSchedule(Schedule_Model schedule) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println(("In java"));
		System.out.println(schedule.getCode()+"  "+schedule.getDriver_id()+"  "+schedule.getBus_id()+"  "+schedule.getSource_id()+"  "+schedule.getDestination_id()+"  "+schedule.getNumber_booking()+"  "+schedule.getDept_date()+"  "+schedule.getDept_time());;
		int check = usersService1.saveSchedule(schedule);
		if(check==0)
		{
			map.put("status","0");
			map.put("message","Code already existed!");
		}
		else if(check==1)
		{
			map.put("status","1");
			map.put("message","Schedule has just been created successfully");
		}
		else
		{
			map.put("status","5");
			map.put("message","Technical problem occurs");
		}
		return map;
		}
	
	//====================To getBookingByScheduleId by admin============================
		@RequestMapping(value="/getBookingByScheduleId", method=RequestMethod.GET)
		public @ResponseBody Map<String,Object> toGetBookingByScheduleId(@RequestParam(value = "id", required=true) Integer id) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			List <Booking_Master> bookings  = new ArrayList<Booking_Master>();
			bookings = usersService1.getBookingByScheduleId(id);
			int check =bookings.size();
			if(check>0)
				map.put("message","Not Fine");
			else
				map.put("message","Fine");
			return map;
			}
//====================To delete Schedule By id by admin============================
		@RequestMapping(value="/deleteSchedule", method=RequestMethod.GET)
		public @ResponseBody Map<String,Object> deleteSchedule(@RequestParam(value = "id", required=true) Integer id) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			List <Booking_Master> bookings  = new ArrayList<Booking_Master>();
			bookings = usersService1.getBookingByScheduleId(id);
			int check =bookings.size();
			if(check<=0)
			{
				
				if(usersService1.deleteSchedule(id)==1)
					{
						map.put("status", "1");
						map.put("message","This schedule has just been deleted successfully!");
					}
				else
					{
						map.put("status", "0");
						map.put("message","This schedule has not been deleted!");
					}
				
			}
				
			else
				{
				map.put("status", "5");
				map.put("message","This schedule cannot be deleted, please make sure this schedule is not containing any bookings.");
				}
			return map;
			}
//====================To save schedule by admin============================
	@RequestMapping(value="/updateSchedule", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toUpdateSchedule(Schedule_Model schedule) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println(schedule.getCode()+"  "+schedule.getDriver_id()+"  "+schedule.getBus_id()+"  "+schedule.getSource_id()+"  "+schedule.getDestination_id()+"  "+schedule.getNumber_booking()+"  "+schedule.getDept_date()+"  "+schedule.getDept_time());;
		int check = usersService1.updateSchedule(schedule);
		if(check==0)
		{
			map.put("status","0");
			map.put("message","Code already existed!");
		}
		else if(check==1)
		{
			map.put("status","1");
			map.put("message","Schedule has just been updated successfully");
		}
		else
		{
			map.put("status","5");
			map.put("message","Technical problem occurs");
		}
		return map;
		}
//====================To move booking to new schedule by admin============================
	@RequestMapping(value="/moveNew", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toMoveNew(Schedule_Model s) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		int all_booking = 0;
		int a[] =s.getB();
		for(int i=0; i<a.length;i++)
			all_booking+=usersService1.getBookingById(a[i]).getNumber_booking();
		Schedule_Model newSchedule = new Schedule_Model();
		Schedule_Master schedule = usersService1.getScheduleById(s.getId());
		newSchedule.setIdd(s.getId());
		newSchedule.setCode(getScheduleSequence());
		newSchedule.setDriver_id(s.getDriver_id());
		newSchedule.setBus_id(s.getBus_id());
		newSchedule.setSource_id(schedule.getSource_id());
		newSchedule.setDestination_id(schedule.getDestination_id());
		newSchedule.setNumber_booking(all_booking);
		newSchedule.setFrom_id(schedule.getFrom_id());
		newSchedule.setTo_id(schedule.getTo_id());
		int check = usersService1.saveSchedule2(newSchedule);
		if(check==0)
		{
			map.put("status","0");
			map.put("message","Schedule code already existed!");
		}
		else if(check==-5)
		{
			map.put("status","0");
			map.put("message","Technical problem when creating new schedule occurs!");
		}
		else
		{
			int check2 = usersService1.moveSchedule(a,check);
			if(check2==1)
			{
				map.put("status","1");
				map.put("message","New schedule has just been created and its code is "+usersService1.getScheduleById(check).getCode()+"!");
			}
			else
			{
				usersService1.deleteSchedule(check);
				map.put("status","0");
				map.put("message","New schedule has not just been created and bookings are not moved to anywhere!");
			}
		}
		return map;
		}	
	
	
	
//====================To move booking to existing schedule by admin============================
	@RequestMapping(value="/moveSimple", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toMoveSimlple(Schedule_Model s) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		int check = usersService1.moveSimple(s.getB(), s.getOld_id(), s.getNew_id(), s.getNumber_booking());
		if(check==1)
		{
			map.put("status","1");
			map.put("message","Bookings have just been successfully moved!");
		}
		else
		{
			map.put("status","0");
			map.put("message","Bookings have not been successfully moved!");
		}
		return map;
		}		
	
	
	
	
//====================To confirm request by admin============================
	@RequestMapping(value="/confirmRequest", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toConfirmRequest(Booking_Request_Master request) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		int check = usersService1.confirmRequest(request);
		if(check==0)
		{
			map.put("status","0");
			map.put("message","Technical problem occurs!");

		}
		else
		{
			map.put("status","1");
			map.put("message","This request has just been confirmed successfully");
			User_Info user = usersService1.getCustomerById(request.getUser_id());
			String email = user.getEmail();
			usersService1.confirmedRequest(email);	
		}
		
		return map;
		}
	
	
	
//====================To reject request by admin============================
	@RequestMapping(value="/rejectRequest", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toRejectRequest(Booking_Request_Master request) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		int check = usersService1.rejectRequest(request);
		if(check==0)
		{
			map.put("status","0");
			map.put("message","Technical problem occurs!");
			
		}
		else
		{
			map.put("status","1");
			map.put("message","This request has just been rejected successfully");
			User_Info user = usersService1.getCustomerById(request.getUser_id());
			String email = user.getEmail();
			usersService1.rejectedRequest(email);
		}
		
		return map;
		}
	
	
	
//====================To update bus============================
		@RequestMapping(value="/updateBus", method=RequestMethod.GET)
		public @ResponseBody Map<String,Object> toUpdateBus(Bus_Master bus) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			int check = usersService1.updateBus(bus);
			if(check==0)
			{
				map.put("status","0");
				map.put("message","Plate number already existed!");
			}
			else if(check==1)
			{
				map.put("status","1");
				map.put("message","Bus has just been updated successfully");
			}
			else
			{
				map.put("status","5");
				map.put("message","Technical problem occurs");
			}
			return map;
			}
//====================To update location============================
		@RequestMapping(value="/updateLocation", method=RequestMethod.GET)
		public @ResponseBody Map<String,Object> toUpdateLocation(Location_Master location) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			int check = usersService1.updateLocation(location);
			if(check==0)
			{
				map.put("status","0");
				map.put("message","Location name already existed!");
			}
			else if(check==1)
			{
				map.put("status","1");
				map.put("message","Location has just been updated successfully");
			}
			else
			{
				map.put("status","5");
				map.put("message","Technical problem occurs");
			}
			return map;
			}
//====================To update pick up location============================
		@RequestMapping(value="/updatePickUpLocation", method=RequestMethod.GET)
		public @ResponseBody Map<String,Object> toUpdatePickUpLocation(Pickup_Location_Master p_location) throws Exception{
			Map<String,Object> map = new HashMap<String,Object>();
			int check = usersService1.updatePickUpLocation(p_location);
			if(check==0)
			{
				map.put("status","0");
				map.put("message","Pick up location name already existed!");
			}
			else if(check==1)
			{
				map.put("status","1");
				map.put("message","Pick up location has just been updated successfully");
			}
			else
			{
				map.put("status","5");
				map.put("message","Technical problem occurs");
			}
			return map;
			}
//====================To delete bus============================
		@RequestMapping(value="/deleteBus", method = RequestMethod.GET)
		public @ResponseBody Map<String,String>  deleteProjectDetail(@RequestParam(value = "id", required=false) Integer id){
			Map<String,String> map = new HashMap<String,String>();
			int status = 0;
			status= usersService1.deleteBus(id);
			if(status==1)
				map.put("status", "200");
			else 	
				map.put("status", "300");
			
			return map;
			}
//====================To delete location============================
		@RequestMapping(value="/deleteLocation", method = RequestMethod.GET)
		public @ResponseBody Map<String,String>  deleteLocation(@RequestParam(value = "id", required=false) Integer id){
			Map<String,String> map = new HashMap<String,String>();
			int status = 0;
			status= usersService1.deleteLocation(id);
			if(status==1)
				map.put("status", "200");
			else 	
				map.put("status", "300");
			
			return map;
			}
//====================To delete pick up location============================
		@RequestMapping(value="/deletePickUpLocation", method = RequestMethod.GET)
		public @ResponseBody Map<String,String>  deletePickUpLocation(@RequestParam(value = "id", required=false) Integer id){
			Map<String,String> map = new HashMap<String,String>();
			int status = 0;
			status= usersService1.deletePickUpLocation(id);
			if(status==1)
				map.put("status", "200");
			else 	
				map.put("status", "300");
			
			return map;
			}
		
//====================To save location============================
	@RequestMapping(value="/createLocation", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toSaveLocation(Location_Master location) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		int check = usersService1.saveLocation(location);
		if(check==0)
		{
			map.put("status","0");
			map.put("message","Location name already existed!");
		}
		else if(check==1)
		{
			map.put("status","1");
			map.put("message","Location has just been created successfully");
		}
		else
		{
			map.put("status","5");
			map.put("message","Technical problem occurs");
		}
		return map;
		}
//====================Booking report============================
	@RequestMapping(value="/bookingReport", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> bookingReport(){
		Map<String,Object> map = new HashMap<String,Object>();
			map.put("locations",usersService1.getAllLocations());
		return map;
		}
//====================To save location============================
	@RequestMapping(value="/createPickUpLocation", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> toSavePickUpLocation(Pickup_Location_Master p_location) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		int check = usersService1.savePickUpLocation(p_location);
		if(check==0)
		{
			map.put("status","0");
			map.put("message","Pick up location name already existed!");
		}
		else if(check==1)
		{
			map.put("status","1");
			map.put("message","Pick up location has just been created successfully");
		}
		else
		{
			map.put("status","5");
			map.put("message","Technical problem occurs");
		}
		return map;
		}
	@RequestMapping(value="/getAllBuses", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getProjectCategoryList(){
				
		 Map<String,Object> map = new HashMap<String,Object>();
	
		   // DaoClasses.userDaoImpl dao = new DaoClasses.userDaoImpl();
			List<Bus_Master> list = usersService1.getAllBuses();
			 		
			if (list != null)
				map.put("buses", list);
			else
				map.put("message","Data not found");			
			
			return map;
	}
	@RequestMapping(value="/getAllCurrentBookings", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAllCurrentBookings(){
				
		 Map<String,Object> map = new HashMap<String,Object>();
	
		   // DaoClasses.userDaoImpl dao = new DaoClasses.userDaoImpl();
			List<Booking_Master> list = usersService1.getAllCurrentBookings();
			List<Location_Master> list2 =  usersService1.getAllLocations();
			 		
			if (list != null)
			{
			map.put("locations", list2);
			map.put("bookings", list);
			map.put("customers", usersService1.getAlCustomers());
			}
			else
				map.put("message","Data not found");			
			
			return map;
	}
	
	
	
	
	
	@RequestMapping(value="/getAllCurrentBookingRequests", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAllCurrentBookingRequests(){
				
		 Map<String,Object> map = new HashMap<String,Object>();
	
		   // DaoClasses.userDaoImpl dao = new DaoClasses.userDaoImpl();
			List<Booking_Request_Master> list = usersService1.getAllCurrentBookingRequests();
			List<Location_Master> list2 =  usersService1.getAllLocations();
			 		
			if (list != null)
			{
			map.put("locations", list2);
			map.put("requests", list);
			}
			else
				map.put("message","Data not found");			
			
			return map;
	}


	@RequestMapping(value="/getBookingRequestNotification", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getBookingRequestNotification(){
				
		 Map<String,Object> map = new HashMap<String,Object>();
	
		   // DaoClasses.userDaoImpl dao = new DaoClasses.userDaoImpl();
			List<Booking_Request_Master> list = usersService1.getAllCurrentBookingRequests();
			
			if (list != null)
				map.put("requests", list);
			else
				map.put("message","Data not found");			
			
			return map;
	}
	
	
	
	
	@RequestMapping(value="/getAllHistoricalBookingRequests", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAllHistoricalBookingRequests(){
				
		 Map<String,Object> map = new HashMap<String,Object>();
	
		   // DaoClasses.userDaoImpl dao = new DaoClasses.userDaoImpl();
			List<Booking_Request_Master> list = usersService1.getAllHistoricalBookingRequests();
			List<Location_Master> list2 =  usersService1.getAllLocations();
			 		
			if (list != null)
			{
			map.put("locations", list2);
			map.put("requests", list);
			}
			else
				map.put("message","Data not found");			
			
			return map;
	}
	
	
	
	
	
	
	@RequestMapping(value="/getAllCurrentSchedules", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAllCurrentSchedules(){
				
			Map<String,Object> map = new HashMap<String,Object>();
			List<Schedule_Master> list = usersService1.getAllCurrentSchedules();			 		
			if (list != null)
			{
			map.put("schedules", list);
			map.put("locations", usersService1.getAllLocations());
			map.put("buses", usersService1.getAllBuses());
			map.put("drivers", usersService1.getAlDrivers());
			}
			else
				map.put("message","Data not found");			
			
			return map;
	}
	
	
	
	@RequestMapping(value="/getAllHistoricalSchedules", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAllHistoricalSchedules(){
				
			Map<String,Object> map = new HashMap<String,Object>();
			List<Schedule_Master> list = usersService1.getAllHistoricalSchedules();			 		
			if (list != null)
			{
			map.put("schedules", list);
			map.put("locations", usersService1.getAllLocations());
			map.put("buses", usersService1.getAllBuses());
			map.put("drivers", usersService1.getAlDrivers());
			}
			else
				map.put("message","Data not found");			
			
			return map;
	}
	

	
	
	@RequestMapping(value="/getAllHistoricalBookings", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAllHistoricalBookings(){
				
		 Map<String,Object> map = new HashMap<String,Object>();
	
		   // DaoClasses.userDaoImpl dao = new DaoClasses.userDaoImpl();
			List<Booking_Master> list = usersService1.getAllHistoricalBookings();
			List<Location_Master> list2 =  usersService1.getAllLocations();
			 		
			if (list != null)
				{
				map.put("locations", list2);
				map.put("bookings", list);
				map.put("customers", usersService1.getAlCustomers());
				}
			
			else
				map.put("message","Data not found");			
			
			return map;
	}
	
	@RequestMapping(value="/getAllSchedulesByMonth", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAllSchedulesByMonth(@RequestParam(value = "month", required=true, defaultValue = "0") Integer month, @RequestParam(value = "year", required=true, defaultValue = "0") Integer year) throws ParseException{
		 Map<String,Object> map = new HashMap<String,Object>();
			List<Schedule_Master> list = usersService1.getAllSchedulesByMonth(Integer.toString(month),Integer.toString(year));
			if (list != null)	
				map.put("schedules", list);
			else
				map.put("message","Data not found");			
			
			return map;
	}
//=========================Returns schedule list by month view================================
	@RequestMapping(value="/schedule_list", method=RequestMethod.GET)
	public ModelAndView schedule_list(@RequestParam(value = "date", required=true, defaultValue = "0") Integer date,@RequestParam(value = "month", required=true, defaultValue = "0") Integer month, @RequestParam(value = "year", required=true, defaultValue = "0") Integer year) throws ParseException{
		List<Schedule_Master> list = usersService1.schedule_list(Integer.toString(date),Integer.toString(month),Integer.toString(year));
		List<Pickup_Location_Master> list2 =  usersService1.getAllPickUpLocations();
		List<Bus_Master> list3 =  usersService1.getAllBuses();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("schedules", list);
		map.put("locations", list2);
		map.put("buses", list3);
		map.put("drivers", usersService1.getAlDrivers());
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("schedule_list","data",json);
	}
	
	
	@RequestMapping(value="/getAllLocations", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAllLocations(){
				
		 Map<String,Object> map = new HashMap<String,Object>();
	
		   // DaoClasses.userDaoImpl dao = new DaoClasses.userDaoImpl();
			List<Location_Master> list = usersService1.getAllLocations();
			List<Pickup_Location_Master> list2 =  usersService1.getAllPickUpLocations();
			 		
			if (list != null)
				{
				map.put("locations", list);
				map.put("p_locations", list2);
				}
			else
				map.put("message","Data not found");			
			
			return map;
	}
	
	@RequestMapping(value="/getLocationById", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getLocationById(@RequestParam(value = "id", required=true, defaultValue = "0") Integer id) {
		Location_Master location = usersService1.getLocationById(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("location", location);
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value="/getPickUpLocationById", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getPickUpLocationById(@RequestParam(value = "id", required=true, defaultValue = "0") Integer id) {
		Pickup_Location_Master p_location = usersService1.getPickUpLocationById(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("p_location", p_location);
		ObjectMapper mapper = new ObjectMapper();
		String json="";
		try {
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static String getScheduleSequence(){
		List<Schedule_Master> schedules = new ArrayList<Schedule_Master>();
		schedules = new userDaoImpl().getAllSchedules();
		int code;
		String scode= "000001";
		for(Schedule_Master s : schedules)
			System.out.println(s.getId());
		if(schedules.size()>0){
			code = 1000000+schedules.get(schedules.size()-1).getId()+1;
			scode = Integer.toString(code);
			scode = scode.substring(1);
			return "S"+scode;
		}
		else 
			return "S"+scode;
		
	}
	
	public static String getBookingSequence(){
		List<Booking_Master> bookings = new ArrayList<Booking_Master>();
		bookings = new userDaoImpl().getAllBookings();
		int code;
		String scode= "000001";
		for(Booking_Master s : bookings)
			System.out.println(s.getId());
		if(bookings.size()>0){
			code = 1000000+bookings.get(bookings.size()-1).getId()+1;
			scode = Integer.toString(code);
			scode = scode.substring(1);
			return "B"+scode;
		}
		else 
			return "B"+scode;
		
	}
}
