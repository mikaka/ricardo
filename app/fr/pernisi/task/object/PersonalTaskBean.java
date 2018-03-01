package fr.pernisi.task.object;

import fr.pernisi.task.entities.PersonalTask;

public class PersonalTaskBean {

	public String id;

	public String title;

	public String description;

	
	public PersonalTaskBean() {
		
	}
	
	public PersonalTaskBean(String id, String title, String description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}



	public PersonalTaskBean(PersonalTask personalTask) {
		description = personalTask.description;
		title = personalTask.title;
		id = personalTask.id.toString();
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
