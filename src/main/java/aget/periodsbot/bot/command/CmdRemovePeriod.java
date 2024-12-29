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

package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Command remove user's period.
 *
 * @since 0.1.0
 */
public final class CmdRemovePeriod implements Cmd<Update, AbsSender> {
    /**
     * Transaction.
     */
    private final Transaction<Users> transaction;

    /**
     * Convert date entered by the user to LocalDate.
     */
    private final Function<String, LocalDate> convert;

    public CmdRemovePeriod(
        final Transaction<Users> transaction,
        final Function<String, LocalDate> convert
    ) {
        this.transaction = transaction;
        this.convert = convert;
    }

    @Override
    public Optional<Send<AbsSender>> execute(final Update update) {
        this.transaction.consume(
            users ->
                users.user(update.getMessage().getFrom().getId())
                    .periods()
                    .remove(
                        this.convert.apply(
                            update.getMessage().getText()
                        ))
        );
        return Optional.of(new SendMsg(update, "Есть, мэм!"));
    }
}
