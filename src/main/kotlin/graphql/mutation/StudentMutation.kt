package com.graphql.mutation

import com.dto.StudentCreateDTO
import com.entity.Student
import com.exception.StudentDuplicateEmailException
import com.exception.StudentNotFoundException
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import com.repository.RoleRepository
import com.service.StudentService
import jakarta.validation.Valid
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import org.springframework.validation.annotation.Validated
import org.springframework.security.access.prepost.PreAuthorize
@DgsComponent
@Validated
open class StudentMutation(
    private val studentService: StudentService,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository
) {

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    open fun createStudent(@Valid @InputArgument student: StudentCreateDTO): Student? {
        println(student.passwordUser + "pass được tạo")
        val newStudent = Student(
            name = student.name,
            age = student.age,
            email = student.email,
            major = student.major,
            passwordUser = passwordEncoder.encode(student.passwordUser),
            role = roleRepository.findByName("STUDENT")
                .orElseThrow { IllegalStateException("Role not found in DB") },
            enrollmentDate = LocalDate.now()
        )
        println("create by object")
        if (!studentService.checkDuplicateEmail(student.email)) {
            return studentService.create(newStudent)
        } else {
            println("update by object duplicate")
            throw StudentDuplicateEmailException("${newStudent.email} is exists!")
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    fun updateStudent(@Valid @InputArgument student: StudentCreateDTO): Student? {
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

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    fun delete(@InputArgument id: Int): Boolean {
        println("delete")
        if (studentService.findById(id) == null) {
            throw StudentNotFoundException("Không thể tìm thấy học sinh!")
        } else return studentService.delete(id)
    }
}