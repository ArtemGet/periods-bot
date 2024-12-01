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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Optional;

public class PeriodsBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(PeriodsBot.class);
    private final BotProps props;
    private final TelegramBotsApi telegramBotsApi;
    private final Route<Update, AbsSender> route;

    public PeriodsBot(BotProps props, Route<Update, AbsSender> route) throws TelegramApiException {
        this(props, new TelegramBotsApi(DefaultBotSession.class), route);
    }

    public PeriodsBot(BotProps props,
                      TelegramBotsApi telegramBotsApi,
                      Route<Update, AbsSender> route) {
        super(props.botToken());
        this.props = props;
        this.telegramBotsApi = telegramBotsApi;
        this.route = route;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        this.route.route(new TgBotWrap(update))
            .flatMap(cmd -> this.handleExecution(cmd, update))
            .ifPresent(this::handleSend);
    }

    @Override
    public String getBotUsername() {
        return this.props.botName();
    }

    public void start() {
        try {
            this.telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Error starting bot: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public Optional<Send<AbsSender>> handleExecution(
        final Cmd<Update, AbsSender> cmd,
        final Update update
    ) {
        Optional<Send<AbsSender>> resp;
        try {
            resp = cmd.execute(update);
        } catch (CmdException exception) {
            resp = Optional.empty();
            exception.printStackTrace();
        }
        return resp;
    }

    public void handleSend(final Send<AbsSender> send) {
        try {
            send.send(this);
        } catch (SendException exception) {
            exception.printStackTrace();
        }
    }
}
