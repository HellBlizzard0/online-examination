package com.beans;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.security.NoSuchAlgorithmException;  
import java.security.MessageDigest;  
import com.entities.Student;
import com.models.StudentModel;


@SessionScoped
@ManagedBean(name = "studentManagedBean")
public class StudentBean {

	private Student student;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public StudentBean() {
		this.student = new Student();
	}

	public String save() {
		StudentModel studentModel = new StudentModel();
		student.setPassword(encrypt(student.getPassword()));
		studentModel.create(this.student);
		this.student=new Student();
		return "success?faces-redirect=true";
	}
	
	private String encrypt(String password) {
        String encryptedpassword = null;  

		try   
	        {  
	            MessageDigest m = MessageDigest.getInstance("MD5");  
	              
	            m.update(password.getBytes());  
	              
	            byte[] bytes = m.digest();  
	              
	            StringBuilder s = new StringBuilder();  
	            for(int i=0; i< bytes.length ;i++)  
	            {  
	                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));  
	            }  
	              
	            encryptedpassword = s.toString();  
	        }   
	        catch (NoSuchAlgorithmException e)   
	        {  
	            e.printStackTrace();  
	        }
		return encryptedpassword;  	
		}

	
}
