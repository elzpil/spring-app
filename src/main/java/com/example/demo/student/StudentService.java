package com.example.demo.student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class StudentService { 

	private final StudentRepository studentRepository;
	@Autowired
	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public List<Student> getStudents() {
		return studentRepository.findAll();
	}

    public void addNewstudent(Student student) {
        
		Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
		if(studentOptional.isPresent()) {
			throw new IllegalStateException("email taken");
		}
		studentRepository.save(student);
		System.out.println(student);
    }

	public void deleteStudent(Long studentId) {
		boolean studentExists = studentRepository.existsById(studentId);
		if(!studentExists) {
			throw new IllegalStateException("student does not exist id = " + studentId);

		}
		studentRepository.deleteById(studentId);
	}

	@Transactional
    public void updateStudent(Long studentId, Student updatedStudent) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student does not exist id = " + studentId));

        if (updatedStudent.getName() != null && !updatedStudent.getName().isEmpty()
                && !Objects.equals(student.getName(), updatedStudent.getName())) {
            student.setName(updatedStudent.getName());
        }

        if (updatedStudent.getEmail() != null && !updatedStudent.getEmail().isEmpty()
                && !Objects.equals(student.getEmail(), updatedStudent.getEmail())) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(updatedStudent.getEmail());
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("Email taken");
            }
            student.setEmail(updatedStudent.getEmail());
        }

        if (updatedStudent.getDateOfBirth() != null && !Objects.equals(student.getDateOfBirth(), updatedStudent.getDateOfBirth())) {
            student.setDateOfBirth(updatedStudent.getDateOfBirth());
        }
    }
}
