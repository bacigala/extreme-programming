package sk.bacigala.hike;

import java.util.Date;

public class Hike {
    private final long id, peak_id, difficulty, author_id;
    private final String name;
    private final Date date;

    public Hike(long id, long peak_id, long difficulty, long author_id, String name, Date date) {
        this.id = id;
        this.peak_id = peak_id;
        this.difficulty = difficulty;
        this.author_id = author_id;
        this.name = name;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public long getPeak_id() {
        return peak_id;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public long getAuthor_id() {
        return author_id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

}
