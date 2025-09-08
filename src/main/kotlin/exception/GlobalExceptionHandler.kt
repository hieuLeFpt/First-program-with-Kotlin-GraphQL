package com.exception

import com.netflix.graphql.dgs.DgsComponent
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import java.util.concurrent.CompletableFuture
import graphql.GraphqlErrorBuilder

@DgsComponent
class GlobalExceptionHandler : DataFetcherExceptionHandler {
    override fun handleException(
        params: DataFetcherExceptionHandlerParameters?
    ): CompletableFuture<DataFetcherExceptionHandlerResult?>? {
        val ex = params?.exception
        val (msg, code) = when (ex) {
            is StudentNotFoundException -> (ex.message ?: "Student not found") to "STUDENT_NOT_FOUND"
            is StudentDuplicateEmailException -> (ex.message ?: "Duplicate email") to "DUPLICATE_EMAIL"
            is StudentNameException -> (ex.message ?: "Invalid name") to "INVALID_STUDENT_NAME"
            else -> (ex?.message ?: "Unexpected error") to "INTERNAL_ERROR"
        }

        val error = GraphqlErrorBuilder.newError()
            .message(msg ?: "Unexpected error")
            .path(params?.path)
            .location(params?.sourceLocation)
            .extensions(mapOf("code" to code))
            .build()

        val result = DataFetcherExceptionHandlerResult.newResult()
            .error(error)
            .build()

        return CompletableFuture.completedFuture(result)
    }
}
