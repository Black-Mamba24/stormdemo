package org.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student implements Comparable<Student>{
	private double mark;
	private String name;
	
	public Student(String name,double mark){
		this.name=name;
		this.mark=mark;
	}
	
	public int compareTo(Student o) {
		// TODO Auto-generated method stub
		if(!(o instanceof Student)) return 1;
		Student tmp=(Student)o;
		if(this.getMark()>tmp.getMark()) return 1;
		if(this.getMark()<tmp.getMark()) return -1;
		return 0;
	}
	public double getMark() {
		return mark;
	}
	public void setMark(double mark) {
		this.mark = mark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static void main(String args[]){
		List<Student> list=new ArrayList<Student>();
		for(int i=0;i<10;i++){
			list.add(new Student("S_NAME_"+i,Math.random()*100));
		}
		for(Student s:list){
			System.out.println(s.getName()+"-"+s.getMark()+"\t");
		}
		Collections.sort(list);
		for(Student s:list){
			System.out.println(s.getName()+"-"+s.getMark()+"\t");
		}
	}
}
