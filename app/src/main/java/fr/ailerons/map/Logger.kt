package fr.ailerons.map

import android.util.Log

fun logger(vararg content: Any?) = Log.d("DEBUG", content.joinToString { it.toString() })
fun <T> T.log(transform: (T) -> String = { it.toString() }) = also { logger(transform(this@log)) }
