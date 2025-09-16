package com.service

import com.entity.Course
import com.repository.CourseRepository
import org.springframework.stereotype.Service

@Service
open class CourseService(
    private val courseRepo: CourseRepository
) {
    fun getAllCourse(): List<Course> {
        return courseRepo.findAll()
    }

}