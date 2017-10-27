package com.qinhetea.api.controller

import com.qinhetea.api.entity.User
import com.qinhetea.api.repository.UserRepository
import mu.KotlinLogging
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RepositoryRestController
@RequestMapping("/users")
class UserController(override val repository: UserRepository)
  : FuzzySearchController<User> {

  @PostMapping("")
  @Secured("ROLE_${User.Role.ADMIN}")
  fun create(@RequestBody user: User) =
    if (repository.findByUsername(user.username).isPresent)
      ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf(
        "timestamp" to System.currentTimeMillis(),
        "status" to HttpStatus.CONFLICT.value(),
        "error" to HttpStatus.CONFLICT.reasonPhrase,
        "message" to "Conflict username"
      ))
    else
      ResponseEntity.ok(
        repository.save(user.encodePassword())
      )

}

private val logger = KotlinLogging.logger {}