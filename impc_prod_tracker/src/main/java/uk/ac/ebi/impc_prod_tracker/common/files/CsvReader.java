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
package uk.ac.ebi.impc_prod_tracker.common.files;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CsvReader
{
    public List<List<String>> getCsvContentFromMultipartFile(MultipartFile file)
    {
        List<String[]> csvOutput;
        List<List<String>> result = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is)))
        {
            csvOutput = readAll(br);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new UserOperationFailedException(
                "CSV file could not be read/parsed: " + e.getMessage());
        }
        csvOutput.forEach(element -> {
            List<String> rowElements = Arrays.asList(element);
            result.add(rowElements);
        });
        return result;
    }

    private List<String[]> readAll(Reader reader) throws Exception
    {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }
}
