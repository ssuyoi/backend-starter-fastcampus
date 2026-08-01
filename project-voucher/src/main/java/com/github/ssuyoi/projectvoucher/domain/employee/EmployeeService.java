package com.github.ssuyoi.projectvoucher.domain.employee;

import com.github.ssuyoi.projectvoucher.app.controller.response.EmployeeResponse;
import com.github.ssuyoi.projectvoucher.storage.employee.EmployeeEntity;
import com.github.ssuyoi.projectvoucher.storage.employee.EmployeeRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // 사원 생성
    public Long create(String name, String position, String department) {
        EmployeeEntity employee = new EmployeeEntity(name, position, department);
        EmployeeEntity savedEmployee = employeeRepository.save(employee);
        return savedEmployee.getId();
    }

    // 사원 조회
    public EmployeeResponse get(Long no) {
        EmployeeEntity employee = employeeRepository.findById(no)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return new EmployeeResponse(employee.getId(), employee.getName(), employee.getPosition(), employee.getDepartment());
    }
}
