package com.backendstarter.testdata.service.exporter;

import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;


@Component
public class CsvFileExporter extends DelimiterBasedFileExporter implements MockDataFileExporter {

    public CsvFileExporter(
        MockDataGeneratorContext mockDataGeneratorContext) {
        super(mockDataGeneratorContext);
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.CSV;
    }

    @Override
    public String getDelimiter() {
        return ",";
    }

}
