package com.dsstudio.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_DIR = System.getProperty("user.home") + File.separator + ".dsstudio";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + File.separator + "ds_studio.db";
    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            File dir = new File(DB_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            connection = DriverManager.getConnection(DB_URL);
            initSchema(connection);
        }
        return connection;
    }

    private static void initSchema(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Profile & Gamification
            stmt.execute("CREATE TABLE IF NOT EXISTS profile (" +
                    "id INTEGER PRIMARY KEY DEFAULT 1," +
                    "username TEXT NOT NULL DEFAULT 'CS Scholar'," +
                    "xp INTEGER NOT NULL DEFAULT 0," +
                    "level INTEGER NOT NULL DEFAULT 1," +
                    "streak INTEGER NOT NULL DEFAULT 1," +
                    "last_active_date TEXT," +
                    "theme TEXT DEFAULT 'DARK'" +
                    ");");

            stmt.execute("INSERT OR IGNORE INTO profile (id, username, xp, level, streak, last_active_date, theme) " +
                    "VALUES (1, 'CS Scholar', 120, 2, 3, date('now'), 'DARK');");

            // Lesson Progress
            stmt.execute("CREATE TABLE IF NOT EXISTS lesson_progress (" +
                    "topic_id TEXT PRIMARY KEY," +
                    "completed INTEGER DEFAULT 0," +
                    "bookmarked INTEGER DEFAULT 0," +
                    "last_read_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");");

            // Quiz Attempts
            stmt.execute("CREATE TABLE IF NOT EXISTS quiz_attempts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "topic TEXT NOT NULL," +
                    "difficulty TEXT NOT NULL," +
                    "score INTEGER NOT NULL," +
                    "total_questions INTEGER NOT NULL," +
                    "accuracy REAL NOT NULL," +
                    "xp_earned INTEGER NOT NULL," +
                    "attempt_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");");

            // Achievements
            stmt.execute("CREATE TABLE IF NOT EXISTS achievements (" +
                    "code TEXT PRIMARY KEY," +
                    "title TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "unlocked INTEGER DEFAULT 0," +
                    "unlocked_at TEXT" +
                    ");");

            // Seed initial achievements
            stmt.execute("INSERT OR IGNORE INTO achievements (code, title, description, unlocked, unlocked_at) VALUES " +
                    "('FIRST_STEP', 'First Step', 'Launched DS Studio and initiated learning', 1, datetime('now'))," +
                    "('ARRAY_MASTER', 'Array Artificer', 'Completed Array lesson & visualizer', 1, datetime('now'))," +
                    "('STACK_PEEKER', 'LIFO Pioneer', 'Pushed and popped from Stack', 1, datetime('now'))," +
                    "('QUEUE_ROUTINE', 'FIFO Navigator', 'Successfully enqueued elements', 0, NULL)," +
                    "('LINKED_EXPLORER', 'Pointer Pro', 'Traversed a Linked List', 0, NULL)," +
                    "('QUIZ_CHAMP', 'Quiz Ace', 'Scored 100% on any quiz challenge', 0, NULL)," +
                    "('STREAK_WARRIOR', 'Consistent Coder', 'Maintained a 3-day learning streak', 1, datetime('now'));");

            // Bookmarks
            stmt.execute("CREATE TABLE IF NOT EXISTS bookmarks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");");
        }
    }
}
