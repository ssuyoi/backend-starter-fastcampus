package com.github.ssuyoi.projectvoucher.storage.employee;

import com.github.ssuyoi.projectvoucher.storage.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "employee")
@Entity
public class EmployeeEntity extends BaseEntity {

    private String name;
    private String position;
    private String department;

    public EmployeeEntity() {
    }

    public EmployeeEntity(String name, String position, String department) {
        this.name = name;
        this.position = position;
        this.department = department;
    }


    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }
}
