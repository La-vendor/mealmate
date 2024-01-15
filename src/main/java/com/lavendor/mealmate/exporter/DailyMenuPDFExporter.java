package com.lavendor.mealmate.exporter;

import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.model.Recipe;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class DailyMenuPDFExporter {

    private final List<DailyMenu> dailyMenus;

    public DailyMenuPDFExporter(List<DailyMenu> dailyMenus) {
        this.dailyMenus = dailyMenus;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.decode("#B2DFFC"));
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Day", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Recipe", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        for(DailyMenu dailyMenu : dailyMenus){
            table.addCell(String.valueOf(dailyMenu.getDate().getDayOfWeek()));
            table.addCell(String.valueOf(dailyMenu.getDate()));

            StringBuilder recipesStringBuilder = new StringBuilder();
            for (Recipe recipe : dailyMenu.getRecipeList()) {
                recipesStringBuilder.append(recipe.getRecipeName()).append(", ");
            }
            String recipesString = recipesStringBuilder.toString().replaceAll(", $", "");

            table.addCell(recipesString);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(20);
        font.setColor(Color.BLACK);

        Paragraph p = new Paragraph("Daily Menu", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(3);
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }
}
