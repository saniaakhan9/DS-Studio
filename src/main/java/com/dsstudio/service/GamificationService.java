package com.dsstudio.service;

import com.dsstudio.db.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GamificationService {
    public static class ProfileData {
        public String username;
        public int xp;
        public int level;
        public String levelTitle;
        public int streak;
        public String theme;
    }

    public static class AchievementItem {
        public String code;
        public String title;
        public String description;
        public boolean unlocked;
        public String unlockedAt;
    }

    public static ProfileData getProfile() {
        ProfileData data = new ProfileData();
        data.username = "CS Scholar";
        data.xp = 150;
        data.level = 2;
        data.levelTitle = getLevelTitle(2);
        data.streak = 3;
        data.theme = "DARK";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, xp, level, streak, theme, last_active_date FROM profile WHERE id = 1")) {
            if (rs.next()) {
                data.username = rs.getString("username");
                data.xp = rs.getInt("xp");
                data.level = calculateLevel(data.xp);
                data.levelTitle = getLevelTitle(data.level);
                data.streak = rs.getInt("streak");
                data.theme = rs.getString("theme");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void addXp(int amount) {
        ProfileData p = getProfile();
        int newXp = p.xp + amount;
        int newLevel = calculateLevel(newXp);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE profile SET xp = ?, level = ?, last_active_date = ? WHERE id = 1")) {
            ps.setInt(1, newXp);
            ps.setInt(2, newLevel);
            ps.setString(3, LocalDate.now().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int calculateLevel(int xp) {
        if (xp < 100) return 1;
        if (xp < 250) return 2;
        if (xp < 500) return 3;
        if (xp < 1000) return 4;
        return 5;
    }

    public static String getLevelTitle(int level) {
        return switch (level) {
            case 1 -> "Novice Explorer";
            case 2 -> "Data Craftsman";
            case 3 -> "Algorithm Adept";
            case 4 -> "System Architect";
            default -> "Data Structures Master";
        };
    }

    public static List<AchievementItem> getAchievements() {
        List<AchievementItem> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT code, title, description, unlocked, unlocked_at FROM achievements")) {
            while (rs.next()) {
                AchievementItem item = new AchievementItem();
                item.code = rs.getString("code");
                item.title = rs.getString("title");
                item.description = rs.getString("description");
                item.unlocked = rs.getInt("unlocked") == 1;
                item.unlockedAt = rs.getString("unlocked_at");
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void unlockAchievement(String code) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE achievements SET unlocked = 1, unlocked_at = CURRENT_TIMESTAMP WHERE code = ? AND unlocked = 0")) {
            ps.setString(1, code);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                addXp(50); // Bonus 50 XP for achievement unlock
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
