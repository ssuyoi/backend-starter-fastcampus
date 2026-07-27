package com.backendstarter.testdata.service.exporter;

import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;

@Component
public class TsvFileExporter extends DelimiterBasedFileExporter implements MockDataFileExporter {

    public TsvFileExporter(MockDataGeneratorContext mockDataGeneratorContext) {
        super(mockDataGeneratorContext);
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.TSV;
    }

    @Override
    public String getDelimiter() {
        return "\t";
    }

}
