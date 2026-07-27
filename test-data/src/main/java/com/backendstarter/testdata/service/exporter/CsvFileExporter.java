package com.backendstarter.testdata.service.exporter;

import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.dto.SchemaFieldDto;
import com.backendstarter.testdata.dto.TableSchemaDto;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class CsvFileExporter extends DelimiterBasedFileExporter implements MockDataFileExporter {

    @Override
    public ExportFileType getType() {
        return ExportFileType.CSV;
    }

    @Override
    public String getDelimiter() {
        return ",";
    }

}
