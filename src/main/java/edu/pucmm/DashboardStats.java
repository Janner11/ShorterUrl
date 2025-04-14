package edu.pucmm;

import java.util.Map;

/**
 * POJO para agrupar las estadísticas globales
 */
public class DashboardStats {
    private long totalUrls;
    private long totalHits;
    private Map<String, Integer> hitsByDay;
    private Map<String, Integer> hitsByBrowser;
    private Map<String, Integer> hitsByOs;

    public long getTotalUrls() {
        return totalUrls;
    }

    public void setTotalUrls(long totalUrls) {
        this.totalUrls = totalUrls;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public Map<String, Integer> getHitsByDay() {
        return hitsByDay;
    }

    public void setHitsByDay(Map<String, Integer> hitsByDay) {
        this.hitsByDay = hitsByDay;
    }

    public Map<String, Integer> getHitsByBrowser() {
        return hitsByBrowser;
    }

    public void setHitsByBrowser(Map<String, Integer> hitsByBrowser) {
        this.hitsByBrowser = hitsByBrowser;
    }

    public Map<String, Integer> getHitsByOs() {
        return hitsByOs;
    }

    public void setHitsByOs(Map<String, Integer> hitsByOs) {
        this.hitsByOs = hitsByOs;
    }
}
