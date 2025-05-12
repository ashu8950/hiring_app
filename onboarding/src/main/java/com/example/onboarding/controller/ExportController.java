package com.example.onboarding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onboarding.dto.CandidateDTO;
import com.example.onboarding.service.imp.CandidateService;
import com.example.onboarding.service.imp.ReportService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ExportController {

	@Autowired
	private CandidateService candidateService;
	@Autowired
	private ReportService reportService;

	@GetMapping("/export/pdf")
	public void exportPdf(HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=onboarding_report.pdf");

		List<CandidateDTO> candidates = candidateService.getAllCandidateReportData();
		reportService.exportCandidateReport(candidates, response);
	}

}
