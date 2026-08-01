package com.github.ssuyoi.projectvoucher.domain.employee;

import com.github.ssuyoi.projectvoucher.app.controller.response.EmployeeResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final Map<Long, EmployeeResponse> employeeResponseMap = new HashMap<>();

    // 사원 생성
    public Long create(String name, String position, String department) {
        Long no = employeeResponseMap.size() + 1L;
        employeeResponseMap.put(no, new EmployeeResponse(no, name, position, department));
        return no;
    }

    // 사원 조회
    public EmployeeResponse get(Long no) {
        return employeeResponseMap.get(no);
    }
}
