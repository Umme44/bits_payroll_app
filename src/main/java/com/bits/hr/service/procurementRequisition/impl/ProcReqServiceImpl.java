package com.bits.hr.service.procurementRequisition.impl;

import com.bits.hr.domain.ProcReq;
import com.bits.hr.repository.ProcReqRepository;
import com.bits.hr.service.dto.ProcReqDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.ProcurementRequisitionFileService;
import com.bits.hr.service.mapper.ProcReqMapper;
import com.bits.hr.service.procurementRequisition.ProcReqService;
import com.bits.hr.util.MimeTypeToFileType;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProcReq}.
 */
@Service
@Transactional
public class ProcReqServiceImpl implements ProcReqService {

    private final Logger log = LoggerFactory.getLogger(ProcReqServiceImpl.class);

    private final ProcReqRepository procReqRepository;

    private final ProcReqMapper procReqMapper;

    @Autowired
    private ProcurementRequisitionFileService procurementRequisitionFileService;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private MimeTypeToFileType mimeTypeToFileType;

    public ProcReqServiceImpl(ProcReqRepository procReqRepository, ProcReqMapper procReqMapper) {
        this.procReqRepository = procReqRepository;
        this.procReqMapper = procReqMapper;
    }

    @Override
    public ProcReqDTO save(ProcReqDTO procReqDTO) {
        log.debug("Request to save ProcReq : {}", procReqDTO);
        ProcReq procReq = procReqMapper.toEntity(procReqDTO);
        procReq = procReqRepository.save(procReq);
        return procReqMapper.toDto(procReq);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcReqDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcReqs");
        return procReqRepository.findAll(pageable).map(procReqMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcReqDTO> findOne(Long id) {
        log.debug("Request to get ProcReq : {}", id);
        return procReqRepository.findById(id).map(procReqMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProcReq : {}", id);
        procReqRepository.deleteById(id);
    }

    public List<ProcReq> saveAll(Collection<ProcReqDTO> procReqs, long procReqMasterId) {
        procReqs.forEach(procReqDTO -> {
            // file save
            if (procReqDTO.getReferenceFileData() != null && procReqDTO.getReferenceFileDataContentType() != null) {
                File savedFilePath = procurementRequisitionFileService.save(
                    procReqDTO.getReferenceFileData(),
                    procReqDTO.getReferenceFileDataContentType()
                );
                procReqDTO.setReferenceFilePath(savedFilePath.getAbsolutePath());
            }
            procReqDTO.setProcReqMasterId(procReqMasterId);
        });
        Set<ProcReq> procReqSet = procReqs.stream().map(procReqMapper::toEntity).collect(Collectors.toSet());
        return procReqRepository.saveAll(procReqSet);
    }

    @Override
    public void deleteAllProcReqByMasterId(long procReqMasterId, boolean willDeleteFiles) {
        List<ProcReq> procReqList = procReqRepository.findByProcReqMasterId(procReqMasterId);
        if (willDeleteFiles) {
            procReqList.forEach(procReq -> {
                if (procReq.getReferenceFilePath() != null) {
                    try {
                        fileOperationService.delete(procReq.getReferenceFilePath());
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                }
            });
        }
        procReqRepository.deleteAll(procReqList);
    }

    @Override
    public void updateFiles(Collection<ProcReqDTO> procReqDTOList) {
        // algo for update files
        // check file has not changed -> load old files by path, load mimeType by path -> update byte[] file data, content type to procReqDTO
        procReqDTOList.forEach(procReqDTO -> {
            if (procReqDTO.getReferenceFileData() == null && procReqDTO.getReferenceFilePath() != null) {
                boolean fileExits = fileOperationService.isExist(procReqDTO.getReferenceFilePath());
                if (fileExits) {
                    try {
                        procReqDTO.setReferenceFileData(fileOperationService.loadAsByte(procReqDTO.getReferenceFilePath()));
                        procReqDTO.setReferenceFileDataContentType(mimeTypeToFileType.getMimeType(procReqDTO.getReferenceFilePath()));
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        });
    }
}
