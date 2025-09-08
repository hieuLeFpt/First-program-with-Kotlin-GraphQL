package com.controller

import com.dto.StudentDTO
import com.entity.Student
import com.service.StudentService
import jakarta.validation.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
class HomeController(private val service: StudentService) {

    @GetMapping("/")
    fun home(model: Model): String {
        model.addAttribute("students", service.getAll())
        model.addAttribute("studentDTO", StudentDTO())
        return "home" // file templates/home.html
    }

    @PostMapping("/student/create")
    fun create(
        @Valid @ModelAttribute studentDTO: StudentDTO,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createFailed", "Create failed!")
            model.addAttribute("showCreateModal", true)
            model.addAttribute("students", service.getAll())
            return "home" // quay lại form nhập nếu có lỗi validation
        }

        val student = Student(
            id = studentDTO.id,
            name = studentDTO.name,
            age = studentDTO.age,
            email = studentDTO.email,
            major = studentDTO.major,
            enrollmentDate = LocalDate.now()
        )
        if (!service.checkDuplicateEmail(studentDTO.email)) {
            service.create(student)
            model.addAttribute("students", service.getAll())
            model.addAttribute("studentDTO", StudentDTO())
            model.addAttribute("createSuccess", "Create success!")
            return "home"
        } else {
            model.addAttribute("students", service.getAll())
            model.addAttribute("studentDTO", StudentDTO())
            model.addAttribute("duplicateEmail", "Duplicate email!")
            model.addAttribute("showCreateModal", true)
        }
        model.addAttribute("students", service.getAll())
        model.addAttribute("studentDTO", StudentDTO())
        return "home"
    }

    @GetMapping("/student/search")
    fun search(@RequestParam("keyword") keyword: String, model: Model): String {
        model.addAttribute("students", service.findByName(keyword))
        model.addAttribute("studentDTO", StudentDTO())
        return "home";
    }

    @DeleteMapping("/student/update")
    fun update(@Valid @ModelAttribute studentDTO: StudentDTO,
               bindingResult: BindingResult,
               model: Model): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("updateFailed", "Update failed!")
            model.addAttribute("showUpdateModal", true)
            model.addAttribute("students", service.getAll())
            return "home" // quay lại form nhập nếu có lỗi validation
        }
        val student = Student(
            id = studentDTO.id,
            name = studentDTO.name,
            age = studentDTO.age,
            email = studentDTO.email,
            major = studentDTO.major,
            enrollmentDate = LocalDate.now()
        )
        if (!service.checkDuplicateEmail(studentDTO.email)) {
            service.update(studentDTO.id, student)
            model.addAttribute("students", service.getAll())
            model.addAttribute("studentDTO", StudentDTO())
            model.addAttribute("updateSuccess", "Update success!")
            return "home"
        } else {
            model.addAttribute("students", service.getAll())
            model.addAttribute("studentDTO", StudentDTO())
            model.addAttribute("duplicateEmail", "Duplicate email!")
            model.addAttribute("showCreateModal", true)
        }
        model.addAttribute("students", service.getAll())
        model.addAttribute("studentDTO", StudentDTO())
        return "home"
    }

    @PostMapping("/student/delete/{id}")
    fun delete(@PathVariable id: Int?,
               model: Model): String {
        service.delete(id)
        model.addAttribute("deleteSuccess", "Delete success!")
        model.addAttribute("students", service.getAll())
        model.addAttribute("studentDTO", StudentDTO())
        return "home"
    }

}