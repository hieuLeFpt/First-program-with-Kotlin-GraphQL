package com.graphql.mutation

import com.dto.StudentDTO
import com.entity.Student
import com.exception.StudentDuplicateEmailException
import com.exception.StudentNameException
import com.exception.StudentNotFoundException
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import com.service.StudentService
import jakarta.validation.Valid
import java.time.LocalDate

@DgsComponent
class StudentMutation(
    private val studentService: StudentService
) {
    @DgsMutation
    fun create(
        @InputArgument name: String,
        @InputArgument age: Int,
        @InputArgument email: String,
        @InputArgument major: String
    ): Boolean {
        val newStudent = Student(
            name = name,
            age = age,
            email = email,
            major = major,
            enrollmentDate = LocalDate.now()
        )
        print("create")
        if (name.length < 6) throw StudentNameException("Name is at least 6 !")
        if (!studentService.checkDuplicateEmail(email)) {
            studentService.create(newStudent)
            return true
        } else {
            println("update by filed duplicate")
            throw StudentDuplicateEmailException("${newStudent.email} is exists!")
        }
    }

    @DgsMutation
    fun createStudent(@Valid @InputArgument student: StudentDTO): Student? {
        val newStudent = Student(
            name = student.name,
            age = student.age,
            email = student.email,
            major = student.major,
            enrollmentDate = LocalDate.now()
        )
        println("create by object")
        student.name?.length?.let { if (it < 6) throw StudentNameException("Name is at least 6 !") }
        if (!studentService.checkDuplicateEmail(student.email)) {
            return studentService.create(newStudent)
        } else {
            println("update by object duplicate")
            throw StudentDuplicateEmailException("${newStudent.email} is exists!")
        }
    }

    @DgsMutation
    fun updateStudent(@Valid @InputArgument student: StudentDTO): Student? {
        val updateStudent = Student(
            id = student.id,
            name = student.name,
            age = student.age,
            email = student.email,
            major = student.major,
        )
        println("update by object")
        if (!studentService.existsByEmailAndIdNot(student.email, student.id)) {
            return studentService.update(student.id, updateStudent)
        } else throw StudentDuplicateEmailException("${updateStudent.email} is exists!")
    }

    @DgsMutation
    fun update(
        @InputArgument id: Int,
        @InputArgument name: String,
        @InputArgument age: Int,
        @InputArgument email: String,
        @InputArgument major: String
    ): Student? {
        println("update")
        val updateStudent = Student(
            name = name,
            age = age,
            email = email,
            major = major
        )
        if (!studentService.existsByEmailAndIdNot(email, id)) {
            println(studentService.existsByEmailAndIdNot(email, id))
            return studentService.update(id, updateStudent)
        } else throw StudentDuplicateEmailException("${updateStudent.email} is exists!")
    }

    @DgsMutation
    fun delete(@InputArgument id: Int): Boolean {
        println("delete")
        if (studentService.findById(id) == null) {
            throw StudentNotFoundException("Không thể tìm thấy học sinh!")
        } else return studentService.delete(id)
    }
}