package com.example.minor_project.Controller;
import com.example.minor_project.DTO.*;
import com.example.minor_project.Service.StudentService;
import com.example.minor_project.model.Student;
import com.example.minor_project.model.Users;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@RequestBody @Valid CreateStudentRequest createStudent_dto){
        try{
            StudentResponse stu=studentService.createStudent(createStudent_dto.to()).to();
            stu.setMessage("Student got created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(stu);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataIntegrityViolationException) {
                throw (DataIntegrityViolationException) cause;
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StudentErrorResponse(e.getCause().getMessage())
            );
        }
    }

    //only admin can do this
    @GetMapping("/listAll")
    public SearchStudentResponse getALLStudents(){
        List<Student> students=studentService.getALlStudents();
        List<StudentResponse> students_list=new ArrayList<StudentResponse>();
        for(Student student : students){
            students_list.add(student.to());
        }
        return new SearchStudentResponse(students_list);
    }

    //findById , findStudentByEmail , findStudentByRollNo ==>return single student
    @PostMapping("/search")
    public ResponseEntity<?> findBYIdEmailRollNumber(@RequestBody @Valid SearchStudentRequest studentRequest){
        try {
            StudentResponse student = studentService.searchStudentByIdEmailRoll(studentRequest.getSearchKey(),studentRequest.getSearchValue());
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StudentErrorResponse(e.getCause().getMessage())
            );
        }
    }

    //findByName , findByAge returns list of students
    @GetMapping("/search2")
    public ResponseEntity<?> findByNameAge(@RequestParam  String searchKey,@RequestParam  String searchValue){
        try {
            List<Student> students =studentService.searchStudentByNameAge(searchKey,searchValue);
            List<StudentResponse> studentResponses=new ArrayList<StudentResponse>();
            for(Student student : students){
                studentResponses.add(student.to());
            }
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new SearchStudentResponse(studentResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StudentErrorResponse(e.getCause().getMessage())
            );
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> student_self_info() throws Exception {
        //security context holder contains current user current session details
        System.out.println("Starting info endpoint");
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication ==null){
            throw new Exception("USER is not authenticated. ");
        }
        System.out.println("Authentication is " +authentication.toString());
        Users user=(Users) authentication.getPrincipal();
        Integer id=user.getStudent().getId();
        try {
            StudentResponse student = studentService.searchStudentByIdEmailRoll("id", String.valueOf(id));
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StudentErrorResponse(e.getMessage())
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id){
        try{
            studentService.deleteStudent(Integer.parseInt(id));
            return ResponseEntity.status(HttpStatus.OK).body("Student got deleted successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable String id , @RequestParam String key ,@RequestParam String value ){
        try{
            studentService.updateStudent(Integer.valueOf(id),key,value);
            return ResponseEntity.status(HttpStatus.OK).body("Student got Updated successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
