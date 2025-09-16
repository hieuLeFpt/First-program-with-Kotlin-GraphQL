package com.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.*

@Entity
@Table(name = "course")
class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
    var name: String? = null
    var description: String? = null
    var active: Boolean? = null
    var studentId: Int? = null
    @Column(name = "url_img")
    var urlImg: String? = null
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "student_id")
//    var student: Student? = null

}