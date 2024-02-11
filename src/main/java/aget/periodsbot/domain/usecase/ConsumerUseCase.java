package aget.periodsbot.domain.usecase;

public interface ConsumerUseCase<I> {
    void handle(I input);
}
