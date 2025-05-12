package com.example.onboarding.service.imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import com.example.onboarding.entity.CandidateDocument;
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

	@CacheEvict(value = { "candidateCount", "candidateReportData", "hiredCandidates" }, allEntries = true)
	public Candidate createCandidate(CandidateDTO candidateDTO) {
		Candidate candidate = new Candidate();
		candidate.setName(candidateDTO.getName());
		candidate.setEmail(candidateDTO.getEmail());
		candidate.setPhoneNumber(candidateDTO.getPhoneNumber());
		candidate.setStatus(candidateDTO.getStatus());
		candidate.setOnboardStatus(candidateDTO.getOnboardStatus());
		candidate.setDocumentPath(candidateDTO.getDocumentPath());
		candidate.setRole(candidateDTO.getRole());

		LocalDateTime now = LocalDateTime.now();
		candidate.setCreatedAt(now);
		candidate.setUpdatedAt(now);

		return candidateRepository.save(candidate);
	}

	@CacheEvict(value = { "candidate", "candidateCount", "candidateReportData", "hiredCandidates" }, allEntries = true)
	public void deleteCandidate(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidateRepository.delete(candidate);
	}

	@Cacheable(value = "hiredCandidates", key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction")
	public Page<CandidateDTO> getHiredCandidates(int page, int size, String sortBy, String direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
		Page<Candidate> candidates = candidateRepository.findByStatus(CandidateStatus.OFFERED, pageable);
		return candidates.map(CandidateMapper::toDTO);
	}

	@Cacheable(value = "candidate", key = "#id")
	public CandidateDTO getCandidateById(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
		return CandidateMapper.toDTO(candidate);
	}

	@CacheEvict(value = { "candidate", "hiredCandidates", "candidateReportData" }, allEntries = true)
	public Candidate updateCandidateStatus(Long id, CandidateStatus status) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidate.setStatus(status);
		return candidateRepository.save(candidate);
	}

	@CacheEvict(value = { "candidate", "hiredCandidates", "candidateReportData" }, allEntries = true)
	public Candidate updateOnboardStatus(Long id, OnboardStatus status) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidate.setOnboardStatus(status);
		return candidateRepository.save(candidate);
	}

	@CacheEvict(value = { "candidate" }, allEntries = true)
	public void uploadDocument(Long id, MultipartFile file) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		String fileName = fileStorageService.storeFile(file);

		CandidateDocument document = new CandidateDocument();
		document.setDocumentType(file.getOriginalFilename());
		document.setFileUrl(fileName);
		document.setVerified(false);
		document.setCandidate(candidate);

		candidate.setDocument(document);
		candidateRepository.save(candidate);
	}

	@CacheEvict(value = { "candidate", "candidateReportData" }, allEntries = true)
	public Candidate verifyDocument(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		CandidateDocument document = candidate.getDocument();
		if (document != null) {
			document.setVerified(true);
		}

		candidate.setOnboardStatus(OnboardStatus.DOCUMENT_VERIFIED);
		return candidateRepository.save(candidate);
	}

	@CacheEvict(value = { "candidate", "candidateReportData" }, allEntries = true)
	public CandidatePersonalInfo updatePersonalInfo(Long id, CandidatePersonalInfoDTO newInfo) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		CandidatePersonalInfo info = candidate.getPersonalInfo();
		if (info == null) {
			info = new CandidatePersonalInfo();
		}

		info.setName(newInfo.getName());
		info.setEmail(newInfo.getEmail());
		info.setPhoneNumber(newInfo.getPhoneNumber());
		info.setDateOfBirth(newInfo.getDateOfBirth());
		info.setGender(newInfo.getGender());
		info.setAddress(newInfo.getAddress());
		info.setNationality(newInfo.getNationality());

		info.setCandidate(candidate);
		candidate.setPersonalInfo(info);

		candidate.setName(newInfo.getName());
		candidate.setEmail(newInfo.getEmail());
		candidate.setPhoneNumber(newInfo.getPhoneNumber());

		candidateRepository.save(candidate);

		return info;
	}

	@CacheEvict(value = { "candidate", "candidateReportData" }, allEntries = true)
	public CandidateBankInfo updateBankInfo(Long id, CandidateBankInfoDTO bankInfoDTO) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		CandidateBankInfo existing = candidate.getBankInfo();
		if (existing == null) {
			existing = new CandidateBankInfo();
			candidate.setBankInfo(existing);
		}

		existing.setAccountNumber(bankInfoDTO.getAccountNumber());
		existing.setBankName(bankInfoDTO.getBankName());
		existing.setIfscCode(bankInfoDTO.getIfscCode());
		existing.setCandidate(candidate);

		candidateRepository.save(candidate);

		return existing;
	}

	@CacheEvict(value = { "candidate", "candidateReportData" }, allEntries = true)
	public CandidateEducationalInfo updateEducationalInfo(Long id, CandidateEducationalInfoDTO educationalInfoDTO) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		CandidateEducationalInfo existing = candidate.getEducationalInfo();
		if (existing == null) {
			existing = new CandidateEducationalInfo();
			candidate.setEducationalInfo(existing);
		}

		existing.setHighestQualification(educationalInfoDTO.getHighestQualification());
		existing.setUniversity(educationalInfoDTO.getUniversity());
		existing.setGraduationYear(educationalInfoDTO.getGraduationYear());
		existing.setCandidate(candidate);

		candidateRepository.save(candidate);

		return existing;
	}

	public void sendJobOfferEmail(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		emailService.sendJobOfferEmail(candidate.getEmail());
	}

	public void sendJobOfferNotificationViaRabbitMQ(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		jobOfferProducer.sendJobOffer(candidate.getId());
	}

	@Cacheable(value = "candidateCount")
	public Long getCandidateCount() {
		return candidateRepository.count();
	}

	@Cacheable(value = "candidateReportData")
	public List<CandidateDTO> getAllCandidateReportData() {
		List<Candidate> candidates = candidateRepository.findAll();
		return candidates.stream().map(CandidateMapper::toDTO).collect(Collectors.toList());
	}
}
