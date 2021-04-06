package org.gentar.biology.plan.attempt.crispr.guide;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class GuideValidator
{
    private static final String NULL_FIELD_ERROR = "%s cannot be null.";
    private static final String LENGTH_GUIDE_SEQUENCE = "%s needs to be at least 16bps.";
    private static final String START_STOP_POSITIVE_DIFFERENCE = "Stop guide location needs to be bigger " +
            "than start guide location.";
    private static final String SAME_TOTAL_LENGTH = "Error guides information. Sequence has a total length " +
            "of %sbps and the difference between start and stop plus one is %s. Please add the correct " +
            "coordinates.";
    private static final String SEQUENCE_ERROR = "Error guides information. Please enter the guide sequence " +
            "and the PAM";

    public GuideValidator () {

    }

    public void validateGuideData (Guide guide)
    {
        checkSequenceInformation(guide);
        sequenceValidation(guide);
        checkCoordinates(guide);
    }

    private void checkCoordinates(Guide guide)
    {
        if (guide.getStop() != null && guide.getStart() != null) {
            Integer diff = guide.getStop() - guide.getStart();
            if (diff < 0) {
                throw new UserOperationFailedException(START_STOP_POSITIVE_DIFFERENCE);
            }

            Integer totalLength = null;
            if (guide.getSequence() != null && !guide.getSequence().isEmpty())
            {
                totalLength = guide.getSequence().length();
            }
            else if ((guide.getGuideSequence() != null && guide.getPam() != null) &&
                    (!guide.getGuideSequence().isEmpty() && !guide.getPam().isEmpty()))
            {
                totalLength = guide.getGuideSequence().length() + guide.getPam().length();
            }

            Integer diff2 = diff + 1;
            if (diff2 != totalLength && totalLength != null) {
                throw new UserOperationFailedException(String.format(SAME_TOTAL_LENGTH,
                        totalLength.toString(), diff2.toString()));
            }
        } else {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, "Guide(s) start and stop fields"));
        }
    }

    private void sequenceValidation(Guide guide)
    {
        if (guide.getGuideSequence() != null && !guide.getGuideSequence().isEmpty())
        {
            String guideSequence = guide.getGuideSequence();
            if (guideSequence.length() < 16) {
                throw new UserOperationFailedException(String.format(LENGTH_GUIDE_SEQUENCE, "guide sequence"));
            }
        }
    }

    private void checkSequenceInformation(Guide guide)
    {
        String guideSequence = guide.getGuideSequence();
        String pam = guide.getPam();

        if (guideSequence == null && pam == null)
        {
            throw new UserOperationFailedException(SEQUENCE_ERROR);
        }
    }
}
