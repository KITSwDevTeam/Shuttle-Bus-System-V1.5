package com.EntityClasses;

import java.sql.Timestamp;
import java.util.Date;

public class Location_Master {
		private int id;
		private String name;
		private Timestamp created_at;
		private Timestamp updated_at;
		private boolean enabled;
		
		
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Timestamp getCreated_at() {
			return created_at;
		}
		public void setCreated_at(Timestamp created_at) {
			this.created_at = created_at;
		}
		public Timestamp getUpdated_at() {
			return updated_at;
		}
		public void setUpdated_at(Timestamp updated_at) {
			this.updated_at = updated_at;
		}
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		
		
}
