package helloproject;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class HelloClass implements Serializable{
	String message = "Hello Java Server Faces On Eclipse IDE!";
	
	public String getMessage() {
		return message;
	}
}