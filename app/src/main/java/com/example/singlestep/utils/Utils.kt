package com.example.singlestep.utils

import com.example.singlestep.model.Item

fun getSampleItems(): MutableList<Item> {
    val items: MutableList<Item> = mutableListOf()
    for(i in 0..10) {
        items.add(
            Item(i, "Item $i", "This is the description for Item $i"))
    }
    return items
}