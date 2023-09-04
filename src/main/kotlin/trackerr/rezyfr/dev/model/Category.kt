package trackerr.rezyfr.dev.model

import kotlinx.serialization.SerialName

data class Category(
    val name: String,
    val userEmail: String,
    val type: CategoryType,
    val iconId: Int
) {
    companion object {
        fun getInitialCategories(userEmail: String) = listOf(
            Category(name = "Food", type = CategoryType.EXPENSE, userEmail = userEmail, iconId = 1),
            Category(name = "Salary", type = CategoryType.INCOME, userEmail = userEmail, iconId = 1),
            Category(name = "Transportation", type = CategoryType.EXPENSE, userEmail = userEmail, iconId = 1),
            Category(name = "Entertainment", type = CategoryType.EXPENSE, userEmail = userEmail, iconId = 1),
            Category(name = "Shopping", type = CategoryType.EXPENSE, userEmail = userEmail, iconId = 1),
            Category(name = "Investment", type = CategoryType.INCOME, userEmail = userEmail, iconId = 1),
            Category(name = "Gift", type = CategoryType.INCOME, userEmail = userEmail, iconId = 1),
            Category(name = "Other", type = CategoryType.EXPENSE, userEmail = userEmail, iconId = 1),
        )
    }
}

enum class CategoryType {
    @SerialName("income")
    INCOME,
    @SerialName("expense")
    EXPENSE;
}