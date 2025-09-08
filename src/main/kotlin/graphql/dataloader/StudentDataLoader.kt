package com.graphql.dataloader

import com.entity.Course
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsDataLoader
import com.service.StudentService
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@DgsComponent
class StudentDataLoader(
    private val studentService: StudentService
) {
//    @DgsDataLoader(name = "courses")
//    class CourseDataLoader(
//        private val studentService: StudentService
//    ) : MappedBatchLoader<Int, List<Course>> {
//        override fun load(keys: Set<Int>): CompletionStage<Map<Int, List<Course>>> {
//            println(keys + "keys")
//            return CompletableFuture.supplyAsync {
//                val all: List<Course> = studentService.findCoursesByStudentIds(keys.toList())
//                println(all + "all course")
//                val grouped: Map<Int, List<Course>> = all.groupBy { it.studentId!! }
//                println("grouped: $grouped")
//                keys.associateWith { id -> grouped[id] ?: emptyList() }.toMutableMap()
//            }
//        }
//    }
}