package aget.periodsbot.domain.usecase;

public interface FunctionUseCase<I, O> {
    O handle(I input);
}
