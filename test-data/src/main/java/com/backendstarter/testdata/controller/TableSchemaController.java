package com.backendstarter.testdata.controller;

import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.domain.constant.MockDataType;
import com.backendstarter.testdata.dto.request.TableSchemaExportRequest;
import com.backendstarter.testdata.dto.request.TableSchemaRequest;
import com.backendstarter.testdata.dto.response.SchemaFieldResponse;
import com.backendstarter.testdata.dto.response.SimpleTableSchemaResponse;
import com.backendstarter.testdata.dto.response.TableSchemaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class TableSchemaController {

    private final ObjectMapper mapper;

    @GetMapping("/table-schema")
    public String tableSchema(
        @RequestParam(required = false) String schemaName,
        Model model) {
        var tableSchema = defaultTableSchema(schemaName);

        model.addAttribute("tableSchema", tableSchema);
        model.addAttribute("mockDataTypes", MockDataType.toObjects());
        model.addAttribute("fileTypes", Arrays.stream(ExportFileType.values()).toList());

        return "table-schema";
    }

    @PostMapping("/table-schema")
    public String createUpdateTableSchema(
        TableSchemaRequest request,
        RedirectAttributes redirectAttrs
    ) {
        redirectAttrs.addFlashAttribute("tableSchemaRequest", request);
        return "redirect:/table-schema";
    }

    @GetMapping("/table-schema/my-schemas")
    public String mySchemas(Model model) {
        var tableSchemas = mySampleSchemas();

        model.addAttribute("tableSchemas", tableSchemas);

        return "my-schemas";
    }

    @PostMapping("/table-schema/my-schemas/{schemaName}")
    public String deleteMySchema(
        @PathVariable String schemaName,
        RedirectAttributes redirectAttrs
    ) {
        return "redirect:/my-schemas";
    }

    // @ResponseBody

    @GetMapping("/table-schema/export")
    public ResponseEntity<String> exportTableSchema(TableSchemaExportRequest request) {

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table_schema.txt")
            .body(json(request));
    }
    private static TableSchemaResponse defaultTableSchema(String schemaName) {
        return new TableSchemaResponse(
            schemaName != null ? schemaName : "schema_name",
            "ssuyoi",
            List.of(
                new SchemaFieldResponse(MockDataType.STRING, "fieldName1", 1, 0, null, null),
                new SchemaFieldResponse(MockDataType.NUMBER, "fieldName2", 2, 10, null, null),
                new SchemaFieldResponse(MockDataType.NAME, "fieldName3", 3, 20, null, null)
            )
        );
    }

    private static List<SimpleTableSchemaResponse> mySampleSchemas() {
        return List.of(
            new SimpleTableSchemaResponse("schema_name1", "ssuyoi",
                LocalDate.of(2026, 1, 1).atStartOfDay()),
            new SimpleTableSchemaResponse("schema_name2", "ssuyoi",
                LocalDate.of(2026, 2, 1).atStartOfDay()),
            new SimpleTableSchemaResponse("schema_name3", "ssuyoi",
                LocalDate.of(2026, 3, 1).atStartOfDay())
        );
    }

    private String json(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
