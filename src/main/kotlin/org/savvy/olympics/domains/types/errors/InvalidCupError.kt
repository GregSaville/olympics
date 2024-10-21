package org.savvy.olympics.domains.types.errors

import java.lang.RuntimeException

data class InvalidCupError(val cupPosition: Int): RuntimeException("Invalid Cup Position for cup: $cupPosition")
