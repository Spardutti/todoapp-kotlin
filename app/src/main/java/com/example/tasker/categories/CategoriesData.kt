package com.example.tasker.categories

data class CategoriesData(val name: String, val id: String){
    override fun toString(): String {
        return name
    }
}