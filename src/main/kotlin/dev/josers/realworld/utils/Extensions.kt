package dev.josers.realworld.utils

import io.jsonwebtoken.lang.Strings

fun String.updateVal(newVal: String?) = if(Strings.hasText(newVal)) { newVal!! } else this