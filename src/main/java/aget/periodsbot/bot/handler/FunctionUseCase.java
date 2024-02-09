package aget.periodsbot.bot.handler;

public interface FunctionUseCase<I, O> {
    O handle(I input);
}
