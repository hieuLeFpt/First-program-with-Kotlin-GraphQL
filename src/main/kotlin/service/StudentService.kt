package com.service

import com.entity.Course
import com.entity.Student
import com.exception.StudentNotFoundException
import com.repository.CourseRepository
import com.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
open class StudentService(
    private val repo: StudentRepository,
    private val courseRepo: CourseRepository
) {
    fun getAll(): List<Student> {
        return repo.findAll()
    }

    open fun create(student: Student): Student {
        return repo.save(student)
    }

    open fun delete(id: Int?): Boolean {
        return if (repo.existsById(id)) {
            repo.deleteById(id)
            true
        } else {
            false
        }
    }

    open fun update(id: Int?, newStudent: Student): Student {
        println("check in service")
        val existingStudent = repo.findById(id).orElseThrow { StudentNotFoundException("Student not found!") }
        existingStudent.name = newStudent.name
        existingStudent.email = newStudent.email
        existingStudent.age = newStudent.age
        existingStudent.major = newStudent.major
        return repo.save(existingStudent)
    }

    fun checkDuplicateEmail(email: String?): Boolean {
        return repo.existsByEmail(email)
    }

    fun existsByEmailAndIdNot(email: String?, id: Int?): Boolean {
        println("check dup in service")
        return repo.isDuplicateEmailOtherStudent(email, id)
    }

    fun findByName(name: String?): List<Student> {
        return repo.findByNameContainsIgnoreCase(name)
    }

    fun findById(id: Int?): Student {
        return repo.findById(id).orElseThrow { StudentNotFoundException("Student not found!") }
    }

    fun findCoursesByStudentIds(ids: Collection<Int>): List<Course> {
        return courseRepo.findByStudentIdIn(ids)
    }

    fun findByUserName(name: String?): Student {
        return repo.findByUsername(name)
    }

    fun findByEmail(email: String?): Student {
        return repo.findByEmail(email).get()
    }
}