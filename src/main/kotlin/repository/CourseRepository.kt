package com.repository

import com.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository: JpaRepository<Course, Int> {
    fun findByStudentIdIn(ids: Collection<Int>): List<Course>
}