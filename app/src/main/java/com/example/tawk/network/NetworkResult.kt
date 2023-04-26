package com.example.tawk.network

/**
 *
 * Contains network states and return with each request
 */
sealed class NetworkResult<out T> {
    data class OnSuccess<out T>(val data: T) : NetworkResult<T>()
    data class OnUnexpected(val messageId: Int) : NetworkResult<Nothing>()
    data class OnNotAuthorized(val messageId: Int) : NetworkResult<Nothing>()
    object OnLoading : NetworkResult<Nothing>()
}