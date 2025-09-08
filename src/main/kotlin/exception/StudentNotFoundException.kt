package com.exception

class StudentNotFoundException: RuntimeException {
    constructor(message: String) : super("Lỗi: $message")
}