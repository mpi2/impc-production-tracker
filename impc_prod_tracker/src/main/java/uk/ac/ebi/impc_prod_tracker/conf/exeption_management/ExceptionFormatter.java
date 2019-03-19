/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package uk.ac.ebi.impc_prod_tracker.conf.exeption_management;

/**
 * Classes implementing this interface should receive an exception and provide an error and debug message from it,
 * ignoring unnecessary details.
 */
public interface ExceptionFormatter
{
    /**
     * Gets a formatted exception message.
     * @return String with formatted exception message.
     */
    String getMessage();

    /**
     * Gets a formatted exception debug message.
     * @return String with formatted exception debug message.
     */
    String getDebugMessage();
}
