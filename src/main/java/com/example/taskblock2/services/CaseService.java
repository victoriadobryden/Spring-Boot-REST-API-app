import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CaseService {
    @Autowired
    private CaseRepository caseRepository;

    public Case save(Case aCase) {
        return caseRepository.save(aCase);
    }

    public Case findById(Long id) {
        return caseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Case not found"));
    }

    public void deleteById(Long id) {
        caseRepository.deleteById(id);
    }

    public Page<Case> listCases(Long investigatorId, PageRequest pageRequest) {
        return caseRepository.findByInvestigatorId(investigatorId, pageRequest);
    }

    public byte[] generateReport(Long investigatorId) {
        return new byte[0]; // Placeholder
    }

    public ImportResult importCases(MultipartFile file) {
        return new ImportResult(); // Placeholder
    }
}
