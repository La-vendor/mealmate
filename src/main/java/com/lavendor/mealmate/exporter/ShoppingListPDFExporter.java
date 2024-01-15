package com.lavendor.mealmate.exporter;

import com.lavendor.mealmate.model.BasicIngredient;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class ShoppingListPDFExporter {

    private final Map<BasicIngredient, Double> ingredientQuantityMap;

    public ShoppingListPDFExporter(Map<BasicIngredient, Double> ingredientQuantityMap) {
        this.ingredientQuantityMap = ingredientQuantityMap;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.decode("#B2DFFC"));
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Ingredient name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        for(Map.Entry<BasicIngredient, Double> entry : ingredientQuantityMap.entrySet()){
            table.addCell(entry.getKey().getBasicIngredientName());
            table.addCell(entry.getValue() + " " + entry.getKey().getUnit());
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
         document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(20);
        font.setColor(Color.BLACK);

        Paragraph p = new Paragraph("Shopping List", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }
}
