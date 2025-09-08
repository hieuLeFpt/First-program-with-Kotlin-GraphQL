package com.exception

class StudentDuplicateEmailException : RuntimeException {
    constructor(message: String) : super("Email is duplicate: $message")
}