package com.graphql.datafetcher

import com.entity.Course
import com.entity.Student
import com.graphql.dataloader.StudentDataLoader
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.service.StudentService
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture

@DgsComponent
class StudentDataFetcher(
    private val studentService: StudentService
) {

    @DgsQuery
    fun getAll(): List<Student> {
        return studentService.getAll()
    }

    @DgsQuery
    fun getStudentById(@InputArgument id: Int?): Student {
        return studentService.findById(id)
    }

    @DgsQuery
    fun getStudentByUserName(@InputArgument name: String?): Student {
        return studentService.findByUserName(name)
    }

    @DgsQuery
    fun getStudentByEmail(@InputArgument email: String?): Student {
        return studentService.findByEmail(email)
    }

    @DgsData(parentType = "Student", field = "courses")
    fun courses(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<Course>> {
        val loader: DataLoader<Int, List<Course>> =
            dfe.getDataLoader(StudentDataLoader.CourseDataLoader::class.java)
        val student = dfe.getSource<Student>()
        return loader.load(student.id)
    }
}