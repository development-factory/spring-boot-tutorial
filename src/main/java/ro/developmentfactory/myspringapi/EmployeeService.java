package ro.developmentfactory.myspringapi;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService{

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void createEmployee(Employee employee) {
        validateEmail(employee.getEmailAddress());
        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        Employee employeeToUpdate = employeeRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("Employee with id %s doesn't exist", id)));
        validateEmail(employee.getEmailAddress());

        employeeToUpdate.setFirstName(employee.getFirstName());
        employeeToUpdate.setLastName(employee.getLastName());
        employeeToUpdate.setAddress(employee.getAddress());
        employeeToUpdate.setAge(employee.getAge());
        employeeToUpdate.setEmailAddress(employee.getEmailAddress());
        employeeToUpdate.setRole(employee.getRole());

        employeeRepository.save(employeeToUpdate);
    }

    private void validateEmail(String email){
        Optional<Employee> employeeOptional = employeeRepository.getEmployeeByEmailAddress(email);
        if(employeeOptional.isPresent()){
            throw new IllegalStateException(String.format("Email address %s already exists", email));
        }
    }

    public void deleteEmployee(Long id){
        boolean employeeExists = employeeRepository.existsById(id);
        if(!employeeExists){
            throw new IllegalStateException(String.format("Employee with id %s does not exist", id));
        }
        employeeRepository.deleteById(id);
    }
}
