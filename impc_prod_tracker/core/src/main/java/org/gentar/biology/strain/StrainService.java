package org.gentar.biology.strain;

public interface StrainService {

    Strain getStrainByName(String name);

    Strain getStrainById(Long id);
}
