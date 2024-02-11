package aget.periodsbot.bot.convert;

public interface Convert<S, T> {
    T convert(S source);
}
