package ru.mts.data.utils

import timber.log.Timber
import java.util.concurrent.CancellationException

sealed class Result<out S, out E> {

    data class Success<out S>(val data: S) : Result<S, Nothing>()

    data class Error<out E>(val error: E) : Result<Nothing, E>()
}

typealias VoidResult<E> = Result<Unit, E>

fun <S> Result<S, Throwable>.getOrThrow(): S =
    when (this) {
        is Result.Success -> this.data
        is Result.Error -> throw this.error
    }

inline fun <S, E, R> Result<S, E>.mapSuccess(block: (S) -> R): Result<R, E> =
    when (this) {
        is Result.Success -> Result.Success(data = block(this.data))
        is Result.Error -> Result.Error(error = this.error)
    }

inline fun <S, E, R> Result<S, E>.mapError(block: (E) -> R): Result<S, R> =
    when (this) {
        is Result.Success -> Result.Success(data = this.data)
        is Result.Error -> Result.Error(error= block(this.error))
    }

inline fun <S, E, R> Result<S, E>.mapNestedSuccess(
    block: (S) -> Result<R, E>,
): Result<R, E> =
    when (this) {
        is Result.Success -> block(this.data)
        is Result.Error -> Result.Error(error = this.error)
    }

inline fun <S, E> Result<S, E>.doOnSuccess(block: (S) -> Unit): Result<S, E> {
    if (this is Result.Success) {
        block(this.data)
    }
    return this
}

inline fun <S, E> Result<S, E>.doOnError(block: (E) -> Unit): Result<S, E> {
    if (this is Result.Error) {
        Timber.e("Result.doOnError: ".plus(this.error.toString()))
        block(this.error)
    }
    return this
}

inline fun <S, R> S.runOperationCatching(block: S.() -> R): Result<R, Throwable> {
    return try {
        Result.Success(block())
    } catch (e: CancellationException) {
        Timber.e("runOperationCatching: ".plus(e.toString()))
        throw e
    } catch (e: Throwable) {
        Timber.e("runOperationCatching: ".plus(e.toString()))
        Result.Error(e)
    }
}

inline fun <reified S, reified E> List<Result<S, E>>.toSuccessOrErrorList(): Result<List<S>, List<E>> {
    var successResults: MutableList<S>? = null
    var errorResults: MutableList<E>? = null

    var hasErrors = false

    for (item: Result<S, E> in this) {
        when {
            ((item is Result.Success) && !hasErrors) -> {
                if (successResults == null) {
                    successResults = mutableListOf()
                }

                successResults.add(item.data)
            }
            (item is Result.Error) -> {
                hasErrors = true

                if (errorResults == null) {
                    errorResults = mutableListOf()
                }

                errorResults.add(item.error)
            }
        }
    }

    return if (errorResults != null) {
        Result.Error(error = errorResults)
    } else {
        Result.Success(data = successResults.orEmpty())
    }
}