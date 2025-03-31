package com.example.minor_project.Service;
import com.example.minor_project.Repository.AdminRepo;
import com.example.minor_project.Repository.StudentRepo;
import com.example.minor_project.model.Admin;
import com.example.minor_project.model.Student;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {
    @Autowired
    StudentRepo studentdao;
    @Autowired
    AdminRepo adminrepo;

    public Integer parseCSVAndSaveData(InputStream inputStream, String entity) throws Exception {
        switch(entity){
            case "student":
                List<Student> students= parseCSVStudentAndSaveData(inputStream);
                studentdao.saveAll(students);
                return students.size();
            case "admin":
                List<Admin> admins=parseCSVAdminAndSaveData(inputStream);
                adminrepo.saveAll(admins);
                return admins.size();
        }
        return 0;
    }

    private List<Admin> parseCSVAdminAndSaveData(InputStream inputStream) {
        try(BufferedReader bufferedreader= new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
            //csvFormat defines the structure of the data,it tells how the csv data should be read
            // without this parser wouldn't know what headers to expect
            CSVFormat csvFormat=CSVFormat.DEFAULT.builder()
                    .setHeader("name","email")
                    .setSkipHeaderRecord(true) //instead of removing the header when entering the data into the list
                    .build();//we can tell the format to skip it
            //as the csv format is ready ,parser will read the data using the format rules i.e. using format object
            CSVParser csvparser =csvFormat.parse(bufferedreader);
            //getRecords() returns the list
            List<CSVRecord> csvRecords=csvparser.getRecords();
            //csvrecords contains all the data now including headers and student data
            //headers also will be there we have to remove it
            //csvRecords.removeFirst(); as we used setSkipHeaderRecord as true we dont need to deal with this line
            List<Admin> admins=new ArrayList<>();
            for(CSVRecord csvRecord:csvRecords){
               Admin admin=Admin.builder()
                       .email(csvRecord.get("email"))
                       .name(csvRecord.get("name"))
                       .build();
               admins.add(admin);
            }
            return admins;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Student> parseCSVStudentAndSaveData(InputStream inputStream) {
        try(BufferedReader bufferedreader= new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
            //building ths csv format , now this will map the objects correctly
            CSVFormat csvFormat=CSVFormat.DEFAULT.builder()
                    .setHeader("name","age","roll_number","email")
                    .setSkipHeaderRecord(true)
                    .build();
            //get actual data into the csvparser , so that every data will get's stored
            CSVParser csvparser =csvFormat.parse(bufferedreader);
            List<CSVRecord> csvrecords=csvparser.getRecords();
            //csvrecords contains all the data now including headers and student data
            //headers also will be there we have to remove it
            //csvrecords.removeFirst();
            //building list objecvt so we can save everything in one go using saveAll option
            List<Student> students=new ArrayList<>();
            for(CSVRecord csvRecord:csvrecords){
                Student student=Student.builder()
                        .name(csvRecord.get("name"))
                        .age(Integer.parseInt(csvRecord.get("age")))
                        .rollNumber(csvRecord.get("roll_number"))
                        .email(csvRecord.get("email"))
                        .build();
                students.add(student);
            }
            return students;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
