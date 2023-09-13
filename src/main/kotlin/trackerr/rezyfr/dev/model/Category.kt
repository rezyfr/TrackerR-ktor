package trackerr.rezyfr.dev.model

import kotlinx.serialization.SerialName

data class Category(
    val name: String,
    val userEmail: String,
    val type: CategoryType,
    val iconId: Int,
    val color: String
) {
    companion object {
        fun getInitialCategories(userEmail: String) = listOf(
            Category(name = "Food", type = CategoryType.EXPENSE, userEmail = userEmail, iconId = 4, color = "#7f3dff"),
            Category(
                name = "Salary",
                type = CategoryType.INCOME,
                userEmail = userEmail,
                iconId = 5,
                color = "#7f3dff"
            ),
            Category(
                name = "Transportation",
                type = CategoryType.EXPENSE,
                userEmail = userEmail,
                iconId = 2,
                color = "#7f3dff"
            ),
//            Category(
//                name = "Entertainment",
//                type = CategoryType.EXPENSE,
//                userEmail = userEmail,
//                iconId = 14,
//                color = "#7f3dff"
//            ),
//            Category(
//                name = "Shopping",
//                type = CategoryType.EXPENSE,
//                userEmail = userEmail,
//                iconId = 1,
//                color = "#7f3dff"
//            ),
//            Category(
//                name = "Investment",
//                type = CategoryType.INCOME,
//                userEmail = userEmail,
//                iconId = 13,
//                color = "#7f3dff"
//            ),
//            Category(name = "Gift", type = CategoryType.INCOME, userEmail = userEmail, iconId = 10, color = "#7f3dff"),
//            Category(
//                name = "Other",
//                type = CategoryType.EXPENSE,
//                userEmail = userEmail,
//                iconId = 15,
//                color = "#7f3dff"
//            ),
        )
    }
}

enum class CategoryType {
    @SerialName("INCOME")
    INCOME,

    @SerialName("EXPENSE")
    EXPENSE;
}