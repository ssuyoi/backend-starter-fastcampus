package com.backendstarter.testdata.controller;

import com.backendstarter.testdata.dto.request.TableSchemaRequest;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TableSchemaController {

    @GetMapping("/table-schema")
    public String tableSchema(TableSchemaRequest request) {
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
    public String mySchemas() {
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
    public ResponseEntity<String> exportTableSchema(TableSchemaRequest request) {

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table_schema.txt")
            .body("download complete!");
    }
}
