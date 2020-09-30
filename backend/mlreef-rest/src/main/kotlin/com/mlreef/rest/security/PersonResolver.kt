package com.mlreef.rest.security

import com.mlreef.rest.Person
import com.mlreef.rest.PersonRepository
import com.mlreef.rest.exceptions.AccessDeniedException
import com.mlreef.rest.exceptions.UserNotFoundException
import com.mlreef.rest.external_api.gitlab.TokenDetails
import org.springframework.core.MethodParameter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class PersonResolver(
    val personRepository: PersonRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType.equals(Person::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val tokenDetails = SecurityContextHolder.getContext().authentication.principal as? TokenDetails
            ?: throw AccessDeniedException("Token details can not be resolved in current context")

        return if (tokenDetails.isVisitor) {
            Person(tokenDetails.personId,"","", -1L)
        } else {
            personRepository.findByIdOrNull(tokenDetails.personId)
                ?: throw UserNotFoundException(personId = tokenDetails.personId)
        }
    }
}