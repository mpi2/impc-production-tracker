package org.gentar.helpers;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvWriter<T>
{
    public void writeListToCsv(PrintWriter writer, List<? extends CsvRecord> data, String[] headers)
    {
        List<String[]> entries = new ArrayList<>();
        entries.add(headers);
        data.forEach(x -> entries.add(x.getRowAsArray()));
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeAll(entries);
    }

    public void csvWriterOneByOne(
        PrintWriter writer, List<? extends CsvRecord> data, String[] headers)
    {
        List<String[]> entries = new ArrayList<>();
        entries.add(headers);
        data.forEach(x -> {
            entries.add(x.getRowAsArray());
        });
        CSVWriter csvWriter = new CSVWriter(writer);
        entries.forEach(csvWriter::writeNext);
        writer.close();
    }

    public void csvWriterOneByOne(PrintWriter printWriter, String[] headers)
    {
        CSVWriter csvWriter = new CSVWriter(printWriter);
        csvWriter.writeNext(headers);
    }
}
