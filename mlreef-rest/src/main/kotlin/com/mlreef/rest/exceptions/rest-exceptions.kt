package com.mlreef.rest.exceptions

import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ResponseStatus

enum class ErrorCode(val errorCode: Int, val errorName: String) {
    // authentication and general errors: 1xxx
    NotFound(1404, "Entity not found"),
    Conflict(1409, "Entity already exists"),

    // specific user management errors 2xxx
    UserAlreadyExisting(2001, "User already exists"),
    UserNotExisting(2002, "User does not exist"),
    GitlabUserCreationFailed(2101, "Cannot create user in gitlab"),
    GitlabUserTokenCreationFailed(2102, "Cannot create user token in gitlab"),
    GitlabUserNotExisting(2103, "Cannot find user in gitlab via token"),
    GitlabGroupCreationFailed(2104, "Cannot create group in gitlab"),
    GitlabUserAddingToGroupFailed(2105, "Cannot add user to group in gitlab"),
    GitlabProjectCreationFailed(2106, "Cannot create project in gitlab"),
    GitlabProjectUpdateFailed(2107, "Cannot update project in gitlab"),
    GitlabProjectDeleteFailed(2108, "Cannot delete project in gitlab"),
    GitlabVariableCreationFailed(2109, "Cannot create group variable in gitlab"),
    GitlabCommonError(2110, "Gitlab common error"),
    GitlabBadGateway(2111, "Gitlab server is unavailable"),
    GitlabBranchCreationFailed(2112, "Cannot create branch in gitlab"),
    GitlabCommitFailed(2113, "Cannot commit files in gitlab"),
    GitlabProjectAlreadyExists(2114, "Cannot create project in gitlab. Project already exists"),

    // Business errors: 3xxx
    ValidationFailed(3000, "ValidationFailed"),

    // Creating Experiments 31xx
    DataProcessorNotUsable(3101, "DataProcessor cannot be used"),
    ProcessorParameterNotUsable(3102, "ProcessorParameter cannot be used"),
    CommitPipelineScriptFailed(3103, "Could not commit mlreef file"),
    ExperimentCannotBeChanged(3104, "Could not change status of Experiment"),

    ProjectCreationFailed(3201, "Could not create project"),
}

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Operation cannot be executed due to malformed input or invalid states.")
open class RestException(
    val errorCode: Int,
    val errorName: String,
    msg: String? = null,
    cause: Throwable? = null) : RuntimeException(msg, cause) {

    constructor(errorCode: ErrorCode) : this(errorCode.errorCode, errorCode.errorName)
    constructor(errorCode: ErrorCode, msg: String) : this(errorCode.errorCode, errorCode.errorName, msg)
}

class ValidationException(val validationErrors: Array<FieldError?>) : RestException(ErrorCode.ValidationFailed, validationErrors.joinToString("\n") { it.toString() })

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity not found")
class NotFoundException(message: String) : RestException(ErrorCode.NotFound, message)

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Gitlab not reachable!")
class GitlabConnectException(message: String) : RestException(ErrorCode.NotFound, message)

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Gitlab cannot create entity due to a duplicate conflict:")
class GitlabAlreadyExistingConflictException(errorCode: ErrorCode, message: String) : RestException(errorCode, message)

class UserAlreadyExistsException(username: String, email: String) : RestException(ErrorCode.UserAlreadyExisting, "User ($username/$email) already exists and cant be created")
class UserNotExistsException(username: String, email: String) : RestException(ErrorCode.UserNotExisting, "User ($username/$email) does not exist")

class ExperimentCreateException(errorCode: ErrorCode, parameterName: String) : RestException(errorCode, "Name/Slug: '$parameterName'")
class ExperimentStartException(message: String) : RestException(ErrorCode.CommitPipelineScriptFailed, message)
class ExperimentUpdateException(message: String) : RestException(ErrorCode.ExperimentCannotBeChanged, message)

class ProjectCreationException(errorCode: ErrorCode, message: String) : RestException(errorCode, message)
class ProjectUpdateException(errorCode: ErrorCode, message: String) : RestException(errorCode, message)
class ProjectDeleteException(errorCode: ErrorCode, message: String) : RestException(errorCode, message)


@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Gitlab is unavailable")
class GitlabCommonException(error: ErrorCode? = null, message: String? = null) : RestException(error
    ?: ErrorCode.GitlabCommonError, message ?: "Gitlab common exception")

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Gitlab cannot create entity due to a bad request")
class GitlabBadRequestException(error: ErrorCode, message: String) : RestException(error, message)

@ResponseStatus(code = HttpStatus.BAD_GATEWAY, reason = "Gitlab is unavailable")
class GitlabBadGatewayException : RestException(ErrorCode.GitlabBadGateway, "Gitlab server is unavailable")

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Gitlab cannot create entity due to a conflict")
class GitlabConflictException(error: ErrorCode, message: String) : RestException(error, message)

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Gitlab not found the object")
class GitlabNotFoundException(error: ErrorCode, message: String) : RestException(error, message)

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Gitlab authentication failed")
class GitlabAuthenticationFailedException(error: ErrorCode, message: String) : RestException(error, message)


