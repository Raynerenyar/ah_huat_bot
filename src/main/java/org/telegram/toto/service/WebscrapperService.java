package org.telegram.toto.service;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.toto.models.Draw;

@Service
public class WebscrapperService {

    @Value("${site.url}")
    private String url;
    @Value("${site.url.2}")
    private String url2;
    @Value("${site.url.3}")
    private String url3;

    public Optional<Draw> getNextDraw() {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();

        } catch (IOException e) {
            return Optional.empty();
        }

        Elements possibleCurrencyElements = doc.select("span");
        String stringOriginal = possibleCurrencyElements.first().text();
        String stringCleaned = stringOriginal
                .replaceAll(",", "")
                .replace("est", "")
                .replace("$", "")
                .trim();

        Elements possibleDateElements = doc.getElementsByClass("toto-draw-date");
        String date = possibleDateElements.first().text();
        date = date.replaceAll("\\s", "");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E,dMMMyyyy,h.mma", Locale.ENGLISH);
        DateTimeFormatter customFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(formatter)
                .toFormatter(Locale.ENGLISH);

        LocalDateTime localDateTime = LocalDateTime.parse(date, customFormatter);

        return Optional.of(new Draw(Long.valueOf(stringCleaned), localDateTime));
    }

    public String getNextDrawInString() {
        Optional<Draw> opt = this.getNextDraw();
        if (opt.isPresent()) {
            return getNextDrawInString(opt.get());
        }
        return "Unable to get next draw";
    }

    public String getNextDrawInStringWithUrl() {
        return getNextDrawInString() + "\n" + url2;
    }

    public String getNextDrawInString(Draw draw) {
        DateTimeFormatter finalFormatter = DateTimeFormatter.ofPattern("EEEE d/MM/yyyy h:mma",
                Locale.ENGLISH);
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.US);

        String result = draw.getDatetime().format(finalFormatter) +
                "\n" +
                "$" + numberFormatter.format(draw.getValue());
        return result;
    }

    public String getPreviousDraw() {

        Document doc;
        try {
            doc = Jsoup.connect(url3).get();
        } catch (IOException e) {
            return "Unable to get previous draw";
        }
        Elements tables = doc.getElementsByTag("table");

        StringBuilder sb = new StringBuilder();

        String drawDate = doc.getElementsByClass("drawDate").first().text();
        String winningNumbersHeading = tables.get(1).getElementsByTag("thead").text();
        String winningNumbersDigits = tables.get(1).getElementsByTag("tbody").text();
        String additionalNumsHeading = tables.get(2).getElementsByTag("thead").text();
        String additionalNumsDigits = tables.get(2).getElementsByTag("tbody").text();
        String groupPrizeHeading = tables.get(3).getElementsByTag("thead").text();
        String groupPrizeDigits = tables.get(3).getElementsByTag("tbody").text();

        sb.append(drawDate + "\n")
                .append("\n")
                .append(winningNumbersHeading + "\n")
                .append(winningNumbersDigits.strip().replaceAll(" ", ", ") + "\n")
                .append("\n")
                .append(additionalNumsHeading + "\n")
                .append(additionalNumsDigits + "\n")
                .append("\n")
                .append(groupPrizeHeading + "\n")
                .append(groupPrizeDigits + "\n")
                .append("\n");

        Elements winningSharesRows = tables.get(4).getElementsByTag("tr");

        int numOfRows = 9;

        sb.append("```\n");
        String s1 = "-".repeat(5);
        String s2 = "-".repeat(12);
        String s3 = "-".repeat(14);

        sb.append(String.format("| %-5s | %-12s | %-14s |%n", "Group", "Amount", "No. of shares"));
        sb.append(String.format("| %s | %s | %s |%n", s1, s2, s3));

        for (int i = 2; i < numOfRows; i++) {
            Elements cells = winningSharesRows.get(i).getElementsByTag("td");

            // for (Element cell : cells) {
            // System.out.println(cell.text());
            // sb.append(cell.text()).append("|");
            // }

            sb.append(String.format(
                    "| %-5s | %-12s | %-14s |",
                    cells.get(0).text().replace("Group", "").trim(),
                    cells.get(1).text(),
                    cells.get(2).text()));
            sb.append("\n");
        }
        sb.append("```");

        return sb.toString();
    }

}
