package org.example.employees.models;

import java.util.ArrayList;

public class Employee {

    private Long id;
    private String firstName;
    private String lastName;
    private String position;

    private List<Attenadace> attenadaceRecords = new ArrayList<>(); //OnewToMany

    public Employee() {
    }

    public Employee(Long id, String firstName, String lastName, String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<Attendance> getAttenadaceRecords() {
        return attenadaceRecords;
    }

    public void setAttenadaceRecords(List<Attenadace> attenadaceRecords) {
        this.attenadaceRecords = attenadaceRecords;
    }
}
