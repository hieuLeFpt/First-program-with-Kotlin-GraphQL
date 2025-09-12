package com.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.validator.internal.metadata.facets.Cascadable
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name = "student")
data class Student(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    var name: String? = null,
    var age: Int? = null,
    var email: String? = null,
    var major: String? = null,
    var enrollmentDate: LocalDate? = null,
    var passwordUser: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnore
    var role: Role? = null,

//    @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    var listCourse: List<Course> = emptyList()   // ánh xạ sang Course

) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        val authority = "ROLE_${role?.name}"
        return listOf(SimpleGrantedAuthority(authority))
    }

    override fun getPassword(): String? {
        return passwordUser
    }

    override fun getUsername(): String? {
        return email
    }

}