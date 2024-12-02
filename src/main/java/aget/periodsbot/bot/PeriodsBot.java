/*
 * MIT License
 *
 * Copyright (c) 2024 Artem Getmanskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package aget.periodsbot.bot;

import aget.periodsbot.bot.update.TgBotWrap;
import aget.periodsbot.config.BotProps;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.command.CmdException;
import com.github.artemget.teleroute.route.Route;
import com.github.artemget.teleroute.send.Send;
import com.github.artemget.teleroute.send.SendException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Main bot class.
 *
 * @since 0.1.0
 */
public final class PeriodsBot extends TelegramLongPollingBot {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(PeriodsBot.class);

    /**
     * Bot properties.
     */
    private final BotProps props;

    /**
     * Telegram bots API.
     */
    private final TelegramBotsApi api;

    /**
     * Route.
     */
    private final Route<Update, AbsSender> route;

    public PeriodsBot(
        final BotProps props,
        final Route<Update, AbsSender> route
    ) throws TelegramApiException {
        this(props, new TelegramBotsApi(DefaultBotSession.class), route);
    }

    public PeriodsBot(
        final BotProps props,
        final TelegramBotsApi api,
        final Route<Update, AbsSender> route
    ) {
        super(props.botToken());
        this.props = props;
        this.api = api;
        this.route = route;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        this.route.route(new TgBotWrap(update))
            .flatMap(cmd -> handleExecution(cmd, update))
            .ifPresent(this::handleSend);
    }

    @Override
    public String getBotUsername() {
        return this.props.botName();
    }

    public void start() {
        try {
            this.api.registerBot(this);
        } catch (final TelegramApiException exception) {
            LOG.error("Error starting bot: {}", exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }

    private static Optional<Send<AbsSender>> handleExecution(
        final Cmd<Update, AbsSender> cmd,
        final Update update
    ) {
        Optional<Send<AbsSender>> resp;
        try {
            resp = cmd.execute(update);
        } catch (final CmdException exception) {
            resp = Optional.empty();
            LOG.error("Error executing command: {}", exception.getMessage(), exception);
        }
        return resp;
    }

    private void handleSend(final Send<AbsSender> send) {
        try {
            send.send(this);
        } catch (final SendException exception) {
            LOG.error("Error sending message: {}", exception.getMessage(), exception);
        }
    }
}
