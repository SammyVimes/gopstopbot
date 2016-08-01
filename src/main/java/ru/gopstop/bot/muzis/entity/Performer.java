package ru.gopstop.bot.muzis.entity;

/**
 * Created by Semyon on 30.07.2016.
 */
public class Performer {

    private Long id;

    // тип обьекта (для performer всегда 3)
    private Long type;

    // название исполнителя
    private String title;

    // постер
    private String poster;

    public Performer() {
    }

    public Long getType() {
        return type;
    }

    public void setType(final Long type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(final String poster) {
        this.poster = poster;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
