package com.controller

import com.dto.StudentCreateDTO
import com.entity.Student
import com.service.StudentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import org.springframework.web.bind.annotation.DeleteMapping

@RequestMapping("/api/student")
@RestController
class StudentControllerRestfulAPI(private val studentService: StudentService) {

    @GetMapping("/getAll")
    fun getAll(): List<Student> = studentService.getAll()

    @PostMapping("/create")
    fun create(
        @Valid @RequestBody student: StudentCreateDTO,
        bindingResult: BindingResult
    ): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(mapOf("errors" to bindingResult.allErrors))
        }
        val studentCreate = Student(
            id = student.id,
            name = student.name,
            age = student.age,
            email = student.email,
            major = student.major,
            enrollmentDate = LocalDate.now()
        )
        if (!studentService.checkDuplicateEmail(student.email)) {
            studentService.create(studentCreate)
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapOf("success" to true))
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("error" to "Duplicate email!"))
        }
    }

    @GetMapping("/search")
    fun search(@RequestParam("keyword") keyword: String): List<Student> {
        return studentService.findByName(keyword)
    }

    @PutMapping("/update")
    fun update(
        @Valid @RequestBody studentCreateDTO: StudentCreateDTO,
        bindingResult: BindingResult
    ): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(mapOf("errors" to bindingResult.allErrors))
        }
        val studentCreate = Student(
            id = studentCreateDTO.id,
            name = studentCreateDTO.name,
            age = studentCreateDTO.age,
            email = studentCreateDTO.email,
            major = studentCreateDTO.major,
            enrollmentDate = LocalDate.now()
        )
        if (!studentService.checkDuplicateEmail(studentCreateDTO.email)) {
            studentService.update(studentCreateDTO.id, studentCreate)
            return ResponseEntity.ok(mapOf("success" to true))
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "Duplicate email!"))
        }
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Int?): ResponseEntity<Any> {
        if (studentService.delete(id)) return ResponseEntity.ok(mapOf("success" to true))
        else return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to "Not found!"))
    }
}