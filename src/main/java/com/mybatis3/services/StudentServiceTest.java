package com.mybatis3.services;

import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mybatis3.domain.PhoneNumber;
import com.mybatis3.domain.Student;

public class StudentServiceTest {
	private static StudentService studentService;
	
	@BeforeClass
	public static void setup(){
		studentService = new StudentService();
	}

	@AfterClass
	public static void teardown(){
		studentService = null;
	}

	@Test
	public void testFindAllStudents(){
		List<Student> students = studentService.findAllStudents();
		Assert.assertNotNull(students);
		for(Student student:students){
			System.out.println(student);
		}
	}

	@Test
	public void testFindStudentById(){
		Student stu = studentService.findStudentById(1);
		Assert.assertNotNull(stu);
		System.out.println(stu);
	}
	
	@Test
	public void testCreateStudent(){
		Student stu = new Student();
		int id = 4;
		stu.setStudId(id);
		stu.setName("student"+id);
		stu.setEmail("student_"+id+"@gmail.com");
		PhoneNumber phoneNumber = new PhoneNumber("123", "456", "7890");
		stu.setPhoneNumber(phoneNumber);
		stu.setDob(new Date());
		studentService.createStudent(stu);
		Student stuCreated = studentService.findStudentById(id);
		Assert.assertNotNull(stuCreated);
		System.out.println(stuCreated);
	}
}
