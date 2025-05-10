package com.example.onboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.onboarding.dto.ApiResponse;
import com.example.onboarding.dto.CandidateBankInfoDTO;
import com.example.onboarding.dto.CandidateDTO;
import com.example.onboarding.dto.CandidateDocumentDTO;
import com.example.onboarding.dto.CandidateEducationalInfoDTO;
import com.example.onboarding.dto.CandidatePersonalInfoDTO;
import com.example.onboarding.dto.ErrorResponse;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.CandidateBankInfo;
import com.example.onboarding.entity.CandidateEducationalInfo;
import com.example.onboarding.entity.CandidatePersonalInfo;
import com.example.onboarding.enums.CandidateStatus;
import com.example.onboarding.enums.OnboardStatus;
import com.example.onboarding.mapper.CandidateBankInfoMapper;
import com.example.onboarding.mapper.CandidateDocumentMapper;
import com.example.onboarding.mapper.CandidateEducationalInfoMapper;
import com.example.onboarding.mapper.CandidateMapper;
import com.example.onboarding.mapper.CandidatePersonalInfoMapper;
import com.example.onboarding.service.imp.CandidateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

	@Autowired
	private CandidateService candidateService;

	@PostMapping("/")
	public ResponseEntity<ApiResponse<CandidateDTO>> createCandidate(@Valid @RequestBody CandidateDTO candidateDTO) {
		Candidate createdCandidate = candidateService.createCandidate(candidateDTO);
		CandidateDTO responseDTO = CandidateMapper.toDTO(createdCandidate);
		ApiResponse<CandidateDTO> response = new ApiResponse<>(true, "Candidate created successfully", responseDTO);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCandidate(@PathVariable Long id) {
		candidateService.deleteCandidate(id);
		ApiResponse<String> response = new ApiResponse<>(true, "Candidate deleted successfully", null);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/hired")
	public ResponseEntity<Object> getHiredCandidates(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		var candidates = candidateService.getHiredCandidates(page, size, sortBy, direction);
		if (candidates.isEmpty()) {
			ErrorResponse errorResponse = new ErrorResponse("No hired candidates found.",
					"There are no candidates with the 'OFFERED' status.");
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}
		ApiResponse<Object> response = new ApiResponse<>(true, "Hired candidates fetched successfully", candidates);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CandidateDTO>> getCandidateById(@PathVariable Long id) {
		CandidateDTO responseDTO = candidateService.getCandidateById(id);
		ApiResponse<CandidateDTO> response = new ApiResponse<>(true, "Candidate retrieved successfully", responseDTO);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/count")
	public ResponseEntity<ApiResponse<Long>> getCandidateCount() {
		long count = candidateService.getCandidateCount();
		ApiResponse<Long> response = new ApiResponse<>(true, "Candidate count fetched", count);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{id}/status")
	public ResponseEntity<ApiResponse<CandidateDTO>> updateStatus(@PathVariable Long id,
			@RequestBody CandidateStatus status) {
		Candidate updated = candidateService.updateCandidateStatus(id, status);
		CandidateDTO responseDTO = CandidateMapper.toDTO(updated);
		ApiResponse<CandidateDTO> response = new ApiResponse<>(true, "Candidate status updated", responseDTO);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/onboard-status")
	public ResponseEntity<ApiResponse<CandidateDTO>> updateOnboardingStatus(@PathVariable Long id,
			@RequestBody OnboardStatus status) {
		Candidate updated = candidateService.updateOnboardStatus(id, status);
		CandidateDTO responseDTO = CandidateMapper.toDTO(updated);
		ApiResponse<CandidateDTO> response = new ApiResponse<>(true, "Onboarding status updated", responseDTO);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{id}/notify-job-offer")
	public ResponseEntity<ApiResponse<String>> notifyJobOffer(@PathVariable Long id) {
		candidateService.sendJobOfferEmail(id);
		ApiResponse<String> response = new ApiResponse<>(true, "Job offer notification sent successfully", null);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{id}/notify-job-offer-rabbit")
	public ResponseEntity<ApiResponse<String>> notifyJobOfferRabbit(@PathVariable Long id) {
		candidateService.sendJobOfferNotificationViaRabbitMQ(id);
		ApiResponse<String> response = new ApiResponse<>(true, "Job offer notification queued", null);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/personal-info")
	public ResponseEntity<ApiResponse<CandidatePersonalInfoDTO>> updatePersonalInfo(@PathVariable Long id,
			@Valid @RequestBody CandidatePersonalInfoDTO personalInfoDTO) {
		CandidatePersonalInfo updated = candidateService.updatePersonalInfo(id, personalInfoDTO);
		CandidatePersonalInfoDTO responseDTO = CandidatePersonalInfoMapper.toDTO(updated);
		ApiResponse<CandidatePersonalInfoDTO> response = new ApiResponse<>(true, "Personal info updated", responseDTO);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/bank-info")
	public ResponseEntity<ApiResponse<CandidateBankInfoDTO>> updateBankInfo(@PathVariable Long id,
			@Valid @RequestBody CandidateBankInfoDTO bankInfoDTO) {
		CandidateBankInfo updated = candidateService.updateBankInfo(id, bankInfoDTO);
		CandidateBankInfoDTO responseDTO = CandidateBankInfoMapper.toDTO(updated);
		ApiResponse<CandidateBankInfoDTO> response = new ApiResponse<>(true, "Bank info updated", responseDTO);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/educational-info")
	public ResponseEntity<ApiResponse<CandidateEducationalInfoDTO>> updateEducationalInfo(@PathVariable Long id,
			@Valid @RequestBody CandidateEducationalInfoDTO educationalInfoDTO) {
		CandidateEducationalInfo updated = candidateService.updateEducationalInfo(id, educationalInfoDTO);
		CandidateEducationalInfoDTO responseDTO = CandidateEducationalInfoMapper.toDTO(updated);
		ApiResponse<CandidateEducationalInfoDTO> response = new ApiResponse<>(true, "Educational info updated",
				responseDTO);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{id}/upload-document")
	public ResponseEntity<ApiResponse<String>> uploadDocument(@PathVariable Long id,
			@RequestParam("file") MultipartFile file) {
		candidateService.uploadDocument(id, file);
		ApiResponse<String> response = new ApiResponse<>(true, "Document uploaded successfully", null);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/verify-document")
	public ResponseEntity<ApiResponse<CandidateDocumentDTO>> verifyDocument(@PathVariable Long id) {
		Candidate verifiedDocument = candidateService.verifyDocument(id); // Assuming it returns
																			// CandidateDocument
		CandidateDocumentDTO responseDTO = CandidateDocumentMapper.toDTO(verifiedDocument); // Corrected to map
																							// CandidateDocument
		ApiResponse<CandidateDocumentDTO> response = new ApiResponse<>(true, "Document verified successfully",
				responseDTO);
		return ResponseEntity.ok(response);
	}

}
