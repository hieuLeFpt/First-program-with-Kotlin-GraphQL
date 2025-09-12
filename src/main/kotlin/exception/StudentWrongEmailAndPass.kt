package com.exception

class StudentWrongEmailAndPass: RuntimeException {
    constructor(message: String) : super(message)
}