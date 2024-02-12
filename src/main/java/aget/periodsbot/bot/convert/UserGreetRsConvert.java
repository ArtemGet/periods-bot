package aget.periodsbot.bot.convert;

public class UserGreetRsConvert implements Convert<String, String> {
    @Override
    public String convert(String userName) {
        return String.format("Добро пожаловать %s, снова.", userName);
    }
}
