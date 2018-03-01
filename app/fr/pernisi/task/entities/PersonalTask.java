package fr.pernisi.task.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personaltask")
public class PersonalTask {

	 public PersonalTask() {
		 
	 }
	
    public PersonalTask(String l_title, String l_description) {
    	title=l_title;
    	description = l_description;
	}

	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    
    public String title;
    
    public String description;	
	
}
