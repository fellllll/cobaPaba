package PABA.UAS.c14210001.apiCostService

data class costModel(
    val origin: String,
    val destination: String,
    val weight: Int,  // Weight in grams
    val courier: String
)
