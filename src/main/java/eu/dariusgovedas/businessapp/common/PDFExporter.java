package eu.dariusgovedas.businessapp.common;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.dariusgovedas.businessapp.clients.entities.ClientDTO;
import eu.dariusgovedas.businessapp.sales.entities.InvoiceDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderLineDTO;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PDFExporter {

    private final ClientDTO clientDTO;
    private final ClientDTO supplierDTO;
    private final OrderDTO orderDTO;
    private final List<OrderLineDTO> orderLineDTOS;

    public PDFExporter(InvoiceDTO invoiceDTO){
        this.clientDTO = invoiceDTO.getCustomer();
        this.supplierDTO = invoiceDTO.getSupplier();
        this.orderDTO = invoiceDTO.getOrder();
        this.orderLineDTOS = invoiceDTO.getOrderLines();

    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBackgroundColor(Color.white);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.black);

        cell.setPhrase(new Phrase("Supplier", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Client", font));
        table.addCell(cell);
    }

    private void writeClientSupplierDataTable(PdfPTable table){
        PdfPCell cell = table.getDefaultCell();
        cell.setBorder(0);

        table.addCell(supplierDTO.getBusinessName());
        table.addCell(clientDTO.getBusinessName());
        table.addCell(supplierDTO.getBusinessID().toString());
        table.addCell(clientDTO.getBusinessID().toString());
        table.addCell(supplierDTO.getCountry() + ", " + supplierDTO.getCity());
        table.addCell(clientDTO.getCountry() + ", " + clientDTO.getCity());
        table.addCell(supplierDTO.getStreetAddress());
        table.addCell(clientDTO.getStreetAddress());
    }

    private void writeOrderLinesTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBackgroundColor(Color.white);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.black);

        cell.setPhrase(new Phrase("Product", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Unit Price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total Price", font));
        table.addCell(cell);
    }

    private void writeOrderLinesDataTable(PdfPTable table){
        for(OrderLineDTO orderLineDTO : orderLineDTOS){
            table.addCell(orderLineDTO.getItemName());
            table.addCell(orderLineDTO.getOrderQuantity().toString());
            table.addCell(orderLineDTO.getPurchasePrice().toString());
            table.addCell(orderLineDTO.getLinePrice().toString());
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.black);

        Paragraph p = new Paragraph("INVOICE", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.0f, 1.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeClientSupplierDataTable(table);

        Paragraph p2 = new Paragraph("PRODUCTS", font);
        p2.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p2);

        PdfPTable dataTable = new PdfPTable(4);
        dataTable.setWidthPercentage(100f);
        dataTable.setWidths(new float[]{2.0f, 1.0f, 1.5f, 1.5f});
        dataTable.setSpacingBefore(10);

        writeOrderLinesTableHeader(dataTable);
        writeOrderLinesDataTable(dataTable);

        document.add(table);
        document.add(dataTable);

        document.close();
    }
}
