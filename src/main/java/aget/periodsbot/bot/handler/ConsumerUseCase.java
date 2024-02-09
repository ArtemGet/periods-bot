package aget.periodsbot.bot.handler;

public interface ConsumerUseCase<I> {
    void handle(I input);
}
