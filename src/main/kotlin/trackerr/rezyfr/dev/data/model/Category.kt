package trackerr.rezyfr.dev.data.model

import kotlinx.serialization.SerialName

data class Category(
    val name: String,
    val userEmail: String,
    val type: CategoryType
) {
    companion object {
        fun getInitialCategories(userEmail: String) = listOf(
            Category(name = "Food", type = CategoryType.EXPENSE, userEmail = userEmail),
            Category(name = "Salary", type = CategoryType.INCOME, userEmail = userEmail),
            Category(name = "Transportation", type = CategoryType.EXPENSE, userEmail = userEmail),
            Category(name = "Entertainment", type = CategoryType.EXPENSE, userEmail = userEmail),
            Category(name = "Shopping", type = CategoryType.EXPENSE, userEmail = userEmail),
            Category(name = "Investment", type = CategoryType.INCOME, userEmail = userEmail),
            Category(name = "Gift", type = CategoryType.INCOME, userEmail = userEmail),
            Category(name = "Other", type = CategoryType.EXPENSE, userEmail = userEmail),
        )
    }
}

enum class CategoryType{
    @SerialName("income")
    INCOME,
    @SerialName("expense")
    EXPENSE
}