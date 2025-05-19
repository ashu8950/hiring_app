package com.example.onboarding.service.imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import com.example.onboarding.repository.CandidateBankInfoRepository;
import com.example.onboarding.repository.CandidateDocumentRepository;
import com.example.onboarding.repository.CandidateEducationalInfoRepository;
import com.example.onboarding.repository.CandidateRepository;

@Service
public class CandidateService {

	private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

	private final CandidateRepository candidateRepository;
	private final CandidateBankInfoRepository candidateBankInfoRepository;
	private final CandidateEducationalInfoRepository candidateEducationalInfoRepository;
	private final CandidateDocumentRepository candidateDocumentRepository;
	private final FileStorageService fileStorageService;
	private final EmailService emailService;
	private final JobOfferProducer jobOfferProducer;
	private final CacheManager cacheManager;

	public CandidateService(CandidateRepository candidateRepository,
			CandidateEducationalInfoRepository candidateEducationalInfoRepository,
			CandidateBankInfoRepository candidateBankInfoRepository,
			CandidateDocumentRepository candidateDocumentRepository, FileStorageService fileStorageService,
			EmailService emailService, CacheManager cacheManager, JobOfferProducer jobOfferProducer) {
		this.candidateRepository = candidateRepository;
		this.candidateEducationalInfoRepository = candidateEducationalInfoRepository;
		this.candidateBankInfoRepository = candidateBankInfoRepository;
		this.candidateDocumentRepository = candidateDocumentRepository;
		this.fileStorageService = fileStorageService;
		this.emailService = emailService;
		this.cacheManager = cacheManager;
		this.jobOfferProducer = jobOfferProducer;
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

	@CacheEvict(value = { "candidate", "candidateCount", "candidateReportData", "hiredCandidates" }, key = "#id")
	@Transactional
	public void deleteCandidate(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		candidateRepository.delete(candidate);
	}

	@Cacheable(value = "hiredCandidates", key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction")
	public Page<CandidateDTO> getHiredCandidates(int page, int size, String sortBy, String direction) {
		logger.info("Cache miss: Fetching hired candidates from DB - page={}, size={}, sortBy={}, direction={}", page,
				size, sortBy, direction);
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
		Page<Candidate> candidates = candidateRepository.findByStatus(CandidateStatus.OFFERED, pageable);
		return candidates.map(CandidateMapper::toDTO);
	}

	@Cacheable(value = "candidate", key = "#id")
	public CandidateDTO getCandidateById(Long id) {
		logger.info("Cache miss: Fetching candidate with ID {} from DB", id);
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
		return CandidateMapper.toDTO(candidate);
	}

	@CacheEvict(value = { "candidate", "hiredCandidates", "candidateReportData" }, key = "#id")
	public Candidate updateCandidateStatus(Long id, CandidateStatus status) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidate.setStatus(status);
		candidate.setUpdatedAt(LocalDateTime.now());
		return candidateRepository.save(candidate);
	}

	@CacheEvict(value = { "candidate", "hiredCandidates", "candidateReportData" }, key = "#id")
	public Candidate updateOnboardStatus(Long id, OnboardStatus status) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
		candidate.setOnboardStatus(status);
		candidate.setUpdatedAt(LocalDateTime.now());
		return candidateRepository.save(candidate);
	}

	@Transactional
	@CacheEvict(value = { "candidate" }, key = "#id")
	public void uploadDocument(Long id, MultipartFile file) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		CandidateDocument existingDocument = candidateDocumentRepository.findByCandidateId(candidate.getId());

		if (existingDocument != null) {
			fileStorageService.deleteFile(existingDocument.getFileUrl());
			candidateDocumentRepository.delete(existingDocument);
		}

		String originalName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		String extension = "";

		int dotIndex = originalName.lastIndexOf(".");
		if (dotIndex > 0) {
			extension = originalName.substring(dotIndex);
		}

		String uniqueFileName = UUID.randomUUID().toString() + extension;

		String newFileName = fileStorageService.storeFile(file, uniqueFileName);

		CandidateDocument newDocument = new CandidateDocument();
		newDocument.setDocumentType(originalName);
		newDocument.setFileUrl(newFileName);
		newDocument.setVerified(false);
		newDocument.setCandidate(candidate);

		candidateDocumentRepository.save(newDocument);

		candidate.setDocument(newDocument);
		candidate.setUpdatedAt(LocalDateTime.now());
		candidateRepository.save(candidate);
	}

	@CacheEvict(value = { "candidate", "candidateReportData" }, key = "#id")
	public Candidate verifyDocument(Long id) {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		CandidateDocument document = candidate.getDocument();
		if (document != null) {
			document.setVerified(true);
			candidateDocumentRepository.save(document);
		}

		candidate.setOnboardStatus(OnboardStatus.DOCUMENT_VERIFIED);
		candidate.setUpdatedAt(LocalDateTime.now());
		return candidateRepository.save(candidate);
	}

	@CacheEvict(value = { "candidate", "candidateReportData" }, key = "#id")
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
		candidate.setUpdatedAt(LocalDateTime.now());

		candidateRepository.save(candidate);
		return info;
	}

	@CacheEvict(value = { "candidate", "candidateReportData" }, key = "#id")
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

		candidate.setUpdatedAt(LocalDateTime.now());
		candidateRepository.save(candidate);
		return existing;
	}

	@CacheEvict(value = { "candidate", "candidateReportData" }, key = "#id")
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

		candidate.setUpdatedAt(LocalDateTime.now());
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
		logger.info("Cache miss: Fetching candidate count from DB");
		return candidateRepository.count();
	}

	@Cacheable(value = "candidateReportData")
	public List<CandidateDTO> getAllCandidateReportData() {
		logger.info("Cache miss: Fetching all candidate report data from DB");
		List<Candidate> candidates = candidateRepository.findAll();
		return candidates.stream().map(CandidateMapper::toDTO).collect(Collectors.toList());
	}
}
