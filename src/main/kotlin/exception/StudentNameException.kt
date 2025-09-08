package com.exception

class StudentNameException : RuntimeException {
    constructor(message: String) : super("Lỗi: $message")
}