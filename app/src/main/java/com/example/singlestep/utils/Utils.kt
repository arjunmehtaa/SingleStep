package com.example.singlestep.utils

import com.example.singlestep.model.Item

fun getSampleItems(): MutableList<Item> {
    val items: MutableList<Item> = mutableListOf()
    for (i in 0..10) {
        items.add(
            Item(i, "Item $i", "Please click this card for Item $i details")
        )
    }
    return items
}