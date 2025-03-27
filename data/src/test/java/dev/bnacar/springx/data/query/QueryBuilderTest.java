package dev.bnacar.springx.data.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QueryBuilderTest {

    @Test
    public void testSelectAllFromTable() {
        // Arrange & Act
        QueryBuilder queryBuilder = QueryBuilder.select().from("users");

        // Assert
        assertEquals("SELECT * FROM users", queryBuilder.getQuery());
        assertEquals(0, queryBuilder.getParameters().length);
    }

    @Test
    public void testSelectSpecificColumnsFromTable() {
        // Arrange & Act
        QueryBuilder queryBuilder = QueryBuilder.select("id", "name", "email").from("users");

        // Assert
        assertEquals("SELECT id, name, email FROM users", queryBuilder.getQuery());
        assertEquals(0, queryBuilder.getParameters().length);
    }

    @Test
    public void testSelectWithWhereClause() {
        // Arrange & Act
        QueryBuilder queryBuilder = QueryBuilder.select()
                .from("users")
                .where("id = ?", 1);

        // Assert
        assertEquals("SELECT * FROM users WHERE id = ?", queryBuilder.getQuery());
        assertEquals(1, queryBuilder.getParameters().length);
        assertEquals(1, queryBuilder.getParameters()[0]);
    }

    @Test
    public void testSelectWithMultipleWhereConditions() {
        // Arrange & Act
        QueryBuilder queryBuilder = QueryBuilder.select()
                .from("users")
                .where("id = ?", 1)
                .and("status = ?", "active");

        // Assert
        assertEquals("SELECT * FROM users WHERE id = ? AND status = ?", queryBuilder.getQuery());
        assertEquals(2, queryBuilder.getParameters().length);
        assertEquals(1, queryBuilder.getParameters()[0]);
        assertEquals("active", queryBuilder.getParameters()[1]);
    }

    @Test
    public void testSelectWithOrCondition() {
        // Arrange & Act
        QueryBuilder queryBuilder = QueryBuilder.select()
                .from("users")
                .where("id = ?", 1)
                .or("id = ?", 2);

        // Assert
        assertEquals("SELECT * FROM users WHERE id = ? OR id = ?", queryBuilder.getQuery());
        assertEquals(2, queryBuilder.getParameters().length);
        assertEquals(1, queryBuilder.getParameters()[0]);
        assertEquals(2, queryBuilder.getParameters()[1]);
    }

    @Test
    public void testSelectWithOrderBy() {
        // Arrange & Act
        QueryBuilder queryBuilder = QueryBuilder.select()
                .from("users")
                .orderBy("name", "id DESC");

        // Assert
        assertEquals("SELECT * FROM users ORDER BY name, id DESC", queryBuilder.getQuery());
        assertEquals(0, queryBuilder.getParameters().length);
    }

    @Test
    public void testSelectWithLimitAndOffset() {
        // Arrange & Act
        QueryBuilder queryBuilder = QueryBuilder.select()
                .from("users")
                .limit(10)
                .offset(20);

        // Assert
        assertEquals("SELECT * FROM users LIMIT ? OFFSET ?", queryBuilder.getQuery());
        assertEquals(2, queryBuilder.getParameters().length);
        assertEquals(10, queryBuilder.getParameters()[0]);
        assertEquals(20, queryBuilder.getParameters()[1]);
    }

    @Test
    public void testComplexQuery() {
        // Arrange & Act
        QueryBuilder queryBuilder = QueryBuilder.select("u.id", "u.name", "u.email")
                .from("users u")
                .where("u.status = ?", "active")
                .and("u.created_at > ?", "2023-01-01")
                .orderBy("u.name ASC")
                .limit(10)
                .offset(0);

        // Assert
        assertEquals("SELECT u.id, u.name, u.email FROM users u WHERE u.status = ? AND u.created_at > ? ORDER BY u.name ASC LIMIT ? OFFSET ?",
                queryBuilder.getQuery());
        assertEquals(4, queryBuilder.getParameters().length);
        assertEquals("active", queryBuilder.getParameters()[0]);
        assertEquals("2023-01-01", queryBuilder.getParameters()[1]);
        assertEquals(10, queryBuilder.getParameters()[2]);
        assertEquals(0, queryBuilder.getParameters()[3]);
    }

    @Test
    public void testAndWithoutWhere() {
        // Arrange & Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            QueryBuilder.select().from("users").and("id = ?", 1);
        });
    }

    @Test
    public void testOrWithoutWhere() {
        // Arrange & Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            QueryBuilder.select().from("users").or("id = ?", 1);
        });
    }
}
