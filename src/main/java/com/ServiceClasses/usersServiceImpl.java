package com.ServiceClasses;


import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DaoClasses.usersDao;
import com.EntityClasses.Batch_Master;
import com.EntityClasses.Booking_Master;
import com.EntityClasses.Booking_Request_Master;
import com.EntityClasses.Bus_Master;
import com.EntityClasses.Location_Master;
import com.EntityClasses.Pickup_Location_Master;
import com.EntityClasses.Schedule_Master;
import com.EntityClasses.User_Info;
import com.ModelClasses.Schedule_Model;
import com.ModelClasses.UserModel;




@Service
public class usersServiceImpl implements usersService{

	@Autowired
	usersDao usersDao1;
	public User_Info findByUserName(String name){
		return usersDao1.findByUserName(name);
	}
	public int saveBus(Bus_Master bus) {
		return usersDao1.saveBus(bus);
	}
	public List<Bus_Master> getAllBuses(){
		return usersDao1.getAllBuses();
	}
	public Bus_Master getBusById (int id){
		return usersDao1.getBusById(id);
	}
	public int updateBus(Bus_Master bus){
		return usersDao1.updateBus(bus);
	}
	public int deleteBus(int id){
		return usersDao1.deleteBus(id);
	}
	public int saveLocation(Location_Master location) {
		return usersDao1.saveLocation(location);
	}
	public List<Location_Master> getAllLocations(){
		return usersDao1.getAllLocations();
	}
	public int savePickUpLocation(Pickup_Location_Master p_location){
		return usersDao1.savePickUpLocation(p_location);
	}
	public List<Pickup_Location_Master> getAllPickUpLocations(){
		return usersDao1.getAllPickUpLocations();
	}
	public Location_Master getLocationById(int id){
		return usersDao1.getLocationById(id);
	}
	public int updateLocation(Location_Master location){
		return usersDao1.updateLocation(location);
	}
	public Pickup_Location_Master getPickUpLocationById(int id){
		return usersDao1.getPickUpLocationById(id);
	}
	public int updatePickUpLocation(Pickup_Location_Master p_location){
		return usersDao1.updatePickUpLocation(p_location);
	}
	public int deleteLocation(int id){
		return usersDao1.deleteLocation(id);
	}
	public int deletePickUpLocation(int id){
		return usersDao1.deletePickUpLocation(id);
	}
	public List <Booking_Master> getAllCurrentBookings(){
		return usersDao1.getAllCurrentBookings();
	}
	public List <Booking_Master> getAllHistoricalBookings(){
		return usersDao1.getAllHistoricalBookings();
	}
	public Booking_Master getBookingById (int id){
		return usersDao1.getBookingById(id);
	}
	public List <Schedule_Master> getAllSchedulesByMonth(String month, String year) throws ParseException, java.text.ParseException{
		return usersDao1.getAllSchedulesByMonth(month, year);
	}
	public List <Schedule_Master> schedule_list(String date, String month, String year) throws ParseException, java.text.ParseException{
		return usersDao1.schedule_list(date, month, year);
	}
	public List<Booking_Master> getBookingByScheduleId (int id){
		return usersDao1.getBookingByScheduleId(id);
	}
	public Schedule_Master getScheduleById (int id){
		return usersDao1.getScheduleById(id);
	}
	public List <Schedule_Master> getAllHistoricalSchedules(){
		return usersDao1.getAllHistoricalSchedules();
	}
	public List <Schedule_Master> getAllCurrentSchedules(){
		return usersDao1.getAllCurrentSchedules();
	}
	public List <Schedule_Master> getAllSchedules(){
		return usersDao1.getAllSchedules();
	}
	public List<User_Info> getAllUsers(){
		return usersDao1.getAllUsers();
	}
	public int saveSchedule(Schedule_Model schedule)throws ParseException{
		return usersDao1.saveSchedule(schedule);
	}
	public int updateSchedule(Schedule_Model schedule) throws ParseException{
		return usersDao1.updateSchedule(schedule);
	}
	public List <Booking_Request_Master> getAllCurrentBookingRequests(){
		return usersDao1.getAllCurrentBookingRequests();
	}
	public Booking_Request_Master getBookingRequestById (int id){
		return usersDao1.getBookingRequestById(id);
	}
	public int confirmRequest(Booking_Request_Master request){
		return usersDao1.confirmRequest(request);
	}
	public List <Booking_Request_Master> getAllHistoricalBookingRequests(){
		return usersDao1.getAllHistoricalBookingRequests();
	}
	public int rejectRequest(Booking_Request_Master request){
		return usersDao1.rejectRequest(request);
	}
	public int deleteSchedule(int id){
		return usersDao1.deleteSchedule(id);
	}
	public int moveSchedule(int arr[], int id){
		return usersDao1.moveSchedule(arr, id);
	}
	public int saveSchedule2(Schedule_Model schedule)throws ParseException{
		return usersDao1.saveSchedule2(schedule);
	}
	public int moveSimple(int arr[], int old_id, int new_id, int bookings){
		return usersDao1.moveSimple(arr, old_id, new_id, bookings);
	}
	public boolean createUser(UserModel user,String type) {

		return usersDao1.createUser(user,type);
	}
	public boolean updateUser(User_Info user, UserModel user_model,String type) {
		
		return usersDao1.updateUser(user, user_model,type);
	}
	public List<User_Info> getAlDrivers(){
		return usersDao1.getAlDrivers();
	}
	public List<User_Info> getAlCustomers(){
		return usersDao1.getAlCustomers();
	}
	public void rejectedRequest(String email){
		 usersDao1.rejectedRequest(email);
	}
	public void confirmedRequest(String email){
		usersDao1.confirmedRequest(email);
	}
	public User_Info getCustomerById(int id){
		return usersDao1.getCustomerById(id);
	}
	
	
}


