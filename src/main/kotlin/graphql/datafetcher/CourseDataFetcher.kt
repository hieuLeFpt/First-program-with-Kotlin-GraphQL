package com.graphql.datafetcher

import com.entity.Course
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.service.CourseService

@DgsComponent
class CourseDataFetcher(
    private val courseService:  CourseService
) {
    @DgsQuery
    fun getAllCourse(): List<Course> {
        return courseService.getAllCourse()
    }
}