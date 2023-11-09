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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.toto.models.Draw;

@Service
public class WebscrapperService {

    @Value("${site.url}")
    private String url;

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

    public String getNextDrawInString(Draw draw) {
        DateTimeFormatter finalFormatter = DateTimeFormatter.ofPattern("EEEE d/MM/yyyy h:mma",
                Locale.ENGLISH);
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.US);

        String result = "$" +
                numberFormatter.format(draw.getValue()) +
                " " +
                draw.getDatetime().format(finalFormatter);
        return result;
    }

}
