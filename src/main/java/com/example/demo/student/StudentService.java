package com.example.demo.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public void addNewStudent(Student student) {
        validateEmail(student.getEmail());
        studentRepository.save(student);
        System.out.println(student);
    }

    public void deleteStudent(Long studentId) {
        validateStudentExists(studentId);
        studentRepository.deleteById(studentId);
    }

	@Transactional
    public void updateStudent(Long studentId, Student updatedStudent) {
        Student student = validateStudentExists(studentId);

        if (updatedStudent.getName() != null && !updatedStudent.getName().isEmpty()
                && !Objects.equals(student.getName(), updatedStudent.getName())) {
            student.setName(updatedStudent.getName());
        }

        if (updatedStudent.getEmail() != null && !updatedStudent.getEmail().isEmpty() && !Objects.equals(student.getEmail(), updatedStudent.getEmail())) {
			validateEmail(updatedStudent.getEmail());
			student.setEmail(updatedStudent.getEmail());
        }

        if (updatedStudent.getDateOfBirth() != null && !Objects.equals(student.getDateOfBirth(), updatedStudent.getDateOfBirth())) {
            student.setDateOfBirth(updatedStudent.getDateOfBirth());
        }
    }

	
    private void validateEmail(String email) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
        if(studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
    }

    private Student validateStudentExists(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student does not exist id = " + studentId));
    }
}
