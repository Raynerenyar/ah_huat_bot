package org.telegram.toto.util;

/**
 * This class simplifies building messages for telegram.
 * It compliments the existing StringBuilder class and further builds on it to reduce repeated code such as
 * writing a new line, or formatting text for telegram.
 */
public class telegramMessageBuilder {

    private final StringBuilder sb;

    public telegramMessageBuilder() {
        this.sb = new StringBuilder();
    }

    public telegramMessageBuilder newLine() {
        sb.append("\n");
        return this;
    }

    public telegramMessageBuilder boldText(String text) {
        sb.append("<b>");
        sb.append(text);
        sb.append("</b>");
        return this;
    }

    public telegramMessageBuilder addText(String text) {
        sb.append(text);
        return this;
    }

    public telegramMessageBuilder addCodeBlock(String text) {
        sb.append("```");
        sb.append("\n");
        sb.append(text);
        sb.append("\n");
        sb.append("```");
        sb.append("\n");
        return this;
    }

    public String toString() {
        return this.sb.toString();
    }
}
