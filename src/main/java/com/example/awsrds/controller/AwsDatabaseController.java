package com.example.awsrds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AwsDatabaseController {

    private final JdbcTemplate jdbcTemplate;

    public AwsDatabaseController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/top_customers")
    public List<Map<String, Object>> getTopCustomers() {
        String query = """
            SELECT 
                c.customer_id,
                c.name,
                c.email,
                SUM(oi.quantity * oi.unit_price) AS total_spent
            FROM customers c
            JOIN orders o ON c.customer_id = o.customer_id
            JOIN order_items oi ON o.order_id = oi.order_id
            GROUP BY c.customer_id, c.name, c.email
            ORDER BY total_spent DESC
        """;
        return jdbcTemplate.queryForList(query);
    }

    @GetMapping("/monthly_sales")
    public List<Map<String, Object>> getMonthlySales() {
        String query = """
            SELECT 
                YEAR(o.order_date) AS year,
                MONTH(o.order_date) AS month,
                SUM(oi.quantity * oi.unit_price) AS total_sales
            FROM orders o
            JOIN order_items oi ON o.order_id = oi.order_id
            WHERE o.status IN ('Shipped', 'Delivered')
            GROUP BY YEAR(o.order_date), MONTH(o.order_date)
            ORDER BY year DESC, month DESC
        """;
        return jdbcTemplate.queryForList(query);
    }

    @GetMapping("/products_never_ordered")
    public List<Map<String, Object>> getProductsNeverOrdered() {
        String query = """
            SELECT 
                p.product_id,
                p.name,
                p.category,
                p.price
            FROM products p
            LEFT JOIN order_items oi ON p.product_id = oi.product_id
            WHERE oi.product_id IS NULL
        """;
        return jdbcTemplate.queryForList(query);
    }

    @GetMapping("/avg_order_value_by_country")
    public List<Map<String, Object>> getAvgOrderValueByCountry() {
        String query = """
            SELECT 
                c.country,
                AVG(order_total) AS avg_order_value
            FROM (
                SELECT 
                    o.order_id,
                    SUM(oi.quantity * oi.unit_price) AS order_total,
                    o.customer_id
                FROM orders o
                JOIN order_items oi ON o.order_id = oi.order_id
                GROUP BY o.order_id, o.customer_id
            ) AS order_values
            JOIN customers c ON order_values.customer_id = c.customer_id
            GROUP BY c.country
            ORDER BY avg_order_value DESC
        """;
        return jdbcTemplate.queryForList(query);
    }

    @GetMapping("/frequent_buyers")
    public List<Map<String, Object>> getFrequentBuyers() {
        String query = """
            SELECT 
                c.customer_id,
                c.name,
                c.email,
                COUNT(o.order_id) AS order_count
            FROM customers c
            JOIN orders o ON c.customer_id = o.customer_id
            GROUP BY c.customer_id, c.name, c.email
            HAVING COUNT(o.order_id) > 1
            ORDER BY order_count DESC
        """;
        return jdbcTemplate.queryForList(query);
    }
}
