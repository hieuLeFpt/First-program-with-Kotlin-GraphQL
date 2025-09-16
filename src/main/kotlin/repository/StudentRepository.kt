package com.repository

import com.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StudentRepository : JpaRepository<Student, Int> {
    fun existsByEmail(email: String?): Boolean
    fun findByNameContainsIgnoreCase(name: String?): List<Student>
    fun existsByEmailAndIdNot(email: String?, id: Int?): Boolean
    fun findByEmail(email: String?): Optional<Student>

    //JPQL
    @Query("SELECT s FROM Student s")
    fun getAllStudent(): List<Student>

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Student s WHERE s.email = :email")
    fun isDuplicateEmail(@Param("email") email: String?): Boolean

    @Query("SELECT s FROM Student s WHERE s.name = :name")
    fun getStudentByName(@Param("name") name: String?): Student?

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Student s WHERE s.email =:email AND s.id <> :id")
    fun isDuplicateEmailOtherStudent(
        @Param("email") email: String?,
        @Param("id") id: Int?
    ): Boolean

    @Query("SELECT s FROM Student s WHERE s.name = :name")
    fun findByUsername(@Param ("name")name: String?): Student
}