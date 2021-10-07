package com.zubayr.service.control.domain.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

/**
 * BaseEntity
 *
 * @author Denis_Kholmogorov
 */
@Entity
@Table(name = "test")
class BaseEntity : Serializable {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: UUID? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }


}