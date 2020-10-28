@file:Suppress("UNUSED_PARAMETER")

package lesson7

import kotlin.math.max

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
// время = память =  О(длина 1 * длина 2)
fun longestCommonSubSequence(first: String, second: String): String {
    if (first == "" || second == "") return ""
    val a = MutableList(first.length + 1) { IntArray(second.length + 1) }
    var firstLen = 0
    var secondLen = 0

    for (i in first.length - 1 downTo 0) {
        for (j in second.length - 1 downTo 0) {
            a[i][j] = when {
                first[i] == second[j] -> a[i + 1][j + 1] + 1
                else -> max(a[i + 1][j], a[i][j + 1])

            }
        }
    }

    return buildString {
        while (a[firstLen][secondLen] != 0 && firstLen < first.length && secondLen < second.length) {
            if (first[firstLen] == second[secondLen]) {
                append(first[firstLen])
                firstLen++
                secondLen++
            } else {
                if (a[firstLen][secondLen] == a[firstLen + 1][secondLen]) firstLen++ else secondLen++
            }
        }
    }
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
// время О(n^2)
// память О(n)
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    if (list.isEmpty()) return list

    val size = list.size
    val plusList = MutableList(size) { 1 }
    val minusList = MutableList(size) { -1 }
    val result = mutableListOf<Int>()

    for (i in list.indices) {
        for (j in 0 until i) {
            if (list[i] > list[j] && plusList[i] < plusList[j] + 1) {
                plusList[i] = plusList[j] + 1
                minusList[i] = j
            }
        }
    }

    var maxPos = plusList.indexOf(plusList.max())
    while (maxPos != -1) {
        result.add(0, list[maxPos])
        maxPos = minusList[maxPos]
    }

    return result
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    TODO()
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5