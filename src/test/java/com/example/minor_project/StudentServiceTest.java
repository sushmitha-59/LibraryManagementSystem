package com.example.minor_project;
import com.example.minor_project.Repository.StudentRepo;
import com.example.minor_project.Service.StudentService;
import com.example.minor_project.model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StudentServiceTest {

    //we are faking repository,to check if services are calling intended methods
    @Mock
    private StudentRepo mockStudentRepo;
    //let's say we are testing service , then it is dependent on the repository , repository we already mocked but
    //spring has to know that this service object uses the repo object which we mocked, so for that we need to inject mocking
    @InjectMocks
    private StudentService mockStudentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test //we have to mention this to tell spring that it is a unit testcase ,it has to be executed
    public void TestFindStudentByEmail() throws Exception { //here as we are testing studentService class functionality , it has this method searchStudentByIdEmailRoll
        //based on searchKey it will execute the functions and returns , in unit testcases we dont validate return values
        //we just check if it is calling correct methods are not , so as searchKey is email , it should call findbyEmail method
        //that we will check in this testcase
        String searchKey="email";
        String searchValue="Sushmitha@123";
        Student Expectedstudent= Student.builder()
                .id(1)
                .email(searchValue)
                .name("Sushmitha")
                .age(23)
                .rollNumber("192uec090")
                .build();

        when(mockStudentRepo.findByEmail(searchValue)).thenReturn(Expectedstudent);
        Student ActualWeGot=mockStudentService.searchStudentByIdEmailRoll(searchKey,searchValue);
        assertEquals(Expectedstudent,ActualWeGot); //test will fail , if these both are not equal
    }

    @Test
    public void TestFindStudentById() throws Exception {
        String searchKey="id";
        Integer searchValue=1;
        Student Expectedstudent= Student.builder()
                .id(searchValue)
                .email("Sushmitha@123")
                .name("Sushmitha")
                .age(23)
                .rollNumber("192uec090")
                .build();

        when(mockStudentRepo.findById(searchValue)).thenReturn(Optional.ofNullable(Expectedstudent));
        Student ActualWeGot=mockStudentService.searchStudentByIdEmailRoll(searchKey,searchValue.toString());
        assertEquals(Expectedstudent,ActualWeGot); //test will fail , if these both are not equal
    }
    @Test
    public void TestFindStudentByRoll() throws Exception {
        String searchKey="rollNumber";
        String searchValue="192uec090";
        Student Expectedstudent= Student.builder()
                .id(1)
                .email("Sushmitha@123")
                .name("Sushmitha")
                .age(23)
                .rollNumber(searchValue)
                .build();

        when(mockStudentRepo.findByRollNumber(searchValue)).thenReturn(Expectedstudent);
        Student ActualWeGot=mockStudentService.searchStudentByIdEmailRoll(searchKey,searchValue);
        assertEquals(Expectedstudent,ActualWeGot); //test will fail , if these both are not equal
    }

    @Test
    public void InvalidKey(){
        String searchKey="InvalidKey";
        String searchValue="192uec090";
        //According to studentService ,it should throw exception as the key is invalid ,as we are searching for invalid key , expecting it should throw exception
        //assertThrows excepts which type of exception it should throw and which method to call
        Assertions.assertThrows(Exception.class,()->mockStudentService.searchStudentByIdEmailRoll(searchKey,searchValue));
    }


}
