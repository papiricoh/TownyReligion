package org.papiricoh.townyreligion.object.book;

import java.util.ArrayList;
import java.util.List;

public class BookUtils {
    private static final int CHAR_LIMIT_PER_LINE = 19; // Aproximado, dependiendo de la longitud exacta de los caracteres.
    private static final int LINES_PER_PAGE = 14; // Número de líneas en una página de libro.

    public static String replaceGodNameInContent(String content, String godName) {
        return content.replace("<god>", godName);
    }
    private static List<String> splitContentIntoPages(String content) {
        List<String> pages = new ArrayList<>();
        StringBuilder pageContent = new StringBuilder();
        int linesOnCurrentPage = 0;

        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.length() <= CHAR_LIMIT_PER_LINE) {
                if (linesOnCurrentPage + 1 > LINES_PER_PAGE) {
                    pages.add(pageContent.toString());
                    pageContent.setLength(0);
                    linesOnCurrentPage = 0;
                }
                pageContent.append(line).append("\n");
                linesOnCurrentPage++;
            } else {
                String[] words = line.split(" ");
                StringBuilder lineContent = new StringBuilder();
                for (String word : words) {
                    if (lineContent.length() + word.length() > CHAR_LIMIT_PER_LINE) {
                        if (linesOnCurrentPage + 1 > LINES_PER_PAGE) {
                            pages.add(pageContent.toString());
                            pageContent.setLength(0);
                            linesOnCurrentPage = 0;
                        }
                        pageContent.append(lineContent.toString()).append("\n");
                        lineContent.setLength(0);
                        linesOnCurrentPage++;
                    }
                    lineContent.append(word).append(" ");
                }
                if (lineContent.length() > 0) {
                    if (linesOnCurrentPage + 1 > LINES_PER_PAGE) {
                        pages.add(pageContent.toString());
                        pageContent.setLength(0);
                        linesOnCurrentPage = 0;
                    }
                    pageContent.append(lineContent.toString()).append("\n");
                    linesOnCurrentPage++;
                }
            }
        }

        if (pageContent.length() > 0) {
            pages.add(pageContent.toString());
        }

        return pages;
    }
}