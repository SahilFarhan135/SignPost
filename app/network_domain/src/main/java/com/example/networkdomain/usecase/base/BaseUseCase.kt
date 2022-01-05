package com.example.networkdomain.usecase.base

abstract class BaseUseCase<out Result, in Request> {
    abstract fun perform(params: Request): Result
}