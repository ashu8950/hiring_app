package com.example.onboarding.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.onboarding.dto.CandidateBankInfoDTO;
import com.example.onboarding.dto.CandidateDTO;
import com.example.onboarding.dto.CandidateEducationalInfoDTO;
import com.example.onboarding.dto.CandidatePersonalInfoDTO;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.CandidateBankInfo;
import com.example.onboarding.entity.CandidateEducationalInfo;
import com.example.onboarding.entity.CandidatePersonalInfo;
import com.example.onboarding.enums.CandidateStatus;
import com.example.onboarding.enums.OnboardStatus;
import com.example.onboarding.exception.ResourceNotFoundException;
import com.example.onboarding.mapper.CandidateMapper;
import com.example.onboarding.producer.JobOfferProducer;
import com.example.onboarding.repository.CandidateRepository;

@Service
public class CandidateService {

	private final CandidateRepository candidateRepository;
	private final FileStorageService fileStorageService;
	private final EmailService emailService;
	@Autowired
	private JobOfferProducer jobOfferProducer;

	public CandidateService(CandidateRepository candidateRepository, FileStorageService fileStorageService,
			EmailService emailService) {
		this.candidateRepository = candidateRepository;
		this.fileStorageService = fileStorageService;
		this.emailService = emailService;
	}

	// Create a new candidate
	public Candidate createCandidate(CandidateDTO candidateDTO) {
		Candidate candidate = new Candidate();
		candidate.setName(candidateDTO.getName());
		candidate.setEmail(candidateDTO.getEmail());
		candidate.setPhoneNumber(candidateDTO.getPhoneNumber());
		candidate.setStatus(candidateDTO.getStatus());
		candidate.setOnboardStatus(candidateDTO.getOnboardStatus());
		candidate.setDocumentPath(candidateDTO.getDocumentPath());
		candidate.setRole(candidateDTO.getRole());
		return candidateRepository.save(candidate);
	}

	// Delete candidate by ID
	public void deleteCandidate(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidateRepository.delete(candidate);
	}

	// Get hired candidates with pagination
	public Page<CandidateDTO> getHiredCandidates(int page, int size, String sortBy, String direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
		Page<Candidate> candidates = candidateRepository.findByStatus(CandidateStatus.OFFERED, pageable);
		return candidates.map(candidate -> CandidateMapper.toDTO(candidate));
	}

	// Get candidate details by ID
	public CandidateDTO getCandidateById(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
		return CandidateMapper.toDTO(candidate);
	}

	// Update candidate's status
	public Candidate updateCandidateStatus(Long id, CandidateStatus status) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidate.setStatus(status);
		return candidateRepository.save(candidate);
	}

	// Update candidate's onboard status
	public Candidate updateOnboardStatus(Long id, OnboardStatus status) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidate.setOnboardStatus(status);
		return candidateRepository.save(candidate);
	}

	// Upload document for a candidate
	public void uploadDocument(Long id, MultipartFile file) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		String fileName = fileStorageService.storeFile(file);
		candidate.setDocumentPath(fileName);
		candidateRepository.save(candidate);
	}

	// Verify document for a candidate
	public Candidate verifyDocument(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidate.setOnboardStatus(OnboardStatus.DOCUMENT_VERIFIED);
		return candidateRepository.save(candidate);
	}

	public CandidatePersonalInfo updatePersonalInfo(Long id, CandidatePersonalInfoDTO newInfo) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		// Retrieve the current personal info
		CandidatePersonalInfo info = candidate.getPersonalInfo();
		if (info == null) {
			info = new CandidatePersonalInfo();
			candidate.setPersonalInfo(info);
		}

		// Update the personal info fields
		info.setName(newInfo.getName());
		info.setEmail(newInfo.getEmail());
		info.setPhoneNumber(newInfo.getPhoneNumber());
		info.setDateOfBirth(newInfo.getDateOfBirth());

		// Update the main Candidate fields from the personal info
		candidate.setName(newInfo.getName());
		candidate.setEmail(newInfo.getEmail());
		candidate.setPhoneNumber(newInfo.getPhoneNumber());

		// Save both the updated Candidate and CandidatePersonalInfo
		candidateRepository.save(candidate);

		return info;
	}

	// Update candidate's bank info
	public CandidateBankInfo updateBankInfo(Long id, CandidateBankInfoDTO bankInfoDTO) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		// Retrieve the existing bank info for the candidate
		CandidateBankInfo existing = candidate.getBankInfo();
		if (existing == null) {
			existing = new CandidateBankInfo();
			candidate.setBankInfo(existing);
		}

		// Update the bank info fields
		existing.setAccountNumber(bankInfoDTO.getAccountNumber());
		existing.setBankName(bankInfoDTO.getBankName());
		existing.setIfscCode(bankInfoDTO.getIfscCode());

		// Ensure to save both the updated candidate and bank info
		candidateRepository.save(candidate);

		return existing;
	}

	// Update candidate's educational info
	public CandidateEducationalInfo updateEducationalInfo(Long id, CandidateEducationalInfoDTO educationalInfoDTO) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		// Retrieve the existing educational info for the candidate
		CandidateEducationalInfo existing = candidate.getEducationalInfo();
		if (existing == null) {
			existing = new CandidateEducationalInfo();
			candidate.setEducationalInfo(existing);
		}

		// Update the educational info fields
		existing.setHighestQualification(educationalInfoDTO.getHighestQualification());
		existing.setUniversity(educationalInfoDTO.getUniversity());
		existing.setGraduationYear(educationalInfoDTO.getGraduationYear());

		// Ensure that candidate reference is set for the educational info
		existing.setCandidate(candidate);

		// Save the candidate and educational info
		candidateRepository.save(candidate);

		return existing;
	}

	// Normal email notification (using emailService)
	public void sendJobOfferEmail(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		emailService.sendJobOfferEmail(candidate.getEmail()); // Normal email sending
	}

	// Job offer notification using RabbitMQ (separate from normal email)
	public void sendJobOfferNotificationViaRabbitMQ(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		jobOfferProducer.sendJobOffer(candidate.getId());
	}

	// Get total count of candidates
	public Long getCandidateCount() {
		return candidateRepository.count();
	}
}
