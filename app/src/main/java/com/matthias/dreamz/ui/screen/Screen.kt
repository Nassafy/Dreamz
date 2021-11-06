package com.matthias.dreamz.ui.screen

sealed class Screen(val route: String) {
    object DreamList : Screen("dreamlist")
    object ViewDream : Screen("viewdream/{dreamId}") {
        fun createRoute(dreamId: Long): String {
            return "viewDream/$dreamId"
        }
    }

    object EditDream : Screen("editdream/{dreamId}") {
        fun createRoute(dreamId: Long): String {
            return "editdream/$dreamId"
        }
    }

    object Calendar : Screen("calendar")

    object Peoples : Screen("peoples")

    object Tags : Screen("tags")

    object Graph: Screen("graph")

    object Login: Screen("login")

}