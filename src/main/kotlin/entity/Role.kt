package com.entity

import jakarta.persistence.*

@Entity
@Table(name = "role")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
    val name: String? = null
}