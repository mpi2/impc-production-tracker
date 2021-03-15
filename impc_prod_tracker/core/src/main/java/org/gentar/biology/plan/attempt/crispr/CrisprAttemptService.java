/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.plan.attempt.crispr;

import org.gentar.biology.plan.attempt.crispr.guide.GuideFormat;
import org.gentar.biology.plan.attempt.crispr.guide.GuideSource;
import org.gentar.biology.plan.attempt.crispr.nuclease.Nuclease;
import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.MutagenesisDonor;
import org.gentar.biology.plan.attempt.crispr.assay.AssayType;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_class.NucleaseClass;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_type.NucleaseType;
import java.util.List;
import java.util.Optional;

public interface CrisprAttemptService
{
    Optional<CrisprAttempt> getCrisprAttemptById(Long planId);

    List<GenotypePrimer> getGenotypePrimersByCrisprAttempt(CrisprAttempt crisprAttempt);

    List<Nuclease> getNucleasesByCrisprAttempt(CrisprAttempt crisprAttempt);

    List<MutagenesisDonor> getMutagenesisDonorsByCrisprAttempt(CrisprAttempt crisprAttempt);

    AssayType getAssayTypeByName(String assayTypeName);

    NucleaseType getNucleaseTypeByName(String nucleaseTypeName);

    NucleaseClass getNucleaseClassByName(String nucleaseClassName);

    GuideFormat getGuideFormatByName(String guideFormatName);

    GuideSource getGuideSourceByName(String guideSourceName);
}
