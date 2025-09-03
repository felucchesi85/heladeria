package com.heladeria.sistema.service;

import com.heladeria.sistema.model.Cliente;
import com.heladeria.sistema.model.DetalleFactura;
import com.heladeria.sistema.model.Factura;
import com.heladeria.sistema.repository.FacturaRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfFacturaService {

    private final FacturaRepository facturaRepository;
    private final ResourceLoader resourceLoader;

    @Value("${heladeria.empresa.nombre:Heladería}")
    private String empresaNombre;

    @Value("${heladeria.empresa.direccion:-}")
    private String empresaDireccion;

    @Value("${heladeria.empresa.cuit:-}")
    private String empresaCuit;

    // Soporta classpath:/, file:/ o URL absoluta
    @Value("${heladeria.empresa.logo:}")
    private String logoPath;

    public PdfFacturaService(FacturaRepository facturaRepository, ResourceLoader resourceLoader) {
        this.facturaRepository = facturaRepository;
        this.resourceLoader = resourceLoader;
    }

    @Transactional(readOnly = true)
    public byte[] generarFacturaPdf(Long facturaId) throws Exception {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + facturaId));
        // Asegurar carga de colecciones perezosas dentro de la transacción
        factura.getDetalles().size();
        return generarFacturaPdf(factura);
    }

    public byte[] generarFacturaPdf(Factura factura) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(doc, out);
        doc.open();

        // Fuentes
        Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font fSub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fTexto = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font fPeq = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Font fTablaHead = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        // Encabezado (logo + datos empresa + nro)
        PdfPTable header = new PdfPTable(new float[]{2, 5, 3});
        header.setWidthPercentage(100);

        PdfPCell cLogo = new PdfPCell();
        cLogo.setBorder(Rectangle.NO_BORDER);
        Image logo = cargarLogo();
        if (logo != null) {
            logo.scaleToFit(120, 60);
            cLogo.addElement(logo);
        }
        header.addCell(cLogo);

        PdfPCell cEmpresa = new PdfPCell();
        cEmpresa.setBorder(Rectangle.NO_BORDER);
        cEmpresa.addElement(new Paragraph(empresaNombre, fTitulo));
        cEmpresa.addElement(new Paragraph(empresaDireccion, fTexto));
        cEmpresa.addElement(new Paragraph("CUIT: " + empresaCuit, fTexto));
        header.addCell(cEmpresa);

        PdfPCell cFactura = new PdfPCell();
        cFactura.setBorder(Rectangle.NO_BORDER);
        cFactura.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cFactura.addElement(new Paragraph("FACTURA", fSub));
        cFactura.addElement(new Paragraph("N° " + nullSafe(factura.getNumero()), fTexto));
        cFactura.addElement(new Paragraph("Fecha: " + factura.getFecha().format(DateTimeFormatter.ISO_DATE), fTexto));
        header.addCell(cFactura);

        doc.add(header);
        doc.add(Chunk.NEWLINE);

        // Datos del cliente
        Cliente cli = factura.getCliente();
        PdfPTable tCliente = new PdfPTable(new float[]{1, 3});
        tCliente.setWidthPercentage(100);
        tCliente.addCell(cellLabel("Cliente:", fSub));
        tCliente.addCell(cellValue(cli != null ? cli.getNombre() : "-", fTexto));
        tCliente.addCell(cellLabel("Dirección:", fSub));
        tCliente.addCell(cellValue(cli != null ? nullSafe(cli.getDireccion()) : "-", fTexto));
        tCliente.addCell(cellLabel("Teléfono:", fSub));
        tCliente.addCell(cellValue(cli != null ? nullSafe(cli.getTelefono()) : "-", fTexto));
        doc.add(tCliente);
        doc.add(Chunk.NEWLINE);

        PdfPTable t = new PdfPTable(new float[]{5, 1.5f, 2, 2});
        t.setWidthPercentage(100);
        Color gris = new Color(240, 240, 240);

        addHead(t, "Producto", gris, fTablaHead);
        addHead(t, "Producto", gris, fTablaHead);
        addHead(t, "Cant.", gris, fTablaHead);
        addHead(t, "Precio", gris, fTablaHead);
        addHead(t, "Subtotal", gris, fTablaHead);

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        List<DetalleFactura> detalles = factura.getDetalles();
        if (detalles != null) {
            for (DetalleFactura d : detalles) {
                t.addCell(cellValue(d.getProducto() != null ? d.getProducto().getNombre() : "-", fTexto));
                PdfPCell cCant = cellValue(d.getCantidad() != null ? String.valueOf(d.getCantidad()) : "0", fTexto);
                cCant.setHorizontalAlignment(Element.ALIGN_RIGHT);
                t.addCell(cCant);

                PdfPCell cPU = cellValue(nf.format(nullSafeBD(d.getPrecioUnitario())), fTexto);
                cPU.setHorizontalAlignment(Element.ALIGN_RIGHT);
                t.addCell(cPU);

                PdfPCell cSub = cellValue(nf.format(nullSafeBD(d.getSubtotal())), fTexto);
                cSub.setHorizontalAlignment(Element.ALIGN_RIGHT);
                t.addCell(cSub);
            }
        }
        doc.add(t);

        // Total
        PdfPTable tTotal = new PdfPTable(new float[]{7.5f, 2.5f});
        tTotal.setWidthPercentage(100);
        PdfPCell vacio = new PdfPCell(new Phrase(""));
        vacio.setBorder(Rectangle.NO_BORDER);
        tTotal.addCell(vacio);

        PdfPTable boxTotal = new PdfPTable(2);
        boxTotal.setWidthPercentage(100);
        PdfPCell lbl = new PdfPCell(new Phrase("TOTAL", fSub));
        lbl.setHorizontalAlignment(Element.ALIGN_RIGHT);
        lbl.setBackgroundColor(gris);
        PdfPCell val = new PdfPCell(new Phrase(nf.format(nullSafeBD(factura.getTotal())), fSub));
        val.setHorizontalAlignment(Element.ALIGN_RIGHT);
        boxTotal.addCell(lbl);
        boxTotal.addCell(val);

        PdfPCell totalCell = new PdfPCell(boxTotal);
        totalCell.setBorder(Rectangle.NO_BORDER);
        tTotal.addCell(totalCell);

        doc.add(Chunk.NEWLINE);
        doc.add(tTotal);

        // Pie
        doc.add(Chunk.NEWLINE);
        Paragraph pPie = new Paragraph("Gracias por su compra.", fPeq);
        pPie.setAlignment(Element.ALIGN_CENTER);
        doc.add(pPie);

        doc.close();
        return out.toByteArray();
    }

    private Image cargarLogo() {
        try {
            if (logoPath == null || logoPath.isBlank()) return null;
            Resource r = resourceLoader.getResource(logoPath);
            if (!r.exists()) return null;
            URL url = r.getURL();
            return Image.getInstance(url);
        } catch (IOException | BadElementException e) {
            return null;
        }
    }

    private static PdfPCell cellLabel(String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }
    private static PdfPCell cellValue(String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setBorder(Rectangle.NO_BORDER);
    return c;
    }

    private static void addHead(PdfPTable table, String text, Color bg, Font font) {
        PdfPCell headerCell = new PdfPCell(new Phrase(text, font));
        headerCell.setBackgroundColor(bg);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(headerCell);
    }

    private static String nullSafe(String s) {
        return s == null ? "-" : s;
    }

    private static java.math.BigDecimal nullSafeBD(java.math.BigDecimal bd) {
        return bd == null ? java.math.BigDecimal.ZERO : bd;
    }
}
    