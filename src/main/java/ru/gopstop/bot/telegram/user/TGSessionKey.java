package ru.gopstop.bot.telegram.user;

import org.telegram.telegrambots.api.objects.User;

/**
 * Created by Semyon on 30.07.2016.
 */
public class TGSessionKey {

    private final Long chatId;

    private final User user;

    public TGSessionKey(final User user, final Long chatId) {
        this.user = user;
        this.chatId = chatId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TGSessionKey that = (TGSessionKey) o;

        if (!chatId.equals(that.chatId)) return false;
        return user.getId().equals(that.user.getId());

    }

    @Override
    public int hashCode() {
        int result = chatId.hashCode();
        result = 31 * result + user.getId().hashCode();
        return result;
    }
}
