package org.gentar.biology.plan.attempt.crispr.guide;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class GuideValidator {
    private static final String NULL_FIELD_ERROR = "%s cannot be null.";
    private static final String LENGTH_GUIDE_SEQUENCE = "%s needs to be at least 16bps.";
    private static final String START_STOP_POSITIVE_DIFFERENCE = "Stop guide location needs to be bigger than start guide location.";
    private static final String SAME_TOTAL_LENGTH = "Error guides information. Sequence has a total length of %sbps and the difference between start and stop plus one is %s. Please add the correct coordinates.";
    private static final String SEQUENCE_ERROR = "Error guides information. Please enter the valid guide sequence.";
    private static final String PAM_ERROR = "Error guides information. Please enter the valid Pam.";
    private static final LocalDateTime VALIDATION_DATE = LocalDateTime.of(2024, 9, 1, 0, 0);

    public GuideValidator() {
    }

    public void validateGuideData(Set<Guide> guides) {

        checkNullFields(guides);
        sequenceValidation(guides);
        checkSequenceInformation(guides);
        checkCoordinates(guides);
    }

    private void checkNullFields(Set<Guide> guides) {
        String commonChr = null;

        for (Guide guide : guides) {
            if (!isOldGuide(guide.getId())) {
                    validateNotNullField(guide.getGuideSequence(), "Guide Sequence in the guide");
                validateNotNullField(guide.getPam(), "Pam in the guide");
                validateNotNullField(guide.getChr(), "Chromosome in the guide");
                validateNotNullField(guide.getStart(), "Guide start field");
                validateNotNullField(guide.getStop(), "Guide stop field");
                validateNotNullField(guide.getStrand(), "Strand in the guide");
                validateNotNullField(guide.getGenomeBuild(), "Genome Build in the guide");

                if (commonChr == null) {
                    commonChr = guide.getChr();
                } else if (!commonChr.equals(guide.getChr())) {
                    throw new UserOperationFailedException("All guides must have the same chromosome.");
                }

            }
        }
    }


    private void checkCoordinates(Set<Guide> guides) {
        guides.forEach(guide -> {
            if (!isOldGuide(guide.getId())) {
                int diff = guide.getStop() - guide.getStart();
                if (diff < 0) {
                    throw new UserOperationFailedException(START_STOP_POSITIVE_DIFFERENCE);
                }

                Integer totalLength = calculateTotalLength(guide);
                if (totalLength != null && (diff + 1) != totalLength) {
                    throw new UserOperationFailedException(String.format(SAME_TOTAL_LENGTH, totalLength, diff + 1));
                }
            }
        });

    }

    private void sequenceValidation(Set<Guide> guides) {
        guides.forEach(guide -> {
            String guideSequence = guide.getGuideSequence();
            if (!isOldGuide(guide.getId()) && guideSequence != null && guideSequence.length() < 16) {
                throw new UserOperationFailedException(String.format(LENGTH_GUIDE_SEQUENCE, "Guide sequence"));
            }
        });
    }

    private void checkSequenceInformation(Set<Guide> guides) {


        guides.forEach(guide -> {

            if (!isOldGuide(guide.getId()) && !isValidSequence(guide.getGuideSequence())) {
                throw new UserOperationFailedException(SEQUENCE_ERROR);
            }
            if (!isOldGuide(guide.getId()) && !isValidSequence(guide.getPam())) {
                throw new UserOperationFailedException(PAM_ERROR);
            }
        });
    }


    private void validateNotNullField(Object field, String fieldName) {
        if (field == null || field.toString().trim().isEmpty()) {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, fieldName));
        }
    }


    private boolean isOldGuide(Long id) {
        return id != null && id < 38453;

    }

    private Integer calculateTotalLength(Guide guide) {
        if (guide.getSequence() != null && !guide.getSequence().isEmpty()) {
            return guide.getSequence().length();
        } else if (guide.getGuideSequence() != null && guide.getPam() != null &&
                !guide.getGuideSequence().isEmpty() && !guide.getPam().isEmpty()) {
            return guide.getGuideSequence().length() + guide.getPam().length();
        }
        return null;
    }

    private Boolean isValidSequence(String sequence) {


        for (int i = 0; i < sequence.length(); i++) {
            if (!dnaCharacters().contains(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    private List<Character> dnaCharacters() {
        return List
                .of('A', 'C', 'G', 'T', 'U', 'I', 'R', 'Y', 'K', 'M', 'S', 'W', 'B', 'D', 'H', 'V',
                        'N', 'a', 'c', 'g', 't', 'u', 'i', 'r', 'y', 'k', 'm', 's', 'w', 'b', 'd', 'h', 'v',
                        'n', '-');
    }

}