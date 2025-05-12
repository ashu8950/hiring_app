package com.example.onboarding.service.imp;

import java.awt.Color;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.onboarding.dto.CandidateBankInfoDTO;
import com.example.onboarding.dto.CandidateDTO;
import com.example.onboarding.dto.CandidateDocumentDTO;
import com.example.onboarding.dto.CandidateEducationalInfoDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportService {

	public void exportCandidateReport(List<CandidateDTO> candidates, HttpServletResponse response) {
		try {
			Document document = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance(document, response.getOutputStream());

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=candidate_report.pdf");

			document.open();

			// Title
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
			Paragraph title = new Paragraph("Candidate Onboarding Report", titleFont);
			title.setAlignment(Paragraph.ALIGN_CENTER);
			title.setSpacingAfter(20);
			document.add(title);

			// Table with 10 columns
			PdfPTable table = new PdfPTable(10);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10);

			// Headers
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			String[] headers = { "ID", "Name", "Email", "Phone", "Status", "Onboard Status", "Role", "Bank Info",
					"Education Info", "Document Uploaded" };

			for (String header : headers) {
				PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
			}

			// Data rows
			for (CandidateDTO c : candidates) {
				table.addCell(String.valueOf(c.getId()));
				table.addCell(c.getName());
				table.addCell(c.getEmail());
				table.addCell(c.getPhoneNumber());
				table.addCell(c.getStatus() != null ? c.getStatus().name() : "-");
				table.addCell(c.getOnboardStatus() != null ? c.getOnboardStatus().name() : "-");
				table.addCell(c.getRole() != null ? c.getRole().name() : "-");

				// Enhanced checks with null safety for logging
				CandidateBankInfoDTO bankInfo = c.getBankInfo();
				CandidateEducationalInfoDTO eduInfo = c.getEducationalInfo();
				CandidateDocumentDTO documentInfo = c.getDocument();

				boolean hasBankInfo = bankInfo != null && bankInfo.getAccountNumber() != null
						&& !bankInfo.getAccountNumber().isEmpty();
				boolean hasEducationInfo = eduInfo != null && eduInfo.getUniversity() != null
						&& !eduInfo.getUniversity().isEmpty();
				boolean hasDocument = documentInfo != null && documentInfo.getFileUrl() != null
						&& !documentInfo.getFileUrl().isEmpty();

				table.addCell(hasBankInfo ? "Data Available" : "Data Missing");
				table.addCell(hasEducationInfo ? "Data Available" : "Data Missing");
				table.addCell(hasDocument ? "Data Available" : "Data Missing");

			}

			document.add(table);
			document.close();

		} catch (Exception e) {
			throw new RuntimeException("Failed to generate PDF report", e);
		}
	}
}
