package com.example.minor_project;
import com.example.minor_project.Controller.StudentController;
import com.example.minor_project.DTO.CreateStudentRequest;
import com.example.minor_project.Service.StudentService;
import com.example.minor_project.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    //unit testcase for controller to check if its calling correct service methods are not
    //so we should mock service methods so that it won't call actual database calls
    @MockitoBean
    public StudentService mockstudentService;

    @InjectMocks
    public StudentController mockStudentController;

    public ObjectMapper objectMapper=new ObjectMapper();  //for converting object to Json string for API calls payload
    public MockMvc mockMvc;  //mockMvc contains all the post get put Api calls

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockStudentController).build();//it will create new mockmvc instance for testing
    }

    //we will write test for creating student request whether it is calling correct service method or not
    @Test
    public void CreateStudentTest() throws Exception {
        //first we need to form create student dto object
        CreateStudentRequest createStudentRequest = CreateStudentRequest.builder()
                .name("Sushmitha")
                .email("validemail@example.com")
                .age(23)
                .rollNumber("19uec047")
                .build();
        when(mockstudentService.createStudent(any(Student.class))).thenReturn(new Student());
        //mockMvc perform the call , it needs api ,contentType mediatype json/xml and content
        //now this is object we need to transform this to json string , so we use objectMapper to make it as json
        //post Api mock call with valid json body otherwise it will throw bad request error
        mockMvc.perform(
                        post("/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createStudentRequest))
                        )
                .andExpect(status().isCreated())
                .andExpect(content().string(equalTo("Student got created succesfully")));
    }


}
