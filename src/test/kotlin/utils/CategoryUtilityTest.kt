package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.CategoryUtility.categories
import utils.CategoryUtility.isValidCategory

internal class CategoryUtilityTest {
    @Test
    fun categoriesReturnsFullCategoriesSet() {
        Assertions.assertEquals(5, categories.size)
        Assertions.assertTrue(categories.contains("Backstroke"))
        Assertions.assertTrue(categories.contains("Breaststroke"))
        Assertions.assertTrue(categories.contains("Freestyle"))
        Assertions.assertTrue(categories.contains("Medley"))
        Assertions.assertTrue(categories.contains("Butterfly"))
        Assertions.assertFalse(categories.contains(""))
    }

    @Test
    fun isValidCategoryTrueWhenCategoryExists() {
        Assertions.assertTrue(isValidCategory("Backstroke"))
        Assertions.assertTrue(isValidCategory("backstroke"))
        Assertions.assertTrue(isValidCategory("breaststroke"))
        Assertions.assertTrue(isValidCategory("breaststroke"))
        Assertions.assertTrue(isValidCategory("Freestyle"))
        Assertions.assertTrue(isValidCategory("freestyle"))
        Assertions.assertTrue(isValidCategory("Medley"))
        Assertions.assertTrue(isValidCategory("medley"))
        Assertions.assertTrue(isValidCategory("Butterfly"))
        Assertions.assertTrue(isValidCategory("butterfly"))
    }

    @Test
    fun isValidCategoryFalseWhenCategoryDoesNotExist() {
        Assertions.assertFalse(isValidCategory("B"))
        Assertions.assertFalse(isValidCategory("Back"))
        Assertions.assertFalse(isValidCategory("breast"))
        Assertions.assertFalse(isValidCategory("F"))
        Assertions.assertFalse(isValidCategory("free"))
        Assertions.assertFalse(isValidCategory("M"))
        Assertions.assertFalse(isValidCategory("med"))
        Assertions.assertFalse(isValidCategory("Butt"))
        Assertions.assertFalse(isValidCategory("butter"))
        Assertions.assertFalse(isValidCategory(""))
    }
}
