package com.example.employeesalarymanagement.dao;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.employeesalarymanagement.entity.EmployeeDetails;

@Repository
public interface EmployeeDetailsDAO extends PagingAndSortingRepository<EmployeeDetails, String> {

	@Query("SELECT e from EmployeeDetails e where e.login=?1 ")
	public Optional<EmployeeDetails> isLoginExists(String login);

	@Query("SELECT e from EmployeeDetails e where e.salary >= ?1 AND e.salary <?2 ORDER BY e.id")
	public Optional<List<EmployeeDetails>> findAllEmployees(Double minSalary, Double maxSalary, Pageable pageable);
}
